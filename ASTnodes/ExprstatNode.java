package ASTnodes;

import Composer.*;
public class ExprstatNode extends StatNode{
  public ASTNode expr = null;
  @Override public Mypair check() throws Exception {
    return expr.check();
  }
  @Override public Info GenerateIR(Composer machine) {
    return expr.GenerateIR(machine);
  } 
}
