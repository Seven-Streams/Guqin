package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.Queue;

import Composer.Composer;
import IRSentence.*;

public class LoopOptimization {
  Composer machine = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, Integer> idom = null;
  int func_cnt = 0;
  public HashMap<Integer, Boolean> visit = new HashMap<>();
  public HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> loops = new HashMap<>();
  public Queue<FromToPair> heads = new LinkedList<>();
  public HashMap<Integer, ArrayList<IRCode>> new_blocks = new HashMap<>();

  public LoopOptimization(Composer _machine, HashMap<Integer, Integer> _idom) {
    machine = _machine;
    idom = _idom;
  }

  void Optim() {
    CheckGlobal();
    BuildGraph();
    CheckBackEdge();
    GetLoops();
    LoopOptim();
    Rebuild();
  }

  void CheckGlobal() {
    for (IRCode code : machine.global) {
      code.CheckGlobal();
    }
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

  void CheckBackEdge() {
    heads.clear();
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : graph.entrySet()) {
      HashMap<Integer, Boolean> des = entry.getValue();
      for (int value : des.keySet()) {
        int now = entry.getKey();
        while (idom.containsKey(now)) {
          if (idom.get(now) == value) {
            heads.add(new FromToPair(entry.getKey(), value));
            break;
          }
          if (idom.get(now) == now) {
            break;
          } else {
            now = idom.get(now);
          }
        }
      }
    }
    return;
  }

  void GetLoops() {
    while (!heads.isEmpty()) {
      FromToPair now_check = heads.poll();
      // System.out.println(now_check.from + "->" + now_check.to);
      BackDfs(now_check.from, now_check.to);
    }

  }

  void BackDfs(int start, int end) {
    HashMap<Integer, HashMap<Integer, Boolean>> last_visit = new HashMap<>();
    Stack<Integer> check_list = new Stack<>();
    check_list.add(start);
    while (!check_list.isEmpty()) {
      int now = check_list.pop();
      if (!visit.containsKey(now)) {
        if (!loops.containsKey(end)) {
          loops.put(end, new HashMap<>());
        }
        if (loops.get(end).containsKey(now)) {
          continue;
        }
        loops.get(end).put(now, null);
        if (now == end) {
          continue;
        }
        visit.put(now, null);
      }
      boolean finish = true;
      if (pre.containsKey(now)) {
        for (int to : pre.get(now).keySet()) {
          if(!last_visit.containsKey(now)) {
            last_visit.put(now, new HashMap<>());
          }
          if (!visit.containsKey(to) && (!last_visit.get(now).containsKey(to))) {
            last_visit.get(now).put(to, null);
            check_list.add(now);
            check_list.add(to);
            finish = false;
            break;
          }
        }
      }
      if (finish) {
        visit.remove(now);
      }
    }
    return;
  }

