package nodes;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclarNode extends StatNode{
  public String type = null;
  public ASTNode dim_number = null;
  public ArrayList<String> ID = new ArrayList<>();
  public HashMap<Integer, ASTNode> Initial = new HashMap<>();
}
