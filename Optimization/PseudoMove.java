package Optimization;

import java.util.*;

import Composer.Composer;
import IRSentence.*;

public class PseudoMove extends IRCode {
  public String src = null;
  public String des = null;

  public PseudoMove(String _src, String _des) {
    src = new String(_src);
    des = new String(_des);
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    def.put(des, null);
    try {
      Integer.parseInt(src);
    } catch (NumberFormatException e) {
      if (CheckLit(src)) {
        if (!def.containsKey(src)) {
          use.put(src, null);
        }
      } else {
        if (src.equals("true")) {
          src = "1";
        } else {
          src = "0";
        }
      }
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    try {
      int src_value = Integer.parseInt(src);
      int des_value = registers.get(des);
      String target = "t0";
      if (des_value >= 0) {
        target = register_name.get(des_value);
      }
      System.out.println("li " + target + ", " + src_value);
      if (des_value < 0) {
        if ((des_value >> 9) == 0) {
          System.out.println("sw t0, " + (des_value * 4) + "(s0)");
        } else {
          System.out.println("li t1, " + (des_value * 4));
          System.out.println("add t1, t1, s0");
          System.out.println("sw t0, 0(t1)");
        }
      }
    } catch (NumberFormatException e) {
      int src_num = registers.get(src);
      int des_num = registers.get(des);
      if ((src_num >= 0) && (des_num >= 0)) {
        if (src_num != des_num) {
          System.out.println("mv " + register_name.get(des_num) + ", " + register_name.get(src_num));
        }
      }
      if ((src_num >= 0) && (des_num < 0)) {
        String reg = register_name.get(src_num);
        if ((des_num >> 9) == 0) {
          System.out.println("sw " + reg + ", " + (des_num * 4) + "(s0)");
        } else {
          System.out.println("li t1, " + (des_num * 4));
          System.out.println("add t1, t1, s0");
          System.out.println("sw " + reg + ", 0(t1)");
        }
      }
      if ((src_num < 0) && (des_num >= 0)) {
        String reg = register_name.get(des_num);
        if ((src_num >> 9) == 0) {
          System.out.println("lw " + reg + ", " + (src_num * 4) + "(s0)");
        } else {
          System.out.println("li t1, " + (src_num * 4));
          System.out.println("add t1, t1, s0");
          System.out.println("lw " + reg + ", " + "0(t1)");
        }
      }
      if ((src_num < 0) && (des_num < 0)) {
        if ((src_num >> 9) == 0) {
          System.out.println("lw t0, " + (src_num * 4) + "(s0)");
        } else {
          System.out.println("li t1, " + (src_num * 4));
          System.out.println("add t1, t1, s0");
          System.out.println("lw t0, " + "0(t1)");
        }
        if ((des_num >> 9) == 0) {
          System.out.println("sw t0, " + (des_num * 4) + "(s0)");
        } else {
          System.out.println("li t1, " + (des_num * 4));
          System.out.println("add t1, t1, s0");
          System.out.println("sw t0, 0(t1)");
        }
      }
    }
    return;
  }

  @Override
  public void CodePrint() {
    System.out.print(src + "->" + des + ";");
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    PseudoMove new_move = new PseudoMove("null", "null");
    try {
      Integer.parseInt(src);
      new_move.src = new String(src);
    } catch (NumberFormatException e) {
      if (now_name.containsKey(src)) {
        new_move.src = new String(now_name.get(src));
      } else {
        if ((!CheckLit(src)) || (is_global.containsKey(src))) {
          new_move.src = new String(src);
        } else {
          new_move.src = new String("%reg$" + (++machine.tmp_time));
          now_name.put(src, new_move.src);
        }
      }
    }
    if (now_name.containsKey(des)) {
      new_move.des = new String(now_name.get(des));
    } else {
      if ((!CheckLit(des)) || (is_global.containsKey(des))) {
        new_move.des = new String(des);
      } else {
        new_move.des = new String("%reg$" + (++machine.tmp_time));
        now_name.put(des, new_move.des);
      }
    }
    return new_move;
  }
}
