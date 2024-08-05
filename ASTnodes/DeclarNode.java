package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclarNode extends StatNode {
  public boolean is_global = false;
  public String type = null;
  public ASTNode dim_number = null;
  public ArrayList<String> ID = new ArrayList<>();
  public HashMap<Integer, ASTNode> Initial = new HashMap<>();

  @Override
  public Mypair check() throws Exception {
    Mypair resMypair = new Mypair(type, dim);
    if (!class_memory.containsKey(type)) {
      throw new Exception("The class doesn't exist!");
    }
    dim_number.check();
    int cnt = 1;
    for (String id : ID) {
      if (variable_memory.get(variable_memory.size() - 1).containsKey(id)) {
        throw new Exception("The same name of variables");
      }
      if (func_return.containsKey(id)) {
        throw new Exception("The same name of variable and fuc.");
      }
      if (Initial.containsKey(cnt)) {
        Mypair res = Initial.get(cnt).check();
        if (!res.type.equals("null")) {
          if (!resMypair.type.equals(res.type)) {
            throw new Exception("Invalid type in initialization.");
          }
          if (resMypair.dim != res.dim) {
            throw new Exception("Invalid dimension in initialization.");
          }
        } else {
          if (resMypair.dim == 0) {
            if (resMypair.type.equals("string") || resMypair.type.equals("int") || resMypair.type.equals("bool")) {
              throw new Exception("Inner type can't be assigned with null!");
            }
          }
        }
      }
      variable_memory.get(variable_memory.size() - 1).put(id, resMypair);
      cnt++;
    }
    return new Mypair();
  }
}
