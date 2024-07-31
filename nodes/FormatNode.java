package nodes;

import java.util.HashMap;

public class FormatNode extends ExprNode{
  HashMap<Integer, String> words = new HashMap<>();
  HashMap<Integer, ASTNode> values = new HashMap<>();
}
