package ASTnodes;

import Composer.*;
import IRSentence.IRReturn;

public class ReturnNode extends StatNode {
  public ASTNode value = null;

  @Override
  public Mypair check() throws Exception {
    has_return = true;
    if (!in_func) {
      throw new Exception("Invalid Control Flow");
    }
    Mypair to_check = value.check();
    if (in_construct) {
      if (!to_check.type.equals("void")) {
        throw new Exception("Invalid Control Flow");
      } else {
        return new Mypair();
      }
    }
    if ((!to_check.type.equals(return_value.type)) && (!to_check.type.equals("null"))) {
      throw new Exception("Type Mismatch");
    }
    if (to_check.dim != return_value.dim) {
      throw new Exception("Type Mismatch");
    }
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    IRReturn res = new IRReturn();
    if (!machine.func_type.equals("void")) {
      Info return_value = value.GenerateIR(machine);
      res.reg = new String(return_value.reg);
    }
    res.return_type = new String(machine.func_type);
    machine.generated.add(res);
    return new Info();
  }
}
