package ASTnodes;

import Composer.*;
import IRSentence.*;

public class PrintNode extends StatNode {
  public boolean change_line = false;
  public ASTNode value = null;
  public boolean is_int = false;

  @Override
  public Mypair check() throws Exception {
    type = value.check().type;
    if(is_int) {
      if(!type.equals("int")) {
        throw new Exception("Type Mismatch");
      }
    } else {
      if(!type.equals("string")) {
        throw new Exception("Type Mismatch");
      }
    }
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info to_print = value.GenerateIR(machine);
    IRFuncall res = new IRFuncall();
    res.func_type = "void";
    res.reg.add(to_print.reg);
    if (type.equals("int")) {
      res.type.add("i32");
      if (change_line) {
        res.func_name = "printIntln";
      } else {
        res.func_name = "printInt";
      }
    } else {
      if (change_line) {
        res.func_name = "println";
      } else {
        res.func_name = "print";
      }
      res.type.add("ptr");
    }
    machine.generated.add(res);
    return new Info();
  }
}
