package ASTnodes;

import java.util.ArrayList;
import Composer.*;
import IRSentence.IRElement;
import IRSentence.IRFuncall;
import IRSentence.IRStore;

public class ArrayNode extends ASTNode {
  public ArrayList<ASTNode> elements = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    if (elements.size() == 0) {
      return new Mypair();
    }
    Mypair res = elements.get(0).check();
    for (ASTNode element : elements) {
      Mypair check = element.check();
      if (res.type.equals(check.type) || res.type.equals("") || check.type.equals("")) {
        if (res.type.equals("")) {
          res = check;
        }
      } else {
        throw new Exception("Invalid type in array.");
      }
      if (check.dim != 0) {
        dim = check.dim + 1;
      }
    }
    return new Mypair(res.type, dim);
  }

  @Override
  public Info GenerateIR(Composer machine) {
    Info return_value = new Info();
    ArrayList<String> to_build = new ArrayList<>();
    for (ASTNode element : elements) {
      Info to_res = element.GenerateIR(machine);
      to_build.add(to_res.reg);
    }
    if (to_build.isEmpty()) {
      return_value.reg = "null";
      return return_value;
    }
    String to_store = "%reg$" + Integer.toString(++machine.tmp_time);
    boolean is_ptr = true;
    if (elements.get(0) instanceof ArrayNode) {
      IRFuncall create = new IRFuncall();
      create.target_reg = to_store;
      create.type.add("i32");
      create.reg.add(Integer.toString(elements.size()));
      create.func_type = "ptr";
      create.func_name = "ptr_array";
      machine.generated.add(create);
    } else {
      if (type.equals("int") || type.equals("bool")) {
        is_ptr = false;
        IRFuncall create = new IRFuncall();
        create.target_reg = to_store;
        create.type.add("i32");
        create.reg.add(Integer.toString(elements.size()));
        create.func_type = "ptr";
        create.func_name = "int_array";
        machine.generated.add(create);
      } else {
        IRFuncall create = new IRFuncall();
        create.target_reg = to_store;
        create.type.add("i32");
        create.reg.add(Integer.toString(elements.size()));
        create.func_type = "ptr";
        create.func_name = "ptr_array";
        machine.generated.add(create);
      }
    }
    int cnt = 0;
    for (String init_value : to_build) {
      String store_tmp = "%reg$" + Integer.toString(++machine.tmp_time);
      IRStore ele_store = new IRStore();
      IRElement ele_get = new IRElement();
      if (is_ptr) {
        ele_get.now_type = "ptr";
        ele_store.type = "ptr";
      } else {
        if (type.equals("int")) {
          ele_get.now_type = "i32";
          ele_store.type = "i32";
        } else {
          ele_get.now_type = "i1";
          ele_store.type = "i1";
        }
      }
      ele_get.src = new String(to_store);
      ele_get.num = Integer.toString(cnt++);
      ele_get.output = new String(store_tmp);
      machine.generated.add(ele_get);
      ele_store.from = new String(init_value);
      ele_store.name = new String(store_tmp);
      machine.generated.add(ele_store);
    }
    return_value.reg = new String(to_store);
    return return_value;
  }
}