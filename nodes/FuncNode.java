package nodes;

import java.util.ArrayList;

public class FuncNode extends ASTNode{
  public String id = null;
  public ArrayList<ASTNode> args = new ArrayList<>();
  public ArrayList<ASTNode> stats = new ArrayList<>();
}
