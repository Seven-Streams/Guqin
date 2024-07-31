package nodes;

import java.util.ArrayList;

public class FuncallNode extends ExprNode{
  public String name = null;
  public ArrayList<ExprNode> args = new ArrayList<>();
}
