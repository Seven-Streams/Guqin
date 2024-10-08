package Optimization;

import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import IRSentence.*;
import Composer.*;

public class LinearScan {
  Composer machine = null;
  int number = 0;
  int func_cnt = 0;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> liveness = new HashMap<>();
  public HashMap<Integer, HashMap<String, Interval>> interval_check = new HashMap<>();
  public HashMap<Integer, Boolean> visit = new HashMap<>();
  public HashMap<Integer, Integer> block_entries = new HashMap<>();
  public HashMap<Integer, PriorityQueue<Interval>> intervals = new HashMap<>();
  public HashMap<Integer, HashMap<String, Integer>> registers = new HashMap<>();
  public HashMap<Integer, Integer> stack_variables = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> use = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> def = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> in = new HashMap<>();
  public HashMap<Integer, HashMap<String, Boolean>> out = new HashMap<>();
  public HashMap<Integer, String> register_names = new HashMap<>();
  public HashMap<Integer, Interval> func_length = new HashMap<>();
  public HashMap<Integer, Integer> max_register_use = new HashMap<>();
  public TreeMap<Integer, HashMap<Integer, Boolean>> calling_use = new TreeMap<>();
  public HashMap<Integer, Integer> visit_cnt = new HashMap<>();
  public HashMap<String, String> from_to_pair = new HashMap<>();
  public HashMap<String, Boolean> move_des = new HashMap<>();
  public HashMap<Integer, HashMap<String, Integer>> pre_alloc = new HashMap<>();

  public LinearScan(Composer _machine) {
    machine = _machine;
  }

  public void Allocator(int degree) {
    CheckUnecessaryCalling();
    CheckMove();
    BuildGraph();
    NumberIns();
    UseDefCheck();
    InOutCheck();
    GetSentenceInOut();
    CheckMove();
    SortingIntervals();
    AllocateAll(degree);
    CalculateStack();
    RegisterName();
  }

  void CheckMove() {
    for (IRCode code : machine.generated) {
      if (code instanceof MoveBlock) {
        MoveBlock move_block = (MoveBlock) code;
        for (PseudoMove move : move_block.moves) {
          if (!move.dead) {
            try {
              Integer.parseInt(move.src);
            } catch (NumberFormatException e) {
              move_des.put(move.des, null);
              from_to_pair.put(move.src, move.des);
            }
          }
        }
      }
      if (code instanceof PseudoMove) {
        PseudoMove move = (PseudoMove) code;
        try {
          Integer.parseInt(move.src);
        } catch (NumberFormatException e) {
          move_des.put(move.des, null);
          from_to_pair.put(move.src, move.des);
        }
      }
    }
  }

