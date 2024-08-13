package Optimization;

import java.util.ArrayList;
import java.util.HashMap;

import IRSentence.*;
import Composer.*;

public class Mem2Reg {
  public Composer object = null;

  public Mem2Reg(Composer _object) {
    object = _object;
  }

  public void Mem2RegEmpty() {
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
      if(!remove) {
        break;
      }
    }
    return;
  }
}