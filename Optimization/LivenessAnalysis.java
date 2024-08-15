package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import IRSentence.*;
import Composer.*;

public class LivenessAnalysis {
  Composer machine = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> liveness = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> use = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> def = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> in = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> out = new HashMap<>();

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
      if (code instanceof IRFuncend) {
        use.put(now, res_use);
        def.put(now, res_def);
      }
      code.UseDefCheck(res_def, res_use);
    }
    return;
  }

  void InOutCheck() {
    ArrayList<Integer> start = new ArrayList<>();
    for (int node : graph.keySet()) {
      if (graph.get(node).isEmpty()) {
        start.add(node);
      }
    }
    Queue<Integer> check_list = new LinkedList<>();
    for (int end : start) {
      check_list.add(end);
    }
    while (!check_list.isEmpty()) {
      boolean flag = false;
      int to_check = check_list.poll();
      HashMap<String, Boolean> res = new HashMap<>();
      for (String out_v : out.get(to_check).keySet()) {
        res.put(out_v, null);
      }
      for (String def_v : def.get(to_check).keySet()) {
        res.remove(def_v);
      }
      if (!in.containsKey(to_check)) {
        in.put(to_check, new HashMap<>());
      }
      HashMap<String, Boolean> to_operate = in.get(to_check);
      for (String use_v : use.get(to_check).keySet()) {
        if (!to_operate.containsKey(use_v)) {
          to_operate.put(use_v, null);
          flag = true;
        }
      }
      for (String res_v : res.keySet()) {
        if (!to_operate.containsKey(res_v)) {
          to_operate.put(res_v, null);
          flag = true;
        }
      }
      if (flag) {
        if (pre.containsKey(to_check)) {
          for (int pre_v : pre.get(to_check).keySet()) {
            if (!out.containsKey(pre_v)) {
              out.put(pre_v, new HashMap<>());
            }
            boolean flag_2 = false;
            HashMap<String, Boolean> out_check = out.get(pre_v);
            for (String value : to_operate.keySet()) {
              if(!out_check.containsKey(value)) {
                out_check.put(value, null);
                flag_2 = true;
              }
            }
            if(flag_2) {
              check_list.add(pre_v);
            }
          }
        }
      }
    }
    return;
  }
}
