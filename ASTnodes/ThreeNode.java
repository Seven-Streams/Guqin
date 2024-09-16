package ASTnodes;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRLabel;
import IRSentence.IRPhi;
import IRSentence.IRjmp;

public class ThreeNode extends ExprNode {
  public ASTNode condition = null;
  public ASTNode value1 = null;
  public ASTNode value2 = null;

  @Override
  public Mypair check() throws Exception {
    Mypair res = condition.check();
    if (res.dim != 0) {
      throw new Exception("Type Mismatch");
    }
    if (!res.type.equals("bool")) {
      throw new Exception("Type Mismatch");
    }
    Mypair res1 = value1.check();
    Mypair res2 = value2.check();
    if ((res1.dim != res2.dim) && (!res1.type.equals("null")) && (!res2.type.equals("null"))) {
      throw new Exception("Type Mismatch");
    }
    if ((!res1.type.equals(res2.type)) && (!res1.type.equals("null")) && (!res2.type.equals("null"))) {
      throw new Exception("Type Mismatch");
    }
    is_left = false;
    type = (res1.type.equals("null") ? res2.type : res1.type);
    return res1.type.equals("null") ? res2 : res1;
  }

  @Override
  public Info GenerateIR(Composer machine) {
    int tmp = ++(machine.tmp_time);
    String reg_name = "%reg$" + Integer.toString(tmp);
    int value_1 = ++machine.label_number;
    int value_2 = ++machine.label_number;
    int let_1 = ++machine.label_number;
    int let_2 = ++machine.label_number;
    int end = ++machine.label_number;
    Info cond_value = condition.GenerateIR(machine);
    machine.generated.add(new Conditionjmp(value_1, value_2, cond_value.reg));
    machine.generated.add(new IRLabel(value_1));
    Info result1 = value1.GenerateIR(machine);
    machine.generated.add(new IRjmp(let_1));
    machine.generated.add(new IRLabel(value_2));
    Info result2 = value2.GenerateIR(machine);
    machine.generated.add(new IRjmp(let_2));
    machine.generated.add(new IRLabel(let_1));
    machine.generated.add(new IRjmp(end));
    machine.generated.add(new IRLabel(let_2));
    machine.generated.add(new IRjmp(end));
    machine.generated.add(new IRLabel(end));
    IRPhi phi = new IRPhi();
    phi.target = (reg_name);
    phi.labels.add(let_1);
    phi.labels.add(let_2);
    phi.values.add(result1.reg);
    phi.values.add(result2.reg);
    if (dim != 0) {
      phi.type = "ptr";
    } else {
      switch (type) {
        case ("int"): {
          phi.type = "i32";
          break;
        }
        case ("bool"): {
          phi.type = "i1";
          break;
        }
        default: {
          phi.type = "ptr";
          break;
        }
      }
    }
    machine.generated.add(phi);
    Info output = new Info();
    output.reg = reg_name;
    return output;
  }
}
