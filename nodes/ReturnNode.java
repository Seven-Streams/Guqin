package nodes;

public class ReturnNode extends StatNode {
  public ASTNode value = null;

  @Override
  public Mypair check() throws Exception {
    if(!in_func) {
      throw new Exception("Return should be in a function!");
    }
    Mypair to_check = value.check();
    if (!to_check.type.equals(return_value.type)) {
      throw new Exception("The return value is incorrect!");
    }
    if (to_check.dim != return_value.dim) {
      throw new Exception("The return dimension is incorrect!");
    }
    ExprNode return_check = null;
    if (value instanceof ExprNode) {
      return_check = (ExprNode) value;
      if (return_check.is_left) {
        return_left = true;
      } else {
        return_left = false;
      }
    } else {
      return_left = false;
    }
    return new Mypair();
  }
}
