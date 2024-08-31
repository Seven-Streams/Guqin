package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import Composer.Composer;
import IRSentence.Conditionjmp;
import IRSentence.IRClass;
import IRSentence.IRCode;
import IRSentence.IRFunc;
import IRSentence.IRFuncend;
import IRSentence.IRLabel;
import IRSentence.IRjmp;

public class LoopOptimization {
  Composer machine = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> dominate = new HashMap<>();
  int func_cnt = 0;
  public HashMap<Integer, Integer> Idom = new HashMap<>();
  public HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();
  public HashMap<Integer, ArrayList<Integer>> Idom_up_down = new HashMap<>();

  public LoopOptimization(Composer _machine) {
    machine = _machine;
  }

  void Optim() {
    BuildGraph();
    BuildDominate();
    BuildIdom();
    // TODO:
  }

  void BuildGraph() {
    graph.clear();
    int now = 0;
    HashMap<Integer, Boolean> nxt = null;
    ArrayList<IRCode> buffer = null;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRClass) {
        continue;
      }
      if (code instanceof IRFunc) {
        now = --func_cnt;
        buffer = new ArrayList<>();
        blocks.put(now, buffer);
        nxt = new HashMap<>();
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
      if (code instanceof IRFuncend) {
        graph.put(now, nxt);
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        graph.put(now, nxt);
        nxt = new HashMap<>();
        now = label.label;
        buffer = new ArrayList<>();
        blocks.put(now, buffer);
      }
      buffer.add(code);
    }
    return;
  }

  void BuildDominate() {
    dominate.clear();
    ArrayList<Integer> to_init = new ArrayList<>();
    for (int value : graph.keySet()) {
      to_init.add(value);
    }
    for (int key : graph.keySet()) {
      HashMap<Integer, Boolean> init_domain = new HashMap<>();
      for (int value : to_init) {
        init_domain.put(value, null);
      }
      dominate.put(key, init_domain);
    }
    for (int i = -1; i >= func_cnt; i--) {
      boolean update = true;
      dominate.get(i).clear();
      dominate.get(i).put(i, null);
      while (update) {
        update = false;
        Queue<Integer> to_visit = new LinkedList<>();
        for (int value : graph.get(i).keySet()) {
          to_visit.add(value);
        }
        while (!to_visit.isEmpty()) {
          int now = to_visit.poll();
          HashMap<Integer, Boolean> from = pre.get(now);
          HashMap<Integer, Boolean> op = dominate.get(now);
          int last = op.size();
          op.clear();
          ArrayList<Integer> from_point = new ArrayList<>();
          for (int value : from.keySet()) {
            from_point.add(value);
          }
          for (int to_test : dominate.get(from_point.get(0)).keySet()) {
            boolean flag = true;
            for (int j = 1; j < from_point.size(); j++) {
              if (!dominate.get(from_point.get(j)).containsKey(to_test)) {
                flag = false;
                break;
              }
            }
            if (flag) {
              op.put(to_test, null);
            }
          }
          op.put(now, null);
          if (last != op.size()) {
            for (int to_add : graph.get(now).keySet()) {
              to_visit.add(to_add);
            }
            update = true;
          }
        }
      }
    }
    return;
  }

  void BuildIdom() {
    Idom.clear();
    Idom_up_down.clear();
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : dominate.entrySet()) {
      int size = entry.getValue().size();
      for (int value : entry.getValue().keySet()) {
        if ((dominate.get(value).size() + 1) == size) {
          Idom.put(entry.getKey(), value);
          if (!Idom_up_down.containsKey(value)) {
            Idom_up_down.put(value, new ArrayList<>());
          }
          Idom_up_down.get(value).add(entry.getKey());
          break;
        }
      }
    }
    return;
  }

}
