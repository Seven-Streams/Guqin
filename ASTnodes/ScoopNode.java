package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoopNode extends StatNode{
  public ArrayList<ASTNode> stats = new ArrayList<>();
  @Override public Mypair check() throws Exception {
    variable_memory.add(new HashMap<>());
    for(ASTNode stat: stats) {
      stat.check();
    }
    variable_memory.remove(variable_memory.size() - 1);
    return new Mypair();
  }
}
