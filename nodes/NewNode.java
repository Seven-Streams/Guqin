package nodes;

public class NewNode extends ExprNode{
  public ASTNode value = null;
  public ASTNode dims = null;
  @Override public Mypair check() throws Exception {
    is_left = false;
    if((value == null) && (dims == null)) {
      return new Mypair(type, dim);
    }
    if(value != null) {
      Mypair res = value.check();
      if(res.type != type) {
        throw new Exception("Invalid type in new expression!");
      }
      return res;
    }
    Mypair res = dims.check();
    return new Mypair(type, res.dim);
  }
}
