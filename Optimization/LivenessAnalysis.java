package Optimization;

import java.util.HashMap;
import IRSentence.*;
import Composer.*;

public class LivenessAnalysis {
  Composer machine = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> liveness = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> use = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> def = new HashMap<>();

  public LivenessAnalysis(Composer _machine) {
    machine = _machine;
  }

  void BuildGraph() {
    int func_cnt = 0;
    int now = 0;
    HashMap<Integer, Boolean> nxt = null;
    for (IRCode code : machine.generated) {
      if (code instanceof IRFunc) {
        now = --func_cnt;
        nxt = new HashMap<>();
      }
      if (code instanceof IRFuncend) {
        graph.put(now, nxt);
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        graph.put(now, nxt);
        nxt = new HashMap<>();
        now = label.label;
      }
      if (code instanceof IRjmp) {
        IRjmp jmp = (IRjmp) code;
        nxt.put(jmp.label, null);
        if (!pre.containsKey(jmp.label)) {
          pre.put(jmp.label, new HashMap<>());
        }
        pre.get(jmp.label).put(now, null);
      }
      if (code instanceof Conditionjmp) {
        Conditionjmp jmp = (Conditionjmp) code;
        nxt.put(jmp.label1, null);
        nxt.put(jmp.label2, null);
        if (!pre.containsKey(jmp.label1)) {
          pre.put(jmp.label1, new HashMap<>());
        }
        if (!pre.containsKey(jmp.label2)) {
          pre.put(jmp.label2, new HashMap<>());
        }
        pre.get(jmp.label1).put(now, null);
        pre.get(jmp.label2).put(now, null);
      }
      if (code instanceof MoveBlock) {
        MoveBlock move = (MoveBlock) code;
        nxt.put(move.to, null);
        if (!pre.containsKey(move.to)) {
          pre.put(move.to, new HashMap<>());
        }
        now = move.num;
        pre.get(move.to).put(move.num, null);
      }
    }
    return;
  }

  void UseDefCheck() {
    int func = 0;
    int now = 0;
    HashMap<String, Boolean> res_def = null;
    HashMap<String, Boolean> res_use = null;
    for (IRCode code : machine.generated) {
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        res_def = new HashMap<>();
        res_use = new HashMap<>();
        now = label.label;
      }
      if (code instanceof IRFunc) {
        if (res_def != null) {
          use.put(now, res_use);
          def.put(now, res_def);
        }
        res_def = new HashMap<>();
        res_use = new HashMap<>();
        now = --func;
      }
      if (code instanceof MoveBlock) {
        MoveBlock move = (MoveBlock) code;
        use.put(now, res_use);
        def.put(now, res_def);
        res_def = new HashMap<>();
        res_use = new HashMap<>();
        now = move.num;
      }
      if(code instanceof IRFuncend) {
        use.put(now, res_use);
        def.put(now, res_def);
      }
      code.UseDefCheck(res_def, res_use);
    }
    return;
  }
}
