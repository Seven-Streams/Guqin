package nodes;

import java.util.ArrayList;

public class MemNode extends ExprNode {
  public ArrayList<String> calling = new ArrayList<>();
  public ArrayList<ASTNode> dims = new ArrayList<>();
  @Override
  public Mypair check() throws Exception {
    is_left = true;
    Mypair res_type = null;
    for (int i = 0; i < calling.size(); i++) {
      if (res_type == null) {
        String id = calling.get(0);
        for (int j = variable_memory.size() - 1; j >= 0; j--) {
          if (variable_memory.get(j).containsKey(id)) {
            res_type = variable_memory.get(j).get(id);
            res_type = new Mypair();
            break;
          }
        }
        if (res_type == null) {
          throw new Exception("The variable doesn't exist!");
        }
      } else {
        String res_id = calling.get(i);
        if (!class_memory.get(res_type.type).containsKey(res_id)) {
          throw new Exception("This member doesn't exist!");
        }
        res_type.dim = class_memory.get(res_type.type).get(res_id).dim;
        res_type.type = class_memory.get(res_type.type).get(res_id).type;
      }
      Mypair dim_check = dims.get(i).check();
      if (i != (calling.size() - 1)) {
        if (dim_check.dim != res_type.dim) {
          throw new Exception("invalid dimension in member relationship.");
        }
      } else {
        res_type.dim -= dim_check.dim;
      }
    }
    type = res_type.type;
    dim = res_type.dim;
    return res_type;
  }
}
