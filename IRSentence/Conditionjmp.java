package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
import Optimization.NameLabelPair;

public class Conditionjmp extends IRCode {
  public int label1 = 0;
  public int label2 = 0;
  public String reg = null;

  @Override
  public void CodePrint() {
    System.out.println("br i1 " + reg + "," + " label %b" + label1 + ", label %b" + label2);
    return;
  }

  public Conditionjmp() {
  }

  public Conditionjmp(int n1, int n2, String _reg) {
    label1 = n1;
    label2 = n2;
    reg = new String(_reg);
  }

  @Override
  public void Codegen() throws Exception {
    if (is_global.containsKey(reg) && is_global.get(reg)) {
      throw new Exception("I don't think I will use this.");
    } else {
      String addr = relative_addr.get(reg);
      System.out.println("lw a0, " + addr);
      System.out.println("beqz a0, b" + Integer.toString(label2));
      System.out.println("j b" + Integer.toString(label1));
      return;
    }
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
    try {
      Integer.parseInt(reg);
    } catch (NumberFormatException e) {
      if ((!def.containsKey(reg)) && CheckLit(reg)) {
        use.put(reg, null);
      }
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name) {
    if (CheckLit(reg)) {
      int value = registers.get(reg);
      if (value >= 0) {
        System.out.println("beqz " + register_name.get(value) + ", b" + label2);
      } else {
        value = -value;
        if((value >> 10) == 0) {
        System.out.println("lw t0, " + (-value * 4) + "(s0)");
        } else {
          System.out.println("lui t0" + (value >> 10));
          System.out.println("addi t0, t0, " + ((value << 2) & 0x00000fff));
          System.out.println("neg t0, t0");
          System.out.println("add t0, t0, s0");
          System.out.println("lw t0, 0(t0)");
        }
        System.out.println("beqz t0, b" + label2);
      }
    } else {
      if (reg.equals("true")) {
        System.out.println("j b" + label1);
      } else {
        System.out.println("j b" + label2);
      }
      return;
    }
    System.out.println("j b" + label1);
    return;
  }
}
