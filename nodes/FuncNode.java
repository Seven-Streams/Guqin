package nodes;

import java.util.ArrayList;
import java.util.HashMap;

public class FuncNode extends ASTNode {
  public String id = null;
  public ArrayList<ASTNode> args = new ArrayList<>();
  public ArrayList<ASTNode> stats = new ArrayList<>();
  @Override public Mypair check() throws Exception{
    variable_memory.add(new HashMap<>());
    if(in_class) {
      return_value = class_func_return.get(this_class).get(id);
    } else {
      return_value = func_return.get(id);
    }
    for(ASTNode arg: args) {
      Mypair res = arg.check();
      IdNode res_id = (IdNode)arg;
      if(variable_memory.get(variable_memory.size() - 1).containsKey(res_id.id)) {
        throw new Exception("Re defination in function args.");
      }
      variable_memory.get(variable_memory.size() - 1).put(res_id.id, res);
    }
    for(ASTNode stat: stats) {
      stat.check();
    }
    return new Mypair();
  }
}
