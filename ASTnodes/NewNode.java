package ASTnodes;

import java.util.ArrayList;

import Composer.*;
public class NewNode extends ExprNode{
  public ASTNode value = null;
  public ASTNode dims = null;
  @Override public Mypair check() throws Exception {
    is_left = true;
    if((value == null) && (dims == null)) {
      return new Mypair(type, dim);
    }
    if(value != null) {
      Mypair res = value.check();
      if(!res.type.equals(type)) {
        throw new Exception("Invalid type in new expression!");
      }
      return res;
    }
    Mypair res = dims.check();
    return new Mypair(type, res.dim);
  }
}
