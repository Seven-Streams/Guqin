package ASTnodes;

import java.util.ArrayList;

import Composer.*;
import IRSentence.IRElement;
import IRSentence.IRLoad;

public class With_dimenNode extends ExprNode {
  public DimensionNode dim_node = null;
  public ExprNode ex = null;
  public int expr_dim = 0;

  @Override
  public Mypair check() throws Exception {
    Mypair ex_pair = ex.check();
    Mypair dim_pair = dim_node.check();
    expr_dim = ex_pair.dim;
    if (ex_pair.dim < dim_pair.dim) {
      throw new Exception("The dimension is out of range!");
    }
    is_left = ex.is_left;
    return new Mypair(ex_pair.type, ex_pair.dim - dim_pair.dim);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info from = ex.GenerateIR(machine);
    if (expr_dim > dim) {
      ArrayList<String> dim_list = new ArrayList<>();
      DimensionNode value_check = (DimensionNode) dim_node;
      for (int i = 0; i < dim; i++) {
        if (value_check.dim_expr.containsKey(i)) {
          Info res = value_check.dim_expr.get(i).GenerateIR(machine);
          dim_list.add(res.reg);
        } else {
          break;
        }
      }
      String last = from.reg;
      for (int i = 0; i < dim_list.size(); i++) {
        String tmp = new String("%reg" + Integer.toString(++machine.tmp_time));
        IRElement ele = new IRElement();
        ele.now_type = "ptr";
        ele.num = dim_list.get(i);
        ele.src = new String(last);
        ele.output = tmp;
        machine.generated.add(ele);
        last = tmp;
      }
      Info return_info = new Info();
      return_info.reg = new String(last);
      return return_info;
    } else {
      ArrayList<String> dim_list = new ArrayList<>();
      DimensionNode value_check = (DimensionNode) dim_node;
      for (int i = 0; i < dim; i++) {
        if (value_check.dim_expr.containsKey(i)) {
          Info res = value_check.dim_expr.get(i).GenerateIR(machine);
          dim_list.add(res.reg);
        } else {
          break;
        }
      }
      String last = from.reg;
      for (int i = 0; i < dim_list.size(); i++) {
        String tmp = new String("%reg$" + Integer.toString(++machine.tmp_time));
        IRElement ele = new IRElement();
        ele.now_type = "ptr";
        ele.num = dim_list.get(i);
        ele.src = new String(last);
        ele.output = tmp;
        machine.generated.add(ele);
        last = tmp;
      }
      IRLoad load = new IRLoad();
      String output = new String("%reg$" + Integer.toString(++machine.tmp_time));
      load.des = output;
      load.src = last;
      load.type = type;
      machine.generated.add(load);
      Info return_info = new Info();
      return_info.reg = new String(output);
      return return_info;
    }
  }

  @Override public Info GetLeftValuePtr(Composer machine) {
        Info from = ex.GenerateIR(machine);
      ArrayList<String> dim_list = new ArrayList<>();
      DimensionNode value_check = (DimensionNode) dim_node;
      for (int i = 0; i < dim; i++) {
        if (value_check.dim_expr.containsKey(i)) {
          Info res = value_check.dim_expr.get(i).GenerateIR(machine);
          dim_list.add(res.reg);
        } else {
          break;
        }
      }
      String last = from.reg;
      for(int i = 0; i < dim_list.size(); i++) {
        String tmp = new String("%regs" + Integer.toString(++machine.tmp_time));
        IRElement ele = new IRElement();
        ele.now_type = "ptr";
        ele.num = dim_list.get(i);
        ele.src = new String(last);
        ele.output = tmp;
        machine.generated.add(ele);
        last = tmp;
      }
      Info return_info = new Info();
      return_info.reg = new String(last);
    return return_info;
  }
}
