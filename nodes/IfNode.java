package nodes;

import java.util.ArrayList;

public class IfNode extends StatNode{
  public ASTNode condition = null;
  public ArrayList<ASTNode> branch = new ArrayList<>();
  public ArrayList<ASTNode> else_branch = new ArrayList<>();
}
