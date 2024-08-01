package nodes;

import java.util.HashMap;

public class FormatNode extends ExprNode {
  HashMap<Integer, String> words = new HashMap<>();
  HashMap<Integer, ASTNode> values = new HashMap<>();

  @Override
  Mypair check() throws Exception {
    is_left = false;
    for (int i = 0;; i++) {
      if ((!values.containsKey(i)) && (!words.containsKey(i))) {
        break;
      } else {
        if(values.containsKey(i)) {
          Mypair to_check = values.get(i).check();
          if(to_check.dim != 0) {
            throw new Exception("Invalid dimension in format string.");
          }
          if((!to_check.type.equals("int")) && (!to_check.type.equals("bool")) && (!to_check.equals("string"))) {
            throw new Exception("Invalid types in format string.");
          }
        }
      }
    }
    return new Mypair("string", 0);
  }
}
