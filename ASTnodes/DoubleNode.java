package ASTnodes;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRBin;
import IRSentence.IRIcmp;
import IRSentence.IRLabel;
import IRSentence.IRjmp;

public class DoubleNode extends ExprNode {
  public String symbol = null;
  public ASTNode value1 = null;
  public ASTNode value2 = null;

  @Override
  public Mypair check() throws Exception {
    is_left = false;
    Mypair type1 = value1.check();
    Mypair type2 = value2.check();
    if ((!type1.type.equals(type2.type)) && (!type1.type.equals("null")) && (!type2.type.equals("null"))) {
      throw new Exception("Different type in two-arg expression.");
    }
    if ((type1.dim != type2.dim) && (!type1.type.equals("null")) && (!type2.type.equals("null"))) {
      throw new Exception("Different dimension in two-arg expression.");
    }
    if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%") || symbol.equals("-") || symbol.equals("<<")
        || symbol.equals(">>") || symbol.equals("&") || symbol.equals("^") || symbol.equals("|")) {
      if (!type1.type.equals("int")) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      return new Mypair("int", 0);
    }
    if (symbol.equals("+")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      if ((!type1.type.equals("int")) && (!type1.type.equals("string"))) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      return new Mypair(type1.type, 0);
    }
    if (symbol.equals("<") || symbol.equals(">") || symbol.equals("<=") || symbol.equals(">=")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      if ((!type1.type.equals("int")) && (!type1.type.equals("string"))) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      return new Mypair("bool", 0);
    }
    if (symbol.equals("&&") || symbol.equals("||")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid dimension in two-arg expression.");
      }
      if (!type1.type.equals("bool")) {
        throw new Exception("Invalid type in two-arg expression.");
      }
      return new Mypair("bool", 0);
    }
    if (symbol.equals("==") || symbol.equals("!=")) {
      return new Mypair("bool", 0);
    }
    throw new Exception("Invalid Sysmbol.");
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info return_value = new Info();
    if (type.equals("int")) {
      int tmp = ++machine.tmp_time;
      String target_reg = "%" + Integer.toString(tmp);
      Info info1 = value1.GenerateIR(machine);
      Info info2 = value2.GenerateIR(machine);
      if (symbol.equals("==") || symbol.equals("!=") || symbol.equals("<") || symbol.equals(">") || symbol.equals("<=")
          || symbol.equals(">=")) {
        IRIcmp res = new IRIcmp();
        res.target_reg = target_reg;
        res.op1 = info1.reg;
        res.op2 = info2.reg;
        res.symbol = symbol;
        res.type = "i32";
        machine.generated.add(res);
        return_value.reg = target_reg;
        return return_value;
      } else {
        IRBin res = new IRBin();
        res.target_reg = target_reg;
        res.op1 = info1.reg;
        res.op2 = info2.reg;
        res.symbol = symbol;
        res.type = "i32";
        machine.generated.add(res);
        return_value.reg = target_reg;
        return return_value;
      }
    }
    if (type == "bool") {
      if (symbol.equals("==") || symbol.equals("!=")) {
        int tmp = ++machine.tmp_time;
        String target_reg = "%" + Integer.toString(tmp);
        IRBin res = new IRBin();
        Info info1 = value1.GenerateIR(machine);
        Info info2 = value2.GenerateIR(machine);
        res.op1 = info1.reg;
        res.op2 = info2.reg;
        res.symbol = symbol;
        res.type = "i1";
        machine.generated.add(res);
        return_value.reg = target_reg;
        return return_value;
      }
      if(symbol.equals("||")) {
        //TODO:Finish the part of bool expression.
      }
    }
  }
}
