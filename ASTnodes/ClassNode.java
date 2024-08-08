package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Composer.*;
import IRSentence.IRAlloc;
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
    construction.put(name, null);
    HashMap<String, Mypair> res = class_memory.get(name);
    variable_memory.add(res);
    for (ASTNode dim : dims) {
      dim.check();
    }
    for (ASTNode mem : member) {
      if (mem instanceof FuncNode) {
        mem.check();
      }
    }
    variable_memory.remove(variable_memory.size() - 1);
    in_class = false;
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
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
    if(!construction.containsKey(name)) {
      IRFunc default_construct = new IRFunc();
      default_construct.return_type = "void";
      default_construct.name = name + "." + name;
      default_construct.types.add("ptr");
      default_construct.names.add("%0");
      machine.generated.add(default_construct);
      IRAlloc to_alloc = new IRAlloc();
      to_alloc.des = "%0";
      to_alloc.type = "%struct." + name;
      machine.generated.add(to_alloc);
      machine.generated.add(new IRFuncend());
    }
    for(ASTNode func: member) {
      if(func instanceof FuncNode) {
        func.GenerateIR(machine);
      }
    }
    machine.now_name.pop();
    in_class = false;
    return new Info();
  }
}
