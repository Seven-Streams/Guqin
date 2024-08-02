package nodes;

import java.util.HashMap;

public class DimensionNode extends ExprNode {
  public HashMap<Integer, ASTNode> dim_expr = new HashMap<>();

  @Override public Mypair check() throws Exception {
    boolean not_good = false;
    for(int i = 0; i < dim; i++) {
      if(dim_expr.containsKey(i)) {
        if(not_good) {
          throw new Exception("The assignment in arry should be in order!");
        }
        Mypair to_check = dim_expr.get(i).check();
        if((!to_check.type.equals("int") )|| (to_check.dim != 0)) {
          throw new Exception("Invalid expression in dimension.");
        }
      } else {
        not_good = true;
      }
    }
    return new Mypair("", dim);
  }
}
