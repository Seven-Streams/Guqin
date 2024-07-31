package nodes;

import java.util.HashMap;

public class FstringNode extends ExprNode{
  public HashMap<String, ASTNode> contents = new HashMap<>();
  public String value = null;
}
