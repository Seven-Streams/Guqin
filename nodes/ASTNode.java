package nodes;

import java.util.HashMap;
import java.util.ArrayList;

public class ASTNode {
  public int dim = 0;
  public String type = null;
  public static boolean in_construct;
  public static boolean return_left;
  public static boolean has_return = false;
  public static String this_class = null;
  public static boolean in_class = false;
  public static boolean in_func = false;
  public static int in_loop = 0;
  public static Mypair return_value = new Mypair();
  public static HashMap<String, Boolean> construction = new HashMap<>();
  public static HashMap<String, HashMap<String, Mypair>> class_memory = new HashMap<>();
  public static HashMap<String, HashMap<String, Mypair>> class_func_return = new HashMap<>();
  public static HashMap<String, HashMap<String, ArrayList<Mypair>>> class_func_args = new HashMap<>();
  public static HashMap<String, ArrayList<Mypair>> func_args = new HashMap<>();
  public static HashMap<String, Mypair> func_return = new HashMap<>();
  public static ArrayList<HashMap<String, Mypair>> variable_memory = new ArrayList<>();
  public static HashMap<String, Boolean> return_func_left = new HashMap<>();
  public Mypair check() throws Exception {
    return new Mypair(type, dim);
  }
}
