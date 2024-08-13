package IRSentence;

import java.util.ArrayList;
import java.util.HashMap;

public class IRFunc extends IRCode {
  public String name = null;
  public String return_type = null;
  public int size = 0;
  public ArrayList<String> types = new ArrayList<>();
  public ArrayList<String> names = new ArrayList<>();

  @Override
  public void CodePrint() {
    System.out.print("define " + return_type + " @" + name + "( ");
    for (int i = 0; i < (types.size() - 1); i++) {
      System.out.print(types.get(i) + " " + names.get(i) + ", ");
    }
    if (!types.isEmpty()) {
      System.out.print(types.get(types.size() - 1) + " " + names.get(types.size() - 1));
    }
    System.out.println(") {");
    return;
  }

  @Override
  public void Codegen() throws Exception {
    func_num++;
    System.out.println("");
    System.out.println(name + ":");
    size *= 4;
    size += 12;
    size /= 16;
    size *= 16;
    now_s0 = 8;
    sp_length = size;
    if ((size >> 12) == 0) {
      System.out.println("addi sp, sp, -" + size);
    } else {
      System.out.println("lui a0, " + (size >> 12));
      System.out.println("addi a0, a0, " + (size & 0x00000fff));
      System.out.println("sub sp, sp, a0");
    }
    System.out.println("sw ra, " + (size - 4) + "(sp)");
    System.out.println("sw s0, " + (size - 8) + "(sp)");
    System.out.println("addi s0, sp, " + size);
    if (names.size() < 8) {
      for (int i = 0; i < names.size(); i++) {
        is_global.put(names.get(i), false);
        now_s0 += 4;
        relative_addr.put(names.get(i), (-now_s0) + "(s0)");
        System.out.println("sw a" + i + " , " + (-now_s0) + "(s0)");
      }
    } else {
      for (int i = 0; i < 8; i++) {
        is_global.put(names.get(i), false);
        now_s0 += 4;
        relative_addr.put(names.get(i), (-now_s0) + "(s0)");
        System.out.println("sw a" + i + " , " + (-now_s0) + "(s0)");
      }
      for (int i = 8; i < names.size(); i++) {
        is_global.put(names.get(i), false);
        relative_addr.put(names.get(i), ((i - 8) * 4) + "(s0)");
      }
    }
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
    for (String name : names) {
      try {
        Integer.parseInt(name);
      } catch (NumberFormatException e) {
        if (def.containsKey(name)) {
          def.put(name, 1);
        }
      }
    }
    return;
  }
}
