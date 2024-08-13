package Optimization;

import java.util.ArrayList;
import java.util.HashMap;

import IRSentence.*;
import Composer.*;

public class Mem2Reg {
  public Composer object = null;
  public HashMap<String, Integer> use = new HashMap<>();
  public HashMap<String, Integer> def = new HashMap<>();

  public Mem2Reg(Composer _object) {
    object = _object;
  }

  public void Mem2RegEmpty() {
    for (ArrayList<IRCode> alloc_func : object.alloc.values()) {
      for (IRCode alloc_single : alloc_func) {
        IRAlloc true_alloc = (IRAlloc) alloc_single;
        use.put(true_alloc.des, 0);
        def.put(true_alloc.des, 0);
      }
    }
    for (IRCode code : object.generated) {
      code.CheckTime(use, def);
    }
    for (IRCode code : object.init) {
      code.CheckTime(use, def);
    }
    HashMap<String, Boolean> empty = new HashMap<>();
    for (ArrayList<IRCode> alloc_func : object.alloc.values()) {
      boolean flag = false;
      for (int i = alloc_func.size() - 1; i >= 0; i--) {
        if (flag) {
          alloc_func.remove(i + 1);
        }
        IRAlloc true_alloc = (IRAlloc) alloc_func.get(i);
        if (use.get(true_alloc.des) == 0) {
          flag = true;
          empty.put(true_alloc.des, null);
        } else {
          flag = false;
        }
      }
      if (flag) {
        alloc_func.remove(0);
      }
    }
    boolean flag = false;
    for (int i = object.generated.size() - 1; i >= 0; i--) {
      if (flag) {
        object.generated.remove(i + 1);
      }
      flag = object.generated.get(i).EmptyStore(empty);
    }
    if (flag) {
      object.generated.remove(0);
    }
    return;
  }
}