package nodes;

import java.util.ArrayList;

public class InnerNode extends ExprNode {
  public String name = null;
  public ArrayList<ASTNode> args = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    if (name == "ord") {
      Mypair to_check = args.get(0).check();
      if (to_check.type != "string") {
        throw new Exception("Invalid type in string function.");
      }
      if (to_check.dim != 0) {
        throw new Exception("Invalid dimension in string function.");
      }
      to_check = args.get(1).check();
      if (to_check.type != "int") {
        throw new Exception("Invalid type in string function.");
      }
      if (to_check.dim != 0) {
        throw new Exception("Invalid dimension in string function.");
      }
      return new Mypair("int", 0);
    }
    if (name == "length" || name == "parseint") {
      Mypair to_check = args.get(0).check();
      if (to_check.type != "string") {
        throw new Exception("Invalid type in string function.");
      }
      if (to_check.dim != 0) {
        throw new Exception("Invalid dimension in string function.");
      }
      return new Mypair("int", 0);
    }
    if (name == "substring") {
      Mypair to_check = args.get(0).check();
      if (to_check.type != "string") {
        throw new Exception("Invalid type in string function.");
      }
      if (to_check.dim != 0) {
        throw new Exception("Invalid dimension in string function.");
      }
      to_check = args.get(1).check();
      if (to_check.type != "int") {
        throw new Exception("Invalid type in string function.");
      }
      if (to_check.dim != 0) {
        throw new Exception("Invalid dimension in string function.");
      }
      to_check = args.get(2).check();
      if (to_check.type != "int") {
        throw new Exception("Invalid type in string function.");
      }
      if (to_check.dim != 0) {
        throw new Exception("Invalid dimension in string function.");
      }
      return new Mypair("string", 0);
    }
    throw new Exception("Invalid string method.");
  }
}
