package ASTnodes;

import java.util.ArrayList;

public class FstringNode extends ExprNode{
  public ArrayList<ASTNode> exprs = new ArrayList<>();
  @Override public Mypair check() throws Exception {
    for(ASTNode expr:exprs) {
      Mypair to_check = expr.check();
      if(to_check.dim != 0) {
        throw new Exception("Invalid dimension in fstring.");
      }
      if(to_check.type != "bool" && to_check.type != "int" && to_check.type != "string") {
        throw new Exception("Invalid type in fstring.");
      }
    }
    return new Mypair(type, 0);
  }
}
