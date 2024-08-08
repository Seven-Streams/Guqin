package ASTnodes;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRBin;
import IRSentence.IRLabel;
import IRSentence.IRjmp;

public class ThreeNode extends ExprNode {
  public ASTNode condition = null;
  public ASTNode value1 = null;
  public ASTNode value2 = null;

  @Override
  public Mypair check() throws Exception {
    Mypair res = condition.check();
    if (res.dim != 0) {
      throw new Exception("Invalid dimension in condition of 3-arg expression.");
    }
    if (!res.type.equals("bool")) {
      throw new Exception("Invalid type in condition of 3-arg expression.");
    }
    Mypair res1 = value1.check();
    Mypair res2 = value2.check();
    if ((res1.dim != res2.dim) && (!res1.type.equals("null")) && (!res2.type.equals("null"))) {
      throw new Exception("Different dimension in  3-arg expression.");
    }
    if ((!res1.type.equals(res2.type)) && (!res1.type.equals("null")) && (!res2.type.equals("null"))) {
      throw new Exception("Different type in 3-arg expression.");
    }
    is_left = false;
    return res1.type.equals("null") ? res2 : res1;
  }

  @Override
  public Info GenerateIR(Composer machine) {
    int tmp = ++(machine.tmp_time);
    String reg_name = "%reg$" + Integer.toString(tmp);
    int value_1 = ++machine.label_number;
    int value_2 = ++machine.label_number;
    int end = ++machine.label_number;
    Info cond_value = condition.GenerateIR(machine);
    machine.generated.add(new Conditionjmp(value_1, value_2, cond_value.reg));
    machine.generated.add(new IRLabel(value_1));
    Info result1 = value1.GenerateIR(machine);
    IRBin ass_1 = new IRBin();
    ass_1.op1 = result1.reg;
    ass_1.op2 = "0";
    ass_1.symbol = "+";
    ass_1.target_reg = reg_name;
    ass_1.type = "i32";
    machine.generated.add(ass_1);
    machine.generated.add(new IRjmp(end));
    machine.generated.add(new IRLabel(value_2));
    Info result2 = value2.GenerateIR(machine);
    IRBin ass_2 = new IRBin();
    ass_2.op1 = result2.reg;
    ass_2.op2 = "0";
    ass_2.symbol = "+";
    ass_2.target_reg = reg_name;
    ass_2.type = "i32";
    machine.generated.add(ass_2);
    machine.generated.add(new IRjmp(end));
    machine.generated.add(new IRLabel(end));
    Info output = new Info();
    output.reg = reg_name;
    return output;
  }
}
