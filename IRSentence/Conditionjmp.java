package IRSentence;

import java.util.HashMap;
import java.util.Stack;
import Composer.Composer;
import Optimization.NameLabelPair;

public class Conditionjmp extends IRCode {
  public int label1 = 0;
  public int label2 = 0;
  public static int cnt = 0;
  public String reg = null;
  public Integer const_jmp = null;

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
    try {
      int value = Integer.parseInt(reg);
      if (value == 1) {
        buffer.add("j b" + label1);
      } else {
        buffer.add("j b" + label2);
      }
    } catch (NumberFormatException e) {
      if (CheckLit(reg)) {
        int now_label = ++cnt;
        int value = registers.get(reg);
        if (value >= 0) {
          buffer.add("beqz " + register_name.get(value) + ", cond." + now_label);
        } else {
          value = now_depth + (value * 4);
          if ((value >> 11) == 0) {
            buffer.add("lw t0, " + value + "(sp)");
          } else {
            buffer.add("li t0, " + value);
            buffer.add("add t0, t0, sp");
            buffer.add("lw t0, 0(t0)");
          }
          buffer.add("beqz t0, " + "cond." + now_label);
        }
        buffer.add("j b" + label1);
        buffer.add("cond." + now_label + ":");
        buffer.add("j b" + label2);
        return;
      } else {
        if (reg.equals("true")) {
          buffer.add("j b" + label1);
        } else {
          buffer.add("j b" + label2);
        }
        return;
      }
    }
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    Conditionjmp return_value = new Conditionjmp();
    try {
      Integer.parseInt(reg);
      return_value.reg = reg;
    } catch (NumberFormatException e) {
      if ((CheckLit(reg)) && (!is_global.containsKey(reg))) {
        if (now_name.containsKey(reg)) {
          return_value.reg = now_name.get(reg);
        } else {
          return_value.reg = "%reg$" + (++machine.tmp_time);
          now_name.put(reg, return_value.reg);
        }
      } else {
        return_value.reg = reg;
      }
    }
    if (now_label.containsKey(label1)) {
      return_value.label1 = now_label.get(label1);
    } else {
      return_value.label1 = ++machine.label_number;
      now_label.put(label1, return_value.label1);
    }
    if (now_label.containsKey(label2)) {
      return_value.label2 = now_label.get(label2);
    } else {
      return_value.label2 = ++machine.label_number;
      now_label.put(label2, return_value.label2);
    }
    return return_value;
  }

  @Override
  public void GlobalConstReplace(HashMap<String, String> mapping) {
    if (mapping.containsKey(reg)) {
      reg = new String(mapping.get(reg));
    }
    return;
  }

  @Override
  public String ConstCheck(HashMap<String, String> replace) {
    if (replace.containsKey(reg)) {
      reg = new String(replace.get(reg));
    }
    try{
      int res_value = Integer.parseInt(reg);
      const_jmp = res_value == 0 ? 2 : 1;
    }catch(NumberFormatException e) {
      if(!CheckLit(reg)) {
        const_jmp = reg.equals("true") ? 1 : 2;
      }
    }
    return null;
  }
}