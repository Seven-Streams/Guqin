package nodes;

import java.util.ArrayList;

public class FuncallNode extends ExprNode{
  public String name = null;
  public ASTNode from = null;
  public ArrayList<ASTNode> args = new ArrayList<>();
}
