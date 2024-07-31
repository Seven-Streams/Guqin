package nodes;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclarNode extends StatNode{
  public String type = null;
  int dim = 0;
  public ArrayList<ASTNode> dim_number = new ArrayList<>();
  public ArrayList<String> ID = new ArrayList<>();
  public HashMap<String, ASTNode> Initial = new HashMap<>();
}
