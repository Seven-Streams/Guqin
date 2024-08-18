package IRSentence;

import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;
import Composer.Composer;
import Optimization.NameLabelPair;

public class IRStore extends IRCode {
  public String name = null;
  public String from = null;
  public String type = null;
  public boolean be_alloc = false;

  @Override
  public void CodePrint() {
    System.out.println("store " + type + " " + from + ",ptr " + name);
    return;
  }

  @Override
  public void Codegen() {
    if (!(from.equals("true") || from.equals("false") || from.equals("null"))) {
      try {
        int num = Integer.parseInt(from);
        if ((num >> 12) != 0) {
          System.out.println("lui a1, " + (num >> 12));
        } else {
          System.out.println("andi a1, a1, 0");
        }
        System.out.println("addi a1, a1, " + (num & 0x00000fff));
      } catch (NumberFormatException e) {
        String addr_from = relative_addr.get(from);
        System.out.println("lw a1, " + addr_from);
      }
    } else {
      if (from.equals("true")) {
        System.out.println("li a1, 1");
      } else {
        System.out.println("li a1, 0");
      }
    }
    if (is_global.containsKey(name)) {
      if (is_global.get(name)) {
        System.out.println("lui a0, " + "%hi(" + name.substring(1) + ")");
        System.out.println("addi a0, a0, %lo(" + name.substring(1) + ")");
        System.out.println("sw a1, 0(a0)");
      } else {
        String addr_des = relative_addr.get(name);
        System.out.println("lw a0, " + addr_des);
        System.out.println("sw a1, 0(a0)");
      }
    }
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
    try {
      Integer.parseInt(name);
    } catch (NumberFormatException e) {
      boolean is_alloc = false;
      for (ArrayList<IRCode> allocs : machine.alloc.values()) {
        for (IRCode alloc_raw : allocs) {
          IRAlloc trans_alloc = (IRAlloc) alloc_raw;
          if (trans_alloc.des.equals(name)) {
            is_alloc = true;
            break;
          }
        }
      }
      be_alloc = is_alloc;
      if (!is_alloc) {
        if (use.containsKey(name)) {
          use.put(name, use.get(name) + 1);
        } else {
          use.put(name, 1);
        }
      } else {
        if (def.containsKey(name)) {
          def.put(name, def.get(name) + 1);
        } else {
          def.put(name, 1);
        }
      }

    }
    try {
      Integer.parseInt(from);
    } catch (NumberFormatException e) {
      if (use.containsKey(from)) {
        use.put(from, use.get(from) + 1);
      } else {
        use.put(from, 1);
      }
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
    return !use.containsKey(name);
  }

  @Override
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated) {
    if (deprecated.containsKey(name)) {
      replace.put(name, from);
    }
    while (replace.containsKey(from)) {
      from = new String(replace.get(from));
    }
    return;
  }

  @Override
  public boolean AssignOnceRemove(HashMap<String, ?> deprecated) {
    if (deprecated.containsKey(name)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int CheckBlock(HashMap<String, HashMap<Integer, Boolean>> times, int now_block) {
    if (times.containsKey(name)) {
      times.get(name).put(now_block, null);
    }
    return now_block;
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    while (single.containsKey(from)) {
      from = new String(single.get(from));
    }
    if (single.containsKey(name)) {
      single.put(name, from);
    }
    return;
  }

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    if (variable_stack.containsKey(from)) {
      from = new String(variable_stack.get(from).peek().name);
    }
    if (reg_value.containsKey(from)) {
      from = new String(reg_value.get(from));
    }
    if (variable_stack.containsKey(name)) {
      variable_stack.get(name).push(new NameLabelPair(from, now_block));
    }
    return;
  }

  @Override
  public boolean ToRemove(HashMap<String, Stack<NameLabelPair>> variable_stack) {
    return variable_stack.containsKey(name);
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    try {
      Integer.parseInt(from);
    } catch (NumberFormatException e) {
      if ((!def.containsKey(from)) && CheckLit(from)) {
        use.put(from, null);
      }
    }
    if ((!is_global.containsKey(name)) && (!def.containsKey(name))) {
      use.put(name, null);
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    String src_reg = "t0";
    if (!(from.equals("true") || from.equals("false") || from.equals("null"))) {
      try {
        int num = Integer.parseInt(from);
        boolean signal = num >= 0;
        num = signal ? num : -num;
        if ((num >> 12) != 0) {
          System.out.println("lui t0, " + (num >> 12));
          System.out.println("addi t0, t0, " + (num & 0x00000fff));
        } else {
          System.out.println("li t0, " + num);
        }
        if (!signal) {
          System.out.println("neg t0, t0");
        }
      } catch (NumberFormatException e) {
        if (registers.get(from) >= 0) {
          src_reg = register_name.get(registers.get(from));
        } else {
          System.out.println("lw t0, " + (registers.get(from) * 4) + "(s0)");
        }
      }
    } else {
      if (from.equals("true")) {
        System.out.println("li t0, 1");
      } else {
        System.out.println("li t0, 0");
      }
    }
    if (is_global.containsKey(name)) {
      System.out.println("lui t1, " + "%hi(" + name.substring(1) + ")");
      System.out.println("addi t1, t1, %lo(" + name.substring(1) + ")");
      System.out.println("sw " + src_reg + ", 0(t1)");
    } else {
      int value = registers.get(name);
      if (value >= 0) {
        System.out.println("sw " + src_reg + ", 0(" + register_name.get(value) + ")");
      } else {
        value = -value;
        if ((value >> 10) == 0) {
          System.out.println("lw t1, " + (4 * -value) + "(s0)");
        } else {

          System.out.println("lui t1, " + (value >> 10));
          System.out.println("addi t1, t1, " + ((value << 2) & 0x00000fff));
          System.out.println("neg t1, t1");
          System.out.println("add t1, t1, s0");
          System.out.println("lw t1, 0(t1)");
        }
        System.out.println("sw " + src_reg + ", 0(t1)");
      }
    }

    return;
  }
}
