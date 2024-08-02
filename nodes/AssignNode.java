package nodes;

import java.util.ArrayList;

public class AssignNode extends ExprNode {
  public ArrayList<ASTNode> ids = new ArrayList<>();
  public ASTNode values = null;
  int clock = 0;
  @Override
  public Mypair check() throws Exception {
    Mypair demanded = values.check();
    clock++;
    Mypair output = null;
    for (ASTNode id : ids) {
      Mypair to_check = id.check();
      ExprNode node_check = (ExprNode) id;
      if (!node_check.is_left) {
        throw new Exception("The assigned value should be a left value!");
      }
      if (!demanded.type.equals("null")) {
        if (to_check.dim != demanded.dim) {
          throw new Exception("The assigned dimension is invalid!");
        }
        if (!to_check.type.equals(demanded.type)) {
          throw new Exception("The assigned type is invalid!");
        }
        output = to_check;
      } else {
        output = demanded;
        if (to_check.dim != 0) {
          continue;
        }
        if (to_check.type.equals("string") || to_check.type.equals("int") || to_check.type.equals("bool")) {
          throw new Exception("Inner type can't be assigned with null!");
        }
      }
    }
    return output;
  }
}