  void PrintPre() {
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> node : pre.entrySet()) {
      System.out.print(node.getKey() + ":");
      for (int to : node.getValue().keySet()) {
        System.out.print(to + ", ");
      }
      System.out.println();
    }
  }

  void CheckUnecessaryCalling() {
    for (int i = 0; i < machine.generated.size(); i++) {
      if (machine.generated.get(i) instanceof IRFunc) {
        if (machine.generated.get(i + 1) instanceof IRFuncend) {
          IRFunc.Empty_func.put(((IRFunc) machine.generated.get(i)).name, null);
        }
      }
    }
    return;
  }

  void PrintInOut() {
    for (Map.Entry<Integer, HashMap<String, Boolean>> entry : in.entrySet()) {
      System.out.println(entry.getKey() + ":");
      System.out.print("in:");
      for (String in_v : entry.getValue().keySet()) {
        System.out.print(in_v + ", ");
      }
      System.out.println();
      System.out.print("out:");
      for (String out_v : out.get(entry.getKey()).keySet()) {
        System.out.print(out_v + ", ");
      }
      System.out.println();
    }
  }

  void PrintInterval() {
    for (HashMap<String, Interval> check : interval_check.values()) {
      for (Interval to_print : check.values()) {
        System.out.println(to_print.name + ":" + to_print.start + "->" + to_print.end);
      }
    }
  }

  void PrintNum() {
    for (IRCode code : machine.generated) {
      System.out.print(code.sentence_number + ":");
      code.CodePrint();
    }
  }

  void PrintReg() {
    for (HashMap<String, Integer> entry : registers.values()) {
      for (Map.Entry<String, Integer> pair : entry.entrySet()) {
        System.out.println(pair.getKey() + ":" + pair.getValue());
      }
    }
  }

  void PrintName() {
    for (Map.Entry<Integer, String> pair : register_names.entrySet()) {
      System.out.println(pair.getKey() + " " + pair.getValue());
    }
  }

  void PrintGraph() {
    for (Map.Entry<Integer, HashMap<Integer, Boolean>> node : graph.entrySet()) {
      System.out.print(node.getKey() + ":");
      for (int to : node.getValue().keySet()) {
        System.out.print(to + ", ");
      }
      System.out.println();
    }
  }

  public void Codegen() throws Exception {
    int cnt = 0;
    System.out.println(".data");
    IRCode.buffer.clear();
    for (IRCode code : machine.const_str) {
      code.CodegenWithOptim(null, register_names);
    }
    for (IRCode code : machine.global) {
      code.CodegenWithOptim(null, register_names);
    }
    System.out.println("");
    System.out.println(".text");
    System.out.println(".globl main");
    boolean last_return = false;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if ((code.sentence_number == 0) && (!(code instanceof IRFuncend || code instanceof IRTail))) {
        continue;
      }
      if (code instanceof IRFunc) {
        cnt--;
      }
      if (last_return) {
        if (code instanceof IRFuncend) {
          IRFuncend end = (IRFuncend) code;
          end.last_return = true;
        }
      }
      code.CodegenWithOptim(registers.get(cnt), register_names);
      if (code instanceof IRReturn) {
        last_return = true;
      } else {
        last_return = false;
      }
    }
    HashMap<Integer, Integer> label_place = new HashMap<>();
    HashMap<String, Integer> cond_place = new HashMap<>();
    int now_memory = 0;
    for (String code : IRCode.buffer) {
      if (code.equals("") || (code.endsWith(":"))) {
        if (code.startsWith("b")) {
          try {
            int label = Integer.parseInt(code.substring(1, code.length() - 1));
            label_place.put(label, now_memory);
          } catch (NumberFormatException e) {
          }
        } else {
          cond_place.put(code, now_memory);
        }
      } else {
        if (code.startsWith("li"))
          now_memory++;
      }
      now_memory++;
    }
    for (int i = IRCode.buffer.size() - 1; i >= 0; i--) {
      String code = IRCode.buffer.get(i);
      if (code.contains("cond.") && code.endsWith(":")) {
        int now = cond_place.get(code);
        String target_ins = IRCode.buffer.get(i + 1);
        int target_label = Integer.parseInt(target_ins.substring(3, target_ins.length()));
        int label = label_place.get(target_label);
        if (Math.abs(label - now) < 1023) {
          IRCode.buffer.remove(i + 1);
          String to_remove = code.substring(0, code.length() - 1);
          String to_operate = IRCode.buffer.get(i - 2);
          IRCode.buffer.set(i - 2, to_operate.replace(to_remove, "b" + Integer.toString(target_label)));
          IRCode.buffer.remove(i);
        }
      }
    }
    for (String code : IRCode.buffer) {
      System.out.println(code);
    }
    return;
  }

  void BuildGraph() {
    graph.clear();
    int now = 0;
    HashMap<Integer, Boolean> nxt = null;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFunc) {
        now = --func_cnt;
        block_entries.put(now, i);
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
        block_entries.put(now, i);
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
        pre.get(move.to).put(move.num, null);
        block_entries.put(now, i);
      }
    }
    return;
  }

  void NumberIns() {
    visit.clear();
    for (int i = -1; i >= func_cnt; i--) {
      NumberBlocks(i);
    }
  }

  void NumberBlocks(int start) {
    Stack<Integer> check_list = new Stack<>();
    HashMap<Integer, HashMap<Integer, Boolean>> last_visit = new HashMap<>();
    check_list.add(start);
    while (!check_list.isEmpty()) {
      int index = check_list.pop();
      if (!visit.containsKey(index)) {
        int begin = block_entries.get(index);
        if (!visit_cnt.containsKey(index)) {
          visit_cnt.put(index, 0);
        }
        visit_cnt.put(index, visit_cnt.get(index) + 1);
        machine.generated.get(begin).sentence_number = ++number;
        for (int i = begin + 1; i < machine.generated.size(); i++) {
          if (machine.generated.get(i) instanceof IRLabel || machine.generated.get(i) instanceof IRFunc
              || machine.generated.get(i) instanceof MoveBlock) {
            break;
          } else {
            machine.generated.get(i).sentence_number = ++number;
            if (machine.generated.get(i) instanceof IRFuncall) {
              calling_use.put(number, new HashMap<>());
            }
          }
        }
        visit.put(index, null);
        last_visit.put(index, new HashMap<>());
      }
      boolean finished = true;
      for (int nxt : graph.get(index).keySet()) {
        if ((!visit.containsKey(nxt)) && (!last_visit.get(index).containsKey(nxt))) {
          check_list.add(index);
          check_list.add(nxt);
          last_visit.get(index).put(nxt, null);
          finished = false;
          break;
        }
      }
      if (finished) {
        if (visit_cnt.get(index) < 50) {
          visit.remove(index);
        }
      }
    }
    return;
  }

  void UseDefCheck() {
    for (IRCode code : machine.global) {
      code.UseDefCheck(null, null);
    }
    for (IRCode code : machine.const_str) {
      code.UseDefCheck(null, null);
    }
    use.clear();
    def.clear();
    int func = 0;
    int now = 0;
    HashMap<String, Boolean> res_def = null;
    HashMap<String, Boolean> res_use = null;
    for (IRCode code : machine.generated) {
      if (code instanceof IRLabel) {
        if (res_def != null) {
          use.put(now, res_use);
          def.put(now, res_def);
        }
        IRLabel label = (IRLabel) code;
        res_def = new HashMap<>();
        res_use = new HashMap<>();
        now = label.label;
      }
      if (code instanceof IRFunc) {
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
      code.UseDefCheck(res_def, res_use);
      if (code instanceof IRFuncend) {
        use.put(now, res_use);
        def.put(now, res_def);
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
      HashMap<String, Boolean> res = new HashMap<>();
      if (!in.containsKey(to_check)) {
        in.put(to_check, new HashMap<>());
      }
      if (!out.containsKey(to_check)) {
        out.put(to_check, new HashMap<>());
      }
      for (String out_v : out.get(to_check).keySet()) {
        res.put(out_v, null);
      }
      for (String def_v : def.get(to_check).keySet()) {
        res.remove(def_v);
      }
      for (String use_v : use.get(to_check).keySet()) {
        res.put(use_v, null);
      }
      HashMap<String, Boolean> to_operate = in.get(to_check);
      for (String res_v : res.keySet()) {
        if (!to_operate.containsKey(res_v)) {
          to_operate.put(res_v, null);
        }
      }
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
          if (!visit.containsKey(pre_v)) {
            flag_2 = true;
            visit.put(pre_v, null);
          }
          if (flag_2) {
            check_list.add(pre_v);
          }
        }
      }
    }
    return;
  }

  void SortingIntervals() {
    for (Map.Entry<Integer, HashMap<String, Interval>> entry : interval_check.entrySet()) {
      intervals.put(entry.getKey(), new PriorityQueue<>(new Interval().new IntervalComparator()));
      for (Interval to_add : entry.getValue().values()) {
        intervals.get(entry.getKey()).add(to_add);
      }
    }
    return;
  }

  void AllocateAll(int degree) {
    for (int i = -1; i >= func_cnt; i--) {
      RegisterAllocate(i, degree);
    }
    IRCode.register_use = max_register_use;
    IRFuncall.to_save_registers = calling_use;
  }

  void RegisterAllocate(int func_num, int degree) {
    HashMap<String, Integer> alloc = pre_alloc.get(func_num);
    max_register_use.put(func_num, -1);
    ArrayList<Interval> free = new ArrayList<>();
    for (int i = 0; i < degree; i++) {
      free.add(null);
    }
    int stack_num = -16;
    PriorityQueue<Interval> to_alloc = intervals.get(func_num);
    registers.put(func_num, new HashMap<>());
    while (!to_alloc.isEmpty()) {
      Interval now_alloc = to_alloc.poll();
      boolean spilled = true;
      Map<Integer, HashMap<Integer, Boolean>> submap = calling_use.subMap(now_alloc.start, now_alloc.end);
      if (submap.isEmpty()) {
        if (alloc.containsKey(now_alloc.name)) {
          int target_res = alloc.get(now_alloc.name);
          if ((free.get(target_res) == null) || free.get(target_res).end < now_alloc.start) {
            free.set(target_res, now_alloc);
            registers.get(func_num).put(now_alloc.name, target_res);
            spilled = false;
            continue;
          }
        }
        if (from_to_pair.containsKey(now_alloc.name)) {
          if (registers.get(func_num).containsKey(from_to_pair.get(now_alloc.name))
              && (registers.get(func_num).get(from_to_pair.get(now_alloc.name)) >= 12)) {
            int value = registers.get(func_num).get(from_to_pair.get(now_alloc.name));
            if ((free.get(value) == null) || free.get(value).end < now_alloc.start) {
              free.set(value, now_alloc);
              registers.get(func_num).put(now_alloc.name, value);
              if (max_register_use.get(func_num) < value) {
                max_register_use.put(func_num, value);
              }
              continue;
            }
          }
        }
        if (!spilled) {
          continue;
        }
        if (from_to_pair.containsKey(now_alloc.name)) {
          if (registers.get(func_num).containsKey(from_to_pair.get(now_alloc.name))
              && (registers.get(func_num).get(from_to_pair.get(now_alloc.name)) >= 0)
              && (registers.get(func_num).get(from_to_pair.get(now_alloc.name)) < 12)) {
            int value = registers.get(func_num).get(from_to_pair.get(now_alloc.name));
            if ((free.get(value) == null) || free.get(value).end < now_alloc.start) {
              free.set(value, now_alloc);
              registers.get(func_num).put(now_alloc.name, value);
              if (max_register_use.get(func_num) < value) {
                max_register_use.put(func_num, value);
              }
              for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : submap.entrySet()) {
                if ((now_alloc.start < entry.getKey()) && (now_alloc.end > entry.getKey())) {
                  entry.getValue().put(value, null);
                    int cnt = 0;
                for (int use_reg : entry.getValue().keySet()) {
                  if (use_reg > 11) {
                    cnt++;
                  }
                }
                max_register_use.put(func_num, Integer.max(cnt, max_register_use.get(func_num)));
                }
              }
              continue;
            }
          }
        }
        for (int i = 12; i < degree; i++) {
          if ((free.get(i) == null) || free.get(i).end < now_alloc.start) {
            free.set(i, now_alloc);
            registers.get(func_num).put(now_alloc.name, i);
            spilled = false;
            break;
          }
        }
      }
      if (!spilled) {
        continue;
      }
      if (from_to_pair.containsKey(now_alloc.name)) {
        if (registers.get(func_num).containsKey(from_to_pair.get(now_alloc.name))
            && (registers.get(func_num).get(from_to_pair.get(now_alloc.name)) >= 0)) {
          int value = registers.get(func_num).get(from_to_pair.get(now_alloc.name));
          if ((free.get(value) == null) || free.get(value).end < now_alloc.start) {
            free.set(value, now_alloc);
            registers.get(func_num).put(now_alloc.name, value);
            if (max_register_use.get(func_num) < value) {
              max_register_use.put(func_num, value);
            }
            for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : submap.entrySet()) {
              if ((now_alloc.start < entry.getKey()) && (now_alloc.end > entry.getKey())) {
                entry.getValue().put(value, null);
                int cnt = 0;
                for (int use_reg : entry.getValue().keySet()) {
                  if (use_reg > 11) {
                    cnt++;
                  }
                }
                max_register_use.put(func_num, Integer.max(cnt, max_register_use.get(func_num)));
              }
            }
            continue;
          }
        }
      }
      for (int i = 0; i < 12; i++) {
        if ((free.get(i) == null) || free.get(i).end < now_alloc.start) {
          free.set(i, now_alloc);
          registers.get(func_num).put(now_alloc.name, i);
          spilled = false;
          if (max_register_use.get(func_num) < i) {
            max_register_use.put(func_num, i);
          }
          for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : submap.entrySet()) {
            if ((now_alloc.start < entry.getKey()) && (now_alloc.end > entry.getKey())) {
              entry.getValue().put(i, null);
            }
          }
          break;
        }
      }
      if (!spilled) {
        continue;
      }
      if (alloc.containsKey(now_alloc.name)) {
        int target_res = alloc.get(now_alloc.name);
        if ((free.get(target_res) == null) || free.get(target_res).end < now_alloc.start) {
          free.set(target_res, now_alloc);
          registers.get(func_num).put(now_alloc.name, target_res);
          spilled = false;
          for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : submap.entrySet()) {
            if ((now_alloc.start < entry.getKey()) && (now_alloc.end > entry.getKey())) {
              entry.getValue().put(target_res, null);
                int cnt = 0;
                for (int use_reg : entry.getValue().keySet()) {
                  if (use_reg > 11) {
                    cnt++;
                  }
                }
                max_register_use.put(func_num, Integer.max(cnt, max_register_use.get(func_num)));
            }
          }
          continue;
        }
      }
      for (int i = 12; i < degree; i++) {
        if ((free.get(i) == null) || free.get(i).end < now_alloc.start) {
          free.set(i, now_alloc);
          registers.get(func_num).put(now_alloc.name, i);
          spilled = false;
          if (max_register_use.get(func_num) < i) {
            max_register_use.put(func_num, i);
          }
          for (Map.Entry<Integer, HashMap<Integer, Boolean>> entry : submap.entrySet()) {
            if ((now_alloc.start < entry.getKey()) && (now_alloc.end > entry.getKey())) {
              entry.getValue().put(i, null);
                int cnt = 0;
                for (int use_reg : entry.getValue().keySet()) {
                  if (use_reg > 11) {
                    cnt++;
                  }
                }
                max_register_use.put(func_num, Integer.max(cnt, max_register_use.get(func_num)));
            }
          }
          break;
        }
      }
      if (spilled) {
        registers.get(func_num).put(now_alloc.name, --stack_num);
      }
    }
    IRFunc main_check = (IRFunc) (machine.generated.get(block_entries.get(func_num)));
    if (main_check.name.equals("main")) {
      max_register_use.put(func_num, -1);
    }
    stack_variables.put(func_num, -stack_num);
    return;
  }

  void CalculateStack() {
    IRFunc check = null;
    int func_res = 0;
    int now_func = 0;
    for (IRCode code : machine.generated) {
      if (code instanceof IRFunc) {
        now_func--;
        check = (IRFunc) code;
      }
      if (code instanceof IRFuncall) {
        IRFuncall res = (IRFuncall) code;
        int reg_cnt = 0;
        if (res.sentence_number == 0) {
          continue;
        }
        for (int to_save : calling_use.get(res.sentence_number).keySet()) {
          if (to_save > 11) {
            reg_cnt++;
          }
        }
        func_res = Integer.max(func_res, (reg_cnt + 2 + Integer.max(0, res.reg.size() - 8)));
      }
      if (code instanceof IRFuncend) {
        check.size = func_res + stack_variables.get(now_func) + Integer.min(max_register_use.get(now_func) + 1, 16);
      }
    }
    return;
  }

  void RegisterName() {
    int cnt = 0;
    for (int i = 0; i <= 11; i++) {
      register_names.put(cnt++, "s" + Integer.toString(i));
    }
    for (int i = 2; i <= 6; i++) {
      register_names.put(cnt++, "t" + Integer.toString(i));
    }
    register_names.put(cnt++, "ra");
    register_names.put(cnt++, "gp");
    register_names.put(cnt++, "tp");
    for (int i = 7; i >= 0; i--) {
      register_names.put(cnt++, "a" + Integer.toString(i));
    }
    register_names.put(cnt++, "t0");
    register_names.put(cnt++, "t1");
    return;
  }

  void GetSentenceInOut() {
    int cnt = 0;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFuncall) {
        IRFuncall call = (IRFuncall) code;
        HashMap<String, Integer> res = pre_alloc.get(cnt);
        for (int j = 0; j < Integer.min(call.reg.size(), 8); j++) {
          try {
            Integer.parseInt(call.reg.get(j));
          } catch (NumberFormatException e) {
            if (IRCode.CheckLit(call.reg.get(j))) {
              res.put(call.reg.get(j), 27 - j);
            }
          }
        }
        if (call.target_reg != null) {
          res.put(call.target_reg, 27);
        }
      }
      if (code instanceof IRReturn) {
        HashMap<String, Integer> res = pre_alloc.get(cnt);
        IRReturn return_ins = (IRReturn) code;
        if (return_ins.reg != null) {
          try {
            Integer.parseInt(return_ins.reg);
          } catch (NumberFormatException e) {
            if (IRCode.CheckLit(return_ins.reg)) {
              res.put(return_ins.reg, 27);
            }
          }
        }
      }
      if (code instanceof IRFunc) {
        interval_check.put(--cnt, new HashMap<>());
        HashMap<String, Integer> res = new HashMap<>();
        pre_alloc.put(cnt, res);
        IRFunc func = (IRFunc) code;
        for (int j = 0; j < Integer.min(func.names.size(), 8); j++) {
          res.put(func.names.get(j), 27 - j);
        }
        int end = 0;
        HashMap<String, Boolean> total_out = out.get(cnt);
        for (int j = i; j < machine.generated.size(); j++) {
          if (machine.generated.get(j) instanceof IRLabel) {
            end = j - 1;
            break;
          }
          if (machine.generated.get(j) instanceof IRFuncend) {
            end = j - 1;
            break;
          }
          if (machine.generated.get(j) instanceof MoveBlock) {
            end = j - 1;
            break;
          }
        }
        HashMap<String, Boolean> use_res = new HashMap<>();
        HashMap<String, Boolean> def_res = new HashMap<>();
        for (int j = end; j >= i; j--) {
          int now_num = machine.generated.get(j).sentence_number;
          if (total_out == null) {
            total_out = new HashMap<>();
          }
          for (String out_v : total_out.keySet()) {
            if (!interval_check.get(cnt).containsKey(out_v)) {
              interval_check.get(cnt).put(out_v, new Interval(now_num, now_num, out_v));
            } else {
              if (interval_check.get(cnt).get(out_v).start > now_num) {
                interval_check.get(cnt).get(out_v).start = now_num;
              }
              if (interval_check.get(cnt).get(out_v).end < now_num) {
                interval_check.get(cnt).get(out_v).end = now_num;
              }
            }
          }
          def_res.clear();
          use_res.clear();
          machine.generated.get(j).UseDefCheck(def_res, use_res);
          for (String def_v : def_res.keySet()) {
            total_out.remove(def_v);
          }
          for (String use_v : use_res.keySet()) {
            total_out.put(use_v, null);
          }
          for (String out_v : total_out.keySet()) {
            if (!interval_check.get(cnt).containsKey(out_v)) {
              interval_check.get(cnt).put(out_v, new Interval(now_num, now_num, out_v));
            } else {
              if (interval_check.get(cnt).get(out_v).start > now_num) {
                interval_check.get(cnt).get(out_v).start = now_num;
              }
              if (interval_check.get(cnt).get(out_v).end < now_num) {
                interval_check.get(cnt).get(out_v).end = now_num;
              }
            }
          }
        }
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) (code);
        int end = 0;
        HashMap<String, Boolean> total_out = out.get(label.label);
        if (total_out == null) {
          total_out = new HashMap<>();
        }
        for (int j = i + 1; j < machine.generated.size(); j++) {
          if (machine.generated.get(j) instanceof IRLabel) {
            end = j - 1;
            break;
          }
          if (machine.generated.get(j) instanceof IRFuncend) {
            end = j - 1;
            break;
          }
          if (machine.generated.get(j) instanceof MoveBlock) {
            end = j - 1;
            break;
          }
        }
        HashMap<String, Boolean> use_res = new HashMap<>();
        HashMap<String, Boolean> def_res = new HashMap<>();
        for (int j = end; j >= i; j--) {
          int now_num = machine.generated.get(j).sentence_number;
          for (String out_v : total_out.keySet()) {
            if (!interval_check.get(cnt).containsKey(out_v)) {
              interval_check.get(cnt).put(out_v, new Interval(now_num, now_num, out_v));
            } else {
              if (interval_check.get(cnt).get(out_v).start > now_num) {
                interval_check.get(cnt).get(out_v).start = now_num;
              }
              if (interval_check.get(cnt).get(out_v).end < now_num) {
                interval_check.get(cnt).get(out_v).end = now_num;
              }
            }
          }
          def_res.clear();
          use_res.clear();
          machine.generated.get(j).UseDefCheck(def_res, use_res);
          for (String def_v : def_res.keySet()) {
            total_out.remove(def_v);
          }
          for (String use_v : use_res.keySet()) {
            total_out.put(use_v, null);
          }
          for (String out_v : total_out.keySet()) {
            if (!interval_check.get(cnt).containsKey(out_v)) {
              interval_check.get(cnt).put(out_v, new Interval(now_num, now_num, out_v));
            } else {
              if (interval_check.get(cnt).get(out_v).start > now_num) {
                interval_check.get(cnt).get(out_v).start = now_num;
              }
              if (interval_check.get(cnt).get(out_v).end < now_num) {
                interval_check.get(cnt).get(out_v).end = now_num;
              }
            }
          }
        }
      }
      if (code instanceof MoveBlock) {
        MoveBlock block = (MoveBlock) code;
        HashMap<String, Boolean> use_res = new HashMap<>();
        HashMap<String, Boolean> def_res = new HashMap<>();
        HashMap<String, Boolean> total_out = out.get(block.num);
        int now_num = block.sentence_number;
        if (total_out == null) {
          total_out = new HashMap<>();
        }
        def_res.clear();
        use_res.clear();
        block.UseDefCheck(def_res, use_res);
        for (String def_v : def_res.keySet()) {
          total_out.remove(def_v);
        }
        for (String use_v : use_res.keySet()) {
          total_out.put(use_v, null);
        }
        for (String out_v : total_out.keySet()) {
          if (!interval_check.get(cnt).containsKey(out_v)) {
            interval_check.get(cnt).put(out_v, new Interval(now_num, now_num, out_v));
          } else {
            if (interval_check.get(cnt).get(out_v).start > now_num) {
              interval_check.get(cnt).get(out_v).start = now_num;
            }
            if (interval_check.get(cnt).get(out_v).end < now_num) {
              interval_check.get(cnt).get(out_v).end = now_num;
            }
          }
        }
        for (String use_v : use_res.keySet()) {
          if (interval_check.get(cnt).get(use_v).end < (now_num + 1)) {
            interval_check.get(cnt).get(use_v).end = now_num + 1;
          }
        }
      }
    }
    return;
  }

  public void PrintBuiltIn() {
    System.out.println("\t.text\r\n" + //
        "\t.attribute\t4, 16\r\n" + //
        "\t.attribute\t5, \"rv32i2p1_m2p0_a2p1_c2p0\"\r\n" + //
        "\t.file\t\"Inner.c\"\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tprint                           # -- Begin function print\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tprint,@function\r\n" + //
        "print:                                  # @print\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tmv a1, a0\r\n" + //
        "\tlui\ta0, %hi(.L.str)\r\n" + //
        "\taddi\ta0, a0, %lo(.L.str)\r\n" + //
        "\tcall\tprintf\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end1:\r\n" + //
        "\t.size\tprint, .Lfunc_end1-print\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tprintInt                        # -- Begin function printInt\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tprintInt,@function\r\n" + //
        "printInt:                               # @printInt\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tmv  a1, a0\r\n" + //
        "\tlui\ta0, %hi(.L.str.1)\r\n" + //
        "\taddi\ta0, a0, %lo(.L.str.1)\r\n" + //
        "\tcall\tprintf\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end2:\r\n" + //
        "\t.size\tprintInt, .Lfunc_end2-printInt\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tprintIntln                      # -- Begin function printIntln\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tprintIntln,@function\r\n" + //
        "printIntln:                             # @printIntln\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tmv  a1, a0\r\n" + //
        "\tlui\ta0, %hi(.L.str.2)\r\n" + //
        "\taddi\ta0, a0, %lo(.L.str.2)\r\n" + //
        "\tcall\tprintf\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end3:\r\n" + //
        "\t.size\tprintIntln, .Lfunc_end3-printIntln\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\ttoString                        # -- Begin function toString\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\ttoString,@function\r\n" + //
        "toString:                               # @toString\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -32\r\n" + //
        "\tsw\tra, 28(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 20(sp)\r\n" + //
        "\tli\ta0, 15\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 16(sp)\r\n" + //
        "\tlw\ta2, 20(sp)\r\n" + //
        "\tlui\ta1, %hi(.L.str.1)\r\n" + //
        "\taddi\ta1, a1, %lo(.L.str.1)\r\n" + //
        "\tcall\tsprintf\r\n" + //
        "\tlw\ta0, 16(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\taddi\ta0, a0, 1\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 12(sp)\r\n" + //
        "\tlw\ta1, 16(sp)\r\n" + //
        "\tcall\tstrcpy\r\n" + //
        "\tlw\ta0, 16(sp)\r\n" + //
        "\tcall\tfree\r\n" + //
        "\tlw\ta0, 12(sp)\r\n" + //
        "\tlw\tra, 28(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 32\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end4:\r\n" + //
        "\t.size\ttoString, .Lfunc_end4-toString\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tgetInt                          # -- Begin function getInt\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tgetInt,@function\r\n" + //
        "getInt:                                 # @getInt\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tlui\ta0, %hi(.L.str.1)\r\n" + //
        "\taddi\ta0, a0, %lo(.L.str.1)\r\n" + //
        "\taddi\ta1, sp, 4\r\n" + //
        "\tcall\tscanf\r\n" + //
        "\tlw\ta0, 4(sp)\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end5:\r\n" + //
        "\t.size\tgetInt, .Lfunc_end5-getInt\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tgetString                       # -- Begin function getString\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tgetString,@function\r\n" + //
        "getString:                              # @getString\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tlui\ta0, 1\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 4(sp)\r\n" + //
        "\tmv  a1, a0\r\n" + //
        "\tlui\ta0, %hi(.L.str)\r\n" + //
        "\taddi\ta0, a0, %lo(.L.str)\r\n" + //
        "\tcall\tscanf\r\n" + //
        "\tlw\ta0, 4(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\taddi\ta0, a0, 1\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 0(sp)\r\n" + //
        "\tlw\ta1, 4(sp)\r\n" + //
        "\tcall\tstrcpy\r\n" + //
        "\tlw\ta0, 4(sp)\r\n" + //
        "\tcall\tfree\r\n" + //
        "\tlw\ta0, 0(sp)\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end6:\r\n" + //
        "\t.size\tgetString, .Lfunc_end6-getString\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_length                   # -- Begin function string_length\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_length,@function\r\n" + //
        "string_length:                          # @string_length\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end7:\r\n" + //
        "\t.size\tstring_length, .Lfunc_end7-string_length\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_substring                # -- Begin function string_substring\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_substring,@function\r\n" + //
        "string_substring:                       # @string_substring\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -32\r\n" + //
        "\tsw\tra, 28(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 20(sp)\r\n" + //
        "\tsw\ta1, 16(sp)\r\n" + //
        "\tsw\ta2, 12(sp)\r\n" + //
        "\tli\ta0, 5\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 8(sp)\r\n" + //
        "\tlw\ta1, 20(sp)\r\n" + //
        "\tlw\ta2, 16(sp)\r\n" + //
        "\tadd\ta1, a1, a2\r\n" + //
        "\tcall\tstrcpy\r\n" + //
        "\tlw\ta0, 8(sp)\r\n" + //
        "\tlw\ta1, 12(sp)\r\n" + //
        "\tlw\ta2, 16(sp)\r\n" + //
        "\tsub\ta1, a1, a2\r\n" + //
        "\tadd\ta1, a1, a0\r\n" + //
        "\tli\ta0, 0\r\n" + //
        "\tsb\ta0, 0(a1)\r\n" + //
        "\tlw\ta0, 8(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\taddi\ta0, a0, 1\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 4(sp)\r\n" + //
        "\tlw\ta1, 8(sp)\r\n" + //
        "\tcall\tstrcpy\r\n" + //
        "\tlw\ta0, 8(sp)\r\n" + //
        "\tcall\tfree\r\n" + //
        "\tlw\ta0, 4(sp)\r\n" + //
        "\tlw\tra, 28(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 32\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end8:\r\n" + //
        "\t.size\tstring_substring, .Lfunc_end8-string_substring\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_parseInt                 # -- Begin function string_parseInt\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_parseInt,@function\r\n" + //
        "string_parseInt:                        # @string_parseInt\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -32\r\n" + //
        "\tsw\tra, 28(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 20(sp)\r\n" + //
        "\tli\ta0, 0\r\n" + //
        "\tsw\ta0, 16(sp)\r\n" + //
        "\tsw\ta0, 12(sp)\r\n" + //
        "\tj\t.LBB9_1\r\n" + //
        ".LBB9_1:                                # =>This Inner Loop Header: Depth=1\r\n" + //
        "\tlw\ta0, 12(sp)\r\n" + //
        "\tsw\ta0, 8(sp)                     # 4-byte Folded Spill\r\n" + //
        "\tlw\ta0, 20(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\tmv\ta1, a0\r\n" + //
        "\tlw\ta0, 8(sp)                     # 4-byte Folded Reload\r\n" + //
        "\tbgeu\ta0, a1, .LBB9_8\r\n" + //
        "\tj\t.LBB9_2\r\n" + //
        ".LBB9_2:                                #   in Loop: Header=BB9_1 Depth=1\r\n" + //
        "\tlw\ta0, 20(sp)\r\n" + //
        "\tlw\ta1, 12(sp)\r\n" + //
        "\tadd\ta0, a0, a1\r\n" + //
        "\tlbu\ta0, 0(a0)\r\n" + //
        "\tli\ta1, 48\r\n" + //
        "\tblt\ta0, a1, .LBB9_5\r\n" + //
        "\tj\t.LBB9_3\r\n" + //
        ".LBB9_3:                                #   in Loop: Header=BB9_1 Depth=1\r\n" + //
        "\tlw\ta0, 20(sp)\r\n" + //
        "\tlw\ta1, 12(sp)\r\n" + //
        "\tadd\ta0, a0, a1\r\n" + //
        "\tlbu\ta1, 0(a0)\r\n" + //
        "\tli\ta0, 57\r\n" + //
        "\tblt\ta0, a1, .LBB9_5\r\n" + //
        "\tj\t.LBB9_4\r\n" + //
        ".LBB9_4:                                #   in Loop: Header=BB9_1 Depth=1\r\n" + //
        "\tlw\ta0, 16(sp)\r\n" + //
        "\tli\ta1, 10\r\n" + //
        "\tmul\ta0, a0, a1\r\n" + //
        "\tsw\ta0, 16(sp)\r\n" + //
        "\tlw\ta0, 20(sp)\r\n" + //
        "\tlw\ta1, 12(sp)\r\n" + //
        "\tadd\ta0, a0, a1\r\n" + //
        "\tlbu\ta0, 0(a0)\r\n" + //
        "\tlw\ta1, 16(sp)\r\n" + //
        "\tadd\ta0, a0, a1\r\n" + //
        "\taddi\ta0, a0, -48\r\n" + //
        "\tsw\ta0, 16(sp)\r\n" + //
        "\tj\t.LBB9_6\r\n" + //
        ".LBB9_5:\r\n" + //
        "\tj\t.LBB9_8\r\n" + //
        ".LBB9_6:                                #   in Loop: Header=BB9_1 Depth=1\r\n" + //
        "\tj\t.LBB9_7\r\n" + //
        ".LBB9_7:                                #   in Loop: Header=BB9_1 Depth=1\r\n" + //
        "\tlw\ta0, 12(sp)\r\n" + //
        "\taddi\ta0, a0, 1\r\n" + //
        "\tsw\ta0, 12(sp)\r\n" + //
        "\tj\t.LBB9_1\r\n" + //
        ".LBB9_8:\r\n" + //
        "\tlw\ta0, 16(sp)\r\n" + //
        "\tlw\tra, 28(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 32\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end9:\r\n" + //
        "\t.size\tstring_parseInt, .Lfunc_end9-string_parseInt\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_ord                      # -- Begin function string_ord\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_ord,@function\r\n" + //
        "string_ord:                             # @string_ord\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 4(sp)\r\n" + //
        "\tsw\ta1, 0(sp)\r\n" + //
        "\tadd\ta0, a0, a1\r\n" + //
        "\tlbu\ta0, 0(a0)\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end10:\r\n" + //
        "\t.size\tstring_ord, .Lfunc_end10-string_ord\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_cmp                      # -- Begin function string_cmp\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_cmp,@function\r\n" + //
        "string_cmp:                             # @string_cmp\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tcall\tstrcmp\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end11:\r\n" + //
        "\t.size\tstring_cmp, .Lfunc_end11-string_cmp\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_cat                      # -- Begin function string_cat\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_cat,@function\r\n" + //
        "string_cat:                             # @string_cat\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -32\r\n" + //
        "\tsw\tra, 28(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 20(sp)\r\n" + //
        "\tsw\ta1, 16(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\tsw\ta0, 8(sp)                     # 4-byte Folded Spill\r\n" + //
        "\tlw\ta0, 16(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\tmv\ta1, a0\r\n" + //
        "\tlw\ta0, 8(sp)                     # 4-byte Folded Reload\r\n" + //
        "\tadd\ta0, a0, a1\r\n" + //
        "\taddi\ta0, a0, 1\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 12(sp)\r\n" + //
        "\tlw\ta1, 20(sp)\r\n" + //
        "\tcall\tstrcpy\r\n" + //
        "\tlw\ta0, 12(sp)\r\n" + //
        "\tlw\ta1, 16(sp)\r\n" + //
        "\tcall\tstrcat\r\n" + //
        "\tlw\ta0, 12(sp)\r\n" + //
        "\tlw\tra, 28(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 32\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end12:\r\n" + //
        "\t.size\tstring_cat, .Lfunc_end12-string_cat\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tptr_array                       # -- Begin function ptr_array\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tptr_array,@function\r\n" + //
        "ptr_array:                              # @ptr_array\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 4(sp)\r\n" + //
        "\tslli\ta0, a0, 2\r\n" + //
        "\taddi\ta0, a0, 4\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tmv  a2, a0\r\n" + //
        "\tlw\ta0, 4(sp)\r\n" + //
        "\tmv  a1, a2\r\n" + //
        "\tsw\ta0, 0(a1)\r\n" + //
        "\tmv  a0, a2\r\n" + //
        "\taddi\ta0, a0, 4\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end13:\r\n" + //
        "\t.size\tptr_array, .Lfunc_end13-ptr_array\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tint_array                       # -- Begin function int_array\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tint_array,@function\r\n" + //
        "int_array:                              # @int_array\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 4(sp)\r\n" + //
        "\tslli\ta0, a0, 2\r\n" + //
        "\taddi\ta0, a0, 4\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "  mv  a1, a0\r\n" + //
        "\tlw\ta0, 4(sp)\r\n" + //
        "\tsw\ta0, 0(a1)\r\n" + //
        "\tmv  a0, a1\r\n" + //
        "\taddi\ta0, a0, 4\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end14:\r\n" + //
        "\t.size\tint_array, .Lfunc_end14-int_array\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tarray_size                      # -- Begin function array_size\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tarray_size,@function\r\n" + //
        "array_size:                             # @array_size\r\n" + //
        "# %bb.0:\r\n" + //
        "\tlw\ta0, -4(a0)\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end15:\r\n" + //
        "\t.size\tarray_size, .Lfunc_end15-array_size\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tstring_copy                     # -- Begin function string_copy\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tstring_copy,@function\r\n" + //
        "string_copy:                            # @string_copy\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tsw\ta0, 4(sp)\r\n" + //
        "\tcall\tstrlen\r\n" + //
        "\taddi\ta0, a0, 1\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tsw\ta0, 0(sp)\r\n" + //
        "\tlw\ta1, 4(sp)\r\n" + //
        "\tcall\tstrcpy\r\n" + //
        "\tlw\ta0, 0(sp)\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end16:\r\n" + //
        "\t.size\tstring_copy, .Lfunc_end16-string_copy\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.globl\tMyNew                           # -- Begin function MyNew\r\n" + //
        "\t.p2align\t1\r\n" + //
        "\t.type\tMyNew,@function\r\n" + //
        "MyNew:                                  # @MyNew\r\n" + //
        "# %bb.0:\r\n" + //
        "\taddi\tsp, sp, -16\r\n" + //
        "\tsw\tra, 12(sp)                      # 4-byte Folded Spill\r\n" + //
        "\tcall\tmalloc\r\n" + //
        "\tlw\tra, 12(sp)                      # 4-byte Folded Reload\r\n" + //
        "\taddi\tsp, sp, 16\r\n" + //
        "\tret\r\n" + //
        ".Lfunc_end17:\r\n" + //
        "\t.size\tMyNew, .Lfunc_end17-MyNew\r\n" + //
        "                                        # -- End function\r\n" + //
        "\t.type\t.L.str,@object                  # @.str\r\n" + //
        "\t.section\t.rodata.str1.1,\"aMS\",@progbits,1\r\n" + //
        ".L.str:\r\n" + //
        "\t.asciz\t\"%s\"\r\n" + //
        "\t.size\t.L.str, 3\r\n" + //
        "\r\n" + //
        "\t.type\t.L.str.1,@object                # @.str.1\r\n" + //
        ".L.str.1:\r\n" + //
        "\t.asciz\t\"%d\"\r\n" + //
        "\t.size\t.L.str.1, 3\r\n" + //
        "\r\n" + //
        "\t.type\t.L.str.2,@object                # @.str.2\r\n" + //
        ".L.str.2:\r\n" + //
        "\t.asciz\t\"%d\\n" + //
        "\"\r\n" + //
        "\t.size\t.L.str.2, 4\r\n" + //
        "\r\n" + //
        "\t.ident\t\"Ubuntu clang version 18.1.8 (++20240731024944+3b5b5c1ec4a3-1~exp1~20240731145000.144)\"\r\n" + //
        "\t.section\t\".note.GNU-stack\",\"\",@progbits\r\n" + //
        "\t.addrsig\r\n" + //
        "\t.addrsig_sym puts\r\n" + //
        "\t.addrsig_sym printf\r\n" + //
        "\t.addrsig_sym malloc\r\n" + //
        "\t.addrsig_sym sprintf\r\n" + //
        "\t.addrsig_sym strlen\r\n" + //
        "\t.addrsig_sym strcpy\r\n" + //
        "\t.addrsig_sym free\r\n" + //
        "\t.addrsig_sym scanf\r\n" + //
        "\t.addrsig_sym string_length\r\n" + //
        "\t.addrsig_sym strcmp\r\n" + //
        "\t.addrsig_sym strcat");
  }
}