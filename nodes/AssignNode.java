package nodes;

import java.util.ArrayList;

public class AssignNode extends ExprNode {
  public ArrayList<ASTNode> ids = new ArrayList<>();
  public ASTNode values = null;

  @Override
  public Mypair check() throws Exception {
    Mypair demanded = values.check();
    Mypair output = null;
    for (ASTNode id : ids) {
      Mypair to_check = id.check();
      if (!demanded.type.equals("null")) {
        if (to_check.dim != demanded.dim) {
          throw new Exception("The assigned dimension is invalid!");
        }
        if (!to_check.type.equals(demanded.type)) {
          System.out.println(demanded.type);
          System.out.println(to_check.type);
          throw new Exception("The assigned type is invalid!");
        }
        output = to_check;
      } else {
        output = demanded;
        if (to_check.type == "string") {
          throw new Exception("String can't be assigned with null!");
        }
      }
    }
    return output;
  }
}
