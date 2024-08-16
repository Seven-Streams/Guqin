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
  public HashMap<String, Integer> start = new HashMap<>();
  public HashMap<String, Integer> end = new HashMap<>();
  public HashMap<Integer, Boolean> visit = new HashMap<>();
  public HashMap<Integer, Integer> block_entries = new HashMap<>();
  public PriorityQueue<Interval> intervals = new PriorityQueue<>(new Interval().new IntervalComparator());
  public LivenessAnalysis(Composer _machine) {
    machine = _machine;
  }

  public void Allocator() {
    BuildGraph();
    NumberIns();
    GetIntervals();
    SortingIntervals();
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
        MoveBlock move = (MoveBlock) code;
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
    for (IRCode code : machine.generated) {
      code.UseDefCheck(def, use);
      for (String def_v : def.keySet()) {
        if (!start.containsKey(def_v)) {
          start.put(def_v, code.sentence_number);
        } else {
          if (start.get(def_v) > code.sentence_number) {
            start.put(def_v, code.sentence_number);
          }
        }
      }
      for (String use_v : use.keySet()) {
        if (!end.containsKey(use_v)) {
          end.put(use_v, code.sentence_number);
        } else {
          if (start.get(use_v) < code.sentence_number) {
            start.put(use_v, code.sentence_number);
          }
        }
      }
    }
    return;
  }
  
  void SortingIntervals() {
    for(Map.Entry<String, Integer> entry: start.entrySet()) {
      Interval new_interval = new Interval(entry.getValue(), end.get(entry.getKey()), entry.getKey());
      intervals.add(new_interval);
    }
    return;
  }
}
