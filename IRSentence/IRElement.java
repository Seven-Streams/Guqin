package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
import Optimization.NameLabelPair;

public class IRElement extends IRCode {
  public String output = null;
  public String now_type = null;
  public String src = null;
  public String num1 = "0";
  public String num2 = null;

  @Override
  public void CodePrint() {
    System.out.print(output + " = getelementptr " + now_type + " , ptr " + src + " , i32 " + num1);
    if (num2 != null) {
      System.out.println(", i32 " + num2);
    } else {
      System.out.println("");
    }
    return;
  }

  @Override
  public void Codegen() throws Exception {
    if (is_global.get(src)) {
      throw new Exception("Unexpected ptr.");
    }
    String src_addr = relative_addr.get(src);
    System.out.println("lw a0, " + src_addr);
    if (num2 != null) {
      try {
        int num_i = Integer.parseInt(num2);
        if ((num_i >> 10) == 0) {
          System.out.println("addi a1, a0, " + Integer.toString(num_i * 4));
        } else {
          System.out.println("lui a1, " + (num_i >> 10));
          System.out.println("addi a1, a1, " + ((num_i << 2) & 0x00000fff));
          System.out.println("addi a1, a0, a1");
        }
      } catch (NumberFormatException e) {
        String num_addr = relative_addr.get(num2);
        System.out.println("lw a2, " + num_addr);
        System.out.println("slli a2, a2, 2");
        System.out.println("add a1, a0, a2");
      }
    } else {
      try {
        int num_i = Integer.parseInt(num1);
        System.out.println("addi a1, a0, " + Integer.toString(num_i * 4));
      } catch (NumberFormatException e) {
        String num_addr = relative_addr.get(num1);
        System.out.println("lw a2, " + num_addr);
        System.out.println("slli a2, a2, 2");
        System.out.println("add a1, a0, a2");
      }
    }
    if (!relative_addr.containsKey(output)) {
      is_global.put(output, false);
      now_s0 += 4;
      relative_addr.put(output, Integer.toString(-now_s0) + "(s0)");
    }
    String out_reg = relative_addr.get(output);
    System.out.println("sw a1, " + out_reg);
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
    try {
      Integer.parseInt(num1);
    } catch (NumberFormatException e) {
      if (use.containsKey(num1)) {
        use.put(num1, use.get(num1) + 1);
      } else {
        use.put(num1, 1);
      }
    }
    try {
      Integer.parseInt(num2);
    } catch (NumberFormatException e) {
      if (use.containsKey(num2)) {
        use.put(num2, use.get(num2) + 1);
      } else {
        use.put(num2, 1);
      }
    }
    try {
      Integer.parseInt(src);
    } catch (NumberFormatException e) {
      if (use.containsKey(src)) {
        use.put(src, use.get(src) + 1);
      } else {
        use.put(src, 1);
      }
    }
    try {
      Integer.parseInt(output);
    } catch (NumberFormatException e) {
      if (def.containsKey(output)) {
        def.put(output, def.get(output) + 1);
      } else {
        def.put(output, 1);
      }
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
    return !use.containsKey(output);
  }

  @Override
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated) {
    while (replace.containsKey(num1)) {
      num1 = new String(replace.get(num1));
    }
    while (replace.containsKey(num2)) {
      num2 = new String(replace.get(num2));
    }
    while (replace.containsKey(src)) {
      src = new String(replace.get(src));
    }
    while (replace.containsKey(output)) {
      output = new String(replace.get(output));
    }
    return;
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    while (single.containsKey(num1)) {
      num1 = new String(single.get(num1));
    }
    while (single.containsKey(num2)) {
      num2 = new String(single.get(num2));
    }
    while (single.containsKey(src)) {
      src = new String(single.get(src));
    }
    while (single.containsKey(output)) {
      output = new String(single.get(output));
    }
    return;
  }

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    if (reg_value.containsKey(num1)) {
      num1 = new String(reg_value.get(num1));
    }
    if (reg_value.containsKey(num2)) {
      num2 = new String(reg_value.get(num2));
    }
    if (variable_stack.containsKey(src)) {
      src = new String(variable_stack.get(src).peek().name);
    }
    if (reg_value.containsKey(src)) {
      src = new String(reg_value.get(src));
    }
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    if ((!def.containsKey(src)) && (!is_global.containsKey(src))) {
      use.put(src, null);
    }
    if (num1 != null) {
      try {
        Integer.parseInt(num1);
      } catch (NumberFormatException e) {
        if (!def.containsKey(num1)) {
          use.put(num1, null);
        }
      }
    }
    if (num2 != null) {
      try {
        Integer.parseInt(num2);
      } catch (NumberFormatException e) {
        if (!def.containsKey(num2)) {
          use.put(num2, null);
        }
      }
    }
    def.put(output, null);
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    int target_value = registers.get(output);
    int src_value = registers.get(src);
    String src_str = "t0";
    if (src_value >= 0) {
      src_str = register_name.get(src_value);
    } else {
      System.out.println("lw t0, " + (4 * src_value) + "(s0)");
    }
    if (num2 != null) {
      try {
        int num_i = Integer.parseInt(num2);
        if ((num_i >> 10) == 0) {
          if (target_value < 0) {
            System.out.println("addi t1, " + src_str + ", " + Integer.toString(num_i * 4));
          } else {
            System.out.println(
                "addi " + register_name.get(target_value) + ", " + src_str + ", " + Integer.toString(num_i * 4));
          }
        } else {
          System.out.println("lui t1, " + (num_i >> 10));
          System.out.println("addi t1, t1, " + ((num_i << 2) & 0x00000fff));
          if (target_value < 0) {
            System.out.println("add t1, " + src_str + ", t1");
          } else {
            System.out.println("add " + register_name.get(target_value) + ", t1, " + src_str);
          }
        }
      } catch (NumberFormatException e) {
        int value_i = registers.get(num2);
        if (value_i >= 0) {
          String num_str = register_name.get(value_i);
          System.out.println("slli t1, " + num_str + ", 2");
        } else {
          if ((value_i >> 9) == 0) {
            System.out.println("lw t1, " + (value_i * 4) + "(s0)");
          } else {
            System.out.println("li t1, " + (value_i * 4));
            System.out.println("add t1, t1, s0");
            System.out.println("lw t1, 0(t1)");
          }
          System.out.println("slli t1, t1, 2");
        }
        if (target_value < 0) {
          System.out.println("add t1, t1, " + src_str);
        } else {
          System.out.println("add " + register_name.get(target_value) + ", t1, " + src_str);
        }
      }
    } else {
      try {
        int num_i = Integer.parseInt(num1);
        if ((num_i >> 9) == 0) {
          if (target_value < 0) {
            System.out.println("addi t1, " + src_str + ", " + Integer.toString(num_i * 4));
          } else {
            System.out.println(
                "addi " + register_name.get(target_value) + ", " + src_str + ", " + Integer.toString(num_i * 4));
          }
        } else {
         System.out.println("li t1, " + (num_i * 4));
          if (target_value < 0) {
            System.out.println("add t1, " + src_str + ", t1");
          } else {
            System.out.println("add " + register_name.get(target_value) + ", t1, " + src_str);
          }
        }
      } catch (NumberFormatException e) {
        int value_i = registers.get(num1);
        if (value_i >= 0) {
          String from_str = register_name.get(value_i);
          System.out.println("slli t1, " + from_str + ", 2");
        } else {
          if ((value_i >> 9) == 0) {
            System.out.println("lw t1, " + (value_i * 4) + "(s0)");
          } else {
            System.out.println("li t1, " + (value_i * 4));
            System.out.println("add t1, t1, s0");
            System.out.println("lw t1, 0(t1)");
          }
          System.out.println("slli t1, t1, 2");
        }
        if (target_value < 0) {
          System.out.println("add t1, t1, " + src_str);
        } else {
          System.out.println("add " + register_name.get(target_value) + ", t1, " + src_str);
        }
      }
    }
    if (target_value < 0) {
      if ((target_value >> 9) == 0) {
        System.out.println("sw t1, " + (target_value * 4) + "(s0)");
      } else {
        System.out.println("li t0, " + (target_value * 4));
        System.out.println("add t0, t0, s0");
        System.out.println("sw t1, 0(t0)");
      }
    }
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRElement return_value = new IRElement();
    return_value.now_type = new String(now_type);
    try {
      Integer.parseInt(num1);
      return_value.num1 = new String(num1);
    } catch (NumberFormatException e) {
      if (!is_global.containsKey(num1)) {
        return_value.num1 = new String("%reg$" + (++machine.tmp_time));
        now_name.put(num1, return_value.num1);
      } else {
        return_value.num1 = new String(num1);
      }
    }
    if (num2 != null) {
      try {
        Integer.parseInt(num2);
        return_value.num2 = new String(num2);
      } catch (NumberFormatException e) {
        if (!is_global.containsKey(num2)) {
          return_value.num2 = new String("%reg$" + (++machine.tmp_time));
          now_name.put(num2, return_value.num2);
        } else {
          return_value.num2 = new String(num2);
        }
      }
    }
    if(!is_global.containsKey(output)) {
      return_value.output = new String(output);
    } else {
      return_value.output = new String("%reg$" + (++machine.tmp_time));
      now_name.put(output, return_value.output);
    }
    return return_value;
  }
}