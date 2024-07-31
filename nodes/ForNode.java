package nodes;

import java.util.ArrayList;

public class ForNode extends StatNode{
  public ASTNode init = null;
  public ASTNode condition = null;
  public ASTNode iterator = null;
  public ArrayList<ASTNode> stats = new ArrayList<>();
}