  void LoopOptim() {
    HashMap<String, Boolean> def = new HashMap<>();
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : loops.entrySet()) {
      def.clear();
      ArrayList<IRCode> loops_code = new ArrayList<>();
      ArrayList<IRStore> stores = new ArrayList<>();
      boolean have_funcall = false;
      for (int constitute : entry.getValue().keySet()) {
        loops_code.addAll(blocks.get(constitute));
      }
      HashMap<String, Boolean> res_use = new HashMap<>();
      HashMap<String, Boolean> res_def = new HashMap<>();
      for (IRCode code : loops_code) {
        if (code.dead) {
          continue;
        }
        res_def.clear();
        res_use.clear();
        code.UseDefCheck(res_def, res_use);
        for (String def_v : res_def.keySet()) {
          def.put(def_v, null);
        }
        if (code instanceof IRStore) {
          stores.add((IRStore) code);
        } else {
          if (code instanceof IRFuncall) {
            have_funcall = true;
          }
        }
      }
      ArrayList<IRCode> new_codes = new ArrayList<>();
      boolean flag = true;
      while (flag) {
        flag = false;
        for (IRCode code : loops_code) {
          if (code.dead) {
            continue;
          }
          boolean all_out = true;
          res_use.clear();
          res_def.clear();
          code.UseDefCheck(res_def, res_use);
          for (String use_v : res_use.keySet()) {
            if (def.containsKey(use_v)) {
              all_out = false;
              break;
            }
          }
          if (all_out) {
            if (code instanceof IRBin) {
              IRBin bin = (IRBin) code;
              IRBin new_bin = new IRBin();
              new_bin.op1 = new String(bin.op1);
              new_bin.op2 = new String(bin.op2);
              new_bin.type = new String(bin.type);
              new_bin.symbol = new String(bin.symbol);
              new_bin.target_reg = new String(bin.target_reg);
              new_codes.add(new_bin);
              bin.dead = true;
              flag = true;
              def.remove(bin.target_reg);
              continue;
            }
            if (code instanceof IRIcmp) {
              IRIcmp cmp = (IRIcmp) code;
              IRIcmp new_cmp = new IRIcmp();
              new_cmp.op1 = new String(cmp.op1);
              new_cmp.op2 = new String(cmp.op2);
              new_cmp.type = new String(cmp.type);
              new_cmp.symbol = new String(cmp.symbol);
              new_cmp.target_reg = new String(cmp.target_reg);
              new_codes.add(new_cmp);
              cmp.dead = true;
              flag = true;
              def.remove(cmp.target_reg);
              continue;
            }
            // if (code instanceof IRElement) {
            // IRElement ele = (IRElement) code;
            // if (ele.num2 == null) {
            // IRElement new_ele = new IRElement();
            // new_ele.src = new String(ele.src);
            // new_ele.output = new String(ele.output);
            // new_ele.num1 = new String(ele.num1);
            // new_ele.now_type = new String(ele.now_type);
            // new_ele.num2 = null;
            // new_codes.add(new_ele);
            // ele.dead = true;
            // flag = true;
            // def.remove(ele.output);
            // continue;
            // }
            // }
            if ((!have_funcall) && (code instanceof IRLoad)) {
              IRLoad load = (IRLoad) code;
              if (IRCode.is_global.containsKey(load.src)) {
                boolean check = true;
                for (IRStore store : stores) {
                  if (store.name.equals(load.src)) {
                    check = false;
                    break;
                  }
                }
                if (check) {
                  IRLoad new_load = new IRLoad();
                  new_load.src = new String(load.src);
                  new_load.des = new String(load.des);
                  new_load.type = new String(load.type);
                  new_codes.add(new_load);
                  load.dead = true;
                  flag = true;
                  def.remove(load.des);
                  continue;
                }
              }
            }
          }
        }
      }
      if (!new_codes.isEmpty()) {
        new_codes.add(new IRjmp(entry.getKey()));
        int new_block_label = ++machine.label_number;
        new_codes.add(0, new IRLabel(new_block_label));
        blocks.put(new_block_label, new_codes);
        graph.put(new_block_label, new HashMap<>());
        pre.put(new_block_label, new HashMap<>());
        graph.get(new_block_label).put(entry.getKey(), null);
        Queue<Integer> to_remove = new LinkedList<>();
        for (int pre_v : pre.get(entry.getKey()).keySet()) {
          int now = entry.getKey();
          while (idom.containsKey(now)) {
            if (idom.get(now) == pre_v) {
              for (int i = 1; i < blocks.get(entry.getKey()).size(); i++) {
                IRCode code = blocks.get(entry.getKey()).get(i);
                if (!(code instanceof IRPhi)) {
                  break;
                } else {
                  IRPhi phi = (IRPhi) code;
                  for (int j = 0; j < phi.labels.size(); j++) {
                    if (phi.labels.get(j) == pre_v) {
                      phi.labels.set(j, new_block_label);
                    }
                  }
                }
              }
              graph.get(pre_v).remove(entry.getKey());
              graph.get(pre_v).put(new_block_label, null);
              to_remove.add(pre_v);
              pre.get(new_block_label).put(pre_v, null);
              ArrayList<IRCode> pre_codes = blocks.get(pre_v);
              if (pre_codes.get(pre_codes.size() - 1) instanceof IRjmp) {
                IRjmp jmp = (IRjmp) pre_codes.get(pre_codes.size() - 1);
                jmp.label = new_block_label;
              }
              if (pre_codes.get(pre_codes.size() - 1) instanceof Conditionjmp) {
                Conditionjmp jmp = (Conditionjmp) pre_codes.get(pre_codes.size() - 1);
                if (jmp.label1 == entry.getKey()) {
                  jmp.label1 = new_block_label;
                } else {
                  jmp.label2 = new_block_label;
                }
              }
              break;
            }
            if (idom.get(now) == now) {
              break;
            } else {
              now = idom.get(now);
            }
          }
        }
        pre.get(entry.getKey()).put(new_block_label, null);
        while (!to_remove.isEmpty()) {
          int value = to_remove.poll();
          pre.get(entry.getKey()).remove(value);
        }
        for (Map.Entry<Integer, HashMap<Integer, Boolean>> loop_inner : loops.entrySet()) {
          if (loop_inner.getKey() == entry.getKey()) {
            continue;
          }
          if (loop_inner.getValue().containsKey(entry.getKey())) {
            loop_inner.getValue().put(new_block_label, null);
          }
        }
        new_blocks.put(entry.getKey(), new_codes);
      }
    }
    return;
  }

  void Rebuild() {
    for (int i = machine.generated.size() - 1; i >= 0; i--) {
      IRCode code = machine.generated.get(i);
      if (code.dead) {
        machine.generated.remove(i);
        continue;
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        if (new_blocks.containsKey(label.label)) {
          machine.generated.addAll(i, new_blocks.get(label.label));
        }
      }
    }
    return;
  }

}
