package ASTnodes;

import Composer.*;
import IRSentence.*;

import java.util.HashMap;

public class ForNode extends StatNode {
  public ASTNode init = null;
  public ASTNode condition = null;
  public ASTNode iterator = null;
  public ASTNode stats = null;

  @Override
  public Mypair check() throws Exception {
    in_loop++;
    variable_memory.add(new HashMap<>());
    if (init != null) {
      init.check();
    }
    if (condition != null) {
      Mypair res_cond = condition.check();
      if (res_cond.dim != 0) {
        throw new Exception("Invalid dimension in condition of loop.");
      }
      if ((!res_cond.type.equals("bool")) && (!res_cond.type.equals("void"))) {
        System.out.println(res_cond.type);
        throw new Exception("Invalid type in condition of loop.");
      }
    }
    if (iterator != null) {
      iterator.check();
    }
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
    res_label.iteration = ++machine.label_number;
    res_label.end = ++machine.label_number;
    Info.loop.add(res_label);
    init.GenerateIR(machine);
    machine.generated.add(new IRLabel(res_label.condition));
    Info cond_reg = condition.GenerateIR(machine);
    Conditionjmp cond_jmp = new Conditionjmp(res_label.body, res_label.end, cond_reg.reg);
    machine.generated.add(cond_jmp);
    machine.generated.add(new IRLabel(res_label.body));
    stats.GenerateIR(machine);
    machine.generated.add(new IRLabel(res_label.iteration));
    cond_reg = iterator.GenerateIR(machine);
    IRjmp check_jmp = new IRjmp(res_label.condition);
    machine.generated.add(check_jmp);
    machine.generated.add(new IRLabel(res_label.end));
    machine.now_name.pop();
    return new Info();
  }
}
