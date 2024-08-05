package ASTnodes;

import java.util.HashMap;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRLabel;
import IRSentence.IRjmp;

public class WhileNode extends StatNode {
  public ASTNode condition = null;
  public ASTNode stats = null;

  @Override
  public Mypair check() throws Exception {
    in_loop++;
    Mypair to_check = condition.check();
    if (to_check.dim != 0) {
      throw new Exception("Invalid dimension in condition!");
    }
    if (!to_check.type.equals("bool")) {
      throw new Exception("Invalid type in condition!");
    }
    variable_memory.add(new HashMap<>());
    stats.check();
    variable_memory.remove(variable_memory.size() - 1);
    in_loop--;
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    machine.now_name.push(new HashMap<>());
    LoopInfo res_label = new LoopInfo();
    res_label.condition = ++machine.label_number;
    res_label.body = ++machine.label_number;
    res_label.end = ++machine.label_number;
    Info.loop.add(res_label);
    machine.generated.add(new IRLabel(res_label.condition));
    Info cond_reg = condition.GenerateIR(machine);
    Conditionjmp cond_jmp = new Conditionjmp(res_label.body, res_label.end, cond_reg.reg);
    machine.generated.add(cond_jmp);
    machine.generated.add(new IRLabel(res_label.body));
    stats.GenerateIR(machine);
    IRjmp check_jmp = new IRjmp(res_label.condition);
    machine.generated.add(check_jmp);
    machine.generated.add(new IRLabel(res_label.end));
    machine.now_name.pop();
    return new Info();
  }
}
