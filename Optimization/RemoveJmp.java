package Optimization;

import Composer.*;
import java.util.*;
import IRSentence.*;

public class RemoveJmp {
  Composer machine = null;
  int func_cnt = 0;
  int largest_label = 0;
  HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();
  HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  HashMap<Integer, TreeMap<Integer, Boolean>> from = new HashMap<>();

  public RemoveJmp(Composer _machine) {
    machine = _machine;
  }

  public void Optim() {
    BuildGraph();
    CheckSingle();
    Rebuild();
  }

  void Rebuild() {
    machine.generated.clear();
    for (int i = -1; i >= func_cnt; i--) {
      TreeMap<Integer, Boolean> code_list = from.get(i);
      for (int label : code_list.keySet()) {
        if (blocks.containsKey(label)) {
          machine.generated.addAll(blocks.get(label));
        }
      }
    }
    return;
  }

  void BuildGraph() {
    func_cnt = 0;
    graph.clear();
    int now = 0;
    boolean accept = false;
    HashMap<Integer, Boolean> nxt = null;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRClass) {
        continue;
      }
      if (code instanceof IRFunc) {
        now = --func_cnt;
        nxt = new HashMap<>();
        blocks.put(now, new ArrayList<>());
        from.put(func_cnt, new TreeMap<>());
        from.get(func_cnt).put(func_cnt, null);
        accept = true;
      }
      if (code instanceof IRFuncend) {
        graph.put(now, nxt);
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        graph.put(now, nxt);
        nxt = new HashMap<>();
        now = label.label;
        if (largest_label < now) {
          largest_label = now;
        }
        from.get(func_cnt).put(now, null);
        blocks.put(now, new ArrayList<>());
        accept = true;
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
        graph.put(now, nxt);
        MoveBlock move = (MoveBlock) code;
        nxt = new HashMap<>();
        nxt.put(move.to, null);
        if (!pre.containsKey(move.to)) {
          pre.put(move.to, new HashMap<>());
        }
        now = move.num;
        if (largest_label < now) {
          largest_label = now;
        }
        pre.get(move.to).put(move.num, null);
        from.get(func_cnt).put(now, null);
        blocks.put(now, new ArrayList<>());
        accept = true;
      }
      if (accept) {
        blocks.get(now).add(code);
      }
      if (code instanceof IRjmp || code instanceof Conditionjmp || code instanceof MoveBlock) {
        accept = false;
      }
    }
    return;
  }

  void CheckSingle() {
    for (int i = -1; i >= func_cnt; i--) {
      boolean flag = true;
      while (flag) {
        flag = false;
        if (graph.get(i).size() == 1) {
          int res = 0;
          for (int value : graph.get(i).keySet()) {
            res = value;
          }
          if (pre.get(res).size() == 1) {
            flag = true;
            ArrayList<IRCode> cat_1 = blocks.get(i);
            ArrayList<IRCode> cat_2 = blocks.get(res);
            if (cat_1.get(cat_1.size() - 1) instanceof MoveBlock) {
              MoveBlock to_change = (MoveBlock) cat_1.get(cat_1.size() - 1);
              to_change.to = null;
            } else {
              cat_1.remove(cat_1.size() - 1);
            }
            if (cat_2.get(0) instanceof MoveBlock) {
              MoveBlock to_change = (MoveBlock) cat_2.get(0);
              to_change.num = null;
            } else {
              cat_2.remove(0);
            }
            cat_1.addAll(cat_2);
            graph.put(i, graph.get(res));
            graph.remove(res);
            pre.remove(res);
            blocks.remove(res);
            for (int value : graph.get(i).keySet()) {
              pre.get(value).remove(res);
              pre.get(value).put(i, null);
            }
          }
        }
      }
    }
    for (int i = 1; i <= largest_label; i++) {
      boolean flag = true;
      if (!graph.containsKey(i)) {
        continue;
      }
      while (flag) {
        flag = false;
        if (graph.get(i).size() == 1) {
          int res = 0;
          for (int value : graph.get(i).keySet()) {
            res = value;
          }
          if (pre.get(res).size() == 1) {
            flag = true;
            ArrayList<IRCode> cat_1 = blocks.get(i);
            ArrayList<IRCode> cat_2 = blocks.get(res);
            if (cat_1.get(cat_1.size() - 1) instanceof MoveBlock) {
              MoveBlock to_change = (MoveBlock) cat_1.get(cat_1.size() - 1);
              to_change.to = null;
            } else {
              cat_1.remove(cat_1.size() - 1);
            }
            if (cat_2.get(0) instanceof MoveBlock) {
              MoveBlock to_change = (MoveBlock) cat_2.get(0);
              to_change.num = null;
            } else {
              cat_2.remove(0);
            }
            cat_1.addAll(cat_2);
            graph.put(i, graph.get(res));
            graph.remove(res);
            pre.remove(res);
            blocks.remove(res);
            for (int value : graph.get(i).keySet()) {
              pre.get(value).remove(res);
              pre.get(value).put(i, null);
            }
          }
        }
      }
    }
  }
}
