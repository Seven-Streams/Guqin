package Composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import ASTnodes.*;
import IRSentence.*;
import Visitor.ASTVisitor;

public class Composer {
  public int tmp_time = 0;
  public int label_number = 0;
  public int scope_time = 0;
  public ASTVisitor from = null;
  public String func_type = null;
  public ArrayList<IRCode> init = new ArrayList<>();
  public ArrayList<IRCode> generated = new ArrayList<>();
  public HashMap<String, Integer> now_class = new HashMap<>();
  // It's used to check now_name of the variable.
  public HashMap<String, HashMap<String, Integer>> class_func = new HashMap<>();
  public HashMap<String, HashMap<String, Integer>> class_mem_number = new HashMap<>();
  public Stack<HashMap<String, TypeNamePair>> now_name = new Stack<>();
  // It's used to give each member in a class a number to visit.
  public Composer(ASTVisitor _src) {
    from = _src;
  }

  public void translate(ProgNode entry) {
    tmp_time = 0;
    label_number = 0;
    generated.clear();
    now_name.clear();
    now_name.push(new HashMap<>());
    for (ASTNode tree : entry.trees) {
      tree.GenerateIR(this);
    }
    return;
  }

  public void IIVROutput() {
    for (IRCode code : generated) {
      code.CodePrint();
    }
    return;
  }
}
