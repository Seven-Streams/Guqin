package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRClass;
import IRSentence.IRCode;
import IRSentence.IRFunc;
import IRSentence.IRFuncend;
import IRSentence.IRLabel;
import IRSentence.IRjmp;
import IRSentence.MoveBlock;

public class NaiveADCE {
  Composer machine = null;
  int func_cnt = 0;
  int largest_label = 0;
  HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();
  HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  HashMap<Integer, TreeMap<Integer, Boolean>> from = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> in = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> out = new HashMap<>();
  public HashMap<Integer, Boolean> contains_end = new HashMap<>();

  public NaiveADCE(Composer _machine) {
    machine = _machine;
  }

  public void Optim() {
    BuildGraph();
    InOutCheck();
    DeadCheck();
    Rebuild();
    RemoveDead();
  }

  void PrintOut() {
    for (int i = -1; i >= func_cnt; i--) {
      System.out.print(i + ":");
      for (String out_v : out.get(i).keySet()) {
        System.out.print(out_v + " ");
      }
      System.out.println("");
    }
    for (int i = 1; i <= largest_label; i++) {
      if (out.containsKey(i)) {
        System.out.print(i + ":");
        for (String out_v : out.get(i).keySet()) {
          System.out.print(out_v + " ");
        }
        System.out.println("");
      }
    }
  }

  void RemoveDead() {
    for (int i = machine.generated.size() - 1; i >= 0; i--) {
      IRCode code = machine.generated.get(i);
      if (code.dead) {
        machine.generated.remove(i);
      }
      if(code instanceof MoveBlock) {
        MoveBlock move_block = (MoveBlock)code;
        for(int j = move_block.moves.size() - 1; j >= 0; j--) {
          if(move_block.moves.get(j).dead) {
            move_block.moves.remove(j);
          }
        }
      }
    }
    return;
  }

  void Rebuild() {
    machine.generated.clear();
    for (int i = -1; i >= func_cnt; i--) {
      TreeMap<Integer, Boolean> code_list = from.get(i);
      int end = 0;
      for (int label : code_list.keySet()) {
        if (contains_end.containsKey(label)) {
          end = label;
        } else {
          machine.generated.addAll(blocks.get(label));
        }
      }
      machine.generated.addAll(blocks.get(end));
    }
    return;
  }

  void DeadCheck() {
    for (int i = -1; i >= func_cnt; i--) {
      ArrayList<IRCode> check_list = blocks.get(i);
      HashMap<String, Boolean> now_out = out.get(i);
      if (now_out == null) {
        continue;
      }
      HashMap<String, Boolean> res_def = new HashMap<>();
      for (int j = check_list.size() - 1; j >= 0; j--) {
        res_def.clear();
        IRCode code = check_list.get(j);
        code.AliveUseDefCheck(res_def, now_out);
        for (String def_v : res_def.keySet()) {
          now_out.remove(def_v);
        }
      }
    }
    for (int i = 1; i <= largest_label; i++) {
      ArrayList<IRCode> check_list = blocks.get(i);
      HashMap<String, Boolean> now_out = out.get(i);
      if (now_out == null) {
        continue;
      }
      HashMap<String, Boolean> res_def = new HashMap<>();
      for (int j = check_list.size() - 1; j >= 0; j--) {
        res_def.clear();
        IRCode code = check_list.get(j);
        code.AliveUseDefCheck(res_def, now_out);
        for (String def_v : res_def.keySet()) {
          now_out.remove(def_v);
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
        contains_end.put(now, null);
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

  void InOutCheck() {
    HashMap<Integer, Boolean> visit = new HashMap<>();
    in.clear();
    out.clear();
    Queue<Integer> check_list = new LinkedList<>();
    for (int node : graph.keySet()) {
      if (graph.get(node).isEmpty()) {
        check_list.add(node);
      }
    }
    while (!check_list.isEmpty()) {
      int to_check = check_list.poll();
      ArrayList<IRCode> codes = blocks.get(to_check);
      HashMap<String, Boolean> res_out = new HashMap<>();
      HashMap<String, Boolean> res_def = new HashMap<>();
      if (!in.containsKey(to_check)) {
        in.put(to_check, new HashMap<>());
      }
      if (!out.containsKey(to_check)) {
        out.put(to_check, new HashMap<>());
      }
      for (String out_v : out.get(to_check).keySet()) {
        res_out.put(out_v, null);
      }
      for (int i = codes.size() - 1; i >= 0; i--) {
        res_def.clear();
        IRCode code = codes.get(i);
        code.AliveUseDefCheck(res_def, res_out);
        for (String def_v : res_def.keySet()) {
          res_out.remove(def_v);
        }
      }
      HashMap<String, Boolean> to_operate = in.get(to_check);
      for (String res_v : res_out.keySet()) {
        if (!to_operate.containsKey(res_v)) {
          to_operate.put(res_v, null);
        }
      }
      if (pre.containsKey(to_check)) {
        for (int pre_v : pre.get(to_check).keySet()) {
          if (!out.containsKey(pre_v)) {
            out.put(pre_v, new HashMap<>());
          }
          boolean flag = false;
          HashMap<String, Boolean> out_check = out.get(pre_v);
          for (String value : to_operate.keySet()) {
            if (!out_check.containsKey(value)) {
              out_check.put(value, null);
              flag = true;
            }
          }
          if (!visit.containsKey(pre_v)) {
            flag = true;
            visit.put(pre_v, null);
          }
          if (flag) {
            check_list.add(pre_v);
          }
        }
      }
    }
    return;
  }

}
