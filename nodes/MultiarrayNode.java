package nodes;

import java.util.ArrayList;

public class MultiarrayNode extends ArrayNode {
  public ArrayList<ASTNode> res = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    Mypair res_mypair = null;
    for (ASTNode ar : res) {
      if (res_mypair == null) {
        res_mypair = ar.check();
      } else {
        Mypair to_check = ar.check();
        if (to_check.dim != res_mypair.dim) {
          throw new Exception("Different dimensions in an array!");
        }
        if (res_mypair.type != to_check.type && (!to_check.type.equals("")) && (!res_mypair.type.equals(""))) {
          throw new Exception("different type in an array!");
        }
        if (res_mypair.type == "") {
          res_mypair = to_check;
        }
      }
    }
    return new Mypair(res_mypair.type, res_mypair.dim + 1);
  }
}
