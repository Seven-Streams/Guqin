package ASTnodes;

import Composer.*;
import IRSentence.IRBin;
import IRSentence.IRIcmp;
import IRSentence.IRStore;

public class SingleNode extends ExprNode {
  public String symbol = null;
  public ASTNode value = null;
  public boolean symbol_left = false;

  @Override
  public Mypair check() throws Exception {
    Mypair to_check = value.check();
    if (symbol.equals("!")) {
      if (!to_check.type.equals("bool")) {
        throw new Exception("Type Mismatch");
      }
      if (to_check.dim != 0) {
        throw new Exception("Type Mismatch");
      }
      is_left = false;
      return new Mypair("bool", 0);
    }
    if (((symbol.equals("++")) || (symbol.equals("--")))) {
      ExprNode real_value = (ExprNode) (value);
      if (!real_value.is_left) {
        throw new Exception("The value should be a left_value!");
      }
      is_left = symbol_left;
    }
    if (!to_check.type.equals("int")) {
      throw new Exception("Type Mismatch");
    }
    if (to_check.dim != 0) {
      throw new Exception("Type Mismatch");
    }
    return new Mypair("int", 0);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    String output = null;
    Info return_value = new Info();
    if (symbol.equals("!")) {
      output = ("%reg$" + ++machine.tmp_time);
      Info res_bool = value.GenerateIR(machine);
      if (res_bool.is_const) {
        return_value.is_const = true;
        return_value.reg = res_bool.reg.equals("true") ? "false" : "true";
        return return_value;
      }
      IRIcmp icmp = new IRIcmp();
      icmp.symbol = "!=";
      icmp.op1 = (res_bool.reg);
      icmp.op2 = ("1");
      icmp.type = "i1";
      icmp.target_reg = output;
      machine.generated.add(icmp);
    }
    if (symbol.equals("~")) {
      output = ("%reg$" + ++machine.tmp_time);
      Info res_int = value.GenerateIR(machine);
      if (res_int.is_const) {
        return_value.is_const = true;
        int to_operate = Integer.parseInt(res_int.reg);
        return_value.reg = Integer.toString(~to_operate);
        return return_value;
      }
      IRBin bin = new IRBin();
      bin.symbol = "^";
      bin.op1 = (res_int.reg);
      bin.op2 = "-1";
      bin.type = "i32";
      bin.target_reg = output;
      machine.generated.add(bin);
    }
    if (symbol.equals("-")) {
      output = ("%reg$" + ++machine.tmp_time);
      Info res_int = value.GenerateIR(machine);
      if (res_int.is_const) {
        return_value.is_const = true;
        int to_operate = Integer.parseInt(res_int.reg);
        return_value.reg = Integer.toString(-to_operate);
        return return_value;
      }
      IRBin bin = new IRBin();
      bin.symbol = "-";
      bin.op2 = (res_int.reg);
      bin.op1 = "0";
      bin.type = "i32";
      bin.target_reg = output;
      machine.generated.add(bin);
    }
    if (symbol.equals("++")) {
      Info res_int = value.GenerateIR(machine);
      Info get_addr = value.GetLeftValuePtr(machine);
      String add_result = ("%reg$" + ++machine.tmp_time);
      IRBin bin = new IRBin();
      bin.symbol = "+";
      bin.op1 = res_int.reg;
      bin.op2 = "1";
      bin.type = "i32";
      bin.target_reg = add_result;
      machine.generated.add(bin);
      IRStore st = new IRStore();
      st.name = (get_addr.reg);
      st.from = add_result;
      st.type = "i32";
      machine.generated.add(st);
      if (symbol_left) {
        output = (add_result);
      } else {
        output = (res_int.reg);
      }
    }
    if (symbol.equals("--")) {
      Info res_int = value.GenerateIR(machine);
      Info get_addr = value.GetLeftValuePtr(machine);
      String sub_result = ("%reg$" + ++machine.tmp_time);
      IRBin bin = new IRBin();
      bin.symbol = "-";
      bin.op1 = res_int.reg;
      bin.op2 = "1";
      bin.type = "i32";
      bin.target_reg = sub_result;
      machine.generated.add(bin);
      IRStore st = new IRStore();
      st.name = (get_addr.reg);
      st.from = sub_result;
      st.type = "i32";
      machine.generated.add(st);
      if (symbol_left) {
        output = (sub_result);
      } else {
        output = (res_int.reg);
      }
    }
    return_value.reg = output;
    return return_value;
  }

  @Override
  public Info GetLeftValuePtr(Composer machine) {
    return value.GetLeftValuePtr(machine);
  }
}
