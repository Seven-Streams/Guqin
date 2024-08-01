package nodes;

import java.util.ArrayList;

public class AssignNode extends ExprNode {
  public ArrayList<ASTNode> ids = new ArrayList<>();
  public ASTNode values = null;

  @Override
  public Mypair check() throws Exception {
    Mypair demanded = values.check();
    for (ASTNode id : ids) {
      Mypair to_check = id.check();
      if (to_check.dim != demanded.dim) {
        throw new Exception("The assigned dimension is invalid!");
      }
      if (!to_check.type.equals(demanded.type)) {
        throw new Exception("The assigned type is invalid!");
      }
    }
    return demanded;
  }
}
