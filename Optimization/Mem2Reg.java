package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import IRSentence.*;
import Composer.*;

public class Mem2Reg {
  Composer object = null;
  int func_cnt = 0;
  int phi_time = 0;
  HashMap<Integer, HashMap<String, Boolean>> reserved_variable = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> dominate = new HashMap<>();
  public HashMap<Integer, Integer> Idom = new HashMap<>();
  public HashMap<Integer, ArrayList<Integer>> frontier = new HashMap<>();
  public HashMap<Integer, ArrayList<Integer>> Idom_up_down = new HashMap<>();
  public HashMap<String, Stack<NameLabelPair>> new_name = new HashMap<>();
  public HashMap<String, String> alloc_type = new HashMap<>();
  public HashMap<Integer, Boolean> visit = new HashMap<>();

  public Mem2Reg(Composer _object) {
    object = _object;
  }

  public void Optim() {
    Insert();
    RemoveNaiveUntouchable();
    Mem2RegEmpty();
    Mem2RegAssignOnce();
    Mem2RegAssignOneBlock();
    BuildGraph();
    BuildDominate();
    BuildIdom();
    BuildFrontier();
    ReservePhi();
    RemoveAlloca();
    PhiReOrder();
  }

  void PhiReOrder() {
    int func = 0;
    for (int i = 0; i < object.generated.size(); i++) {
      if (object.generated.get(i) instanceof IRFunc) {
        for (IRPhi code : object.reserved_phi.get(--func)) {
          object.generated.add(i + 1, code);
        }
        object.reserved_phi.get(func).clear();
      }
      if (object.generated.get(i) instanceof IRLabel) {
        IRLabel label = (IRLabel) (object.generated.get(i));
        for (IRPhi code : object.reserved_phi.get(label.label)) {
          object.generated.add(i + 1, code);
        }
        object.reserved_phi.get(label.label).clear();
      }
    }
  }

  void Insert() {
    for (int i = 0; i < object.generated.size(); i++) {
      if (object.generated.get(i) instanceof IRFunc) {
        IRFunc main_check = (IRFunc) object.generated.get(i);
        if (main_check.name.equals("main")) {
          object.generated.addAll(i + 1, object.init);
          object.init.clear();
        }
      }
    }
  }

