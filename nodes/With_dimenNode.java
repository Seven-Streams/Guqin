package nodes;

public class With_dimenNode extends ExprNode {
  public DimensionNode dim_node = null;
  public ExprNode ex = null;
  @Override public Mypair check() throws Exception {
    Mypair ex_pair = ex.check();
    Mypair dim_pair = dim_node.check();
    if(ex_pair.dim < dim_pair.dim) {
      throw new Exception("The dimension is out of range!");
    }
    is_left = ex.is_left;
    return new Mypair(ex_pair.type, ex_pair.dim - dim_pair.dim);
  }
}
