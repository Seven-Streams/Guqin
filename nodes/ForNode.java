package nodes;

import java.util.ArrayList;

public class ForNode extends StatNode{
  public StatNode init = null;
  public ExprNode condition = null;
  public ExprNode iterator = null;
  public ArrayList<StatNode> stats = new ArrayList<>();
}
