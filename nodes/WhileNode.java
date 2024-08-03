package nodes;

import java.util.HashMap;

public class WhileNode extends StatNode{
  public ASTNode condition = null;
  public ASTNode stats = null;
  @Override public Mypair check() throws Exception {
    in_loop++;
    Mypair to_check = condition.check();
    if(to_check.dim != 0) {
      throw new Exception("Invalid dimension in condition!");
    }
    if(!to_check.type.equals("bool")) {
      throw new Exception("Invalid type in condition!");
    }
    variable_memory.add(new HashMap<>());
      stats.check();
    variable_memory.remove(variable_memory.size() - 1);
    in_loop--;
    return new Mypair();
  }
}
