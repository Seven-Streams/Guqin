package nodes;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclarNode extends StatNode{
  public String type = null;
  public ArrayList<String> ID = new ArrayList<>();
  public HashMap<String, ExprNode> Initial = new HashMap<>();
}
