package ASTnodes;

import java.util.HashMap;
import Composer.*;
import IRSentence.IRElement;
import IRSentence.IRLoad;

public class MemNode extends ExprNode {
  public ExprNode from = null;
  public String id = null;
  public Mypair from_type = null;
  @Override
  public Mypair check() throws Exception {
    Mypair to_check = from.check();
    is_left = true;
    if (to_check.dim != 0) {
      throw new Exception("Type Mismatch MEM");
    }
    HashMap<String, Mypair> resed = class_memory.get(to_check.type);
    Mypair res = new Mypair(resed.get(id).type, resed.get(id).dim);
    type = new String(res.type);
    dim = res.dim;
    from_type = new Mypair(to_check.type, to_check.dim);
    return res;
  }

  @Override
  public Info GenerateIR(Composer machine) {
    IRElement ele_res = new IRElement();
    String target_reg = new String("%reg$" + Integer.toString(++machine.tmp_time));
    ele_res.now_type = new String("%struct." + from_type.type);
    ele_res.output = target_reg;
    HashMap<String, Integer> num_check = machine.class_mem_num.get(from_type.type);
       ele_res.num2 = Integer.toString(num_check.get(id)) ;
    Info from_reg = from.GenerateIR(machine);
    ele_res.src = from_reg.reg;
    machine.generated.add(ele_res);
    String output_reg = new String("%reg$" + Integer.toString(++machine.tmp_time));
    IRLoad load_res = new IRLoad();
    load_res.des = new String(output_reg);
    load_res.src = target_reg;
    if (dim != 0) {
      load_res.type = "ptr";
    } else {
      switch (type) {
        case "int": {
          load_res.type = "i32";
          break;
        }
        case "bool": {
          load_res.type = "i1";
          break;
        }
        default: {
          load_res.type = "ptr";
          break;
        }
      }
    }
    machine.generated.add(load_res);
    Info return_value = new Info();
    return_value.reg = output_reg;
    return return_value;
  }

  @Override
  public Info GetLeftValuePtr(Composer machine) {
    IRElement ele_res = new IRElement();
    String target_reg = new String("%reg$" + Integer.toString(++machine.tmp_time));
    ele_res.now_type = new String("%struct." + from_type.type);
    ele_res.output = target_reg;
    HashMap<String, Integer> num_check = machine.class_mem_num.get(from_type.type);
    ele_res.num2 = Integer.toString(num_check.get(id)) ;
    Info from_reg = from.GenerateIR(machine);
    ele_res.src = from_reg.reg;
    machine.generated.add(ele_res);
    Info return_value = new Info();
    return_value.reg = target_reg;
    return return_value;
  }
}
