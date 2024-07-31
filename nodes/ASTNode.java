package nodes;
import java.util.HashMap;
import java.util.ArrayList;
public class ASTNode {
  public int dim = 0;
  public String type = null;
  public static boolean in_class = false;
  public static HashMap<String, Boolean> construction;
  public static HashMap<String, HashMap<String, Mypair>> class_memory = new HashMap<>();
	 public static HashMap<String, HashMap<String, Mypair>> class_func_return = new HashMap<>();
	 public static HashMap<String, HashMap<String, ArrayList<Mypair>>> class_func_args = new HashMap<>();
	 public static HashMap<String, ArrayList<Mypair>> func_args = new HashMap<>();
	 public static HashMap<String, Mypair> func_return = new HashMap<>();
	 public static ArrayList<HashMap<String, Mypair>> variable_memory = new ArrayList<>();
  public Mypair check() {
    return new Mypair();
  }
}
