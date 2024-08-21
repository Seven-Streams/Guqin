package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
import Optimization.NameLabelPair;

public class IRReturn extends IRCode {
  public String reg = null;
  public String return_type = null;

  @Override
  public void CodePrint() {
    if (reg == null) {
      System.out.println("ret void");
    } else {
      System.out.println("ret " + return_type + " " + reg);
    }
    return;
  }

  @Override
  public void Codegen() {
    if (reg != null) {
      if (!(reg.equals("true") || reg.equals("false") || reg.equals("null"))) {
        String return_value = null;
        try {
          int test = Integer.parseInt(reg);
          System.out.println("li a0, " + test);
        } catch (NumberFormatException e) {
          return_value = relative_addr.get(reg);
          System.out.println("lw a0, " + return_value);
        }
      } else {
        if (reg.equals("true")) {
          System.out.println("li a0, 1");
        } else {
          System.out.println("li a0, 0");
        }
      }
    }
    System.out.println("j .return" + func_num);
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
    if (reg != null) {
      try {
        Integer.parseInt(reg);
      } catch (NumberFormatException e) {
        if (CheckLit(reg)) {
          if (use.containsKey(reg)) {
            use.put(reg, use.get(reg) + 1);
          } else {
            use.put(reg, 1);
          }
        }
      }
    }
    return;
  }

  @Override
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated) {
    while (replace.containsKey(reg)) {
      reg = new String(replace.get(reg));
    }
    return;
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    while (single.containsKey(reg)) {
      reg = new String(single.get(reg));
    }
    return;
  }

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    if (variable_stack.containsKey(reg)) {
      reg = new String(variable_stack.get(reg).peek().name);
    }
    if (reg_value.containsKey(reg)) {
      reg = new String(reg_value.get(reg));
    }
    return;
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    if (reg != null) {
      try {
        Integer.parseInt(reg);
      } catch (NumberFormatException e) {
        if ((!def.containsKey(reg)) && (CheckLit(reg))) {
          use.put(reg, null);
        }
      }
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    if (reg != null) {
      if (!(reg.equals("true") || reg.equals("false") || reg.equals("null"))) {
        try {
          int test = Integer.parseInt(reg);
          System.out.println("li a0, " + test);
        } catch (NumberFormatException e) {
          int num = registers.get(reg);
          if (num >= 0) {
            System.out.println("mv " + "a0, " + register_name.get(num));
          } else {
            if ((num >> 9) == 0) {
              System.out.println("lw a0, " + (num * 4) + "(s0)");
            } else {
              System.out.println("li t0, " + (num * 4));
              System.out.println("add t0, t0, s0");
              System.out.println("lw a0, 0(t0)");
            }
          }
        }
      } else {
        if (reg.equals("true")) {
          System.out.println("li a0, 1");
        } else {
          System.out.println("li a0, 0");
        }
      }
    }
    System.out.println("j .return" + func_num);
    return;
  }
}