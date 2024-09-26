package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
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
  public HashMap<Integer, Integer> idom = new HashMap<>();
  public HashMap<Integer, ArrayList<Integer>> frontier = new HashMap<>();
  public HashMap<String, Stack<NameLabelPair>> new_name = new HashMap<>();
  public HashMap<String, String> alloc_type = new HashMap<>();
  public HashMap<Integer, Boolean> visit = new HashMap<>();
  public ArrayList<Integer> post_order = new ArrayList<>();
  public HashMap<Integer, Integer> finger = new HashMap<>();
  public HashMap<Integer, ArrayList<IRCode>> code_block = new HashMap<>();

  public Mem2Reg(Composer _object) {
    object = _object;
  }

  public void clear() {
    reserved_variable.clear();
    graph.clear();
    pre.clear();
    idom.clear();
    frontier.clear();
    new_name.clear();
    alloc_type.clear();
    visit.clear();
    post_order.clear();
    finger.clear();
    code_block.clear();
  }

  public void Optim() {
    Insert();
    RemoveNaiveUntouchable();
    Mem2RegEmpty();
    Mem2RegAssignOnce();
    Mem2RegAssignOneBlock();
    MakeBlock();
    BuildGraph();
    GetPostOrder();
    BuildDominate();
    BuildFrontier();
    ReservePhi();
    RemoveAlloca();
    PhiReOrder();
    return;
  }

  void GetPostOrder() {
    for (int i = -1; i >= func_cnt; i--) {
      tranverse(i);
    }
    Collections.reverse(post_order);
    return;
  }

  void MakeBlock() {
    int cnt = 0;
    ArrayList<IRCode> check = null;
    for (IRCode code : object.generated) {
      if (code instanceof IRClass) {
        continue;
      }
      if (code instanceof IRFunc) {
        check = new ArrayList<>();
        code_block.put(--cnt, check);
        continue;
      }
      if (code instanceof IRLabel) {
        check = new ArrayList<>();
        int label = ((IRLabel) code).label;
        code_block.put(label, check);
      }
      check.add(code);
    }

  }

  void tranverse(int index) {
    visit.put(index, null);
    Stack<Integer> check_list = new Stack<>();
    check_list.add(index);
    while (!check_list.isEmpty()) {
      int now = check_list.pop();
      boolean flag = false;
      if (graph.containsKey(now)) {
        for (int to : graph.get(now).keySet()) {
          if (!visit.containsKey(to)) {
            visit.put(to, null);
            check_list.add(now);
            check_list.add(to);
            flag = true;
            break;
          }
        }
      }
      if (!flag) {
        post_order.add(now);
        finger.put(now, post_order.size());
      }
    }
    return;
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
    for (int i = -1; i >= func_cnt; i--) {
      idom.put(i, i);
    } // Initialize.
    boolean changed = true;
    while (changed) {
      changed = false;
      for (int block : post_order) {
        if (block < 0) {
          continue;
        }
        int new_idom = 0x7fffffff;
        for (int pre_node : pre.get(block).keySet()) {
          if (!idom.containsKey(pre_node)) {
            continue;
          }
          if (new_idom == 0x7fffffff) {
            new_idom = pre_node;
          } else {
            new_idom = intersect(new_idom, pre_node);
          }
        }
        if ((!idom.containsKey(block)) || idom.get(block) != new_idom) {
          idom.put(block, new_idom);
          changed = true;
        }
      }
    }
    return;
  }

  int intersect(int node1, int node2) {
    int finger1 = finger.get(node1);
    int finger2 = finger.get(node2);
    while (finger1 != finger2) {
      while (finger1 < finger2) {
        node1 = idom.get(node1);
        finger1 = finger.get(node1);
      }
      while (finger2 < finger1) {
        node2 = idom.get(node2);
        finger2 = finger.get(node2);
      }
    }
    return node1;
  }

  void BuildFrontier() {
    for (int block : post_order) {
      if (pre.containsKey(block) && (pre.get(block).size() >= 2)) {
        for (int pre_node : pre.get(block).keySet()) {
          int runner = pre_node;
          while (runner != idom.get(block)) {
            // System.out.println(runner + "target:" + idom.get(block));
            if (!frontier.containsKey(runner)) {
              frontier.put(runner, new ArrayList<>());
            }
            frontier.get(runner).add(block);
            if(idom.containsKey(runner)) {
            runner = idom.get(runner);
            } else {
              id
            }
          }
        }
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
      if (frontier.containsKey(res.label)) {
        for (int value : frontier.get(res.label)) {
          reserved_variable.get(value).put(new String(res.name), null);
          NameLabelPair new_pair = new NameLabelPair(res.name, value);
          if (res.label != new_pair.label) {
            to_add.add(new_pair);
          }
        }
      }
    }
    return;
  }

  void RemoveAlloca() {
    visit.clear();
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

  void RenamePhi(int src_index, HashMap<String, String> reg_values) {
    Stack<Integer> check_list = new Stack<>();
    check_list.add(src_index);
    while (!check_list.isEmpty()) {
      int index = check_list.pop();
      if (!visit.containsKey(index)) {
        ArrayList<IRCode> code_list = code_block.get(index);
        if (index < 0) {
          for (IRCode code : code_list) {
            code.UpdateNames(new_name, reg_values, index);
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
        } else {
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
          for (IRCode code : code_list) {
            code.UpdateNames(new_name, reg_values, index);
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
        }
      }
      visit.put(index, null);
      boolean finished = true;
      if (graph.containsKey(index)) {
        for (int dom : graph.get(index).keySet()) {
          if (!visit.containsKey(dom)) {
            finished = false;
            check_list.add(index);
            check_list.add(dom);
            break;
          }
        }
      }
      if (finished) {
        RenameClear(index);
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