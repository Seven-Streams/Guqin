package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;
import Composer.*;

public class ScoopNode extends StatNode {
  public ArrayList<ASTNode> stats = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    variable_memory.add(new HashMap<>());
    for (ASTNode stat : stats) {
      stat.check();
    }
    variable_memory.remove(variable_memory.size() - 1);
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    machine.now_name.push(new HashMap<>());
    machine.scope_time++;
    for (ASTNode stat : stats) {
      stat.GenerateIR(machine);
    }
    machine.now_name.pop();
    machine.scope_time++;
    return new Info();
  }
}
