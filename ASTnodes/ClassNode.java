package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Composer.*;
import IRSentence.IRClass;
import IRSentence.IRFunc;
import IRSentence.IRFuncend;
public class ClassNode extends ASTNode {
  public String name = null;
  public ArrayList<ASTNode> member = new ArrayList<>();
  public ArrayList<ASTNode> dims = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    in_class = true;
    this_class = name;
    HashMap<String, Mypair> res = class_memory.get(name);
    variable_memory.add(res);
    for (ASTNode dim : dims) {
      dim.check();
    }
    for (ASTNode mem : member) {
      if (mem instanceof FuncNode) {
        FuncNode check_con = (FuncNode) mem;
        if (check_con.is_construct) {
          construction.put(this_class, null);
        }
        mem.check();
      }
    }
    variable_memory.remove(variable_memory.size() - 1);
    in_class = false;
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    machine.scope_time++;
    in_class = true;
    this_class = name;
    IRClass res = new IRClass();
    res.name = new String("%struct." + name);
    HashMap<String, Mypair> res_map = class_memory.get(name);
    HashMap<String, Integer> res_num = new HashMap<>();
    int cnt = 0;
    for (Map.Entry<String, Mypair> entry : res_map.entrySet()) {
      String key = entry.getKey();
      Mypair value = entry.getValue();
      res_num.put(key, cnt++);
      if (value.dim != 0) {
        res.types.add("ptr");
      } else {
        switch (value.type) {
          case ("int"): {
            res.types.add("i32");
            break;
          }
          case ("bool"): {
            res.types.add("i1");
            break;
          }
          default: {
            res.types.add("ptr");
            break;
          }
        }
      }
    }
    machine.class_now_name.put(name, res.name);
    machine.class_mem_num.put(name, res_num);
    machine.now_name.add(null);
    machine.generated.add(res);
    if (!construction.containsKey(name)) {
      IRFunc default_construct = new IRFunc();
      default_construct.return_type = "void";
      default_construct.name = name + "." + name;
      default_construct.types.add("ptr");
      default_construct.names.add("%this");
      machine.generated.add(default_construct);
      machine.alloc.put(default_construct.name, new ArrayList<>());
      machine.generated.add(new IRFuncend());
    }
    for (ASTNode func : member) {
      if (func instanceof FuncNode) {
        func.GenerateIR(machine);
      }
    }
    machine.now_name.pop();
    in_class = false;
    machine.scope_time++;
    return new Info();
  }
}
