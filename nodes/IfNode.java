package nodes;

import java.util.ArrayList;

public class IfNode extends StatNode{
  public ExprNode condition = null;
  public ArrayList<StatNode> branch = new ArrayList<>();
  public ArrayList<StatNode> else_branch = new ArrayList<>();
}
