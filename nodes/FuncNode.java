package nodes;

import java.util.ArrayList;
import java.util.HashMap;

public class FuncNode extends ASTNode {
  public String id = null;
  public boolean is_construct;
  public boolean is_left = false;
  public ArrayList<ASTNode> args = new ArrayList<>();
  public ArrayList<ASTNode> stats = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    in_construct = is_construct;
    if (in_construct && (!id.equals(this_class))) {
      throw new Exception("Invalid construction function name!");
    }
    has_return = false;
    in_func = true;
    variable_memory.add(new HashMap<>());
    if (!is_construct) {
      if (in_class) {
        return_value = class_func_return.get(this_class).get(id);
      } else {
        return_value = func_return.get(id);
      }
    }
    for (ASTNode arg : args) {
      IdNode res_id = (IdNode) arg;
      if (!class_memory.containsKey(arg.type)) {
        throw new Exception("The type is invalid!");
      }
      if (variable_memory.get(variable_memory.size() - 1).containsKey(res_id.id)) {
        throw new Exception("Re defination in function args.");
      }
      variable_memory.get(variable_memory.size() - 1).put(res_id.id, new Mypair(res_id.type, res_id.dim));
    }
    for (ASTNode stat : stats) {
      stat.check();
    }
    variable_memory.remove(variable_memory.size() - 1);
    if ((!return_value.type.equals("void")) && (!has_return) && (!id.equals("main")) && (!is_construct)) {
      throw new Exception("Non-void function should have a return value!");
    }
    in_func = false;
    in_construct = false;
    return new Mypair();
  }
}
