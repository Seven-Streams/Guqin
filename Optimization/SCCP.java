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
  public HashMap<IRCode, Integer> cond_block = new HashMap<>();
  // This is used to show which block cond_jmp is from.
  int func_cnt = 0;
  public HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();
  public HashMap<Integer, Boolean> untouchable = new HashMap<>();

  public SCCP(Composer _machine) {
    machine = _machine;
  }

  void Optim() {
    BuildGraph();
    OptimFunc();
    RemoveDead();
    ReplaceJmp();
  }

  void BuildGraph() {
    graph.clear();
    pre.clear();
    cond_block.clear();
    blocks.clear();
    untouchable.clear();
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
        cond_block.put(code, now);
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
    HashMap<String, HashMap<IRCode, Boolean>> use_relation = new HashMap<>();
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
          if (!use_relation.containsKey(use_v)) {
            use_relation.put(use_v, new HashMap<>());
          }
          use_relation.get(use_v).put(code, null);
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
    // Build use relationships.
    visit.clear();
    visit.put(index, null);
    my_queue.add(index);
    Queue<IRCode> to_update = new LinkedList<>();
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
            for (IRCode influenced : use_relation.get(def_v).keySet()) {
              to_update.add(influenced);
            }
          }
        }
        if ((code instanceof Conditionjmp) && ((Conditionjmp) code).const_jmp != null) {
          Conditionjmp jmp = (Conditionjmp) code;
          int reserved, deprecated;
          reserved = jmp.const_jmp == 1 ? jmp.label1 : jmp.label2;
          deprecated = jmp.const_jmp == 1 ? jmp.label2 : jmp.label1;
          if (reserved != deprecated) {
            int now_index = cond_block.get(code);
            RemoveEdge(now_index, deprecated, to_update);
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
    while (!to_update.isEmpty()) {
      res_use.clear();
      res_def.clear();
      IRCode code = to_update.poll();
      code.UseDefCheck(res_def, res_use);
      String result = code.ConstCheck(value);
      if (result != null) {
        for (String def_v : res_def.keySet()) {
          for (IRCode influenced : use_relation.get(def_v).keySet()) {
            to_update.add(influenced);
          }
        }
      }
      if ((code instanceof Conditionjmp) && ((Conditionjmp) code).const_jmp != null) {
        Conditionjmp jmp = (Conditionjmp) code;
        int reserved, deprecated;
        reserved = jmp.const_jmp == 1 ? jmp.label1 : jmp.label2;
        deprecated = jmp.const_jmp == 1 ? jmp.label2 : jmp.label1;
        if (reserved != deprecated) {
          int now_index = cond_block.get(code);
          RemoveEdge(now_index, deprecated, to_update);
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

  void RemoveEdge(int from, int to, Queue<IRCode> check_list) {
    ArrayList<IRCode> buffer_list = new ArrayList<>();
    if (untouchable.containsKey(to)) {
      return;
    }
    if (graph.containsKey(from)) {
      graph.get(from).remove(to);
    }
    if (pre.containsKey(to)) {
      pre.get(to).remove(from);
    }
    if (blocks.containsKey(to)) {
      for (IRCode code : blocks.get(to)) {
        if (code instanceof IRPhi) {
          IRPhi phi = (IRPhi) code;
          for (int i = phi.values.size() - 1; i >= 0; i--) {
            if (phi.labels.get(i) == from) {
              phi.labels.remove(i);
              phi.values.remove(i);
              buffer_list.add(phi);
              break;
            }
          }
        } else {
          if (code instanceof IRLabel || code instanceof IRFunc) {
            continue;
          }
          break;
        }
      }
    }
    if (pre.containsKey(to)) {
      if (pre.get(to).isEmpty()) {
        untouchable.put(to, null);
        for (IRCode code : blocks.get(to)) {
          code.dead = true;
        }
        if (graph.containsKey(to)) {
          Queue<Integer> to_remove = new LinkedList<>();
          for (int des : graph.get(to).keySet()) {
            to_remove.add(des);
          }
          while (!to_remove.isEmpty()) {
            RemoveEdge(to, to_remove.poll(), check_list);
          }
        }
      } // Untouchable.
    }
    if (!untouchable.containsKey(to)) {
      for (IRCode code : buffer_list) {
        check_list.add(code);
      }
    }
    return;
  }

  void ReplaceJmp() {
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof Conditionjmp) {
        Conditionjmp jmp = (Conditionjmp) code;
        if (jmp.const_jmp != null) {
          IRjmp new_jmp = new IRjmp(jmp.const_jmp == 1 ? jmp.label1 : jmp.label2);
          machine.generated.set(i, new_jmp);
        }
      }
    }
    return;
  }
}