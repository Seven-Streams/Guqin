package ASTnodes;

import Composer.*;
import IRSentence.IRChararray;
import IRSentence.IRFuncall;
import IRSentence.IRLoad;

//To make the function of a class more clearly, let "this" be %0.
public class LiterNode extends ExprNode {
  public String value = null;

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    if (type == "this") {
      value = "this";
      if (!in_class) {
        throw new Exception("Invalid Identifier");
      }
      type = this_class;
      return new Mypair(this_class, 0);
    }
    return new Mypair(type, 0);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info res = new Info();
    if(value.equals("null")) {
      res.reg = "null";
      return res;
    }
    if (value.equals("this")) {
      res.reg = "%this";
      return res;
    }
    if (type.equals("bool") || type.equals("int")) {
      res.reg = new String(value);
      return res;
    }
    if (type.equals("string")) {
      IRChararray to_add = new IRChararray();
      to_add.reg = new String("@.str" + Integer.toString(++machine.str_time));
      to_add.value = new String(value);
      int str_size = 0;
      for (int i = 0; i < value.length(); i++, str_size++) {
        if (value.charAt(i) == '\\') {
          i++;
        }
      }
      to_add.size = ++str_size;
      machine.const_str.add(to_add);
      IRLoad to_load = new IRLoad();
      to_load.type = "ptr";
      to_load.des = new String("%reg$" + Integer.toString(++machine.tmp_time));
      to_load.src = new String(to_add.reg);
      machine.generated.add(to_load);
      res.reg = new String(to_load.des);
      return res;
    }
    System.out.println("INVALID IN LITER!");
    return new Info();
  }
}
