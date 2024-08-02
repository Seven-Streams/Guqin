package nodes;

public class DoubleNode extends ExprNode {
  public String symbol = null;
  public ASTNode value1 = null;
  public ASTNode value2 = null;

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    Mypair type1 = value1.check();
    Mypair type2 = value2.check();
    if (!type1.type.equals(type2.type)) {
      System.out.print(symbol);
      System.out.println(type1.type);
      System.out.println(type2.type);
      throw new Exception("Different type in two-arg expression.");
    }
    if (type1.dim != type2.dim) {
      throw new Exception("Different dimension in two-arg expression.");
    }
    if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%") || symbol.equals("-") || symbol.equals("<<")
        || symbol.equals(">>") || symbol.equals("&") || symbol.equals("^") || symbol.equals("|")) {
      if (!type1.type.equals("int")) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      return new Mypair("int", 0);
    }
    if (symbol.equals("+")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      if ((!type1.type.equals("int")) && (!type1.type.equals("string"))) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      return new Mypair(type1.type, 0);
    }
    if (symbol.equals("<") || symbol.equals(">") || symbol.equals("<=") || symbol.equals(">=")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      if ((!type1.type.equals("int")) && (!type1.type.equals("string"))) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      return new Mypair("bool", 0);
    }
    if (symbol.equals("&&") || symbol.equals("||")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      if (!type1.type.equals("bool")) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      return new Mypair("bool", 0);
    }
    if(symbol.equals("==") || symbol.equals("!=")) {
      return new Mypair("bool", 0);
    }
    throw new Exception("Invalid Sysmbol.");
  }
}
