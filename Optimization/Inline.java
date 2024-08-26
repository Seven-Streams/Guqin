package Optimization;

import java.util.HashMap;
import IRSentence.*;
import Composer.*;

public class Inline {
  Composer machine = null;
  HashMap<String, Boolean> ready_to_inline = new HashMap<>();
  HashMap<String, Integer> entry = new HashMap<>();
  boolean flag = true;
  boolean first = true;

  public Inline(Composer _machine) {
    machine = _machine;
  }

  public void Optim(int bound) throws Exception {
    flag = true;
    first = true;
    int cnt = 0;
    while (flag) {
      flag = false;
      cnt++;
      ready_to_inline.clear();
      CheckGlobal();
      FuncCheck(bound);
      InlineFunc();
      EmbeddingInline();
      first = false;
      if(cnt >= 2) {
        break;
      }
    }
    return;
  }

  void CheckGlobal() {
    for (IRCode code : machine.global) {
      code.CheckGlobal();
    }
    for (IRCode chars : machine.const_str) {
      chars.CheckGlobal();
    }
  }

  void FuncCheck(int bound) {
    ready_to_inline.clear();
    entry.clear();
    String name = null;
    int sentence_cnt = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      sentence_cnt++;
      if (code instanceof IRFunc) {
        name = ((IRFunc) code).name;
        entry.put(name, i);
        sentence_cnt = 1;
      }
      if (code instanceof IRFuncend) {
        if (sentence_cnt <= bound) {
          ready_to_inline.put(new String(name), true);
        } else {
          ready_to_inline.put(new String(name), false);
        }
      }
    }
  }

  void InlineFunc() throws Exception {
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFuncall) {
        IRFuncall call = (IRFuncall) code;
        if (ready_to_inline.containsKey(call.func_name) && ((ready_to_inline.get(call.func_name)) || first)) {
          InlineFunc to_inline = GetInline(entry.get(call.func_name), call);
          machine.generated.set(i, to_inline);
        }
      }
    }
    return;
  }

  InlineFunc GetInline(int start, IRFuncall calling_info) throws Exception {
    InlineFunc return_value = new InlineFunc();
    HashMap<Integer, Integer> label_replace = new HashMap<>();
    HashMap<String, String> name_replace = new HashMap<>();
    IRFunc declar = (IRFunc) machine.generated.get(start);
    for (int i = 0; i < calling_info.reg.size(); i++) {
      name_replace.put(new String(declar.names.get(i)), new String(calling_info.reg.get(i)));
    }
    label_replace.put(0, ++machine.label_number);
    // 0 represented the end of the inline fuc.
    if (calling_info.target_reg != null) {
      name_replace.put("return", calling_info.target_reg);
    }
    // This is the return value.
    for (int i = (start + 1); i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      IRCode to_add = (code.GetInline(name_replace, label_replace, machine));
      if (to_add != null) {
        return_value.operations.add(to_add);
      }
      if (code instanceof IRReturn) {
        return_value.operations.add(new IRjmp(label_replace.get(0)));
      }
      if (code instanceof IRFuncend) {
        break;
      }
    }
    return return_value;
  }

  void EmbeddingInline() {
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof InlineFunc) {
        InlineFunc check = (InlineFunc) code;
        if (check.operations.size() > 1) {
          flag = true;
          IRLabel label = (IRLabel) check.operations.get(check.operations.size() - 1);
          IRjmp res = new IRjmp(label.label);
          check.operations.add(check.operations.size() - 1, res);
          machine.generated.addAll(i + 1, ((InlineFunc) code).operations);
        }
        machine.generated.remove(i);
        i--;
      }
    }
    return;
  }
}
