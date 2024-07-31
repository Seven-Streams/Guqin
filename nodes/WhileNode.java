package nodes;

import java.util.ArrayList;

public class WhileNode extends StatNode{
  public ASTNode condition = null;
  public ArrayList<StatNode> stats = new ArrayList<>();
}
