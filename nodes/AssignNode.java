package nodes;

import java.util.ArrayList;

public class AssignNode extends ExprNode {
  public ArrayList<String> ids = new ArrayList<>();
  public ASTNode values = null;

  @Override
  public Mypair check() throws Exception {
    Mypair demanded = values.check();
    for (String id : ids) {
      for (int i = variable_memory.size() - 1; i >= 0; i--) {
        if (variable_memory.get(i).containsKey(id)) {
          Mypair here = variable_memory.get(i).get(id);
          if (!demanded.type.equals(here.type)) {
            throw new Exception("Invalid type in assignment.");
          }
          if (demanded.dim != here.dim) {
            throw new Exception("Invalid dimension in assignment.");
          }
        }
      }
    }
    return demanded;
  }
}
