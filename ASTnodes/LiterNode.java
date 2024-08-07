package ASTnodes;

import Composer.*;
import IRSentence.IRChararray;
import IRSentence.IRFuncall;

//To make the function of a class more clearly, let "this" be %0.
public class LiterNode extends ExprNode {
  public String value = null;

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    if (type == "this") {
      value = "this";
      if (!in_class) {
        throw new Exception("\" this \" isn't in a class.");
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
      res.reg = "%0";
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
      machine.const_str.add(to_add);
      IRFuncall to_call = new IRFuncall();
      to_call.func_name = ("string_copy");
      to_call.func_type = "ptr";
      to_call.target_reg = new String("%" + machine.tmp_time);
      to_call.reg.add(to_add.reg);
      to_call.type.add("ptr");
      machine.generated.add(to_call);
      res.reg = new String(to_call.target_reg);
      return res;
    }
    System.out.println("INVALID IN LITER!");
    return new Info();
  }
}
