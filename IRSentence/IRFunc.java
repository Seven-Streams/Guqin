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
    if (name.equals("main")) {
      main = true;
    } else {
      main = false;
    }
    func_num++;
    buffer.add("");
    buffer.add(name + ":");
    size *= 4;
    size += 12;
    size /= 16;
    size *= 16;
    now_s0 = 8;
    sp_length = size;
    if (size <= 2047) {
      buffer.add("addi sp, sp, " + -size);
      buffer.add("addi t1, sp, " + size);
    } else {
      buffer.add("li t0, " + size);
      buffer.add("sub sp, sp, t0");
      buffer.add("add t1, sp, t0");
    }
    now_depth = size;
    buffer.add("sw ra, -4(t1)");
    if (register_use.get(-func_num) >= 0) {
      buffer.add("sw s0, -8(t1)");
    }
    for (int i = 1; i <= Integer.min(register_use.get(-func_num), 11); i++) {
      buffer.add("sw s" + i + ", " + (-8 - 4 * i) + "(t1)");
    }
    buffer.add(name + ".tail:");
    if (names.size() < 8) {
      for (int i = 0; i < names.size(); i++) {
        if (!registers.containsKey(names.get(i))) {
          continue;
        }
        int value = registers.get(names.get(i));
        if (value >= 0) {
          if (!register_name.get(value).equals("a" + i)) {
            buffer.add("mv " + register_name.get(value) + ", " + "a" + i);
          }
        } else {
          value = now_depth + (value * 4);
          if ((value >> 11) == 0) {
            buffer.add("sw a" + i + " , " + value + "(sp)");
          } else {
            buffer.add("li t0, " + value);
            buffer.add("add t0, t0, sp");
            buffer.add("sw a" + i + ", 0(t0)");
          }
        }
      }
    } else {
      for (int i = 0; i < 8; i++) {
        if (!registers.containsKey(names.get(i))) {
          continue;
        }
        int value = registers.get(names.get(i));
        if (value >= 0) {
          if (!register_name.get(value).equals("a" + i)) {
            buffer.add("mv " + register_name.get(value) + ", " + "a" + i);
          }
        } else {
          value = now_depth + (value * 4);
          if ((value >> 11) == 0) {
            buffer.add("sw a" + i + " , " + value + "(sp)");
          } else {
            buffer.add("li t0, " + value);
            buffer.add("add t0, t0, sp");
            buffer.add("sw a" + i + ", 0(t0)");
          }
        }
      }
      for (int i = 8; i < names.size(); i++) {
        if (!registers.containsKey(names.get(i))) {
          continue;
        }
        int value = registers.get(names.get(i));
        String target_reg = "t0";
        if (value >= 0) {
          target_reg = register_name.get(value);
        }
        int place = now_depth + ((i - 8) * 4);
        if ((place >> 11) == 0) {
          buffer.add("lw " + target_reg + ", " + place + "(sp)");
        } else {
          buffer.add("li t0, " + place);
          buffer.add("add t0, t0, sp");
          buffer.add("lw " + target_reg + ", 0(t0)");
        }
        if (value < 0) {
          value = now_depth + value * 4;
          if ((value >> 11) == 0) {
            buffer.add("sw t0, " + value + "(sp)");
          } else {
            buffer.add("li t1, " + value);
            buffer.add("add t1, t1, sp");
            buffer.add("sw t0, 0(t1)");
          }
        }
      }
    }
    return;
  }
}