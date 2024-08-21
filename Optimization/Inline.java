package Optimization;

import java.util.HashMap;

import Composer.*;
import IRSentence.*;

public class Inline {
  Composer machine = null;
  HashMap<String, Boolean> ready_to_inline = new HashMap<>();
  HashMap<String, Integer> entry = new HashMap<>();

  public Inline(Composer _machine) {
    machine = _machine;
  }

  public void FuncCheck(int bound) {
    ready_to_inline.clear();
    entry.clear();
    String name = null;
    int sentence_cnt = 0;
    boolean have_calling = false;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      sentence_cnt++;
      if (code instanceof IRFunc) {
        name = ((IRFunc) code).name;
        entry.put(name, i);
        have_calling = false;
        sentence_cnt = 1;
      }
      if (code instanceof IRFuncall) {
        have_calling = true;
      }
      if (code instanceof IRFuncend) {
        if ((sentence_cnt <= bound) && (!have_calling)) {
          ready_to_inline.put(name, true);
        } else {
          ready_to_inline.put(name, false);
        }
      }
    }
  }

  public void InlineFunc() {
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFuncall) {
        IRFuncall call = (IRFuncall)code;
        if (ready_to_inline.get(call.func_name)) {
          InlineFunc to_inline = GetInline(entry.get(call.func_name), call);
        }
      }
    }
  }

  public InlineFunc GetInline(int start, IRFuncall calling_info) {
    InlineFunc return_value = new InlineFunc();

    return return_value;
  }

  public void EmbeddingInline() {
    for(int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if(code instanceof InlineFunc) {
        machine.generated.addAll(i + 1, ((InlineFunc)code).operations);
        machine.generated.remove(i);
        continue;
      }
    }
    return;
  }
}
