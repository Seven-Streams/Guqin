package Optimization;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import Composer.*;
import IRSentence.*;

public class LocalCSE {
  Composer machine = null;
  public HashMap<Integer, HashMap<Integer, Boolean>> graph = new HashMap<>();
  public HashMap<Integer, HashMap<Integer, Boolean>> pre = new HashMap<>();
  int func_cnt = 0;
  public HashMap<Integer, ArrayList<IRCode>> blocks = new HashMap<>();

  public LocalCSE(Composer _machine) {
    machine = _machine;
  }

  void Optim() {
    BuildGraph();
    OptimFunc();
    RemoveDead();
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

  void OptimFunc() {
    for (int i = -1; i >= func_cnt; i--) {
      CSECheck(i);
    }
    return;
  }

  void CSECheck(int index) {
    for (IRCode code : machine.global) {
      code.CheckGlobal();
    }
    HashMap<Integer, Boolean> visit = new HashMap<>();
    HashMap<String, String> value = new HashMap<>();
    HashMap<String, HashMap<Integer, Boolean>> use = new HashMap<>();
    Queue<Integer> my_queue = new LinkedList<>();
    visit.put(index, null);
    my_queue.add(index);
    HashMap<String, Boolean> res_use = new HashMap<>();
    HashMap<String, Boolean> res_def = new HashMap<>();
    while (!my_queue.isEmpty()) {
      int head = my_queue.poll();
      ArrayList<IRCode> to_check = blocks.get(head);
      for (IRCode code : to_check) {
        res_use.clear();
        res_def.clear();
        code.UseDefCheck(res_def, res_use);
        for (String use_v : res_use.keySet()) {
          if (!use.containsKey(use_v)) {
            use.put(use_v, new HashMap<>());
          }
          use.get(use_v).put(head, null);
        }
      }
      if (graph.containsKey(head)) {
        for (int to : graph.get(head).keySet()) {
          if (!visit.containsKey(to)) {
            my_queue.add(to);
            visit.put(to, null);
          }
        }
      }
    } // Build Use-Def-Chain.
    visit.clear();
    my_queue.add(index);
    Queue<Integer> update = new LinkedList<>();
    visit.put(index, null);
    HashMap<Integer, Boolean> in_queue = new HashMap<>();
    while (!my_queue.isEmpty()) {
      int head = my_queue.poll();
      ArrayList<IRCode> to_check = blocks.get(head);
      for (int i = 0; i < to_check.size(); i++) {
        IRCode code = to_check.get(i);
        if (code.dead) {
          continue;
        }
        code.ConstCheck(value);
        if (!(code instanceof IRBin || code instanceof IRIcmp || code instanceof IRElement || code instanceof IRLoad)) {
          continue;
        }
        String global_name = null;
        String global_value = null;
        boolean is_load = false;
        boolean global_load = false;
        HashMap<String, Boolean> elements = new HashMap<>();
        if (code instanceof IRLoad) {
          IRLoad load = (IRLoad) code;
          is_load = true;
          global_name = new String(load.src);
          global_value = new String(load.des);
          if (IRCode.is_global.containsKey(load.src)) {
            global_load = true;
          }
        }
        for (int j = i + 1; j < to_check.size(); j++) {
          IRCode code2 = to_check.get(j);
          if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
            IRElement ele = (IRElement) code;
            if (ele.num2 != null) {
              break;
            }
          }
          if (code2.dead) {
            continue;
          }
          code2.ConstCheck(value);
          if (global_name != null) {
            if (code2 instanceof IRElement) {
              IRElement ele = (IRElement) code2;
              if (ele.num2 == null) {
                if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                  elements.put(ele.output, null);
                }
              }
              continue;
            }
            if (code2 instanceof IRStore) {
              IRStore store = (IRStore) code2;
              if (global_load) {
                if (global_name.equals(store.name)) {
                  global_name = null;
                  break;
                }
              } else {
                if (!elements.containsKey(store.name)) {
                  global_name = null;
                  break;
                }
              }
              continue;
            }
            if (code2 instanceof IRLoad) {
              IRLoad load = (IRLoad) code2;
              if (global_name.equals(load.src)) {
                ((IRLoad) code2).dead = true;
                value.put(new String(load.des), new String(global_value));
                for (Integer to : use.get(load.des).keySet()) {
                  if (!in_queue.containsKey(to)) {
                    update.add(to);
                    in_queue.put(to, null);
                  }
                }
              }
              continue;
            }
            if (code2 instanceof IRFuncall) {
              break;
            }
          }
          boolean check_repeat = code.RepeatOperation(code2);
          if (check_repeat) {
            code2.dead = true;
            if (code2 instanceof IRBin) {
              IRBin bin = (IRBin) code;
              IRBin bin2 = (IRBin) code2;
              value.put(new String(bin2.target_reg), new String(bin.target_reg));
              for (Integer to : use.get(bin2.target_reg).keySet()) {
                if (!in_queue.containsKey(to)) {
                  update.add(to);
                  in_queue.put(to, null);
                }
              }
              continue;
            }
            if (code2 instanceof IRIcmp) {
              IRIcmp cmp = (IRIcmp) code;
              IRIcmp cmp2 = (IRIcmp) code2;
              value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
              for (Integer to : use.get(cmp2.target_reg).keySet()) {
                if (!in_queue.containsKey(to)) {
                  update.add(to);
                  in_queue.put(to, null);
                }
              }
              continue;
            }
            if (code2 instanceof IRElement) {
              IRElement ele = (IRElement) code;
              IRElement ele2 = (IRElement) code2;
              value.put(new String(ele2.output), new String(ele.output));
              for (Integer to : use.get(ele2.output).keySet()) {
                if (!in_queue.containsKey(to)) {
                  update.add(to);
                  in_queue.put(to, null);
                }
              }
              continue;
            }
          }
        }
        if (is_load && global_name == null) {
          continue;
        }
        if (to_check.get(to_check.size() - 1) instanceof IRjmp) {
          IRjmp jmp = (IRjmp) to_check.get(to_check.size() - 1);
          if (pre.get(jmp.label).size() == 1) {
            ArrayList<IRCode> to_check2 = blocks.get(jmp.label);
            for (int j = 0; j < to_check2.size(); j++) {
              IRCode code2 = to_check2.get(j);
              if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
                IRElement ele = (IRElement) code;
                if (ele.num2 != null) {
                  break;
                }
              }
              if (code2.dead) {
                continue;
              }
              code2.ConstCheck(value);
              if (global_name != null) {
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code2;
                  if (ele.num2 == null) {
                    if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                      elements.put(ele.output, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRStore) {
                  IRStore store = (IRStore) code2;
                  if (global_load) {
                    if (global_name.equals(store.name)) {
                      global_name = null;
                      break;
                    }
                  } else {
                    if (!elements.containsKey(store.name)) {
                      global_name = null;
                      break;
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRLoad) {
                  IRLoad load = (IRLoad) code2;
                  if (global_name.equals(load.src)) {
                    ((IRLoad) code2).dead = true;
                    value.put(new String(load.des), new String(global_value));
                    for (Integer to : use.get(load.des).keySet()) {
                      if (!in_queue.containsKey(to)) {
                        update.add(to);
                        in_queue.put(to, null);
                      }
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRFuncall) {
                  break;
                }
              }
              boolean check_repeat = code.RepeatOperation(code2);
              if (check_repeat) {
                code2.dead = true;
                if (code2 instanceof IRBin) {
                  IRBin bin = (IRBin) code;
                  IRBin bin2 = (IRBin) code2;
                  value.put(new String(bin2.target_reg), new String(bin.target_reg));
                  for (Integer to : use.get(bin2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRIcmp) {
                  IRIcmp cmp = (IRIcmp) code;
                  IRIcmp cmp2 = (IRIcmp) code2;
                  value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
                  for (Integer to : use.get(cmp2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code;
                  IRElement ele2 = (IRElement) code2;
                  value.put(new String(ele2.output), new String(ele.output));
                  for (Integer to : use.get(ele2.output).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
              }
            }
          }
        }
        if (to_check.get(to_check.size() - 1) instanceof Conditionjmp) {
          Conditionjmp jmp = (Conditionjmp) to_check.get(to_check.size() - 1);
          String res_name = global_name == null ? null : new String(global_name);
          String res_value = global_value == null ? null : new String(global_value);
          if (pre.get(jmp.label1).size() == 1) {
            ArrayList<IRCode> to_check2 = blocks.get(jmp.label1);
            for (int j = 0; j < to_check2.size(); j++) {
              IRCode code2 = to_check2.get(j);
              if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
                IRElement ele = (IRElement) code;
                if (ele.num2 != null) {
                  break;
                }
              }
              if (code2.dead) {
                continue;
              }
              code2.ConstCheck(value);
              if (global_name != null) {
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code2;
                  if (ele.num2 == null) {
                    if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                      elements.put(ele.output, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRStore) {
                  IRStore store = (IRStore) code2;
                  if (global_load) {
                    if (global_name.equals(store.name)) {
                      global_name = null;
                      break;
                    }
                  } else {
                    if (!elements.containsKey(store.name)) {
                      global_name = null;
                      break;
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRLoad) {
                  IRLoad load = (IRLoad) code2;
                  if (global_name.equals(load.src)) {
                    ((IRLoad) code2).dead = true;
                    value.put(new String(load.des), new String(global_value));
                    for (Integer to : use.get(load.des).keySet()) {
                      if (!in_queue.containsKey(to)) {
                        update.add(to);
                        in_queue.put(to, null);
                      }
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRFuncall) {
                  break;
                }
              }
              boolean check_repeat = code.RepeatOperation(code2);
              if (check_repeat) {
                code2.dead = true;
                if (code2 instanceof IRBin) {
                  IRBin bin = (IRBin) code;
                  IRBin bin2 = (IRBin) code2;
                  value.put(new String(bin2.target_reg), new String(bin.target_reg));
                  for (Integer to : use.get(bin2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRIcmp) {
                  IRIcmp cmp = (IRIcmp) code;
                  IRIcmp cmp2 = (IRIcmp) code2;
                  value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
                  for (Integer to : use.get(cmp2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code;
                  IRElement ele2 = (IRElement) code2;
                  value.put(new String(ele2.output), new String(ele.output));
                  for (Integer to : use.get(ele2.output).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
              }
            }
          }
          global_name = res_name;
          global_value = res_value;
          if (pre.get(jmp.label2).size() == 1) {
            ArrayList<IRCode> to_check2 = blocks.get(jmp.label2);
            for (int j = 0; j < to_check2.size(); j++) {
              IRCode code2 = to_check2.get(j);
              if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
                IRElement ele = (IRElement) code;
                if (ele.num2 != null) {
                  break;
                }
              }
              if (code2.dead) {
                continue;
              }
              code2.ConstCheck(value);
              if (global_name != null) {
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code2;
                  if (ele.num2 == null) {
                    if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                      elements.put(ele.output, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRStore) {
                  IRStore store = (IRStore) code2;
                  if (global_load) {
                    if (global_name.equals(store.name)) {
                      global_name = null;
                      break;
                    }
                  } else {
                    if (!elements.containsKey(store.name)) {
                      global_name = null;
                      break;
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRLoad) {
                  IRLoad load = (IRLoad) code2;
                  if (global_name.equals(load.src)) {
                    ((IRLoad) code2).dead = true;
                    value.put(new String(load.des), new String(global_value));
                    for (Integer to : use.get(load.des).keySet()) {
                      if (!in_queue.containsKey(to)) {
                        update.add(to);
                        in_queue.put(to, null);
                      }
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRFuncall) {
                  break;
                }
              }
              boolean check_repeat = code.RepeatOperation(code2);
              if (check_repeat) {
                code2.dead = true;
                if (code2 instanceof IRBin) {
                  IRBin bin = (IRBin) code;
                  IRBin bin2 = (IRBin) code2;
                  value.put(new String(bin2.target_reg), new String(bin.target_reg));
                  for (Integer to : use.get(bin2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRIcmp) {
                  IRIcmp cmp = (IRIcmp) code;
                  IRIcmp cmp2 = (IRIcmp) code2;
                  value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
                  for (Integer to : use.get(cmp2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code;
                  IRElement ele2 = (IRElement) code2;
                  value.put(new String(ele2.output), new String(ele.output));
                  for (Integer to : use.get(ele2.output).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
              }
            }
          }
        }
      }
      for (int to : graph.get(head).keySet()) {
        if (!visit.containsKey(to)) {
          my_queue.add(to);
          visit.put(to, null);
        }
      }
    }
    while (!update.isEmpty()) {
      int head = update.poll();
      in_queue.remove(head);
      ArrayList<IRCode> to_check = blocks.get(head);
      for (int i = 0; i < to_check.size(); i++) {
        IRCode code = to_check.get(i);
        if (code.dead) {
          continue;
        }
        code.ConstCheck(value);
        if (!(code instanceof IRBin || code instanceof IRIcmp || code instanceof IRElement || code instanceof IRLoad)) {
          continue;
        }
        String global_name = null;
        String global_value = null;
        boolean is_load = false;
        boolean global_load = false;
        HashMap<String, Boolean> elements = new HashMap<>();
        if (code instanceof IRLoad) {
          IRLoad load = (IRLoad) code;
          global_name = new String(load.src);
          global_value = new String(load.des);
          is_load = true;
          if (IRCode.is_global.containsKey(load.src)) {
            global_load = true;
          }
        }
        for (int j = i + 1; j < to_check.size(); j++) {
          IRCode code2 = to_check.get(j);
          if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
            IRElement ele = (IRElement) code;
            if (ele.num2 != null) {
              break;
            }
          }
          if (code2.dead) {
            continue;
          }
          code2.ConstCheck(value);
          if (global_name != null) {
            if (code2 instanceof IRElement) {
              IRElement ele = (IRElement) code2;
              if (ele.num2 == null) {
                if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                  elements.put(ele.output, null);
                }
              }
              continue;
            }
            if (code2 instanceof IRStore) {
              IRStore store = (IRStore) code2;
              if (global_load) {
                if (global_name.equals(store.name)) {
                  global_name = null;
                  break;
                }
              } else {
                if (!elements.containsKey(store.name)) {
                  global_name = null;
                  break;
                }
              }
              continue;
            }
            if (code2 instanceof IRFuncall) {
              break;
            }
            if (code2 instanceof IRLoad) {
              IRLoad load = (IRLoad) code2;
              if (global_name.equals(load.src)) {
                load.dead = true;
                value.put(new String(load.des), new String(global_value));
                for (Integer to : use.get(load.des).keySet()) {
                  if (!in_queue.containsKey(to)) {
                    update.add(to);
                    in_queue.put(to, null);
                  }
                }
              }
              continue;
            }
          }
          boolean check_repeat = code.RepeatOperation(code2);
          if (check_repeat) {
            code2.dead = true;
            if (code2 instanceof IRBin) {
              IRBin bin = (IRBin) code;
              IRBin bin2 = (IRBin) code2;
              value.put(bin2.target_reg, bin.target_reg);
              for (Integer to : use.get(bin2.target_reg).keySet()) {
                if (!in_queue.containsKey(to)) {
                  update.add(to);
                  in_queue.put(to, null);
                }
              }
            }
            if (code2 instanceof IRIcmp) {
              IRIcmp cmp = (IRIcmp) code;
              IRIcmp cmp2 = (IRIcmp) code2;
              value.put(cmp2.target_reg, cmp.target_reg);
              for (Integer to : use.get(cmp2.target_reg).keySet()) {
                if (!in_queue.containsKey(to)) {
                  update.add(to);
                  in_queue.put(to, null);
                }
              }
            }
            if (code2 instanceof IRElement) {
              IRElement ele = (IRElement) code;
              IRElement ele2 = (IRElement) code2;
              value.put(new String(ele2.output), new String(ele.output));
              for (Integer to : use.get(ele2.output).keySet()) {
                if (!in_queue.containsKey(to)) {
                  update.add(to);
                  in_queue.put(to, null);
                }
              }
              continue;
            }
          }
        }
        if (is_load && global_name == null) {
          continue;
        }
        if (to_check.get(to_check.size() - 1) instanceof IRjmp) {
          IRjmp jmp = (IRjmp) to_check.get(to_check.size() - 1);
          if (pre.get(jmp.label).size() == 1) {
            ArrayList<IRCode> to_check2 = blocks.get(jmp.label);
            for (int j = 0; j < to_check2.size(); j++) {
              IRCode code2 = to_check2.get(j);
              if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
                IRElement ele = (IRElement) code;
                if (ele.num2 != null) {
                  break;
                }
              }
              if (code2.dead) {
                continue;
              }
              code2.ConstCheck(value);
              if (global_name != null) {
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code2;
                  if (ele.num2 == null) {
                    if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                      elements.put(ele.output, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRStore) {
                  IRStore store = (IRStore) code2;
                  if (global_load) {
                    if (global_name.equals(store.name)) {
                      global_name = null;
                      break;
                    }
                  } else {
                    if (!elements.containsKey(store.name)) {
                      global_name = null;
                      break;
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRLoad) {
                  IRLoad load = (IRLoad) code2;
                  if (global_name.equals(load.src)) {
                    ((IRLoad) code2).dead = true;
                    value.put(new String(load.des), new String(global_value));
                    for (Integer to : use.get(load.des).keySet()) {
                      if (!in_queue.containsKey(to)) {
                        update.add(to);
                        in_queue.put(to, null);
                      }
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRFuncall) {
                  break;
                }
              }
              boolean check_repeat = code.RepeatOperation(code2);
              if (check_repeat) {
                code2.dead = true;
                if (code2 instanceof IRBin) {
                  IRBin bin = (IRBin) code;
                  IRBin bin2 = (IRBin) code2;
                  value.put(new String(bin2.target_reg), new String(bin.target_reg));
                  for (Integer to : use.get(bin2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRIcmp) {
                  IRIcmp cmp = (IRIcmp) code;
                  IRIcmp cmp2 = (IRIcmp) code2;
                  value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
                  for (Integer to : use.get(cmp2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code;
                  IRElement ele2 = (IRElement) code2;
                  value.put(new String(ele2.output), new String(ele.output));
                  for (Integer to : use.get(ele2.output).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
              }
            }
          }
        }
        if (to_check.get(to_check.size() - 1) instanceof Conditionjmp) {
          Conditionjmp jmp = (Conditionjmp) to_check.get(to_check.size() - 1);
          String res_name = global_name == null ? null : new String(global_name);
          String res_value = global_value == null ? null : new String(global_value);
          if (pre.get(jmp.label1).size() == 1) {
            ArrayList<IRCode> to_check2 = blocks.get(jmp.label1);
            for (int j = 0; j < to_check2.size(); j++) {
              IRCode code2 = to_check2.get(j);
              if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
                IRElement ele = (IRElement) code;
                if (ele.num2 != null) {
                  break;
                }
              }
              if (code2.dead) {
                continue;
              }
              code2.ConstCheck(value);
              if (global_name != null) {
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code2;
                  if (ele.num2 == null) {
                    if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                      elements.put(ele.output, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRStore) {
                  IRStore store = (IRStore) code2;
                  if (global_load) {
                    if (global_name.equals(store.name)) {
                      global_name = null;
                      break;
                    }
                  } else {
                    if (!elements.containsKey(store.name)) {
                      global_name = null;
                      break;
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRLoad) {
                  IRLoad load = (IRLoad) code2;
                  if (global_name.equals(load.src)) {
                    ((IRLoad) code2).dead = true;
                    value.put(new String(load.des), new String(global_value));
                    for (Integer to : use.get(load.des).keySet()) {
                      if (!in_queue.containsKey(to)) {
                        update.add(to);
                        in_queue.put(to, null);
                      }
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRFuncall) {
                  break;
                }
              }
              boolean check_repeat = code.RepeatOperation(code2);
              if (check_repeat) {
                code2.dead = true;
                if (code2 instanceof IRBin) {
                  IRBin bin = (IRBin) code;
                  IRBin bin2 = (IRBin) code2;
                  value.put(new String(bin2.target_reg), new String(bin.target_reg));
                  for (Integer to : use.get(bin2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRIcmp) {
                  IRIcmp cmp = (IRIcmp) code;
                  IRIcmp cmp2 = (IRIcmp) code2;
                  value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
                  for (Integer to : use.get(cmp2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code;
                  IRElement ele2 = (IRElement) code2;
                  value.put(new String(ele2.output), new String(ele.output));
                  for (Integer to : use.get(ele2.output).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
              }
            }
          }
          global_name = res_name;
          global_value = res_value;
          if (pre.get(jmp.label2).size() == 1) {
            ArrayList<IRCode> to_check2 = blocks.get(jmp.label2);
            for (int j = 0; j < to_check2.size(); j++) {
              IRCode code2 = to_check2.get(j);
              if ((code2 instanceof IRStore) && (code instanceof IRElement)) {
                IRElement ele = (IRElement) code;
                if (ele.num2 != null) {
                  break;
                }
              }
              if (code2.dead) {
                continue;
              }
              code2.ConstCheck(value);
              if (global_name != null) {
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code2;
                  if (ele.num2 == null) {
                    if (ele.src.equals(global_name) || elements.containsKey(ele.src)) {
                      elements.put(ele.output, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRStore) {
                  IRStore store = (IRStore) code2;
                  if (global_load) {
                    if (global_name.equals(store.name)) {
                      global_name = null;
                      break;
                    }
                  } else {
                    if (!elements.containsKey(store.name)) {
                      global_name = null;
                      break;
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRLoad) {
                  IRLoad load = (IRLoad) code2;
                  if (global_name.equals(load.src)) {
                    ((IRLoad) code2).dead = true;
                    value.put(new String(load.des), new String(global_value));
                    for (Integer to : use.get(load.des).keySet()) {
                      if (!in_queue.containsKey(to)) {
                        update.add(to);
                        in_queue.put(to, null);
                      }
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRFuncall) {
                  break;
                }
              }
              boolean check_repeat = code.RepeatOperation(code2);
              if (check_repeat) {
                code2.dead = true;
                if (code2 instanceof IRBin) {
                  IRBin bin = (IRBin) code;
                  IRBin bin2 = (IRBin) code2;
                  value.put(new String(bin2.target_reg), new String(bin.target_reg));
                  for (Integer to : use.get(bin2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRIcmp) {
                  IRIcmp cmp = (IRIcmp) code;
                  IRIcmp cmp2 = (IRIcmp) code2;
                  value.put(new String(cmp2.target_reg), new String(cmp.target_reg));
                  for (Integer to : use.get(cmp2.target_reg).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
                if (code2 instanceof IRElement) {
                  IRElement ele = (IRElement) code;
                  IRElement ele2 = (IRElement) code2;
                  value.put(new String(ele2.output), new String(ele.output));
                  for (Integer to : use.get(ele2.output).keySet()) {
                    if (!in_queue.containsKey(to)) {
                      update.add(to);
                      in_queue.put(to, null);
                    }
                  }
                  continue;
                }
              }
            }
          }
        }
      }
    }
    return;
  }

  void RemoveDead() {
    for (int i = machine.generated.size() - 1; i >= 0; i--) {
      if (machine.generated.get(i).dead) {
        machine.generated.remove(i);
      }
    }
    return;
  }
}