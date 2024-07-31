package nodes;


public class IfNode extends StatNode{
  public ASTNode condition = null;
  public ASTNode branch = null;
  public ASTNode else_branch = null;
}
