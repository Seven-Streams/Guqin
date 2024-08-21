package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
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
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
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
    while (single.containsKey(src) && (single.get(src) != null)) {
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
      if (!variable_stack.get(src).isEmpty()) {
        reg_value.put(des, variable_stack.get(src).peek().name);
      }
      return;
    }
    if (reg_value.containsKey(src)) {
      src = new String(reg_value.get(src));
    }
    return;
  }

  @Override
  public boolean ToRemove(HashMap<String, Stack<NameLabelPair>> variable_stack) {
    return variable_stack.containsKey(src);
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    if ((!def.containsKey(src)) && (!is_global.containsKey(src))) {
      use.put(src, null);
    }
    def.put(des, null);
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    String addr = "t0";
    if (is_global.containsKey(src)) {
      System.out.println("lui t0, " + "%hi(" + src.substring(1) + ")");
      System.out.println("addi t0, t0, %lo(" + src.substring(1) + ")");
    } else {
      int place = registers.get(src);
      if (place >= 0) {
        addr = register_name.get(place);
      } else {
        place = -place;
        if ((place >> 10) == 0) {
          System.out.println("lw t0, " + (-place * 4) + "(s0)");
        } else {
          System.out.println("lui t0, " + (place >> 10));
          System.out.println("addi t0, t0, " + ((place << 2) & (0x00000fff)));
          System.out.println("neg t0, t0");
          System.out.println("add t0, t0, s0");
          System.out.println("lw t0, 0(t0)");
        }
      }
    }
    int target_value = registers.get(des);
    if (target_value < 0) {
      System.out.println("lw t0, 0(" + addr + ")");
    } else {
      System.out.println("lw " + register_name.get(target_value) + ", 0(" + addr + ")");
    }
    if (target_value < 0) {
      target_value = -target_value;
      if ((target_value >> 10) == 0) {
        System.out.println("sw t0, " + (-target_value * 4) + "(s0)");
      } else {
        System.out.println("lui t1, " + (target_value >> 10));
        System.out.println("addi t1, t1, " + ((target_value << 2) & (0x00000fff)));
        System.out.println("neg t1, t1");
        System.out.println("add t1, t1, s0");
        System.out.println("sw t0, 0(t1)");
      }
    }
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRLoad return_value = new IRLoad();
    return_value.type = new String(type);
    if(now_name.containsKey(src)) {
      return_value.src = new String(now_name.get(src));
    } else {
      return_value.src = new String(src);
    }
    if(now_name.containsKey(des)) {
      return_value.des = new String(now_name.get(des));
    } else {
      if(is_global.containsKey(des)) {
        return_value.des = new String(des);
      } else {
        return_value.des = new String("%reg$" + (++machine.tmp_time));
        now_name.put(des, return_value.des);
      }
    }
    return return_value;
  }
}