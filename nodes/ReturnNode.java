package nodes;

public class ReturnNode extends StatNode {
  public ASTNode value = null;

  @Override
  public Mypair check() throws Exception {
    Mypair to_check = value.check();
    if (!to_check.type.equals(return_value.type)) {
      throw new Exception("The return value is incorrect!");
    }
    if (to_check.dim != return_value.dim) {
      throw new Exception("The return dimension is incorrect!");
    }
    return new Mypair();
  }
}
