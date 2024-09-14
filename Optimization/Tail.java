package Optimization;

import Composer.*;
import IRSentence.*;

public class Tail {
  Composer machine = null;

  public Tail(Composer _machine) {
    machine = _machine;
  }

  public void Optim() {
    String now_name = null;
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof IRFunc) {
        IRFunc func = (IRFunc) code;
        now_name = new String(func.name);
        continue;
      }
      if (code instanceof IRFuncall) {
        IRFuncall call = (IRFuncall) code;
        if (call.func_name.equals(now_name)) {
          IRCode check = machine.generated.get(i + 1);
          if (call.target_reg == null) {
            if (check instanceof IRReturn || check instanceof IRFuncend) {
              IRTail tail = new IRTail(call);
              machine.generated.set(i, tail);
              if (check instanceof IRReturn) {
                machine.generated.remove(i + 1);
              }
            }
          } else {
            if (check instanceof IRReturn) {
              IRReturn return_ins = (IRReturn) check;
              if (return_ins.reg.equals(call.target_reg)) {
                IRTail tail = new IRTail(call);
                machine.generated.set(i, tail);
                machine.generated.remove(i + 1);
              }
            }
          }
        }
      }
    }
    return;
  }
}
