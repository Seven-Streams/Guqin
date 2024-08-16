package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Map;
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
  public HashMap<String, HashMap<String, Boolean>> move_pair = new HashMap<>();
  public HashMap<String, HashMap<String, Boolean>> coalesced_move_pair = new HashMap<>();
  public HashMap<String, HashMap<String, Boolean>> conflict_graph = new HashMap<>();
  public HashMap<String, HashMap<String, Boolean>> simplified_graph = new HashMap<>();
  public HashMap<String, String> coalesced_node = new HashMap<>();
  //In coalesced_node, I'll show which the coalesced node attached to.
  //When I coalesce 2 nodes, I'll change all the relative edges.
  public Stack<String> selected = new Stack<>();
  public HashMap<String, Boolean> move_related = new HashMap<>();
  public HashMap<String, Boolean> now_move_related = new HashMap<>();

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
              if (!out_check.containsKey(value)) {
                out_check.put(value, null);
                flag_2 = true;
              }
            }
            if (flag_2) {
              check_list.add(pre_v);
            }
          }
        }
      }
    }
    return;
  }

  void BuildConflictGraph() {
    int func_cnt = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      if (machine.generated.get(i) instanceof IRFunc) {
        --func_cnt;
        Stack<IRCode> to_operate = new Stack<>();
        for (int j = i; j < machine.generated.size(); j++) {
          if (machine.generated.get(j) instanceof IRFunc || machine.generated.get(j) instanceof IRLabel
              || machine.generated.get(j) instanceof MoveBlock) {
            break;
          } else {
            to_operate.add(machine.generated.get(j));
            i++;
          }
        }
        HashMap<String, Boolean> now_out = new HashMap<>();
        for (String out_v : out.get(func_cnt).keySet()) {
          now_out.put(out_v, null);
        }
        HashMap<String, Boolean> def_res = new HashMap<>();
        HashMap<String, Boolean> use_res = new HashMap<>();
        while (!to_operate.empty()) {
          IRCode op = to_operate.pop();
          def_res.clear();
          use_res.clear();
          op.UseDefCheck(def_res, use_res);
          for (String now_def : def_res.keySet()) {
            now_out.remove(now_def);
            if (!conflict_graph.containsKey(now_def)) {
              conflict_graph.put(now_def, new HashMap<>());
            }
            for (String out_v : now_out.keySet()) {
              if (out_v.equals(now_def)) {
                continue;
              }
              if (!conflict_graph.containsKey(out_v)) {
                conflict_graph.put(out_v, new HashMap<>());
              }
              if (!conflict_graph.get(out_v).containsKey(now_def)) {
                conflict_graph.get(out_v).put(now_def, null);
                conflict_graph.get(now_def).put(out_v, null);
              }
            }
          }
          for (String now_use : use_res.keySet()) {
            now_out.remove(now_use);
          }
        }
      }
      if (machine.generated.get(i) instanceof IRLabel) {
        int now = ((IRLabel) (machine.generated.get(i))).label;
        Stack<IRCode> to_operate = new Stack<>();
        for (int j = i; j < machine.generated.size(); j++) {
          if (machine.generated.get(j) instanceof IRFunc || machine.generated.get(j) instanceof IRLabel
              || machine.generated.get(j) instanceof MoveBlock) {
            break;
          } else {
            to_operate.add(machine.generated.get(j));
            i++;
          }
        }
        HashMap<String, Boolean> now_out = new HashMap<>();
        for (String out_v : out.get(now).keySet()) {
          now_out.put(out_v, null);
        }
        HashMap<String, Boolean> def_res = new HashMap<>();
        HashMap<String, Boolean> use_res = new HashMap<>();
        while (!to_operate.empty()) {
          IRCode op = to_operate.pop();
          def_res.clear();
          use_res.clear();
          op.UseDefCheck(def_res, use_res);
          for (String now_def : def_res.keySet()) {
            now_out.remove(now_def);
            if (!conflict_graph.containsKey(now_def)) {
              conflict_graph.put(now_def, new HashMap<>());
            }
            for (String out_v : now_out.keySet()) {
              if (out_v.equals(now_def)) {
                continue;
              }
              if (!conflict_graph.containsKey(out_v)) {
                conflict_graph.put(out_v, new HashMap<>());
              }
              if (!conflict_graph.get(out_v).containsKey(now_def)) {
                conflict_graph.get(out_v).put(now_def, null);
                conflict_graph.get(now_def).put(out_v, null);
              }
            }
          }
          for (String now_use : use_res.keySet()) {
            now_out.remove(now_use);
          }
        }
      }
      if (machine.generated.get(i) instanceof MoveBlock) {
        MoveBlock move = (MoveBlock) machine.generated.get(i);
        for (PseudoMove move_op : move.moves) {
          if (!move_pair.containsKey(move_op.des)) {
            move_pair.put(move_op.des, new HashMap<>());
          }
          if (!move_pair.containsKey(move_op.src)) {
            move_pair.put(move_op.src, new HashMap<>());
          }
          move_related.put(move_op.des, null);
          move_related.put(move_op.src, null);
          if (!conflict_graph.containsKey(move_op.des)) {
            conflict_graph.put(move_op.des, new HashMap<>());
          }
          for (String out_v : out.get(move.num).keySet()) {
            if (!conflict_graph.containsKey(out_v)) {
              conflict_graph.put(out_v, new HashMap<>());
            }
            if (!out_v.equals(move_op.des)) {
              if (out_v.equals(move_op.src)) {
                conflict_graph.get(out_v).put(move_op.des, null);
                conflict_graph.get(move_op.des).put(out_v, null);
              } else { // Move related
                conflict_graph.get(out_v).put(move_op.des, null);
                conflict_graph.get(move_op.des).put(out_v, null);
              }
              move_related.put(out_v, null);
            }
          }
        }
      }
    }
    return;
  }

  void ConflictInit() {
    simplified_graph.clear();
    coalesced_move_pair.clear();
    coalesced_node.clear();
    now_move_related.clear();
    for (Map.Entry<String, HashMap<String, Boolean>> node : conflict_graph.entrySet()) {
      simplified_graph.put(node.getKey(), new HashMap<>());
      HashMap<String, Boolean> op = simplified_graph.get(node.getKey());
      for (Map.Entry<String, Boolean> pair : node.getValue().entrySet()) {
        op.put(pair.getKey(), null);
      }
    } // Get a copy of the HashMap.

    for (Map.Entry<String, HashMap<String, Boolean>> pairs : move_pair.entrySet()) {
      coalesced_move_pair.put(pairs.getKey(), new HashMap<>());
      HashMap<String, Boolean> op = coalesced_move_pair.get(pairs.getKey());
      for (Map.Entry<String, Boolean> pair : pairs.getValue().entrySet()) {
        op.put(pair.getKey(), null);
      }
    } // Get a copy of move_pairs.
    for (String value : move_related.keySet()) {
      now_move_related.put(value, null);
    }
  }

  void ConflictSimplify(int degree) {
    Queue<String> removes = new LinkedList<>();
    for (Map.Entry<String, HashMap<String, Boolean>> node : simplified_graph.entrySet()) {
      if ((node.getValue().size() < degree) && (!move_related.containsKey(node.getKey()))) {
        removes.add(node.getKey());
      }
    }
    while (!removes.isEmpty()) {
      String remove = removes.poll();
      HashMap<String, Boolean> remove_edge = simplified_graph.get(remove);
      for (String des : remove_edge.keySet()) {
        simplified_graph.get(des).remove(remove);
      }
      simplified_graph.remove(remove);
      selected.push(remove);
    }
    return;
  }

  void ConflictCoalesce(int degree) {
    for(Map.Entry<String, HashMap<String, Boolean>> node: coalesced_move_pair.entrySet()) {
      for(String value: coalesced_move_pair.keySet()) {
        boolean flag = true;
        for(String edge: simplified_graph.get(node.getKey()).keySet()) {
          if((simplified_graph.get(edge).size() < degree) || simplified_graph.get(edge).containsKey(value)) {
            continue;
          } else {
            flag = false;
            break;
          }
        }//George's rule.
        if(flag) {
          coalesced_node.put(value, node.getKey());//value is attached to node.
          for(String res: coalesced_move_pair.get(node.getKey()).keySet()) {
            coalesced_move_pair.get(res).remove(node.getKey());
          }
          for(String res:coalesced_move_pair.get(value).keySet()) {
            coalesced_move_pair.get(res).remove(value);
          }
          coalesced_move_pair.remove(value);
          coalesced_move_pair.remove(node.getKey());
          now_move_related.remove(node.getKey());
          now_move_related.remove(value);
          for(String res: simplified_graph.get(value).keySet()) {
            simplified_graph.get(res).remove(value);
            simplified_graph.get(res).put(node.getKey(), null);
            simplified_graph.get(node.getKey()).put(res, null);
          }
          simplified_graph.remove(value);
        }
      }
    }
    return;
  }
}
