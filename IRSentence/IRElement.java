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
      num1 = (replace.get(num1));
    }
    while (replace.containsKey(num2)) {
      num2 = (replace.get(num2));
    }
    while (replace.containsKey(src)) {
      src = (replace.get(src));
    }
    while (replace.containsKey(output)) {
      output = (replace.get(output));
    }
    return;
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    while (single.containsKey(num1)) {
      num1 = (single.get(num1));
    }
    while (single.containsKey(num2)) {
      num2 = (single.get(num2));
    }
    while (single.containsKey(src)) {
      src = (single.get(src));
    }
    while (single.containsKey(output)) {
      output = (single.get(output));
    }
    return;
  }

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    if (reg_value.containsKey(num1)) {
      num1 = (reg_value.get(num1));
    }
    if (reg_value.containsKey(num2)) {
      num2 = (reg_value.get(num2));
    }
    if (variable_stack.containsKey(src)) {
      src = (variable_stack.get(src).peek().name);
    }
    if (reg_value.containsKey(src)) {
      src = (reg_value.get(src));
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
    if (!registers.containsKey(output)) {
      return;
    }
    int target_value = registers.get(output);
    int src_value = registers.get(src);
    String src_str = "t0";
    if (src_value >= 0) {
      src_str = register_name.get(src_value);
    } else {
      src_value = now_depth + src_value * 4;
      if ((src_value >> 11) == 0) {
        buffer.add("lw t0, " + src_value + "(sp)");
      } else {
        buffer.add("li t0, " + src_value);
        buffer.add("add t0, t0, sp");
        buffer.add("lw t0, 0(t0)");
      }
    }
    if (num2 != null) {
      try {
        int num_i = Integer.parseInt(num2);
        if ((num_i >> 9) == 0) {
          if (num_i == 0) {
            if (target_value < 0) {
              int res_value = now_depth + (target_value * 4);
              if ((res_value >> 11) == 0) {
                buffer.add("sw " + src_str + ", " + res_value + "(sp)");
              } else {
                buffer.add("li t0, " + res_value);
                buffer.add("add t0, t0, sp");
                buffer.add("sw " + src_str + ", 0(t0)");
              }
            }
          }
          if (target_value < 0) {
            buffer.add("addi t1, " + src_str + ", " + Integer.toString(num_i * 4));
          } else {
            buffer.add(
                "addi " + register_name.get(target_value) + ", " + src_str + ", " + Integer.toString(num_i * 4));
          }
        } else {
          buffer.add("li t1, " + (num_i * 4));
          if (target_value < 0) {
            buffer.add("add t1, " + src_str + ", t1");
          } else {
            buffer.add("add " + register_name.get(target_value) + ", t1, " + src_str);
          }
        }
      } catch (NumberFormatException e) {
        int value_i = registers.get(num2);
        if (value_i >= 0) {
          String num_str = register_name.get(value_i);
          buffer.add("slli t1, " + num_str + ", 2");
        } else {
          value_i = now_depth + (value_i * 4);
          if ((value_i >> 11) == 0) {
            buffer.add("lw t1, " + value_i + "(sp)");
          } else {
            buffer.add("li t1, " + value_i);
            buffer.add("add t1, t1, sp");
            buffer.add("lw t1, 0(t1)");
          }
          buffer.add("slli t1, t1, 2");
        }
        if (target_value < 0) {
          buffer.add("add t1, t1, " + src_str);
        } else {
          buffer.add("add " + register_name.get(target_value) + ", t1, " + src_str);
        }
      }
    } else {
      try {
        int num_i = Integer.parseInt(num1);
        if ((num_i >> 9) == 0) {
          if (target_value < 0) {
            buffer.add("addi t1, " + src_str + ", " + Integer.toString(num_i * 4));
          } else {
            buffer.add(
                "addi " + register_name.get(target_value) + ", " + src_str + ", " + Integer.toString(num_i * 4));
          }
        } else {
          buffer.add("li t1, " + (num_i * 4));
          if (target_value < 0) {
            buffer.add("add t1, " + src_str + ", t1");
          } else {
            buffer.add("add " + register_name.get(target_value) + ", t1, " + src_str);
          }
        }
      } catch (NumberFormatException e) {
        int value_i = registers.get(num1);
        if (value_i >= 0) {
          String from_str = register_name.get(value_i);
          buffer.add("slli t1, " + from_str + ", 2");
        } else {
          value_i = now_depth + (value_i * 4);
          if ((value_i >> 11) == 0) {
            buffer.add("lw t1, " + value_i  + "(sp)");
          } else {
            buffer.add("li t1, " + value_i);
            buffer.add("add t1, t1, sp");
            buffer.add("lw t1, 0(t1)");
          }
          buffer.add("slli t1, t1, 2");
        }
        if (target_value < 0) {
          buffer.add("add t1, t1, " + src_str);
        } else {
          buffer.add("add " + register_name.get(target_value) + ", t1, " + src_str);
        }
      }
    }
    if (target_value < 0) {
      target_value = now_depth + (target_value * 4);
      if ((target_value >> 9) == 0) {
        buffer.add("sw t1, " + target_value + "(sp)");
      } else {
        buffer.add("li t0, " + target_value);
        buffer.add("add t0, t0, sp");
        buffer.add("sw t1, 0(t0)");
      }
    }
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRElement return_value = new IRElement();
    return_value.now_type = now_type;
    if (is_global.containsKey(src)) {
      return_value.src = src;
    } else {
      if (now_name.containsKey(src)) {
        return_value.src = now_name.get(src);
      } else {
        return_value.src = "%reg$" + (++machine.tmp_time);
        now_name.put(src, return_value.src);
      }
    }
    try {
      Integer.parseInt(num1);
      return_value.num1 = num1;
    } catch (NumberFormatException e) {
      if (!is_global.containsKey(num1)) {
        if (!now_name.containsKey(num1)) {
          return_value.num1 = "%reg$" + (++machine.tmp_time);
          now_name.put(num1, return_value.num1);
        } else {
          return_value.num1 = now_name.get(num1);
        }
      } else {
        return_value.num1 = num1;
      }
    }
    if (num2 != null) {
      try {
        Integer.parseInt(num2);
        return_value.num2 = num2;
      } catch (NumberFormatException e) {
        if (!is_global.containsKey(num2)) {
          if (!now_name.containsKey(num2)) {
            return_value.num2 = "%reg$" + (++machine.tmp_time);
            now_name.put(num2, return_value.num2);
          } else {
            return_value.num2 = now_name.get(num2);
          }
        } else {
          return_value.num2 = num2;
        }
      }
    }
    if (is_global.containsKey(output)) {
      return_value.output = output;
    } else {
      if (!now_name.containsKey(output)) {
        return_value.output = "%reg$" + (++machine.tmp_time);
        now_name.put(output, return_value.output);
      } else {
        return_value.output = now_name.get(output);
      }
    }
    return return_value;
  }

  @Override
  public void AliveUseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    if (!use.containsKey(output)) {
      dead = true;
    } else {
      dead = false;
      UseDefCheck(def, use);
    }
  }

  @Override
  public void GlobalConstReplace(HashMap<String, String> mapping) {
    if (mapping.containsKey(num1)) {
      num1 = (mapping.get(num1));
    }
    if (mapping.containsKey(num2)) {
      num2 = (mapping.get(num2));
    }
    return;
  }

  @Override
  public String ConstCheck(HashMap<String, String> replace) {
    if (replace.containsKey(num1)) {
      num1 = (replace.get(num1));
    }
    if (num2 != null && replace.containsKey(num2)) {
      num2 = (replace.get(num2));
    }
    if(replace.containsKey(src)) {
      src = (replace.get(src));
    }
    return null;
  }

  @Override
  public boolean RepeatOperation(IRCode rhs) {
    if(!(rhs instanceof IRElement)) {
      return false;
    }
    IRElement ele2 = (IRElement)rhs;
    if(!src.equals(ele2.src)) {
      return false;
    }
    if((num2 != null && ele2.num2 == null) || (num2 == null && ele2.num2 != null)) {
      return false;
    }
    if(num2 != null) {
      return num2.equals(ele2.num2);
    }
    return num1.equals(ele2.num1);
  }
}