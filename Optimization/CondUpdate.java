package Optimization;

import java.util.HashMap;

import Composer.*;
import IRSentence.*;

public class CondUpdate {
  Composer machine = null;
  HashMap<String, IRIcmp> jmp_var = new HashMap<>();

  public CondUpdate(Composer _machine) {
    machine = _machine;
  }

  public void Optim() {
    ScanCond();
    ScanIcmp();
    CheckOtherUse();
    Replace();
    RemoveDead();
  }

  void ScanCond() {
    for (IRCode code : machine.generated) {
      if (code instanceof Conditionjmp) {
        Conditionjmp jmp = (Conditionjmp) code;
        try {
          Integer.parseInt(jmp.reg);
        } catch (NumberFormatException e) {
          if (IRCode.CheckLit(jmp.reg)) {
            jmp_var.put(jmp.reg, null);
          }
        }
      }
    }
    return;
  }

  void ScanIcmp() {
    for (IRCode code : machine.generated) {
      if (code instanceof IRIcmp) {
        IRIcmp cmp = (IRIcmp) code;
        if (jmp_var.containsKey(cmp.target_reg)) {
          jmp_var.put(cmp.target_reg, cmp);
        }
      }
    }
    return;
  }

  void CheckOtherUse() {
    HashMap<String, Boolean> res_use = new HashMap<>();
    HashMap<String, Boolean> res_def = new HashMap<>();
    for (IRCode code : machine.generated) {
      if (code instanceof Conditionjmp) {
        continue;
      }
      res_def.clear();
      res_use.clear();
      code.UseDefCheck(res_def, res_use);
      for (String use_v : res_use.keySet()) {
        if (jmp_var.containsKey(use_v)) {
          jmp_var.remove(use_v);
        }
      }
    }
    return;
  }

  void Replace() {
    for (int i = 0; i < machine.generated.size(); i++) {
      IRCode code = machine.generated.get(i);
      if (code instanceof Conditionjmp) {
        Conditionjmp jmp = (Conditionjmp) code;
        if (IRCode.CheckLit(jmp.reg)) {
          if (jmp_var.containsKey(jmp.reg) && (jmp_var.get(jmp.reg) != null)) {
            IRIcmp cmp = jmp_var.get(jmp.reg);
            cmp.dead = true;
            SpecialJmp new_jmp = new SpecialJmp(jmp.label1, jmp.label2, cmp.op1, cmp.op2, cmp.symbol);
            machine.generated.set(i, new_jmp);
          }
        }
      }
    }
    return;
  }

  void RemoveDead() {
    for(int i = machine.generated.size() - 1; i >= 0; i--) {
      IRCode code = machine.generated.get(i);
      if(code.dead) {
        machine.generated.remove(i);
      }
    }
    return;
  }
}
