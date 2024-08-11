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
    System.out.println("declare void @print(ptr %a)\r\n" + //
        "declare void @printInt(i32 %b)\r\n" + //
        "declare void @println(ptr %a)\r\n" + //
        "declare void @printIntln(i32 %b)\r\n" + //
        "declare ptr @toString(i32 %a)\r\n" + //
        "declare i32 @getInt()\r\n" + //
        "declare ptr @MyNew(i32 %a)\r\n" + //
        "declare ptr @getString()\r\n" + //
        "declare i32 @string_length(ptr %a)\r\n" + //
        "declare ptr @string_substring(ptr %a, i32 %b)\r\n" + //
        "declare i32 @string_parseInt(ptr %a)\r\n" + //
        "declare i32 @string_ord(ptr %a, i32 %b)\r\n" + //
        "declare i32 @string_cmp(ptr %a, ptr %b)\r\n" + //
        "declare i32 @string_cat(ptr %a, ptr %b)\r\n" + //
        "declare ptr @ptr_array(i32 %a)\r\n" + //
        "declare ptr @int_array(i32 %a)\r\n" + //
        "declare i32 @array_size(ptr %a)\r\n" + //
        "declare ptr @string_copy(ptr %a)\r\n" + //
        "");
    String type = null;
    for (IRCode code : const_str) {
      code.CodePrint();
    }
    for (IRCode code : generated) {
      if (code instanceof IRFuncend) {
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
      code.CodePrint();
      if (code instanceof IRFunc) {
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
    }
    return;
  }
}
