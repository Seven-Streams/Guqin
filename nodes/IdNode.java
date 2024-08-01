package nodes;

public class IdNode extends ExprNode {
  public String id = null;

  @Override
  public Mypair check() throws Exception{
    is_left = true;
    for (int i = variable_memory.size() - 1; i >= 0; i--) {
      if(variable_memory.get(i).containsKey(id)) {
        return variable_memory.get(i).get(id);
      }
    }
    throw new Exception("Undeclared variable.");
  }
}
