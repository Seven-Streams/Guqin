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
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    for (String name : names) {
      def.put(name, null);
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    func_num++;
    System.out.println("");
    System.out.println(name + ":");
    size *= 4;
    size += 12;
    size /= 16;
    size *= 16;
    now_s0 = 8;
    sp_length = size;
    System.out.println("li t0, " + size);
    System.out.println("sub sp, sp, t0");
    System.out.println("sw ra, " + (size - 4) + "(sp)");
    System.out.println("sw s0, " + (size - 8) + "(sp)");
    for (int i = 0; i <= Integer.min(register_use.get(-func_num), 10); i++) {
      System.out.println("sw s" + (i + 1) + ", " + (size - 12 - 4 * i) + "(sp)");
    }
    System.out.println("addi s0, sp, " + size);
    if (names.size() < 8) {
      for (int i = 0; i < names.size(); i++) {
        int value = registers.get(names.get(i));
        if (value >= 0) {
          System.out.println("mv " + register_name.get(value) + ", " + "a" + i);
        } else {
          if ((value >> 9) == 0) {
            System.out.println("sw a" + i + " , " + (value * 4) + "(s0)");
          } else {
            System.out.println("li t0, " + (value * 4));
            System.out.println("add t0, t0, s0");
            System.out.println("sw a" + i + ", 0(t0)");
          }
        }
      }
    } else {
      for (int i = 0; i < 8; i++) {
        int value = registers.get(names.get(i));
        if (value >= 0) {
          System.out.println("mv " + register_name.get(value) + ", " + "a" + i);
        } else {
          if ((value >> 9) == 0) {
            System.out.println("sw a" + i + " , " + (value * 4) + "(s0)");
          } else {
            System.out.println("li t0, " + (value * 4));
            System.out.println("add t0, t0, s0");
            System.out.println("sw a" + i + ", 0(t0)");
          }
        }
      }
      for (int i = 8; i < names.size(); i++) {
        if (((i - 8) >> 10) == 0) {
          System.out.println("lw t0, " + ((i - 8) * 4) + "(s0)");
        } else {
          System.out.println("li t0, " + ((i - 8) * 4));
          System.out.println("add t0, t0, s0");
          System.out.println("lw t0, 0(t0)");
        }
        int value = registers.get(names.get(i));
        if (value >= 0) {
          System.out.println("mv " + register_name.get(value) + ", t0");
        } else {
          if ((value >> 9) == 0) {
            System.out.println("sw t0, " + (value * 4) + "(s0)");
          } else {
            System.out.println("li t1, " + (value * 4));
            System.out.println("add t1, t1, s0");
            System.out.println("sw t0, 0(t1)");
          }
        }
      }
    }
    return;
  }
}