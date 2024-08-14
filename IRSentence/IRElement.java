package IRSentence;

import java.util.HashMap;
import java.util.Stack;

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
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
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
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value, int now_block) {
    if(reg_value.containsKey(num1)) {
      num1 = new String(reg_value.get(num1));
    }
    if(reg_value.containsKey(num2)) {
      num2 = new String(reg_value.get(num2));
    }
    if(variable_stack.containsKey(src)) {
      src = new String(variable_stack.get(src).peek().name);
    }
    if(reg_value.containsKey(src)) {
      src = new String(reg_value.get(src));
    }
  }
}
