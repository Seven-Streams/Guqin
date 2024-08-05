package ASTnodes;

public class ExprstatNode extends StatNode{
  public ASTNode expr = null;
  @Override public Mypair check() throws Exception {
    return expr.check();
  }
}
