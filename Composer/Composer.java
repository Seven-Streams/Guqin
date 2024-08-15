package Composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ASTnodes.*;
import IRSentence.*;
import Visitor.ASTVisitor;

public class Composer {
  public int tmp_time = 0;
  public int label_number = 0;
  public int scope_time = 0;
  public int str_time = 0;
  public ASTVisitor from = null;
  public String func_type = null;
  public String func_name = null;
  public HashMap<String, ArrayList<IRCode>> alloc = new HashMap<>();
  public ArrayList<IRCode> init = new ArrayList<>();
  public ArrayList<IRCode> generated = new ArrayList<>();
  public ArrayList<IRCode> const_str = new ArrayList<>();
  public HashMap<String, String> class_now_name = new HashMap<>();
  public HashMap<String, HashMap<String, Integer>> class_mem_num = new HashMap<>();
  public HashMap<String, Integer> now_class = new HashMap<>();
  public HashMap<Integer, Boolean> flag = new HashMap<>();
  public HashMap<Integer, ArrayList<IRPhi>> reserved_phi = new HashMap<>();
  // It's used to check now_name of the variable.
  public Stack<HashMap<String, TypeNamePair>> now_name = new Stack<>();

  public Composer(ASTVisitor _src) {
    from = _src;
  }

  public void translate(ProgNode entry) {
    for (Map.Entry<String, HashMap<String, Mypair>> mems : ASTNode.class_memory.entrySet()) {
      HashMap<String, Integer> to_add = new HashMap<>();
      int cnt = 0;
      if (mems.getKey().equals("string") || mems.getKey().equals("int") || mems.getKey().equals("bool")) {
        class_mem_num.put(mems.getKey(), to_add);
        continue;
      }
      for (Map.Entry<String, Mypair> mem : mems.getValue().entrySet()) {
        to_add.put(mem.getKey(), cnt++);
      }
      class_mem_num.put(mems.getKey(), to_add);
    }
    tmp_time = 0;
    label_number = 0;
    generated.clear();
    now_name.clear();
    now_name.push(new HashMap<>());
    entry.GenerateIR(this);
    return;
  }

  public void LLVMOutput() {
    int func_cnt = 0;
    System.out.println("declare void @print(ptr %a)\r\n" +
        "declare void @printInt(i32 %b)\r\n" +
        "declare void @println(ptr %a)\r\n" +
        "declare void @printIntln(i32 %b)\r\n" +
        "declare ptr @toString(i32 %a)\r\n" +
        "declare i32 @getInt()\r\n" +
        "declare ptr @MyNew(i32 %a)\r\n" +
        "declare ptr @getString()\r\n" +
        "declare i32 @string_length(ptr %a)\r\n" +
        "declare ptr @string_substring(ptr %a, i32 %b)\r\n" +
        "declare i32 @string_parseInt(ptr %a)\r\n" +
        "declare i32 @string_ord(ptr %a, i32 %b)\r\n" +
        "declare i32 @string_cmp(ptr %a, ptr %b)\r\n" +
        "declare i32 @string_cat(ptr %a, ptr %b)\r\n" +
        "declare ptr @ptr_array(i32 %a)\r\n" +
        "declare ptr @int_array(i32 %a)\r\n" +
        "declare i32 @array_size(ptr %a)\r\n" +
        "declare ptr @string_copy(ptr %a)\r\n" +
        "");
    String type = null;
    for (IRCode code : const_str) {
      code.CodePrint();
    }
    boolean last_return = false;
    for (IRCode code : generated) {
      if ((code instanceof IRFuncend) && !last_return) {
        switch (type) {
          case ("i32"): {
            System.out.println("ret i32 0");
            break;
          }
          case ("i1"): {
            System.out.println("ret i1 true");
            break;
          }
          case ("void"): {
            System.out.println("ret void");
            break;
          }
          default: {
            System.out.println("ret ptr null");
            break;
          }
        }
      }
      if (code instanceof IRReturn) {
        last_return = true;
      } else {
        last_return = false;
      }
      code.CodePrint();
      if (code instanceof IRFunc) {
        System.out.println("b" + --func_cnt + ":");
        if (reserved_phi.containsKey(func_cnt)) {
          for (IRPhi phi : reserved_phi.get(func_cnt)) {
            phi.CodePrint();
          }
        }
        IRFunc main_check = (IRFunc) code;
        for (IRCode all : alloc.get(main_check.name)) {
          all.CodePrint();
        }
        type = main_check.return_type;
        if (main_check.name.equals("main")) {
          for (IRCode _init : init) {
            _init.CodePrint();
          }
        }
      }
      if (code instanceof IRLabel) {
        IRLabel label = (IRLabel) code;
        if (reserved_phi.containsKey(label.label)) {
          for (IRPhi phi : reserved_phi.get(label.label)) {
            phi.CodePrint();
          }
        }
      }
    }
    return;
  }

  public void Codegen() throws Exception {
    int init_num = 0;
    init_num += 2;
    for (IRCode IR : init) {
      if (IR instanceof IRFuncall) {
        IRFuncall to_check = (IRFuncall) IR;
        if (to_check.reg.size() > 8) {
          init_num += to_check.reg.size() - 8;
        }
      }
    }
    int last_size = 2;
    IRFunc last_func = null;
    for (IRCode IR : generated) {
      if (IR instanceof IRPhi) {
        IRPhi phi = (IRPhi) (IR);
        flag.put(phi.labels.get(0), true);
        flag.put(phi.labels.get(1), false);
      }
      if (IR instanceof IRFunc) {
        if (last_func != null) {
          if (last_func.name.equals("main")) {
            last_size += init_num;
          }
          last_size += alloc.get(last_func.name).size() * 2;
          last_func.size = last_size;
          last_size = 2;
        }
        last_func = (IRFunc) (IR);
      } else {
        if (IR instanceof IRClass) {
          continue;
        } else {
          if (IR instanceof IRFuncall) {
            IRFuncall to_check = (IRFuncall) IR;
            if (to_check.reg.size() > 8) {
              last_size += to_check.reg.size() - 8;
            } else {
              last_size++;
            }
          } else {
            last_size++;
          }
        }
      }
    }
    last_size += alloc.get(last_func.name).size() * 2;
    if (last_func.name.equals("main")) {
      last_size += init_num;
    }
    last_func.size = last_size;
    for (IRCode phi_check : generated) {
      if (phi_check instanceof IRLabel) {
        IRLabel to_check = (IRLabel) phi_check;
        if (flag.containsKey(to_check.label)) {
          if (flag.get(to_check.label)) {
            to_check.cond = 1;
          } else {
            to_check.cond = 0;
          }
        }
      }
    }
    System.out.println(".data");
    for (IRCode chars : const_str) {
      chars.Codegen();
    }
    for (IRCode global : generated) {
      if (global instanceof IRGlobal) {
        global.Codegen();
      }
    }
    System.out.println("");
    System.out.println(".text");
    System.out.println(".globl main");
    for (int i = 0; i < generated.size(); i++) {
      if (generated.get(i) instanceof IRGlobal) {
        continue;
      }
      generated.get(i).Codegen();
      if (generated.get(i) instanceof IRFunc) {
        IRFunc to_check = (IRFunc) (generated.get(i));
        if (to_check.name.equals("main")) {
          for (IRCode chars : const_str) {
            IRChararray to_init = (IRChararray) chars;
            to_init.Init();
          }
          for (IRCode ini : init) {
            ini.Codegen();
          }
        }
        for (IRCode space : alloc.get(to_check.name)) {
          space.Codegen();
        }
      }
    }
    return;
  }
}
