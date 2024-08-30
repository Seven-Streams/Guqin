package Optimization;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import Composer.*;
import IRSentence.*;

public class SCCP {
  Composer machine = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  int func_cnt = 0;
  public HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();

  public SCCP(Composer _machine) {
    machine = _machine;
  }

  void Optim() {
    BuildGraph();
    OptimFunc();
    RemoveDead();
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

  void OptimFunc() {
    for (int i = -1; i >= func_cnt; i--) {
      Forward(i);
    }
    return;
  }

  void Forward(int index) {
    HashMap<Integer, Boolean> visit = new HashMap<>();
    HashMap<String, String> value = new HashMap<>();
    // 0:bottom. 1:top. 2:const.
    HashMap<String, HashMap<IRCode, Boolean>> use = new HashMap<>();
    Queue<Integer> my_queue = new LinkedList<>();
    visit.put(index, null);
    my_queue.add(index);
    HashMap<String, Boolean> res_use = new HashMap<>();
    HashMap<String, Boolean> res_def = new HashMap<>();
    while (!my_queue.isEmpty()) {
      int head = my_queue.poll();
      ArrayList<IRCode> to_check = blocks.get(head);
      for (IRCode code : to_check) {
        res_use.clear();
        res_def.clear();
        code.UseDefCheck(res_def, res_use);
        for (String use_v : res_use.keySet()) {
          if (!use.containsKey(use_v)) {
            use.put(use_v, new HashMap<>());
          }
          use.get(use_v).put(code, null);
        }
      }
      if (graph.containsKey(head)) {
        for (int to : graph.get(head).keySet()) {
          if (!visit.containsKey(to)) {
            my_queue.add(to);
            visit.put(to, null);
          }
        }
      }
    }
    visit.clear();
    visit.put(index, null);
    my_queue.add(index);
    Queue<IRCode> uses = new LinkedList<>();
    while (!my_queue.isEmpty()) {
      int head = my_queue.poll();
      ArrayList<IRCode> to_check = blocks.get(head);
      for (IRCode code : to_check) {
        res_use.clear();
        res_def.clear();
        code.UseDefCheck(res_def, res_use);
        String output = code.ConstCheck(value);
        if (output != null) {
          for (String def_v : res_def.keySet()) {
            value.put(def_v, output);
            for (IRCode influenced : use.get(def_v).keySet()) {
              uses.add(influenced);
            }
          }
        }
      }
      if (graph.containsKey(head)) {
        for (int to : graph.get(head).keySet()) {
          if (!visit.containsKey(to)) {
            my_queue.add(to);
            visit.put(to, null);
          }
        }
      }
    }
    while (!uses.isEmpty()) {
      res_use.clear();
      res_def.clear();
      IRCode code = uses.poll();
      code.UseDefCheck(res_def, res_use);
      String result = code.ConstCheck(value);
      if (result != null) {
        for (String def_v : res_def.keySet()) {
          value.put(def_v, result);
          for (IRCode influenced : use.get(def_v).keySet()) {
            uses.add(influenced);
          }
        }
      }
    }
    return;
  }

  void RemoveDead() {
    for (int i = machine.generated.size() - 1; i >= 0; i--) {
      if (machine.generated.get(i).dead) {
        machine.generated.remove(i);
      }
    }
    return;
  }
}