package ASTnodes;

public class PrintNode extends StatNode{
  public boolean change_line = false;
  public ASTNode value = null;
  @Override public Mypair check() throws Exception {
    return new Mypair();
  }
}
