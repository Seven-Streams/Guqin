package ASTnodes;

import java.util.HashMap;

public class MemNode extends ExprNode {
  public ExprNode from = null;
  public String id = null;

  @Override
  public Mypair check() throws Exception {
    Mypair to_check = from.check();
    is_left = true;
    if (to_check.dim != 0) {
      throw new Exception("Invalid dimension of a member!");
    }
    HashMap<String, Mypair> resed = class_memory.get(to_check.type);
    Mypair res = new Mypair(resed.get(id).type, resed.get(id).dim);
    return res;

  }
}
