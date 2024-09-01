package IRSentence;

import java.util.HashMap;
import Composer.Composer;

public class IRLoadStr extends IRCode {
  public String des = null;
  public String str_name = null;

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    String target = "t0";
    if (!registers.containsKey(des)) {
      return;
    }
    int value = registers.get(des);
    if (value >= 0) {
      target = register_name.get(value);
    }
    buffer.add("lui " + target + ", %hi(" + str_name.substring(1) + ".data)");
    buffer.add("addi " + target + ", " + target + ", %lo(" + str_name.substring(1) + ".data)");
    if (value < 0) {
      int place = now_depth + (value * 4);
      if ((place >> 11) == 0) {
        buffer.add("sw t0, " + place + "(sp)");
      } else {
        buffer.add("li t1, " + place);
        buffer.add("add t1, t1, sp");
        buffer.add("sw t0, 0(t1)");
      }
    }
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
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
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    def.put(des, null);
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRLoadStr return_value = new IRLoadStr();
    return_value.str_name = new String(str_name);
    if (now_name.containsKey(des)) {
      return_value.des = new String(now_name.get(des));
    } else {
      if (is_global.containsKey(des)) {
        return_value.des = new String(des);
      } else {
        return_value.des = new String("%reg$" + (++machine.tmp_time));
        now_name.put(des, return_value.des);
      }
    }
    return return_value;
  }

  @Override
  public void AliveUseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    if (!use.containsKey(des)) {
      dead = true;
    } else {
      dead = false;
      UseDefCheck(def, use);
    }
  }
}
