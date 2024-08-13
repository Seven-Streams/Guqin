package IRSentence;

import java.util.ArrayList;
import java.util.HashMap;

public class IRPhi extends IRCode {
  public static int phi_cnt = 0;
  public String type = null;
  public String target = null;
  public ArrayList<Integer> labels = new ArrayList<>();
  public ArrayList<String> values = new ArrayList<>();

  @Override
  public void CodePrint() {
    System.out.print(target + " = phi " + type + " ");
    for (int i = 0; i < labels.size(); i++) {
      System.out.print("[" + values.get(i) + " , " + "%b" + Integer.toString(labels.get(i)) + "]");
      if (i != (labels.size() - 1)) {
        System.out.print(",");
      }
    }
    System.out.println("");
    return;
  }

  @Override
  public void Codegen() throws Exception {
    if (!is_global.containsKey(target)) {
      is_global.put(target, false);
      now_s0 += 4;
      relative_addr.put(target, Integer.toString(-now_s0) + "(s0)");
    }
    String place = relative_addr.get(target);
    System.out.println("beqz a7, phi" + (Integer.toString(++phi_cnt)));
    String addr1 = null;
    String addr2 = null;
    try {
      int ins_1 = Integer.parseInt(values.get(0));
      if ((ins_1 >> 12) != 0) {
        System.out.println("lui a0, " + (ins_1 >> 12));
      } else {
        System.out.println("andi a0, a0, 0");
      }
      System.out.println("addi a0, a0, " + (ins_1 & 0x00000fff));
    } catch (NumberFormatException e) {
      if (!relative_addr.containsKey(values.get(0))) {
        is_global.put(values.get(0), false);
        now_s0 += 4;
        relative_addr.put(values.get(0), Integer.toString(-now_s0) + "(s0)");
      }
      addr1 = relative_addr.get(values.get(0));
      System.out.println("lw a0, " + addr1);
    }
    System.out.println("sw a0, " + place);
    System.out.println("j phi" + (++phi_cnt));
    System.out.println("");
    System.out.println("phi" + (phi_cnt - 1) + ":");
    try {
      int ins_2 = Integer.parseInt(values.get(1));
      if ((ins_2 >> 12) != 0) {
        System.out.println("lui a0, " + (ins_2 >> 12));
      } else {
        System.out.println("andi a0, a0, 0");
      }
      System.out.println("addi a0, a0, " + (ins_2 & 0x00000fff));
    } catch (NumberFormatException e) {
      if (!relative_addr.containsKey(values.get(1))) {
        is_global.put(values.get(1), false);
        now_s0 += 4;
        relative_addr.put(values.get(1), Integer.toString(-now_s0) + "(s0)");
      }
      addr2 = relative_addr.get(values.get(1));
      System.out.println("lw a0, " + addr2);
    }
    System.out.println("sw a0, " + place);
    System.out.println("j phi" + (phi_cnt));
    System.out.println("");
    System.out.println("phi" + phi_cnt + ":");
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
    for(String value : values) {
      if(use.containsKey(value)) {
        use.put(value, use.get(value) + 1);
      }
    }
    if(def.containsKey(target)) {
      def.put(target, def.get(target) + 1);
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Boolean> deprecated) {
    return deprecated.containsKey(target);
  }
}
