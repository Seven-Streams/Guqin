package ASTnodes;

import java.util.ArrayList;
import Composer.*;
import IRSentence.IRFuncall;

public class InnerNode extends ExprNode {
  public String name = null;
  public ArrayList<ASTNode> args = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    if (name.equals("ord")) {
      Mypair to_check = args.get(0).check();
      if (!to_check.type.equals("string")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      to_check = args.get(1).check();
      if (!to_check.type.equals("int")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      return new Mypair("int", 0);
    }
    if (name.equals("length") || name.equals("parseInt")) {
      Mypair to_check = args.get(0).check();
      if (!to_check.type.equals("string")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      return new Mypair("int", 0);
    }
    if (name.equals("substring")) {
      Mypair to_check = args.get(0).check();
      if (!to_check.type.equals("string")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      to_check = args.get(1).check();
      if (!to_check.type.equals("int")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      to_check = args.get(2).check();
      if (!to_check.type.equals("int")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      return new Mypair("string", 0);
    }
    throw new Exception("Invalid string method.");
  }

  @Override
  public Info GenerateIR(Composer machine) {
    String target_name = "%reg$" + Integer.toString(++machine.tmp_time);
    IRFuncall buildin_func = new IRFuncall();
    buildin_func.target_reg = target_name;
    buildin_func.func_name = "string_" + name;
    for (ASTNode arg : args) {
      Info res = arg.GenerateIR(machine);
      buildin_func.reg.add(res.reg);
    }
    switch (name) {
      case ("ord"): {
        buildin_func.func_type = "i32";
        buildin_func.type.add("ptr");
        buildin_func.type.add("i32");
        break;
      }
      case ("length"): {
        buildin_func.func_type = "i32";
        buildin_func.type.add("ptr");
        break;
      }
      case ("parseInt"): {
        buildin_func.func_type = "i32";
        buildin_func.type.add("ptr");
        break;
      }
      case ("substring"): {
        buildin_func.func_type = "ptr";
        buildin_func.type.add("ptr");
        buildin_func.type.add("i32");
        buildin_func.type.add("i32");
        break;
      }
      default:
        System.out.println("ERROR IN BUILD-IN FUNCTION!");
        break;
    }
    machine.generated.add(buildin_func);
    Info return_value = new Info();
    return_value.reg = target_name;
    return return_value;
  }
}
