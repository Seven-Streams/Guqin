package nodes;

import java.util.ArrayList;

public class FuncallNode extends ExprNode {
  public String name = null;
  public ASTNode from = null;
  public String from_type = null;
  public ArrayList<ASTNode> args = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    if ((from == null) && (from_type == null)) {
      if (!func_return.containsKey(name)) {
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
          throw new Exception("The dimension of args isn't correct.");
        }
        if (true_arg.get(i).type.equals(res_arg.get(i).type)) {
          throw new Exception("The type of args isn't correct.");
        }
      }
      return func_return.get(name);
    } else {
      Mypair res = null;
      if (from != null) {
        res = from.check();
      } else {
        res = new Mypair(from_type, 0);
      }
      if (res.dim != 0) {
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
        if (true_arg.get(i).type.equals(res_arg.get(i).type)) {
          throw new Exception("The type of args isn't correct.");
        }
      }
      Mypair return_value = class_func_return.get(res.type).get(name);
      return new Mypair(return_value.type, return_value.dim);
    }
  }
}
