package nodes;

import java.util.HashMap;

public class DimensionNode extends ExprNode {
  public HashMap<Integer, ASTNode> dim_expr = new HashMap<>();

  @Override public Mypair check() throws Exception {
    for(int i = 0; i < dim; i++) {
      if(dim_expr.containsKey(i)) {
        Mypair to_check = dim_expr.get(i).check();
        if((!to_check.type.equals("int") )|| (to_check.dim != 0)) {
          throw new Exception("Invalid expression in dimension.");
        }
      }
    }
    return new Mypair("", dim);
  }
}
