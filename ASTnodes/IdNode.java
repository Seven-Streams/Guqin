package ASTnodes;

import Composer.*;
import IRSentence.IRLoad;
import IRSentence.TypeNamePair;
public class IdNode extends ExprNode {
  public String id = null;
  public ExprNode from = null;

  @Override
  public Mypair check() throws Exception {
    is_left = true;
    for (int i = variable_memory.size() - 1; i >= 0; i--) {
      if (variable_memory.get(i).containsKey(id)) {
        Mypair res = new Mypair(variable_memory.get(i).get(id).type, variable_memory.get(i).get(id).dim);
        dim = res.dim;
        return res;
      }
    }
    if(func_return.containsKey(id)) {
      Mypair res = new Mypair(func_return.get(id).type, func_return.get(id).dim);
      return res;
    }
    throw new Exception("Undeclared variable.");
  }

  @Override
  public Info GenerateIR(Composer machine) {
    IRLoad res = new IRLoad();
    String target_reg = "%" + Integer.toString(++machine.tmp_time);
    for(int i = machine.now_name.size() - 1; i >= 0; i--) {
      if(machine.now_name.get(i).containsKey(id)) {
        TypeNamePair to_check = machine.now_name.get(i).get(id);
        res.src = new String(to_check.new_name);
        if(to_check.dim != 0) {
          res.type = "ptr";
        } else {
          switch (to_check.type) {
            case "int":{
              res.type = "i32";
              break;
            }
            case "bool":{
              res.type = "i1";
              break;
            }
            default: {
              res.type = "ptr";
              break;
            }
          }
        }
        break;
      }
    }
    Info return_value = new Info();
    machine.generated.add(res);
    return_value.reg = target_reg;
    return return_value;
  }
}
