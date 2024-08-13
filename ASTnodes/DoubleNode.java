package ASTnodes;

import Composer.*;
import IRSentence.Conditionjmp;
import IRSentence.IRBin;
import IRSentence.IRFuncall;
import IRSentence.IRIcmp;
import IRSentence.IRLabel;
import IRSentence.IRPhi;
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
    dim = (type1.dim == 0)? (type2.dim) : (type1.dim);
    type = new String(type1.type);
    if ((!type1.type.equals(type2.type)) && (!type1.type.equals("null")) && (!type2.type.equals("null"))) {
      throw new Exception("Type Mismatch");
    }
    if ((type1.dim != type2.dim) && (!type1.type.equals("null")) && (!type2.type.equals("null"))) {
      throw new Exception("Type Mismatch");
    }
    if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%") || symbol.equals("-") || symbol.equals("<<")
        || symbol.equals(">>") || symbol.equals("&") || symbol.equals("^") || symbol.equals("|")) {
      if (!type1.type.equals("int")) {
        throw new Exception("Invalid Type");
      }
      if (type1.dim != 0) {
        throw new Exception("Invalid Type");
      }
      return new Mypair("int", 0);
    }
    if (symbol.equals("+")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid Type");
      }
      if ((!type1.type.equals("int")) && (!type1.type.equals("string"))) {
        throw new Exception("Invalid Type");
      }
      return new Mypair(type1.type, 0);
    }
    if (symbol.equals("<") || symbol.equals(">") || symbol.equals("<=") || symbol.equals(">=")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid Type");
      }
      if ((!type1.type.equals("int")) && (!type1.type.equals("string"))) {
        throw new Exception("Invalid Type");
      }
      return new Mypair("bool", 0);
    }
    if (symbol.equals("&&") || symbol.equals("||")) {
      if (type1.dim != 0) {
        throw new Exception("Invalid Type");
      }
      if (!type1.type.equals("bool")) {
        throw new Exception("Invalid Type");
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
    int tmp = ++machine.tmp_time;
    String target_reg = "%reg$" + Integer.toString(tmp);
    return_value.reg = target_reg;
    if (dim == 0) {
      if (type.equals("int")) {
        Info info1 = value1.GenerateIR(machine);
        Info info2 = value2.GenerateIR(machine);
        if (symbol.equals("==") || symbol.equals("!=") || symbol.equals("<") || symbol.equals(">")
            || symbol.equals("<=")
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
      if (type.equals("bool")) {
        if (symbol.equals("==") || symbol.equals("!=")) {
          IRBin res = new IRBin();
          Info info1 = value1.GenerateIR(machine);
          Info info2 = value2.GenerateIR(machine);
          res.op1 = info1.reg;
          res.op2 = info2.reg;
          res.symbol = symbol;
          res.type = "i1";
          machine.generated.add(res);
          res.target_reg = target_reg;
          return_value.reg = target_reg;
          return return_value;
        }
        if (symbol.equals("||")) {
          int br2 = ++machine.label_number;
          int let0 = ++machine.label_number;
          int let1 = ++machine.label_number;
          int end = ++machine.label_number;
          Info info1 = value1.GenerateIR(machine);
          Conditionjmp judge0 = new Conditionjmp(let1, br2, info1.reg);
          machine.generated.add(judge0);
          machine.generated.add(new IRLabel(br2));
          Info info2 = value2.GenerateIR(machine);
          Conditionjmp judge1 = new Conditionjmp(let1, let0, info2.reg);
          machine.generated.add(judge1);
          machine.generated.add(new IRLabel(let1));
          IRjmp jmp_to_end = new IRjmp(end);
          machine.generated.add(jmp_to_end);
          machine.generated.add(new IRLabel(let0));
          machine.generated.add(jmp_to_end);
          machine.generated.add(new IRLabel(end));
          IRPhi phi = new IRPhi();
          phi.type = "i1";
          phi.target = new String(return_value.reg);
          phi.labels.add(let0);
          phi.values.add("0");
          phi.labels.add(let1);
          phi.values.add("1");
          machine.generated.add(phi);
          return return_value;
        }
        if (symbol.equals("&&")) {
          int br2 = ++machine.label_number;
          int let0 = ++machine.label_number;
          int let1 = ++machine.label_number;
          int end = ++machine.label_number;
          Info info1 = value1.GenerateIR(machine);
          Conditionjmp judge0 = new Conditionjmp(br2, let0, info1.reg);
          machine.generated.add(judge0);
          machine.generated.add(new IRLabel(br2));
          Info info2 = value2.GenerateIR(machine);
          Conditionjmp judge1 = new Conditionjmp(let1, let0, info2.reg);
          machine.generated.add(judge1);
          machine.generated.add(new IRLabel(let1));
          IRjmp jmp_to_end = new IRjmp(end);
          machine.generated.add(jmp_to_end);
          machine.generated.add(new IRLabel(let0));
          machine.generated.add(jmp_to_end);
          machine.generated.add(new IRLabel(end));
          IRPhi phi = new IRPhi();
          phi.type = "i1";
          phi.target = new String(return_value.reg);
          phi.labels.add(let0);
          phi.values.add("0");
          phi.labels.add(let1);
          phi.values.add("1");
          machine.generated.add(phi);
          return return_value;
        }
      }
      if (type.equals("string")) {
        Info string1 = value1.GenerateIR(machine);
        Info string2 = value2.GenerateIR(machine);
        switch (symbol) {
          case ("=="): {
            IRFuncall func = new IRFuncall();
            String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
            func.target_reg = tmp1;
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.type.add("ptr");
            func.type.add("ptr");
            func.func_type = "i32";
            func.func_name = "string_cmp";
            machine.generated.add(func);
            IRIcmp res = new IRIcmp();
            res.op1 = tmp1;
            res.op2 = "0";
            res.symbol = "==";
            res.type = "i32";
            res.target_reg = new String(return_value.reg);
            machine.generated.add(res);
            return return_value;
          }
          case ("!="): {
            IRFuncall func = new IRFuncall();
            String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
            func.target_reg = tmp1;
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.type.add("ptr");
            func.type.add("ptr");
            func.func_type = "i32";
            func.func_name = "string_cmp";
            machine.generated.add(func);
            IRIcmp res = new IRIcmp();
            res.op1 = tmp1;
            res.op2 = "0";
            res.symbol = "!=";
            res.type = "i32";
            res.target_reg = new String(return_value.reg);
            machine.generated.add(res);
            return return_value;
          }
          case (">="): {
            IRFuncall func = new IRFuncall();
            String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
            func.target_reg = tmp1;
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.type.add("ptr");
            func.type.add("ptr");
            func.func_type = "i32";
            func.func_name = "string_cmp";
            machine.generated.add(func);
            IRIcmp res = new IRIcmp();
            res.op1 = tmp1;
            res.op2 = "0";
            res.symbol = ">=";
            res.type = "i32";
            res.target_reg = new String(return_value.reg);
            machine.generated.add(res);
            return return_value;
          }
          case ("<="): {
            IRFuncall func = new IRFuncall();
            String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
            func.target_reg = tmp1;
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.func_type = "i32";
            func.func_name = "string_cmp";
            func.type.add("ptr");
            func.type.add("ptr");
            machine.generated.add(func);
            IRIcmp res = new IRIcmp();
            res.op1 = tmp1;
            res.op2 = "0";
            res.symbol = "<=";
            res.type = "i32";
            res.target_reg = new String(return_value.reg);
            machine.generated.add(res);
            return return_value;
          }
          case (">"): {
            IRFuncall func = new IRFuncall();
            String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
            func.target_reg = tmp1;
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.func_type = "i32";
            func.func_name = "string_cmp";
            func.type.add("ptr");
            func.type.add("ptr");
            machine.generated.add(func);
            IRIcmp res = new IRIcmp();
            res.op1 = tmp1;
            res.op2 = "0";
            res.symbol = ">";
            res.type = "i32";
            res.target_reg = new String(return_value.reg);
            machine.generated.add(res);
            return return_value;
          }
          case ("<"): {
            IRFuncall func = new IRFuncall();
            String tmp1 = new String("%reg$" + Integer.toString(++machine.tmp_time));
            func.target_reg = tmp1;
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.type.add("ptr");
            func.type.add("ptr");
            func.func_type = "i32";
            func.func_name = "string_cmp";
            machine.generated.add(func);
            IRIcmp res = new IRIcmp();
            res.op1 = tmp1;
            res.op2 = "0";
            res.symbol = "<";
            res.type = "i32";
            res.target_reg = new String(return_value.reg);
            machine.generated.add(res);
            return return_value;
          }
          case ("+"): {
            IRFuncall func = new IRFuncall();
            func.target_reg = target_reg;
            func.func_name = "string_cat";
            func.func_type = "ptr";
            func.reg.add(string1.reg);
            func.reg.add(string2.reg);
            func.type.add("ptr");
            func.type.add("ptr");
            machine.generated.add(func);
            return return_value;
          }
          default: {
            System.out.println("Unexpected operator in String!");
            break;
          }
        }
      }
    }
    Info info1 = value1.GenerateIR(machine);
    Info info2 = value2.GenerateIR(machine);
    IRIcmp cmp = new IRIcmp();
    cmp.op1 = info1.reg;
    cmp.op2 = info2.reg;
    if (!(symbol.equals("==") || symbol.equals("!="))) {
      System.out.println(symbol);
      System.out.println("A surprising symbol!");
    }
    cmp.symbol = symbol;
    cmp.type = "ptr";
    cmp.target_reg = target_reg;
    machine.generated.add(cmp);
    return return_value;
  }
}
