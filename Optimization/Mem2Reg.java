package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import IRSentence.*;
import Composer.*;

public class Mem2Reg {
  public Composer object = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> domainate = new HashMap<>();

  public Mem2Reg(Composer _object) {
    object = _object;
  }

  public void Optim() {
    RemoveNaiveUntouchable();
    Mem2RegEmpty();
    Mem2RegAssignOnce();
    Mem2RegAssignOneBlock();
    BuildGraph();
    BuildDominate();
  }

  int func_cnt = 0;

  void Mem2RegEmpty() {
    while (true) {
      HashMap<String, Integer> use = new HashMap<>();
      HashMap<String, Integer> def = new HashMap<>();
      for (ArrayList<IRCode> alloc_func : object.alloc.values()) {
        for (IRCode alloc_single : alloc_func) {
          alloc_single.CheckTime(use, def);
        }
      }
      for (IRCode code : object.generated) {
        code.CheckTime(use, def);
      }
      for (IRCode code : object.init) {
        code.CheckTime(use, def);
      }
      boolean remove = false;
      for (ArrayList<IRCode> alloc_func : object.alloc.values()) {
        boolean flag = false;
        for (int i = alloc_func.size() - 1; i >= 0; i--) {
          if (flag) {
            remove = true;
            alloc_func.remove(i + 1);
          }
          flag = alloc_func.get(i).EmptyStore(use);
        }
        if (flag) {
          remove = true;
          alloc_func.remove(0);
        }
      }
      boolean flag = false;
      for (int i = object.generated.size() - 1; i >= 0; i--) {
        if (flag) {
          remove = true;
          object.generated.remove(i + 1);
        }
        flag = object.generated.get(i).EmptyStore(use);
      }
      if (flag) {
        remove = true;
        object.generated.remove(0);
      }
      if (!remove) {
        break;
      }
    }
    return;
  }

  void Mem2RegAssignOnce() {
    HashMap<String, Integer> use = new HashMap<>();
    HashMap<String, Integer> def = new HashMap<>();
    for (IRCode generate : object.generated) {
      generate.CheckTime(use, def);
    }
    HashMap<String, String> replace = new HashMap<>();
    HashMap<String, Boolean> deprecated = new HashMap<>();
    for (ArrayList<IRCode> allocs : object.alloc.values()) {
      boolean flag = false;
      for (int i = allocs.size() - 1; i >= 0; i--) {
        if (flag) {
          allocs.remove(i + 1);
        }
        flag = allocs.get(i).AssignOnce(def);
        IRAlloc alloc = (IRAlloc) allocs.get(i);
        if (flag) {
          deprecated.put(new String(alloc.des), null);
        }
      }
      if (flag) {
        allocs.remove(0);
      }
    }
    for (IRCode generate : object.generated) {
      generate.UpdateAssignOnce(replace, deprecated);
    }
    boolean flag = false;
    for (int i = object.generated.size() - 1; i >= 0; i--) {
      if (flag) {
        object.generated.remove(i + 1);
      }
      flag = object.generated.get(i).AssignOnceRemove(deprecated);
    }
    if (flag) {
      object.generated.remove(0);
    }
    return;
  }

  void Mem2RegAssignOneBlock() {
    HashMap<String, HashMap<Integer, Boolean>> times = new HashMap<>();
    for (ArrayList<IRCode> allocs : object.alloc.values()) {
      for (int i = allocs.size() - 1; i >= 0; i--) {
        HashMap<Integer, Boolean> to_add = new HashMap<>();
        IRAlloc target = (IRAlloc) allocs.get(i);
        times.put(target.des, to_add);
      }
    }
    int now_block = 0;
    for (IRCode code : object.generated) {
      now_block = code.CheckBlock(times, now_block);
    }
    HashMap<String, String> single = new HashMap<>();
    for (Map.Entry<String, HashMap<Integer, Boolean>> value : times.entrySet()) {
      if (value.getValue().size() == 1) {
        single.put(new String(value.getKey()), null);
      }
    }
    for (ArrayList<IRCode> alloc_func : object.alloc.values()) {
      boolean flag = false;
      for (int i = alloc_func.size() - 1; i >= 0; i--) {
        if (flag) {
          alloc_func.remove(i + 1);
        }
        flag = alloc_func.get(i).SingleBlockRemove(single);
      }
      if (flag) {
        alloc_func.remove(0);
      }
    }
    for (IRCode code : object.generated) {
      code.UpdateSingleBlock(single);
    }
    boolean flag = false;
    for (int i = object.generated.size() - 1; i >= 0; i--) {
      if (flag) {
        object.generated.remove(i + 1);
      }
      flag = object.generated.get(i).AssignOnceRemove(single);
    }
    if (flag) {
      object.generated.remove(0);
    }
    return;
  }

  void RemoveNaiveUntouchable() {
    for (int i = 0; i < object.generated.size(); i++) {
      if (object.generated.get(i) instanceof IRReturn || object.generated.get(i) instanceof IRjmp
          || object.generated.get(i) instanceof Conditionjmp) {
        while (!(object.generated.get(i + 1) instanceof IRFuncend || object.generated.get(i + 1) instanceof IRLabel)) {
          object.generated.remove(i + 1);
        }
      }
    }
    return;
  }

  void BuildGraph() {
    func_cnt = 0;
    int now = 0;
    HashMap<Integer, Boolean> nxt = null;
    for (IRCode code : object.generated) {
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
    }
    return;
  }

  void PrintGraph() {
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> value : graph.entrySet()) {
      System.out.println(value.getKey() + ":");
      for (Map.Entry<Integer, Boolean> values : value.getValue().entrySet()) {
        System.out.println(values.getKey());
      }
    }
  }

  void Printpre() {
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> value : pre.entrySet()) {
      System.out.println(value.getKey() + ":");
      for (Map.Entry<Integer, Boolean> values : value.getValue().entrySet()) {
        System.out.println(values.getKey());
      }
    }
  }

  void BuildDominate() {
    ArrayList<Integer> to_init = new ArrayList<>();
    for (int value : graph.keySet()) {
      to_init.add(value);
    }
    for (int key : graph.keySet()) {
      HashMap<Integer, Boolean> init_domain = new HashMap<>();
      for (int value : to_init) {
        init_domain.put(value, null);
      }
      domainate.put(key, init_domain);
    }
    for (int i = -1; i >= func_cnt; i--) {
      boolean update = true;
      domainate.get(i).clear();
      domainate.get(i).put(i, null);
      while (update) {
        update = false;
        Queue<Integer> to_visit = new LinkedList<>();
        for (int value : graph.get(i).keySet()) {
          to_visit.add(value);
        }
        while (!to_visit.isEmpty()) {
          int now = to_visit.poll();
          HashMap<Integer, Boolean> from = pre.get(now);
          HashMap<Integer, Boolean> op = domainate.get(now);
          int last = op.size();
          op.clear();
          ArrayList<Integer> from_point = new ArrayList<>();
          for (int value : from.keySet()) {
            from_point.add(value);
          }
          for (int to_test : domainate.get(from_point.get(0)).keySet()) {
            boolean flag = true;
            for (int j = 1; j < from_point.size(); j++) {
              if (!domainate.get(from_point.get(j)).containsKey(to_test)) {
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

  void PrintDominate() {
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> value : domainate.entrySet()) {
      System.out.println(value.getKey() + ":");
      for (Map.Entry<Integer, Boolean> values : value.getValue().entrySet()) {
        System.out.println(values.getKey());
      }
    }
  }
}