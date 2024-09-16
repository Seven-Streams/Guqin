package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;
import Composer.*;
import IRSentence.IRCode;
import IRSentence.IRGlobal;
import IRSentence.IRAlloc;
import IRSentence.IRStore;
import IRSentence.TypeNamePair;

public class DeclarNode extends StatNode {
  public boolean is_global = false;
  public String type = null;
  public ASTNode dim_number = null;
  public ArrayList<String> ID = new ArrayList<>();
  public HashMap<Integer, ASTNode> Initial = new HashMap<>();

  @Override
  public Mypair check() throws Exception {
    Mypair resMypair = new Mypair(type, dim);
    if (!class_memory.containsKey(type)) {
      if (type.equals("void")) {
        throw new Exception("Invalid Type");
      }
      throw new Exception("Undefined Identifier");
    }
    dim_number.check();
    int cnt = 0;
    for (String id : ID) {
      if (variable_memory.get(variable_memory.size() - 1).containsKey(id)) {
        throw new Exception("Multiple Definitions");
      }
      if (func_return.containsKey(id)) {
        throw new Exception("Multiple Definitions");
      }
      if (Initial.containsKey(cnt)) {
        Mypair res = Initial.get(cnt).check();
        if (!res.type.equals("null")) {
          if (!resMypair.type.equals(res.type) && (!res.type.equals(""))) {
            throw new Exception("Type Mismatch");
          }
          if ((resMypair.dim != res.dim) && (!res.type.equals(""))) {
            throw new Exception("Type Mismatch");
          }
        } else {
          if (resMypair.dim == 0) {
            if (resMypair.type.equals("string") || resMypair.type.equals("int") || resMypair.type.equals("bool")) {
              throw new Exception("Type Mismatch");
            }
          }
        }
      }
      variable_memory.get(variable_memory.size() - 1).put(id, resMypair);
      cnt++;
    }
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    String llvm_type = null;
    if (dim != 0) {
      llvm_type = "ptr";
    } else {
      switch (type) {
        case ("int"): {
          llvm_type = "i32";
          break;
        }
        case ("bool"): {
          llvm_type = "i1";
          break;
        }
        default: {
          llvm_type = "ptr";
          break;
        }
      }
    }
    if (is_global) {
      ArrayList<IRCode> res_codes = machine.generated;
      for (int i = 0; i < ID.size(); i++) {
        TypeNamePair res = new TypeNamePair();
        res.type = type;
        res.dim = dim;
        res.new_name = "@" + ID.get(i) + "." + Integer.toString(machine.scope_time);
        machine.now_name.peek().put(ID.get(i), res);
        IRGlobal to_add = new IRGlobal();
        to_add.name = res.new_name;
        if (dim != 0) {
          to_add.type = "ptr";
        } else {
          switch (type) {
            case "int": {
              to_add.type = "i32";
              break;
            }
            case "bool": {
              to_add.type = "i1";
              break;
            }
            default: {
              to_add.type = "ptr";
              break;
            }
          }
        }
        if (Initial.containsKey(i)) {
          machine.generated = machine.init;
          Info init_info = Initial.get(i).GenerateIR(machine);
          if (init_info.is_const) {
            if (to_add.type.equals("i32")) {
              to_add.value = init_info.reg;
            } else {
              to_add.value = Integer.toString(init_info.reg.equals("true") ? 1 : 0);
            }
          } else {
            IRStore to_store = new IRStore();
            to_store.name = res.new_name;
            to_store.from = init_info.reg;
            to_store.type = llvm_type;
            machine.generated.add(to_store);
          }
          machine.generated = res_codes;
        }
        machine.global.add(to_add);
      }
    } else {
      for (int i = 0; i < ID.size(); i++) {
        TypeNamePair res = new TypeNamePair();
        res.type = type;
        res.dim = dim;
        res.new_name = "%" + ID.get(i) + "." + Integer.toString(machine.scope_time);
        machine.now_name.peek().put(ID.get(i), res);
        IRAlloc to_add = new IRAlloc();
        to_add.des = res.new_name;
        if (dim != 0) {
          to_add.type = "ptr";
        } else {
          switch (type) {
            case "int": {
              to_add.type = "i32";
              break;
            }
            case "bool": {
              to_add.type = "i1";
              break;
            }
            default: {
              to_add.type = "ptr";
              break;
            }
          }
        }
        machine.alloc.get(machine.func_name).add(to_add);
        if (Initial.containsKey(i)) {
          Info init_info = Initial.get(i).GenerateIR(machine);
          IRStore to_store = new IRStore();
          to_store.name = res.new_name;
          to_store.from = init_info.reg;
          to_store.type = llvm_type;
          machine.generated.add(to_store);
        }
      }
    }
    return new Info();
  }
  // In declarstat, the declare dimensions shouldn't have anything.
}
