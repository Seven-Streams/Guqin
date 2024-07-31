package nodes;

public class ASTNode {
  public int dim = 0;
  public String type = null;
  public int GetInt() {
    return dim;
  }
  public String GetType() {
    if(type == null) {
      return "";
    }
    return type;
  }
}
