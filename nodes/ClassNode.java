package nodes;

import java.util.ArrayList;

public class ClassNode extends ASTNode{
  public String name = null;
  public ArrayList<ASTNode> member = new ArrayList<>();
  public ASTNode construct = null;
}
