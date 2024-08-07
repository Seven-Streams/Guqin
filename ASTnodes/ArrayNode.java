package ASTnodes;

import java.util.ArrayList;

public class ArrayNode extends ASTNode {
  public ArrayList<ASTNode> elements = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    if (elements.size() == 0) {
      return new Mypair();
    }
    Mypair res = elements.get(0).check();
    for (ASTNode element : elements) {
      Mypair check = element.check();
      if (res.type.equals(check.type) || res.type.equals("") || check.type.equals("")) {
        if (res.type.equals("")) {
          res = check;
        }
      } else {
        throw new Exception("Invalid type in array.");
      }
      if(check.dim != 0) {
        dim = check.dim + 1;
      }
    }
    return new Mypair(res.type, dim);
  }
}