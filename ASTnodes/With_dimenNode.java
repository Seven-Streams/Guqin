package ASTnodes;

import java.util.ArrayList;

import Composer.*;
import IRSentence.IRElement;
import IRSentence.IRLoad;

public class With_dimenNode extends ExprNode {
  public DimensionNode dim_node = null;
  public ExprNode ex = null;
  public int ex_dim = 0;

  @Override
  public Mypair check() throws Exception {
    Mypair ex_pair = ex.check();
    Mypair dim_pair = dim_node.check();
    type = new String(ex_pair.type);
    ex_dim = ex_pair.dim;
    dim = ex_pair.dim - dim_pair.dim;
    if (ex_pair.dim < dim_pair.dim) {
      throw new Exception("Dimension Out Of Bound");
    }
    is_left = ex.is_left;
    return new Mypair(ex_pair.type, ex_pair.dim - dim_pair.dim);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info from = ex.GenerateIR(machine);
    ArrayList<String> dim_list = new ArrayList<>();
    DimensionNode value_check = (DimensionNode) dim_node;
    for (int i = 0; i < ex_dim; i++) {
      if (value_check.dim_expr.containsKey(i)) {
        Info res = value_check.dim_expr.get(i).GenerateIR(machine);
        dim_list.add(res.reg);
      } else {
        break;
      }
    }
    String last = from.reg;
    for (int i = 0; i < (dim_list.size() - 1); i++) {
      String tmp = new String("%reg$" + Integer.toString(++machine.tmp_time));
      IRElement ele = new IRElement();
      ele.now_type = "ptr";
      ele.num1 = dim_list.get(i);
      ele.src = new String(last);
      ele.output = tmp;
      machine.generated.add(ele);
      IRLoad res = new IRLoad();
      String to_load = new String("%reg$" + Integer.toString(++machine.tmp_time));
      res.des = to_load;
      res.src = tmp;
      res.type = "ptr";
      machine.generated.add(res);
      last = to_load;
    }
    String tmp = new String("%reg$" + Integer.toString(++machine.tmp_time));
    IRElement ele = new IRElement();
    if (dim == 0) {
      switch (type) {
        case "int": {
          ele.now_type = "i32";
          break;
        }
        case "bool": {
          ele.now_type = "i1";
          break;
        }

        default: {
          ele.now_type = "ptr";
          break;
        }
      }
    } else {
      ele.now_type = "ptr";
    }
    ele.num1 = dim_list.get(dim_list.size() - 1);
    ele.src = new String(last);
    ele.output = tmp;
    machine.generated.add(ele);
    last = tmp;
    IRLoad load = new IRLoad();
    String output = new String("%reg$" + Integer.toString(++machine.tmp_time));
    load.des = output;
    load.src = last;
    if (dim == 0) {
      switch (type) {
        case "int": {
          load.type = "i32";
          break;
        }
        case "bool": {
          load.type = "i1";
          break;
        }

        default: {
          load.type = "ptr";
          break;
        }
      }
    } else {
      load.type = "ptr";
    }
    machine.generated.add(load);
    Info return_info = new Info();
    return_info.reg = new String(output);
    return return_info;
  }

  @Override
  public Info GetLeftValuePtr(Composer machine) {
    Info from = ex.GenerateIR(machine);
    ArrayList<String> dim_list = new ArrayList<>();
    DimensionNode value_check = (DimensionNode) dim_node;
    for (int i = 0; i < ex_dim; i++) {
      if (value_check.dim_expr.containsKey(i)) {
        Info res = value_check.dim_expr.get(i).GenerateIR(machine);
        dim_list.add(res.reg);
      } else {
        break;
      }
    }
    String last = from.reg;
    for (int i = 0; i < (dim_list.size() - 1); i++) {
      String tmp = new String("%reg$" + Integer.toString(++machine.tmp_time));
      IRElement ele = new IRElement();
      ele.now_type = "ptr";
      ele.num1 = dim_list.get(i);
      ele.src = new String(last);
      ele.output = tmp;
      machine.generated.add(ele);
      last = tmp;
      IRLoad res = new IRLoad();
      String to_load = new String("%reg$" + Integer.toString(++machine.tmp_time));
      res.des = to_load;
      res.src = tmp;
      res.type = "ptr";
      machine.generated.add(res);
      last = to_load;
    }
    String tmp = new String("%reg$" + Integer.toString(++machine.tmp_time));
    IRElement ele = new IRElement();
    if (dim == 0) {
      switch (type) {
        case "int": {
          ele.now_type = "i32";
          break;
        }
        case "bool": {
          ele.now_type = "i1";
          break;
        }

        default: {
          ele.now_type = "ptr";
          break;
        }
      }
    } else {
      ele.now_type = "ptr";
    }
    ele.num1 = dim_list.get(dim_list.size() - 1);
    ele.src = new String(last);
    ele.output = tmp;
    machine.generated.add(ele);
    Info return_info = new Info();
    return_info.reg = new String(ele.output);
    return return_info;
  }
}
