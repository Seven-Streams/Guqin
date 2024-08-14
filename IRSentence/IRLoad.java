package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Optimization.NameLabelPair;

public class IRLoad extends IRCode {
  public String des = null;
  public String src = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.println(des + " = load " + type + ", ptr " + src);
    return;
  }

  @Override
  public void Codegen() {
    if (is_global.get(src)) {
      System.out.println("lui a0, " + "%hi(" + src.substring(1) + ")");
      System.out.println("addi a0, a0, %lo(" + src.substring(1) + ")");
      System.out.println("lw a0, 0(a0)");
    } else {
      String addr_t = relative_addr.get(src);
      System.out.println("lw a0, " + addr_t);
      System.out.println("lw a0, 0(a0)");
    }
    if (!relative_addr.containsKey(des)) {
      is_global.put(des, false);
      now_s0 += 4;
      relative_addr.put(des, Integer.toString(-now_s0) + "(s0)");
    }
    System.out.println("sw a0, " + relative_addr.get(des));
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
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
      Integer.parseInt(des);
    } catch (NumberFormatException e) {
      if (def.containsKey(des)) {
        def.put(des, def.get(des) + 1);
      } else {
        def.put(des, 1);
      }
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
    return !use.containsKey(des);
  }

  @Override
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated) {
    if (replace.containsKey(src)) {
      deprecated.put(des, null);
      replace.put(des, replace.get(src));
    }
    return;
  }

  @Override
  public boolean AssignOnceRemove(HashMap<String, ?> deprecated) {
    if (deprecated.containsKey(des) || deprecated.containsKey(src)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    boolean flag = false;
    while (single.containsKey(src)) {
      flag = true;
      src = new String(single.get(src));
    }
    if (flag) {
      single.put(des, src);
    }
    return;
  }

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    if (variable_stack.containsKey(src)) {
      reg_value.put(des, variable_stack.get(src).peek().name);
      return;
    }
    if (reg_value.containsKey(src)) {
      src = new String(reg_value.get(src));
    }
    return;
  }
}
