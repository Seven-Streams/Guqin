package nodes;

public class IdNode extends ExprNode {
  public String id = null;
  public ExprNode from = null;

  @Override
  public Mypair check() throws Exception {
    is_left = true;
    for (int i = variable_memory.size() - 1; i >= 0; i--) {
      if (variable_memory.get(i).containsKey(id)) {
        Mypair res = new Mypair(variable_memory.get(i).get(id).type, variable_memory.get(i).get(id).dim);
        dim = res.dim;
        return res;
      }
    }
    if(func_return.containsKey(id)) {
      Mypair res = new Mypair(func_return.get(id).type, func_return.get(id).dim);
      return res;
    }
    if(in_class) {
      if(class_memory.get(this_class).containsKey(id)) {
        return new Mypair(class_memory.get(this_class).get(id).type, class_memory.get(this_class).get(id).dim);
      }
    }
    throw new Exception("Undeclared variable.");
  }
}
