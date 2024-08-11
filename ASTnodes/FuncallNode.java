package ASTnodes;

import java.util.ArrayList;
import Composer.*;
import IRSentence.IRFuncall;

public class FuncallNode extends ExprNode {
  public String name = null;
  public ASTNode from = null;
  public String from_type = null;
  public ArrayList<ASTNode> args = new ArrayList<>();
  public int from_dim = 0;
  @Override
  public Mypair check() throws Exception {
    is_left = false;
    if (in_class) {
      if (class_func_return.get(this_class).containsKey(name)) {
        from_type = this_class;
      }
    }
    if ((from == null) && (from_type == null)) {
      if (!func_return.containsKey(name)) {
        throw new Exception("Undefined Identifier");
      }
      ArrayList<Mypair> res_arg = new ArrayList<>();
      for (ASTNode arg : args) {
        res_arg.add(arg.check());
      }
      ArrayList<Mypair> true_arg = func_args.get(name);
      if (true_arg.size() != res_arg.size()) {
        throw new Exception("Args Mismatch");
      }
      for (int i = 0; i < res_arg.size(); i++) {
        if (true_arg.get(i).dim != res_arg.get(i).dim) {
          if (!res_arg.get(i).type.equals("null")) {
            throw new Exception("Type Mismatch");
          }
        }
        if (!true_arg.get(i).type.equals(res_arg.get(i).type)) {
          if ((!res_arg.get(i).type.equals("null")) | ((true_arg.get(i).dim == 0) && (true_arg.get(i).type.equals("int")
              || true_arg.get(i).type.equals("string") || true_arg.get(i).type.equals("bool")))) {
            throw new Exception("Type Mismatch");
          }
        }
      }
      return func_return.get(name);
    } else {
      Mypair res = null;
      if (from != null) {
        res = from.check();
        from_dim = res.dim;
        from_type = new String(res.type);
      } else {
        res = new Mypair(from_type, 0);
      }
      if (res.dim != 0) {
        if (name.equals("size") && (args.isEmpty())) {
          return new Mypair("int", 0);
        }
        throw new Exception("Undefined Identifier");
      }
      if ((!class_func_return.containsKey(res.type))) {
        throw new Exception("Undefined Identifier");
      }
      ArrayList<Mypair> res_arg = new ArrayList<>();
      for (ASTNode arg : args) {
        res_arg.add(arg.check());
      }
      ArrayList<Mypair> true_arg = class_func_args.get(res.type).get(name);
      if (true_arg.size() != res_arg.size()) {
        throw new Exception("Arg Mismatch");
      }
      for (int i = 0; i < res_arg.size(); i++) {
        if (true_arg.get(i).dim != res_arg.get(i).dim) {
          throw new Exception("Type Mismatch");
        }
        if (!true_arg.get(i).type.equals(res_arg.get(i).type)) {
          throw new Exception("Type Mismatch");
        }
      }
      Mypair return_value = class_func_return.get(res.type).get(name);
      return new Mypair(return_value.type, return_value.dim);
    }
  }

  @Override
  public Info GenerateIR(Composer machine) {
    IRFuncall res = new IRFuncall();
    Info return_value = new Info();
    Mypair return_check;
    ArrayList<Mypair> type_check;
    if (from_type == null) {
      res.func_name = name;
      return_check = func_return.get(name);
      if (!return_check.type.equals("void")) {
        String res_target = "%reg$" + Integer.toString(++machine.tmp_time);
        res.target_reg = res_target;
        return_value.reg = res_target;
      }
      type_check = func_args.get(name);
    } else {
      if (from == null) {
        res.func_name = from_type + "." + name;
        res.reg.add("%this");
        res.type.add("ptr");
        return_check = class_func_return.get(from_type).get(name);
        type_check = class_func_args.get(from_type).get(name);
        if (!return_check.type.equals("void")) {
          String res_target = "%reg$" + Integer.toString(++machine.tmp_time);
          res.target_reg = res_target;
          return_value.reg = res_target;
        }   
      } else {
        Info from_reg = from.GenerateIR(machine);
        if (name.equals("size") && (from_dim != 0)) {
          res.func_name = "array_size";
          res.func_type = "i32";
          res.reg.add(new String(from_reg.reg));
          res.type.add("ptr");
          res.target_reg = "%reg$" + Integer.toString(++machine.tmp_time);
          machine.generated.add(res);
          return_value.reg = new String(res.target_reg);
          return return_value;
        }
        res.func_name = from_type + "." + name;
        res.reg.add(new String(from_reg.reg));
        res.type.add("ptr");
        return_check = class_func_return.get(from_type).get(name);
        type_check = class_func_args.get(from_type).get(name);
        if (!return_check.type.equals("void")) {
          String res_target = "%reg$" + Integer.toString(++machine.tmp_time);
          res.target_reg = res_target;
          return_value.reg = res_target;
        }
      }
    }
    if (return_check.dim != 0) {
      res.func_type = "ptr";
    } else {
      switch (return_check.type) {
        case ("int"): {
          res.func_type = "i32";
          break;
        }
        case ("bool"): {
          res.func_type = "i1";
          break;
        }
        case ("void"): {
          res.func_type = "void";
          break;
        }
        default: {
          res.func_type = "ptr";
          break;
        }
      }
    }
    for (ASTNode arg : args) {
      Info to_add = arg.GenerateIR(machine);
      res.reg.add(to_add.reg);
    }
    for (Mypair type : type_check) {
      if (type.dim != 0) {
        res.type.add("ptr");
      } else {
        switch (type.type) {
          case ("int"): {
            res.type.add("i32");
            break;
          }
          case ("bool"): {
            res.type.add("i1");
            break;
          }
          default: {
            res.type.add("ptr");
            break;
          }
        }
      }
    }
    machine.generated.add(res);
    return return_value;
  }
}
