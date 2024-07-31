package nodes;

import java.util.ArrayList;

public class MemNode extends ASTNode{
  public ArrayList<ASTNode> calling = new ArrayList<>();
  public ArrayList<ASTNode> dims = new ArrayList<>();
  public String id = null;
}
