package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
import Optimization.NameLabelPair;
import Optimization.PseudoMove;

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
          buffer.add("li a0, " + test);
        } catch (NumberFormatException e) {
          int num = registers.get(reg);
          if (num >= 0) {
            if (!register_name.get(num).equals("a0")) {
              buffer.add("mv " + "a0, " + register_name.get(num));
            }
          } else {
            if ((num >> 9) == 0) {
              buffer.add("lw a0, " + (num * 4) + "(s0)");
            } else {
              buffer.add("li t0, " + (num * 4));
              buffer.add("add t0, t0, s0");
              buffer.add("lw a0, 0(t0)");
            }
          }
        }
      } else {
        if (reg.equals("true")) {
          buffer.add("li a0, 1");
        } else {
          buffer.add("li a0, 0");
        }
      }
    }
    buffer.add("j .return" + func_num);
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    if (reg != null) {
      PseudoMove move = new PseudoMove("isnull", "isnull");
      try {
        Integer.parseInt(reg);
        move.src = new String(reg);
      } catch (NumberFormatException e) {
        if (is_global.containsKey(reg) || (!CheckLit(reg))) {
          move.src = new String(reg);
        } else {
          if (now_name.containsKey(reg)) {
            move.src = new String(now_name.get(reg));
          } else {
            move.src = new String("%reg$" + (++machine.tmp_time));
            now_name.put(reg, move.src);
          }
        }
      }
      move.des = new String(now_name.get("return"));
      return move;
    }
    return null;
  }

  @Override
  public void GlobalConstReplace(HashMap<String, String> mapping) {
    if(mapping.containsKey(reg)) {
      reg = new String(mapping.get(reg));
    }
    return;
  }

  @Override
  public String ConstCheck(HashMap<String, String> replace) {
    if(reg != null && replace.containsKey(reg)) {
      reg = new String(replace.get(reg));
    }
    return null;
  }
}