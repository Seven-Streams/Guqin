package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;
import Composer.*;
import IRSentence.IRAlloc;
import IRSentence.IRCode;
import IRSentence.IRFunc;
import IRSentence.IRFuncend;
import IRSentence.IRStore;
import IRSentence.TypeNamePair;

public class FuncNode extends ASTNode {
  public String id = null;
  public boolean is_construct;
  public boolean is_left = false;
  public ArrayList<ASTNode> args = new ArrayList<>();
  public ArrayList<ASTNode> stats = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    in_construct = is_construct;
    if (in_construct) {
      type = "void";
      dim = 0;
    }
    if (in_construct && (!id.equals(this_class))) {
      throw new Exception("Invalid construction function name!");
    }
    has_return = false;
    in_func = true;
    variable_memory.add(new HashMap<>());
    if (!is_construct) {
      if (in_class) {
        return_value = class_func_return.get(this_class).get(id);
      } else {
        return_value = func_return.get(id);
      }
    }
    for (ASTNode arg : args) {
      IdNode res_id = (IdNode) arg;
      if (!class_memory.containsKey(arg.type)) {
        throw new Exception("The type is invalid!");
      }
      if (variable_memory.get(variable_memory.size() - 1).containsKey(res_id.id)) {
        throw new Exception("Re defination in function args.");
      }
      variable_memory.get(variable_memory.size() - 1).put(res_id.id, new Mypair(res_id.type, res_id.dim));
    }
    for (ASTNode stat : stats) {
      stat.check();
    }
    variable_memory.remove(variable_memory.size() - 1);
    if ((!return_value.type.equals("void")) && (!has_return) && (!id.equals("main")) && (!is_construct)) {
      throw new Exception("Non-void function should have a return value!");
    }
    in_func = false;
    in_construct = false;
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    machine.scope_time++;
    in_func = true;
    IRFunc the_coooool_func = new IRFunc();
    ArrayList<Mypair> res_args;
    if (dim != 0) {
      the_coooool_func.return_type = "ptr";
    } else {
      switch (type) {
        case ("int"): {
          the_coooool_func.return_type = "i32";
          break;
        }
        case ("bool"): {
          the_coooool_func.return_type = "i1";
          break;
        }
        case ("void"): {
          the_coooool_func.return_type = "void";
          break;
        }
        default: {
          the_coooool_func.return_type = "ptr";
          break;
        }
      }
    }
    machine.func_type = new String(the_coooool_func.return_type);
    if (!in_class) {
      the_coooool_func.name = new String(id);
      res_args = func_args.get(id);
    } else {
      the_coooool_func.name = this_class + "." + id;
      the_coooool_func.types.add("ptr");
      the_coooool_func.names.add("%this");
      res_args = class_func_args.get(this_class).get(id);
    }
    machine.func_name = new String(the_coooool_func.name);
    machine.alloc.put(machine.func_name, new ArrayList<>());
    for (Mypair arg : res_args) {
      if (arg.dim != 0) {
        the_coooool_func.types.add("ptr");
      } else {
        switch (arg.type) {
          case ("int"): {
            the_coooool_func.types.add("i32");
            break;
          }
          case ("bool"): {
            the_coooool_func.types.add("i1");
            break;
          }
          default: {
            the_coooool_func.types.add("ptr");
            break;
          }
        }
      }
    }
    machine.now_name.add(new HashMap<>());
    ArrayList<IRCode> entry = new ArrayList<>();
    for (ASTNode arg : args) {
      IdNode arg_id = (IdNode) arg;
      String tmp_string = "%reg$" + Integer.toString(++machine.tmp_time);
      the_coooool_func.names.add(new String(tmp_string));
      TypeNamePair to_push = new TypeNamePair();
      String this_type = the_coooool_func.types.get(the_coooool_func.names.size() - 1);
      IRAlloc to_alloc = new IRAlloc();
      String alloc_string = "%reg$" + Integer.toString(++machine.tmp_time);
      to_alloc.type = new String(this_type);
      to_alloc.des = alloc_string;
      entry.add(to_alloc);
      IRStore to_store = new IRStore();
      to_store.from = tmp_string;
      to_store.name = alloc_string;
      to_store.type = this_type;
      entry.add(to_store);
      to_push.new_name = new String(alloc_string);
      to_push.dim = arg_id.dim;
      to_push.type = new String(arg_id.type);
      machine.now_name.peek().put(new String(arg_id.id), to_push);
    }
    machine.generated.add(the_coooool_func);
    for (IRCode ent : entry) {
      machine.generated.add(ent);
    }
    for (ASTNode stat : stats) {
      stat.GenerateIR(machine);
    }
    machine.now_name.pop();
    machine.generated.add(new IRFuncend());
    in_func = false;
    machine.scope_time++;
    return new Info();
  }
}
