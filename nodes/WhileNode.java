package nodes;

import java.util.ArrayList;

public class WhileNode extends StatNode{
  public ExprNode condition = null;
  public ArrayList<StatNode> stats = null;
}
