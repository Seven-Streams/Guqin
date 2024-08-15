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
          if ((test >> 12) != 0) {
            System.out.println("lui a0, " + (test >> 12));
          } else {
            System.out.println("andi a0, a0, 0");
          }
          System.out.println("addi a0, a0, " + (test & 0x00000fff));
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
    try {
      Integer.parseInt(reg);
    } catch (NumberFormatException e) {
      if (use.containsKey(reg)) {
        use.put(reg, use.get(reg) + 1);
      } else {
        use.put(reg, 1);
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
    try{
      Integer.parseInt(reg);
    }catch(NumberFormatException e) {
      if(!def.containsKey(reg)) {
        use.put(reg, null);
      }
    }
    return;
  }
}
