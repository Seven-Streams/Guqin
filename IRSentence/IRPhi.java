package IRSentence;

import java.util.ArrayList;

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
      System.out.println("lui a0, " + (ins_1 >> 12));
      System.out.println("addi a0, " + (ins_1 & 0x00000fff));
    } catch (NumberFormatException e) {
      addr1 = relative_addr.get(values.get(0));
      System.out.println("lw a0, " + addr1);
    }
    System.out.println("sw a0, " + place);
    System.out.println("J phi" + (++phi_cnt));
    System.out.println("");
    System.out.println("phi" + (phi_cnt - 1));
    try {
      int ins_2 = Integer.parseInt(values.get(1));
      System.out.println("lui a0, " + (ins_2 >> 12));
      System.out.println("addi a0, " + (ins_2 & 0x00000fff));
    } catch (NumberFormatException e) {
      addr2 = relative_addr.get(values.get(1));
      System.out.println("lw a0, " + addr2);
    }
    System.out.println("sw a0, " + place);
    System.out.println("J phi" + (phi_cnt));
    System.out.println("");
    System.out.println("phi" + phi_cnt);
    return;
  }
}
