package ASTnodes;

import java.util.HashMap;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRLabel;
import IRSentence.IRjmp;

public class IfNode extends StatNode {
  public ASTNode condition = null;
  public ASTNode branch = null;
  public ASTNode else_branch = null;

  @Override
  public Mypair check() throws Exception {
    Mypair cond = condition.check();
    if (cond.dim != 0) {
      throw new Exception("Invalid dimension in condition.");
    }
    if (!cond.type.equals("bool")) {
      throw new Exception("Invalid type in condition.");
    }
    branch.check();
    if (else_branch != null) {
      else_branch.check();
    }
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    machine.now_name.push(new HashMap<>());
    machine.scope_time++;
    Info judge = condition.GenerateIR(machine);
    int branch_lb = ++machine.label_number;
    int else_branch_lb = ++machine.label_number;
    int end = ++machine.label_number;
    if (else_branch != null) {
      Conditionjmp res_jmp = new Conditionjmp(branch_lb, else_branch_lb, judge.reg);
      machine.generated.add(res_jmp);
    } else {
      Conditionjmp res_jmp = new Conditionjmp(branch_lb, end, judge.reg);
      machine.generated.add(res_jmp);
    }
    machine.generated.add(new IRLabel(branch_lb));
    branch.GenerateIR(machine);
    IRjmp end_jmp = new IRjmp(end);
    machine.generated.add(end_jmp);
    if (else_branch != null) {
      machine.generated.add(new IRLabel(else_branch_lb));
      else_branch.GenerateIR(machine);
      machine.generated.add(end_jmp);
    }
    machine.generated.add(new IRLabel(end));
    machine.now_name.pop();
    machine.scope_time++;
    return new Info();
  }
}
