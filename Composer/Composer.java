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
    for(ASTNode tree: entry.trees) {
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
