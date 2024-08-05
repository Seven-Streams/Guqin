package ASTnodes;

public class SingleNode extends ExprNode {
  public String symbol = null;
  public ASTNode value = null;
  public boolean symbol_left = false;

  @Override
  public Mypair check() throws Exception {
    Mypair to_check = value.check();
    if (symbol.equals("!")) {
      if (!to_check.type.equals("bool")) {
        throw new Exception("The value should be a bool!");
      }
      if (to_check.dim != 0) {
        throw new Exception("invalid dimension!");
      }
      is_left = false;
      return new Mypair("bool", 0);
    }
    if (((symbol.equals("++")) || (symbol.equals("--")))) {
      ExprNode real_value = (ExprNode) (value);
      if (!real_value.is_left) {
        throw new Exception("The value should be a left_value!");
      }
      is_left = symbol_left;
    }
    if (!to_check.type.equals("int")) {
      throw new Exception("The value should be a integer!");
    }
    if (to_check.dim != 0) {
      throw new Exception("invalid dimension!");
    }
    return new Mypair("int", 0);
  }
}
