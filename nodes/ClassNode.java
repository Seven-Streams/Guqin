package nodes;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassNode extends ASTNode {
  public String name = null;
  public ArrayList<ASTNode> member = new ArrayList<>();
  public ArrayList<ASTNode> dims = new ArrayList<>();
  public ASTNode construct = null;

  @Override
  public Mypair check() throws Exception {
    in_class = true;
    this_class = name;
    if(construct != null) {
      construct.check();
    }
    HashMap<String, Mypair> res = class_memory.get(name);
    variable_memory.add(res);
    for (ASTNode dim : dims) {
      dim.check();
    }
    for (ASTNode mem : member) {
      if (mem instanceof FuncNode) {
        mem.check();
      }
    }
    variable_memory.remove(variable_memory.size() - 1);
    in_class = false;
    return new Mypair();
  }
}
