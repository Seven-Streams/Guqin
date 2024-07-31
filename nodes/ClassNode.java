package nodes;

import java.util.ArrayList;

public class ClassNode {
  public String name = null;
  public ArrayList<ASTNode> member = new ArrayList<>();
  public FuncNode construct = null;
  public ArrayList<ASTNode> funcs = new ArrayList<>();
}
