package ASTnodes;

import java.util.ArrayList;

import Composer.*;
import IRSentence.IRFuncall;

public class FuncallNode extends ExprNode {
  public String name = null;
  public ASTNode from = null;
  public String from_type = null;
  public ArrayList<ASTNode> args = new ArrayList<>();

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
        System.out.println(name);
        throw new Exception("The function doesn't exist.");
      }
      ArrayList<Mypair> res_arg = new ArrayList<>();
      for (ASTNode arg : args) {
        res_arg.add(arg.check());
      }
      ArrayList<Mypair> true_arg = func_args.get(name);
      if (true_arg.size() != res_arg.size()) {
        throw new Exception("The number of args isn't correct.");
      }
      for (int i = 0; i < res_arg.size(); i++) {
        if (true_arg.get(i).dim != res_arg.get(i).dim) {
          if (!res_arg.get(i).type.equals("null")) {
            throw new Exception("The dimension of args isn't correct.");
          }
        }
        if (!true_arg.get(i).type.equals(res_arg.get(i).type)) {
          if ((!res_arg.get(i).type.equals("null")) | ((true_arg.get(i).dim == 0) && (true_arg.get(i).type.equals("int")
              || true_arg.get(i).type.equals("string") || true_arg.get(i).type.equals("bool")))) {
            throw new Exception("The type of args isn't correct.");
          }
        }
      }
      return func_return.get(name);
    } else {
      Mypair res = null;
      if (from != null) {
        res = from.check();
        from_type = new String(res.type);
      } else {
        res = new Mypair(from_type, 0);
      }
      if (res.dim != 0) {
        if (name.equals("size") && (args.isEmpty())) {
          return new Mypair("int", 0);
        }
        throw new Exception("The dimension of variable is incorrect.");
      }
      if ((!class_func_return.containsKey(res.type))) {
        throw new Exception("The function doesn't exist.");
      }
      ArrayList<Mypair> res_arg = new ArrayList<>();
      for (ASTNode arg : args) {
        res_arg.add(arg.check());
      }
      ArrayList<Mypair> true_arg = class_func_args.get(res.type).get(name);
      if (true_arg.size() != res_arg.size()) {
        throw new Exception("The number of args isn't correct.");
      }
      for (int i = 0; i < res_arg.size(); i++) {
        if (true_arg.get(i).dim != res_arg.get(i).dim) {
          throw new Exception("The dimension of args isn't correct.");
        }
        if (!true_arg.get(i).type.equals(res_arg.get(i).type)) {
          throw new Exception("The type of args isn't correct.");
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
    if (from_type.equals("null")) {
      res.func_name = name;
      return_check = func_return.get(name);
      if (!return_check.type.equals("void")) {
        String res_target = "%" + Integer.toString(++machine.tmp_time);
        res.target_reg = res_target;
        return_value.reg = res_target;
      }
      type_check = func_args.get(name);
    } else {
      res.func_name = from_type + name;
      return_check = class_func_return.get(from_type).get(name);
      type_check = class_func_args.get(from_type).get(name);
      if (!return_check.type.equals("void")) {
        String res_target = "%" + Integer.toString(++machine.tmp_time);
        res.target_reg = res_target;
        return_value.reg = res_target;
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
    return return_value;
  }
}
