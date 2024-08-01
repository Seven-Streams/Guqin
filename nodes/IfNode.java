package nodes;


public class IfNode extends StatNode{
  public ASTNode condition = null;
  public ASTNode branch = null;
  public ASTNode else_branch = null;
  @Override public Mypair check() throws Exception {
    Mypair cond = condition.check();
    if(cond.dim != 0) {
      throw new Exception("Invalid dimension in condition.");
    }
    if(cond.type != "bool") {
      throw new Exception("Invalid type in condition.");
    }
    branch.check();
    if(else_branch != null) {
      else_branch.check();
    }
    return new Mypair();
  }
}
