import java.util.ArrayList;
import java.util.HashMap;
import IRSentence.*;
import nodes.*;

public class Translator {
  public int scope_time = 0;
  public int tmp_time = 0;
  public int label_number = 0;
  public ArrayList<IRCode> Initial = new ArrayList<>();
  public ArrayList<IRCode> generated = new ArrayList<>();
  public HashMap<String, String> now_name = new HashMap<>();
  // It's used to check now_name of the variable.
  public HashMap<String, HashMap<String, Integer>> class_mem_number = new HashMap<>();

  // It's used to give each member in a class a number to visit.
  public void translate(ProgNode entry) {
    scope_time = 0;
    tmp_time = 0;
    label_number = 0;
    generated.clear();
    now_name.clear();
    for (ASTNode tree : entry.trees) {
      if (tree instanceof DeclarNode) {
        DeclarNode declar = (DeclarNode) tree;
        for (int i = 0; i < declar.ID.size(); i++) {
          IRGlobal res = new IRGlobal();
          res.name = new String(declar.ID.get(i));
          if (declar.dim != 0) {
            res.type = "ptr";
          } else {
            switch (declar.type) {
              case "int": {
                res.type = "i32";
                break;
              }
              case "bool": {
                res.type = "i1";
                break;
              }
              default: {
                res.type = "ptr";
                break;
              }
            }
          }
        }
      }
    }
  }
}
