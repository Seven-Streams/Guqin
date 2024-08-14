package Optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import IRSentence.*;
import Composer.*;

public class Mem2Reg {
  public Composer object = null;

  public Mem2Reg(Composer _object) {
    object = _object;
  }

  public void Optim() {
    Mem2RegEmpty();
    Mem2RegAssignOnce();
    Mem2RegAssignOneBlock();
  }

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
    for(IRCode code:object.generated) {
      code.UpdateSingleBlock(single);
    }
    boolean flag = false;
    for(int i = object.generated.size() - 1; i >= 0; i--) {
      if(flag) {
        object.generated.remove(i + 1);
      }
      flag = object.generated.get(i).AssignOnceRemove(single);
    }
    if(flag) {
      object.generated.remove(0);
    }
    return;
  }
}