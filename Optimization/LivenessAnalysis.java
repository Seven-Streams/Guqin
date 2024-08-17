package Optimization;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import IRSentence.*;
import Composer.*;

public class LivenessAnalysis {
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
  public HashMap<Integer, String> register_names = new HashMap<>();

  public LivenessAnalysis(Composer _machine) {
    machine = _machine;
  }

  public void Allocator(int degree) {
    BuildGraph();
    NumberIns();
    GetIntervals();
    SortingIntervals();
    AllocateAll(degree);
    CalculateStack();
    RegisterName();
  }

  void PrintName() {
    for (Map.Entry<Integer, String> pair : register_names.entrySet()) {
      System.out.println(pair.getKey() + " " + pair.getValue());
    }
  }

  public void Codegen() throws Exception {
    int cnt = 0;
    System.out.println(".data");
    for (IRCode code : machine.const_str) {
      code.CodegenWithOptim(null, register_names);
    }
    for (IRCode code : machine.global) {
      code.CodegenWithOptim(null, register_names);
    }
    System.out.println("");
    System.out.println(".text");
    System.out.println(".globl main");
    for (IRCode code : machine.generated) {
      boolean to_init = false;
      if (code instanceof IRFunc) {
        cnt--;
        IRFunc to_check = (IRFunc) code;
        if (to_check.name.equals("main")) {
          to_init = true;
        }
      }
      code.CodegenWithOptim(registers.get(cnt), register_names);
      if (to_init) {
        for (IRCode chars : machine.const_str) {
          IRChararray init = (IRChararray) chars;
          init.Init();
        }
      }
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
    for (int i = -1; i >= func_cnt; i--) {
      NumberBlocks(i);
    }
  }

  void NumberBlocks(int index) {
    int begin = block_entries.get(index);
    visit.put(index, null);
    machine.generated.get(begin).sentence_number = ++number;
    for (int i = begin + 1; i < machine.generated.size(); i++) {
      if (machine.generated.get(i) instanceof IRLabel || machine.generated.get(i) instanceof IRFunc
          || machine.generated.get(i) instanceof MoveBlock) {
        break;
      } else {
        machine.generated.get(i).sentence_number = ++number;
      }
    }
    for (int nxt : graph.get(index).keySet()) {
      if (!visit.containsKey(nxt)) {
        NumberBlocks(nxt);
      }
    }
    return;
  }

  void GetIntervals() {
    HashMap<String, Boolean> use = new HashMap<>();
    HashMap<String, Boolean> def = new HashMap<>();
    int now_func = 0;
    for (IRCode code : machine.generated) {
      use.clear();
      def.clear();
      code.UseDefCheck(def, use);
      if (code instanceof IRFunc) {
        now_func--;
        interval_check.put(now_func, new HashMap<>());
      }
      for (String def_v : def.keySet()) {
        if (!interval_check.get(now_func).containsKey(def_v)) {
          interval_check.get(now_func).put(def_v, new Interval(code.sentence_number, code.sentence_number, def_v));
        } else {
          Interval to_check = interval_check.get(now_func).get(def_v);
          if (to_check.start > code.sentence_number) {
            to_check.start = code.sentence_number;
          }
          if (to_check.end < code.sentence_number) {
            to_check.end = code.sentence_number;
          }
        }
      }
      for (String use_v : use.keySet()) {
        if (!interval_check.get(now_func).containsKey(use_v)) {
          interval_check.get(now_func).put(use_v, new Interval(code.sentence_number, code.sentence_number, use_v));
        } else {
          Interval to_check = interval_check.get(now_func).get(use_v);
          if (to_check.start > code.sentence_number) {
            to_check.start = code.sentence_number;
          }
          if (to_check.end < code.sentence_number) {
            to_check.end = code.sentence_number;
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
  }

  void RegisterAllocate(int func_num, int degree) {
    HashMap<Integer, Interval> free = new HashMap<>();
    for (int i = 0; i < degree; i++) {
      free.put(i, null);
    }
    int stack_num = -13;
    PriorityQueue<Interval> to_alloc = intervals.get(func_num);
    registers.put(func_num, new HashMap<>());
    while (!to_alloc.isEmpty()) {
      Interval now_alloc = to_alloc.poll();
      boolean spilled = true;
      for (int i = 0; i < degree; i++) {
        if (free.get(i) == null) {
          free.put(i, now_alloc);
          registers.get(func_num).put(now_alloc.name, i);
          spilled = false;
          break;
        }
        if (free.get(i).end < now_alloc.start) {
          free.put(i, now_alloc);
          registers.get(func_num).put(now_alloc.name, i);
          spilled = false;
          break;
        }
      }
      if (spilled) {
        registers.get(func_num).put(now_alloc.name, --stack_num);
      }
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
        if (res.reg.size() > 8) {
          func_res = res.reg.size() + 5;
        } else {
          func_res = 13;
        }
      }
      if (code instanceof IRFuncend) {
        check.size = func_res + stack_variables.get(now_func);
      }
    }
    return;
  }

  void RegisterName() {
    int cnt = 0;
    for (int i = 1; i <= 11; i++) {
      register_names.put(cnt++, "s" + Integer.toString(i));
    }
    for (int i = 2; i <= 6; i++) {
      register_names.put(cnt++, "t" + Integer.toString(i));
    }
    for (int i = 7; i >= 0; i--) {
      register_names.put(cnt++, "a" + Integer.toString(i));
    }
    return;
  }
}
