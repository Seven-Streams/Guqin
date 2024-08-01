package nodes;

public class ThreeNode extends ExprNode {
  public ASTNode condition = null;
  public ASTNode value1 = null;
  public ASTNode value2 = null;

  @Override
  public Mypair check() throws Exception {
    Mypair res = condition.check();
    if (res.dim != 0) {
      throw new Exception("Invalid dimension in condition of 3-arg expression.");
    }
    if (res.type != "bool") {
      throw new Exception("Invalid type in condition of 3-arg expression.");
    }
    Mypair res1 = value1.check();
    Mypair res2 = value2.check();
    if (res1.dim != res2.dim) {
      throw new Exception("Different dimension in condition of 3-arg expression.");
    }
    if (!res1.type.equals(res2.type)) {
      throw new Exception("Different type in condition of 3-arg expression.");
    }
    is_left = false;
    return res1;
  }
}
