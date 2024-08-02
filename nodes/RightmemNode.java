package nodes;

public class RightmemNode extends MemNode{
  public ASTNode right_value = null;
  public ASTNode right_dim = null;
  public ASTNode func_node = null;
@Override
  public Mypair check() throws Exception {
    is_left = false;
    Mypair res_type = right_value.check();
    if(calling.isEmpty()) {
      Mypair dim_check = right_dim.check();
      return new Mypair(res_type.type, res_type.dim - dim_check.dim);
    }
    for (int i = 0; i < calling.size(); i++) {
      if (res_type == null) {
        String id = calling.get(0);
        for (int j = variable_memory.size() - 1; j >= 0; j--) {
          if (variable_memory.get(j).containsKey(id)) {
            Mypair res_type_ori = variable_memory.get(j).get(id);
            res_type = new Mypair(res_type_ori.type, res_type_ori.dim);
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
