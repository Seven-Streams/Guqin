package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import IRSentence.*;
import Composer.*;

public class Inline {
  Composer machine = null;
  HashMap<String, Boolean> ready_to_inline = new HashMap<>();
  HashMap<String, Integer> entry = new HashMap<>();
  ArrayList<IRPhi> phis = new ArrayList<>();

  public Inline(Composer _machine) {
    machine = _machine;
  }

  public void Optim(int bound) throws Exception {
    ready_to_inline.clear();
    CheckGlobal();
    FuncCheck(bound);
    InlineFunc();
    EmbeddingInline();
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
    phis.clear();
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
      if (code instanceof IRPhi) {
        IRPhi phi = (IRPhi) code;
        phis.add(phi);
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
    int now_cnt = 0;
    int func_cnt = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFunc) {
        now_cnt = --func_cnt;
      }
      if (code instanceof IRLabel) {
        now_cnt = ((IRLabel) code).label;
      }
      if (code instanceof InlineFunc) {
        InlineFunc inline = (InlineFunc) code;
        if (inline.operations.get(inline.operations.size() - 1) instanceof IRLabel) {
          now_cnt = ((IRLabel) (inline.operations.get(inline.operations.size() - 1))).label;
        } else {
          now_cnt = ((IRLabel) (inline.operations.get(inline.operations.size() - 2))).label;
        }
      }
      if (code instanceof IRFuncall) {
        IRFuncall call = (IRFuncall) code;
        if (ready_to_inline.containsKey(call.func_name) && (ready_to_inline.get(call.func_name))) {
          InlineFunc to_inline = GetInline(entry.get(call.func_name), call, now_cnt);
          machine.generated.set(i, to_inline);
        }
        code = machine.generated.get(i);
        if (code instanceof InlineFunc) {
          InlineFunc inline = (InlineFunc) code;
          if (inline.operations.get(inline.operations.size() - 1) instanceof IRLabel) {
            now_cnt = ((IRLabel) (inline.operations.get(inline.operations.size() - 1))).label;
          } else {
            now_cnt = ((IRLabel) (inline.operations.get(inline.operations.size() - 2))).label;
          }
        }
      }
    }
    return;
  }

  InlineFunc GetInline(int start, IRFuncall calling_info, int now) throws Exception {
    InlineFunc return_value = new InlineFunc();
    HashMap<Integer, Integer> label_replace = new HashMap<>();
    HashMap<String, String> name_replace = new HashMap<>();
    return_value.operations.add(new IRjmp(++machine.label_number));
    return_value.operations.add(new IRLabel(machine.label_number));
    label_replace.put(-1, machine.label_number);
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
      if (to_add instanceof IRPhi) {
        phis.add((IRPhi) to_add);
      }
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
    if (calling_info.target_reg != null) {
      IRPhi return_phi = new IRPhi();
      return_phi.target = new String(calling_info.target_reg);
      int now_label = 0;
      for (int i = 0; i < return_value.operations.size(); i++) {
        IRCode code = return_value.operations.get(i);
        if (code instanceof IRLabel) {
          IRLabel label = (IRLabel) code;
          now_label = label.label;
        }
        if (code instanceof InlineFunc) {
          InlineFunc inline = (InlineFunc) code;
          if (inline.operations.get(inline.operations.size() - 1) instanceof IRLabel) {
            now_label = ((IRLabel) (inline.operations.get(inline.operations.size() - 1))).label;
          } else {
            now_label = ((IRLabel) (inline.operations.get(inline.operations.size() - 2))).label;
          }
        }
        if (code instanceof PseudoMove) {
          return_phi.labels.add(now_label);
          PseudoMove move = (PseudoMove) code;
          return_phi.values.add(move.src);
        }
      }
      for (int i = return_value.operations.size() - 1; i >= 0; i--) {
        IRCode code = return_value.operations.get(i);
        if (code instanceof PseudoMove) {
          return_value.operations.remove(i);
        }
      }
      return_value.operations.add(return_phi);
      phis.add(return_phi);
    }
    for (IRPhi phi : phis) {
      for (int i = 0; i < phi.labels.size(); i++) {
        if (phi.labels.get(i) == now) {
          phi.labels.set(i, label_replace.get(0));
          break;
        }
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
          if (check.operations.get(check.operations.size() - 1) instanceof IRLabel) {
            IRLabel label = (IRLabel) check.operations.get(check.operations.size() - 1);
            IRjmp res = new IRjmp(label.label);
            check.operations.add(check.operations.size() - 1, res);
          } else {
            IRLabel label = (IRLabel) check.operations.get(check.operations.size() - 2);
            IRjmp res = new IRjmp(label.label);
            check.operations.add(check.operations.size() - 2, res);
          }
          machine.generated.addAll(i + 1, ((InlineFunc) code).operations);
        }
        machine.generated.remove(i);
        i--;
      }
    }
    return;
  }
}