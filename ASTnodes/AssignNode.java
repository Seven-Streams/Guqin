package ASTnodes;

import java.util.ArrayList;
import Composer.*;
import IRSentence.IRStore;
public class AssignNode extends ExprNode {
  public ArrayList<ASTNode> ids = new ArrayList<>();
  public ASTNode values = null;
  @Override
  public Mypair check() throws Exception {
    Mypair demanded = values.check();
    type = new String(demanded.type);
    dim = demanded.dim;
    Mypair output = null;
    for (ASTNode id : ids) {
      Mypair to_check = id.check();
      ExprNode node_check = (ExprNode) id;
      if (!node_check.is_left) {
        throw new Exception("The assigned value should be a left value!");
      }
      if (!demanded.type.equals("null")) {
        if (to_check.dim != demanded.dim) {
          throw new Exception("The assigned dimension is invalid!");
        }
        if (!to_check.type.equals(demanded.type)) {
          throw new Exception("The assigned type is invalid!");
        }
        output = to_check;
      } else {
        output = demanded;
        if (to_check.dim != 0) {
          continue;
        }
        if (to_check.type.equals("string") || to_check.type.equals("int") || to_check.type.equals("bool")) {
          throw new Exception("Inner type can't be assigned with null!");
        }
      }
    }
    return output;
  }

  @Override public Info GenerateIR(Composer machine) {
    Info value = values.GenerateIR(machine);
    String llvm_type = null;
    if(dim != 0) {
      llvm_type = "ptr";
    } else {
      switch (type) {
        case "int": {
          llvm_type = "i32";
          break;
        }
        case "bool": {
          llvm_type = "i1";
          break;
        }
        default: {
          llvm_type = "ptr";
          break;
        }
      }
    }
    for(ASTNode to_assign: ids) {
      Info be_assigned = to_assign.GetLeftValuePtr(machine);
      IRStore to_store = new IRStore();
      to_store.from = new String(value.reg);
      to_store.name = new String(be_assigned.reg);
      to_store.type = new String(llvm_type);
      machine.generated.add(to_store);
    }
    return new Info();
  }
}
