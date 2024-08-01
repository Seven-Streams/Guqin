package nodes;

public class ExprNode extends ASTNode {
  boolean is_left = true;

  @Override public Mypair check() throws Exception{
  return new Mypair(type, dim);
  }
}