  void CheckCode() {
    for (IRCode code : object.init) {
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        System.out.println(label.label);
      }
    }
    for (IRCode code : object.generated) {
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        System.out.println(label.label);
      }
    }
  }

  void Mem2RegEmpty() {
    while (true) {
      HashMap<String, Integer> use = new HashMap<>();
      HashMap<String, Integer> def = new HashMap<>();
      for (ArrayList<IRCode> alloc_func : object.alloc.values()) {
        for (IRCode alloc_single : alloc_func) {
          alloc_single.CheckTime(use, def, object);
        }
      }
      for (IRCode code : object.generated) {
        code.CheckTime(use, def, object);
      }
      for (IRCode code : object.init) {
        code.CheckTime(use, def, object);
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
      generate.CheckTime(use, def, object);
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
      System.out.print(value.getKey() + ":");
      for (Map.Entry<Integer, Boolean> values : value.getValue().entrySet()) {
        System.out.print(values.getKey() + " ");
      }
      System.out.println();
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

  void PrintDominate() {
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> value : dominate.entrySet()) {
      System.out.println(value.getKey() + ":");
      for (Map.Entry<Integer, Boolean> values : value.getValue().entrySet()) {
        System.out.print(values.getKey() + " ");
      }
      System.out.println();
    }
  }

  void BuildIdom() {
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

  void PrintIdom() {
    for (Map.Entry<Integer, ArrayList<Integer>> entry : Idom_up_down.entrySet()) {
      System.out.print(entry.getKey() + ":");
      for (int value : entry.getValue()) {
        System.out.print(value + " ");
      }
      System.out.println();
    }
    return;
  }

  void BuildFrontier() {
    for (int node : graph.keySet()) {
      frontier.put(node, new ArrayList<>());
    }
    for (int node : graph.keySet()) {
      HashMap<Integer, Boolean> res = new HashMap<>();
      if (pre.containsKey(node)) {
        for (int preds : pre.get(node).keySet()) {
          for (int doms : dominate.get(preds).keySet()) {
            res.put(doms, null);
          }
        }
      }
      for (int ndom : dominate.get(node).keySet()) {
        if (res.containsKey(ndom)) {
          if (ndom == node) {
            continue;
          }
          res.remove(ndom);
        }
      }
      for (int value : res.keySet()) {
        frontier.get(value).add(node);
      }
    }
    return;
  }

  void PrintFrontier() {
    for (Map.Entry<Integer, ArrayList<Integer>> entry : frontier.entrySet()) {
      System.out.print(entry.getKey() + ": ");
      for (int value : entry.getValue()) {
        System.out.print(value + " ");
      }
      System.out.println();
    }
    return;
  }

  void ReservePhi() {
    int now_func = 0;
    int now = 0;
    for (int node : graph.keySet()) {
      reserved_variable.put(node, new HashMap<>());
      object.reserved_phi.put(node, new ArrayList<>());
    }
    Queue<NameLabelPair> to_add = new LinkedList<>();
    for (IRCode code : object.generated) {
      if (code instanceof IRFunc) {
        now = --now_func;
      }
      if (code instanceof IRLabel) {
        now = ((IRLabel) code).label;
      }
      if (code instanceof IRStore) {
        String target = new String(((IRStore) code).name);
        IRStore store_ins = (IRStore) code;
        if (store_ins.be_alloc) {
          NameLabelPair new_pair = new NameLabelPair(target, now);
          to_add.add(new_pair);
        }
      }
    }
    while (!to_add.isEmpty()) {
      NameLabelPair res = to_add.poll();
      for (int value : frontier.get(res.label)) {
        reserved_variable.get(value).put(new String(res.name), null);
        NameLabelPair new_pair = new NameLabelPair(res.name, value);
        if (res.label != new_pair.label) {
          to_add.add(new_pair);
        }
      }
    }
    return;
  }

  void RemoveAlloca() {
    HashMap<String, String> reg_value = new HashMap<>();
    for (ArrayList<IRCode> allocs : object.alloc.values()) {
      for (IRCode alloc_raw : allocs) {
        IRAlloc alloc = (IRAlloc) alloc_raw;
        alloc_type.put(alloc.des, alloc.type);
        new_name.put(new String(alloc.des), new Stack<>());
      }
    }
    for (int i = -1; i >= func_cnt; i--) {
      RenamePhi(i, reg_value);
    }
    for (ArrayList<IRCode> allocs : object.alloc.values()) {
      allocs.clear();
    }
    boolean to_remove = false;
    for (int i = object.generated.size() - 1; i >= 0; i--) {
      if (to_remove) {
        object.generated.remove(i + 1);
      }
      to_remove = object.generated.get(i).ToRemove(new_name);
    }
    if (to_remove) {
      object.generated.remove(0);
    }
    return;
  }

  HashMap<String, String> value = new HashMap<>();

  void RenamePhi(int index, HashMap<String, String> reg_values) {
    visit.put(index, null);
    int phi_func = 0;
    for (int i = 0; i < object.generated.size(); i++) {
      if (object.generated.get(i) instanceof IRFunc) {
        if (--phi_func == index) {
          for (int j = i + 1; j < object.generated.size(); j++) {
            if (object.generated.get(j) instanceof IRFuncend || object.generated.get(j) instanceof IRLabel) {
              break;
            }
            object.generated.get(j).UpdateNames(new_name, reg_values, index);
          }
          HashMap<Integer, Boolean> next = graph.get(index);
          for (int nxt : next.keySet()) {
            ArrayList<IRPhi> phis = object.reserved_phi.get(nxt);
            for (String to_update : reserved_variable.get(nxt).keySet()) {
              boolean flag = false;
              for (IRPhi phi : phis) {
                if (phi.ori_name.equals(to_update)) {
                  if (!new_name.get(to_update).empty()) {
                    phi.values.add(new String(new_name.get(to_update).peek().name));
                  } else {
                    switch (phi.type) {
                      case "i32": {
                        phi.values.add("0");
                        break;
                      }
                      case "i1": {
                        phi.values.add("false");
                        break;
                      }
                      default: {
                        phi.values.add("null");
                        break;
                      }
                    }
                  }
                  phi.labels.add(index);
                  flag = true;
                  break;
                }
              }
              if (!flag) {
                IRPhi to_add = new IRPhi();
                to_add.type = alloc_type.get(to_update);
                to_add.ori_name = new String(to_update);
                if (!new_name.get(to_update).empty()) {
                  to_add.values.add(new String(new_name.get(to_update).peek().name));
                } else {
                  switch (to_add.type) {
                    case "i32": {
                      to_add.values.add("0");
                      break;
                    }
                    case "i1": {
                      to_add.values.add("false");
                      break;
                    }
                    default: {
                      to_add.values.add("null");
                      break;
                    }
                  }
                }
                to_add.labels.add(index);
                phis.add(to_add);
              }
            }
          }
          if (graph.containsKey(index)) {
            for (int dom : graph.get(index).keySet()) {
              if (!visit.containsKey(dom)) {
                RenamePhi(dom, reg_values);
              }
            }
          }
          RenameClear(index);
          return;
        }
      }
      if (object.generated.get(i) instanceof IRLabel) {
        IRLabel _label = (IRLabel) (object.generated.get(i));
        if (_label.label == index) {
          ArrayList<IRPhi> to_update_phies = object.reserved_phi.get(index);
          HashMap<String, Boolean> to_update = reserved_variable.get(index);
          for (String phi_object : to_update.keySet()) {
            String update_name = "%phi$" + ++phi_time;
            new_name.get(phi_object).push(new NameLabelPair(update_name, index));
            for (IRPhi to_update_phi : to_update_phies) {
              if (to_update_phi.ori_name.equals(phi_object)) {
                to_update_phi.target = new String(update_name);
                break;
              }
            }
          }
          for (int j = i + 1; j < object.generated.size(); j++) {
            if (object.generated.get(j) instanceof IRFuncend || object.generated.get(j) instanceof IRLabel) {
              break;
            }
            object.generated.get(j).UpdateNames(new_name, reg_values, index);
          }
          HashMap<Integer, Boolean> next = graph.get(index);
          for (int nxt : next.keySet()) {
            ArrayList<IRPhi> phis = object.reserved_phi.get(nxt);
            for (String to_update_string : reserved_variable.get(nxt).keySet()) {
              boolean flag = false;
              for (IRPhi phi : phis) {
                if (phi.ori_name.equals(to_update_string)) {
                  if (!new_name.get(to_update_string).empty()) {
                    phi.values.add(new String(new_name.get(to_update_string).peek().name));
                  } else {
                    switch (phi.type) {
                      case "i32": {
                        phi.values.add("0");
                        break;
                      }
                      case "i1": {
                        phi.values.add("false");
                        break;
                      }
                      default: {
                        phi.values.add("null");
                        break;
                      }
                    }
                  }
                  phi.labels.add(index);
                  flag = true;
                  break;
                }
              }
              if (!flag) {
                IRPhi to_add = new IRPhi();
                to_add.type = alloc_type.get(to_update_string);
                to_add.ori_name = new String(to_update_string);
                if (!new_name.get(to_update_string).empty()) {
                  to_add.values.add(new String(new_name.get(to_update_string).peek().name));
                } else {
                  switch (to_add.type) {
                    case "i32": {
                      to_add.values.add("0");
                      break;
                    }
                    case "i1": {
                      to_add.values.add("false");
                      break;
                    }
                    default: {
                      to_add.values.add("null");
                      break;
                    }
                  }
                }
                to_add.labels.add(index);
                phis.add(to_add);
              }
            }
          }
          if (graph.containsKey(index)) {
            for (int dom : graph.get(index).keySet()) {
              if (!visit.containsKey(dom)) {
                RenamePhi(dom, reg_values);
              }
            }
          }
          RenameClear(index);
          return;
        }
      }
    }
  }

  void RenameClear(int index) {
    for (Stack<NameLabelPair> check : new_name.values()) {
      while ((!check.empty()) && (check.peek().label == index)) {
        check.pop();
      }
    }
    return;
  }

  void PrintReserve() {
    for (Map.Entry<Integer, HashMap<String, Boolean>> entry : reserved_variable.entrySet()) {
      System.out.print(entry.getKey() + ":");
      for (String value : entry.getValue().keySet()) {
        System.out.print(value + " ");
      }
      System.out.println();
    }
  }
}