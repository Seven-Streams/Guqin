package ASTnodes;

public class MultiarrayNode extends ArrayNode {

  @Override
  public Mypair check() throws Exception {
    Mypair res_mypair = null;
    for (ASTNode ar : elements) {
      if (res_mypair == null) {
        res_mypair = ar.check();
        type = res_mypair.type;
      } else {
        Mypair to_check = ar.check();
        if (to_check.dim != res_mypair.dim && (!to_check.type.equals("")) && (!res_mypair.type.equals(""))) {
          throw new Exception("Different dimensions in an array!");
        }
        if (!(res_mypair.type.equals(to_check.type)) && (!to_check.type.equals("")) && (!res_mypair.type.equals(""))) {
          throw new Exception("different type in an array!");
        }
        if (res_mypair.type.equals("")) {
          res_mypair = to_check;
        }
        if (res_mypair.dim == 0) {
          res_mypair.dim = to_check.dim;
        }
      }
    }
    return new Mypair(res_mypair.type, res_mypair.dim + 1);
  }
}
