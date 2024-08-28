package Optimization;
import java.util.HashMap;

import Composer.*;
import IRSentence.*;
public class GlobalConst {
  Composer machine = null;
  public GlobalConst(Composer _machine) {
    machine = _machine;
  }
  public void Optim() {
    HashMap<String, String> global_value = new HashMap<>();
    HashMap<String, Boolean> const_check = new HashMap<>();
    for(IRCode code : machine.global) {
      IRGlobal declare = (IRGlobal)code;
      if(declare.value != null) {
        global_value.put(declare.name, declare.value);
        const_check.put(declare.name, null);
      }
    }
    for(IRCode code: machine.generated) {
      if(code instanceof IRStore) {
        IRStore store = (IRStore)code;
        if(const_check.containsKey(store.name)) {
          const_check.remove(store.name);
        }
      }
    }
    HashMap<String, String> replaced = new HashMap<>();
    for(int i = machine.generated.size() - 1; i >= 0; i--) {
      if(machine.generated.get(i) instanceof IRLoad) {
        IRLoad code = (IRLoad)machine.generated.get(i);
        if(const_check.containsKey(code.src)) {
          replaced.put(code.des, global_value.get(code.src));
          machine.generated.remove(i);
        }
      }
    }
    for(IRCode code: machine.generated) {
      code.GlobalConstReplace(replaced);
    }
    return;
  }
}