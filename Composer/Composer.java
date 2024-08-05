package Composer;

import java.util.ArrayList;
import java.util.HashMap;

import ASTnodes.*;
import IRSentence.*;
import Visitor.ASTVisitor;

public class Composer {
  public int scope_time = 0;
  public int tmp_time = 0;
  public int label_number = 0;
  public ASTVisitor from = null;
  public ArrayList<IRCode> Initial = new ArrayList<>();
  public ArrayList<IRCode> generated = new ArrayList<>();
  public HashMap<String, String> now_name = new HashMap<>();
  // It's used to check now_name of the variable.
  public HashMap<String, HashMap<String, Integer>> class_mem_number = new HashMap<>();

  // It's used to give each member in a class a number to visit.
  public Composer(ASTVisitor _src) {
    from = _src;
  }

  public void translate(ProgNode entry) {
    scope_time = 0;
    tmp_time = 0;
    label_number = 0;
    generated.clear();
    now_name.clear();
    for (ASTNode tree : entry.trees) {
      if (tree instanceof DeclarNode) {
        DeclarNode declar = (DeclarNode) tree;
        for (int i = 0; i < declar.ID.size(); i++) {
          IRGlobal res = new IRGlobal();
          res.name = new String(declar.ID.get(i));
          if (declar.dim != 0) {
            res.type = "ptr";
          } else {
            switch (declar.type) {
              case "int": {
                res.type = "i32";
                break;
              }
              case "bool": {
                res.type = "i1";
                break;
              }
              default: {
                res.type = "ptr";
                break;
              }
            }
          }
          if (declar.Initial.containsKey(i)) {
            // TODO:This part should call a function to add sentence to entry of the main
            // funcion.
          }
          generated.add(res);
        }
      }
      if (tree instanceof ClassNode) {
        IRClass res = new IRClass();
        ClassNode class_def = (ClassNode) (tree);
        HashMap<String, Integer> var_num = new HashMap<>();
        res.name = class_def.name;
        int cnt = 0;
        for (ASTNode mem : class_def.member) {
          if (!(mem instanceof FuncNode)) {
            DeclarNode dec_res = (DeclarNode) mem;
            for (String id : dec_res.ID) {
              var_num.put(id, cnt);
              cnt++;
              if (dec_res.dim != 0) {
                res.types.add("ptr");
              } else {
                switch (dec_res.type) {
                  case ("int"): {
                    res.types.add("i32");
                    break;
                  }
                  case ("bool"): {
                    res.types.add("i8");
                    break;
                  }
                  default:{
                    res.types.add("ptr");
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
