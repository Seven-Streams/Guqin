package ASTnodes;

public class ThisExprNode extends ASTNode{
  public ASTNode expr = null;
  @Override public Mypair check() throws Exception{
    Mypair res = expr.check();
    if(res.dim != 0) {
      throw new Exception("Invalid Type");
    }
    if(res.type.equals("int") || res.type.equals("bool") || res.type.equals("string")) {
      throw new Exception("Invalid Type");
    }
    return res;
  }
}
