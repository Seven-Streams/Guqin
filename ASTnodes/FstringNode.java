package ASTnodes;

import java.util.ArrayList;
import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRFuncall;
import IRSentence.IRLabel;
import IRSentence.IRPhi;
import IRSentence.IRjmp;

public class FstringNode extends ExprNode {
  public ArrayList<ASTNode> exprs = new ArrayList<>();
  public ArrayList<String> types = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    for (ASTNode expr : exprs) {
      Mypair to_check = expr.check();
      types.add(to_check.type);
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      if (!to_check.type.equals("bool") && !to_check.type.equals("int") && !to_check.type.equals("string")) {
        throw new Exception("Type Mismatch");
      }
    }
    return new Mypair(type, 0);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    String res = null;
    for (int i = 0; i < exprs.size(); i++) {
      Info expr = exprs.get(i).GenerateIR(machine);
      switch (types.get(i)) {
        case ("int"): {
          String tmp = new String("%reg$" + Integer.toString(++machine.tmp_time));
          IRFuncall to_string = new IRFuncall();
          to_string.func_name = "toString";
          to_string.func_type = "ptr";
          to_string.target_reg = tmp;
          to_string.type.add("i32");
          to_string.reg.add(expr.reg);
          machine.generated.add(to_string);
          if (res != null) {
            IRFuncall cat = new IRFuncall();
            cat.func_name = "string_cat";
            cat.func_type = "ptr";
            cat.reg.add(new String(res));
            cat.reg.add(new String(tmp));
            cat.target_reg = new String("%reg$" + Integer.toString(++machine.tmp_time));
            cat.type.add("ptr");
            cat.type.add("ptr");
            res = new String(cat.target_reg);
            machine.generated.add(cat);
          } else {
            res = new String(tmp);
          }
          break;
        }
        case ("bool"): {
          String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
          String tmp2 = new String("%reg$" + Integer.toString(++machine.tmp_time));
          int label_true = ++machine.label_number;
          int label_false = ++machine.label_number;
          int end = ++machine.label_number;
          Conditionjmp check = new Conditionjmp(label_true, label_false, expr.reg);
          machine.generated.add(check);
          machine.generated.add(new IRLabel(label_true));
          if (res != null) {
            IRFuncall to_conn = new IRFuncall();
            to_conn.func_name = "string_cat";
            to_conn.func_type = "ptr";
            to_conn.reg.add(res);
            to_conn.reg.add("@.true");
            to_conn.target_reg = new String(tmp1);
            to_conn.type.add("ptr");
            to_conn.type.add("ptr");
            machine.generated.add(to_conn);
          } else {
            IRFuncall cpy = new IRFuncall();
            cpy.func_name = "string_copy";
            cpy.func_type = "ptr";
            cpy.type.add("ptr");
            cpy.reg.add("@.true");
            cpy.target_reg = new String(tmp1);
            machine.generated.add(cpy);
          }
          machine.generated.add(new IRjmp(end));
          machine.generated.add(new IRLabel(label_false));
          if (res != null) {
            IRFuncall to_conn1 = new IRFuncall();
            to_conn1.func_name = "string.cat";
            to_conn1.func_type = "ptr";
            to_conn1.reg.add(res);
            to_conn1.reg.add("@.false");
            to_conn1.target_reg = new String(tmp2);
            to_conn1.type.add("ptr");
            to_conn1.type.add("ptr");
            machine.generated.add(to_conn1);
          } else {
            IRFuncall cpy = new IRFuncall();
            cpy.func_name = "string_copy";
            cpy.func_type = "ptr";
            cpy.type.add("ptr");
            cpy.reg.add("@.false");
            cpy.target_reg = new String(tmp2);
            machine.generated.add(cpy);
          }
          machine.generated.add(new IRjmp(end));
          machine.generated.add(new IRLabel(end));
          IRPhi phi = new IRPhi();
          res = new String("%reg$" + Integer.toString(++machine.tmp_time));
          phi.labels.add(label_true);
          phi.labels.add(label_false);
          phi.values.add(new String(tmp1));
          phi.values.add(new String(tmp2));
          machine.generated.add(phi);
          break;
        }
        default: {
          if (res != null) {
            IRFuncall cat = new IRFuncall();
            cat.func_name = "string_cat";
            cat.func_type = "ptr";
            cat.reg.add(new String(res));
            cat.reg.add(new String(expr.reg));
            cat.target_reg = new String("%reg$" + Integer.toString(++machine.tmp_time));
            cat.type.add("ptr");
            cat.type.add("ptr");
            machine.generated.add(cat);
            res = new String(cat.target_reg);
          } else {
            res = new String(expr.reg);
          }
          break;
        }
      }
    }
    Info return_value = new Info();
    return_value.reg = new String(res);
    return return_value;
  }
}
