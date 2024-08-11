package ASTnodes;

import java.util.ArrayList;
import java.util.HashMap;
import Composer.*;
import IRSentence.IRChararray;

public class ProgNode extends ASTNode {
  public ArrayList<ASTNode> trees = new ArrayList<>();

  @Override
  public Mypair check() throws Exception {
    if (variable_memory.isEmpty()) {
      variable_memory.add(new HashMap<>());
    }
    in_class = false;
    class_memory.put("int", null);
    class_memory.put("string", null);
    class_memory.put("bool", null);
    Mypair getstr = new Mypair("string", 0);
    Mypair getint = new Mypair("int", 0);
    Mypair tostr = new Mypair("string", 0);
    func_return.put("toString", tostr);
    func_return.put("getInt", getint);
    func_return.put("getString", getstr);
    func_return.put("size", getint);
    func_args.put("getString", new ArrayList<>());
    func_args.put("getInt", new ArrayList<>());
    ArrayList<Mypair> a_int = new ArrayList<Mypair>();
    a_int.add(new Mypair("int", 0));
    func_args.put("toString", a_int);
    for (ASTNode tree : trees) {
      if (tree instanceof ClassNode) {
        HashMap<String, Mypair> res_members = new HashMap<>();
        HashMap<String, Mypair> res_func_return = new HashMap<>();
        HashMap<String, ArrayList<Mypair>> res_func_arg = new HashMap<>();
        ClassNode res = (ClassNode) tree;
        String class_name = res.name;
        if (class_memory.containsKey(class_name)) {
          throw new Exception("Multiple Definitions");
        }
        if (func_return.containsKey(class_name)) {
          throw new Exception("Multiple Definitions");
        }
        for (ASTNode member : res.member) {
          if (member instanceof DeclarNode) {
            DeclarNode declar = (DeclarNode) member;
            String type = declar.type;
            int member_dim = declar.dim;
            Mypair mem_Mypair = new Mypair(type, member_dim);
            for (String id : declar.ID) {
              if (res_members.containsKey(id) || res_func_return.containsKey(id)) {
                throw new Exception("Multiple Definitions");
              }
              res_members.put(id, mem_Mypair);
            }
          }
          if (member instanceof FuncNode) {
            FuncNode func = (FuncNode) member;
            if (func.id.equals(class_name)) {
              if (!construction.containsKey(class_name)) {
                construction.put(class_name, null);
              }
            }
            String type = func.type;
            int res_dim = func.dim;
            Mypair func_Mypair = new Mypair(type, res_dim);
            if (res_func_return.containsKey(func.id) || res_members.containsKey(func.id)) {
              throw new Exception("Multiple Definitions");
            }
            res_func_return.put(func.id, func_Mypair);
            ArrayList<Mypair> res_args = new ArrayList<>();
            for (ASTNode arg : func.args) {
              res_args.add(new Mypair(arg.type, arg.dim));
            }
            res_func_arg.put(func.id, res_args);
          }
        }
        class_memory.put(class_name, res_members);
        class_func_return.put(class_name, res_func_return);
        class_func_args.put(class_name, res_func_arg);
      }
      if (tree instanceof FuncNode) {
        FuncNode func = (FuncNode) tree;
        String func_name = func.id;
        if (class_memory.containsKey(func_name)) {
          throw new Exception("Multiple Definitions");
        }
        if (func_return.containsKey(func_name)) {
          throw new Exception("Multiple Definitions");
        }
        String type = func.type;
        int res_dim = func.dim;
        Mypair func_Mypair = new Mypair(type, res_dim);
        func_return.put(func.id, func_Mypair);
        ArrayList<Mypair> res_args = new ArrayList<>();
        for (ASTNode arg : func.args) {
          res_args.add(new Mypair(arg.type, arg.dim));
        }
        func_args.put(func.id, res_args);
      }
    }
    // This part is to build the class and the functions.
    if (!func_return.containsKey("main")) {
      throw new Exception("No main function.");
    }
    if (func_args.get("main").size() != 0) {
      throw new Exception("The main function shouldn't have args!");
    }
    if (!func_return.get("main").type.equals("int")) {
      throw new Exception("Type Mismatch");
    }
    if (func_return.get("main").dim != 0) {
      throw new Exception("Type Mismatch");
    }
    for (ASTNode tree : trees) {
      tree.check();
    }
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    size_of_class.put("string", 4);
    size_of_class.put("int", 4);
    size_of_class.put("bool", 4);
    IRChararray to_add = new IRChararray();
    to_add.reg = new String("@.true");
    to_add.value = "true";
    to_add.size = 5;
    machine.const_str.add(to_add);
    IRChararray to_add1 = new IRChararray();
    to_add1.reg = new String("@.false");
    to_add1.value = "false";
    to_add1.size = 6;
    for (ASTNode tree : trees) {
      tree.GenerateIR(machine);
    }
    return new Info();
  }
}
