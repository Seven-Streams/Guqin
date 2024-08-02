package nodes;

public class IdNode extends ExprNode {
  public String id = null;
  public ExprNode from = null;
  @Override public Mypair check() throws Exception{
    System.out.println(id);
    is_left = true;
    for (int i = variable_memory.size() - 1; i >= 0; i--) {
      if(variable_memory.get(i).containsKey(id)) {
        Mypair res = new Mypair(variable_memory.get(i).get(id).type, variable_memory.get(i).get(id).dim);
        System.out.println(res.dim);
        dim = res.dim;
        return res;
      }
    }
    throw new Exception("Undeclared variable.");
  }
}
