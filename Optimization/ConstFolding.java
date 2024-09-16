package Optimization;

import Composer.Composer;
import IRSentence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class ConstFolding {
  Composer machine = null;

  ConstFolding(Composer _machine) {
    machine = _machine;
  }

  HashMap<String, IRCode> def = new HashMap<>();
  HashMap<String, ArrayList<IRCode>> use = new HashMap<>();

  public void clear() {
    def.clear();
    use.clear();
    return;
  }

  public void Optim() {
    HashMap<String, Boolean> res_def = new HashMap<>();
    HashMap<String, Boolean> res_use = new HashMap<>();
    ArrayList<IRCode> operate = new ArrayList<>();
    for (IRCode code : machine.generated) {
      res_def.clear();
      res_use.clear();
      code.UseDefCheck(res_def, res_use);
      for (String def_v : res_def.keySet()) {
        def.put(def_v, code);
      }
      for (String use_v : res_use.keySet()) {
        if (!use.containsKey(use_v)) {
          use.put(use_v, new ArrayList<>());
        }
        use.get(use_v).add(code);
      }
      operate.add(code);
      if (code instanceof IRFuncend) {
        HashMap<String, String> replace = new HashMap<>();
        Queue<IRCode> to_replace = new LinkedList<>();
        for (IRCode in_code : operate) {
          String new_value = in_code.ConstCheck(replace);
          if (new_value != null && use.containsKey(new_value)) {
            for (IRCode dominated : use.get(new_value)) {
              to_replace.add(dominated);
            }
          }
        }
        while (!to_replace.isEmpty()) {
          IRCode operator = to_replace.poll();
          String new_value = operator.ConstCheck(replace);
          if (new_value != null && use.containsKey(new_value)) {
            for (IRCode dominated : use.get(new_value)) {
              to_replace.add(dominated);
            }
          }
        }
        use.clear();
        def.clear();
        operate.clear();
      }
    }
    for (int i = machine.generated.size() - 1; i >= 0; i--) {
      if (machine.generated.get(i).dead) {
        machine.generated.remove(i);
      }
    }
    return;
  }
}
