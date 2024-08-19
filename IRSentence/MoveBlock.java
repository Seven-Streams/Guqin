package IRSentence;

import Optimization.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MoveBlock extends IRCode {
  public int to = 0;
  public int num = 0;
  public ArrayList<PseudoMove> moves = new ArrayList<>();

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    for (PseudoMove move : moves) {
      try {
        Integer.parseInt(move.des);
      } catch (NumberFormatException e) {
        def.put(move.des, null);
      }
      try {
        Integer.parseInt(move.src);
      } catch (NumberFormatException e) {
        if (CheckLit(move.src)) {
          use.put(move.src, null);
        } else {
          if (move.src.equals("true")) {
            move.src = "1";
          } else {
            move.src = "0";
          }
        }
      }
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    System.out.println("b" + num + ":");
    for (PseudoMove move : moves) {
      try {
        int src_value = Integer.parseInt(move.src);
        int des_value = registers.get(move.des);
        String target = "t0";
        if (des_value >= 0) {
          target = register_name.get(des_value);
        }
        if ((src_value >> 12) != 0) {
          System.out.println("lui " + target + ", " + (src_value >> 12));
          System.out.println("addi " + target + ", " + target + ", " + ((src_value) & 0x00000fff));
        } else {
          System.out.println("li " + target + ", " + src_value);
        }
        if (des_value < 0) {
          des_value = -des_value;
          if ((des_value >> 10) == 0) {
            System.out.println("sw t0, " + (-des_value * 4) + "(s0)");
          } else {
            System.out.println("lui t1, " + (des_value >> 10));
            System.out.println("addi t1, t1, " + ((des_value << 2) & 0x00000fff));
            System.out.println("neg t1, t1");
            System.out.println("add t1, t1, s0");
            System.out.println("sw t0, 0(t1)");
          }
        }
      } catch (NumberFormatException e) {
        int src_num = registers.get(move.src);
        int des_num = registers.get(move.des);
        if ((src_num >= 0) && (des_num >= 0)) {
          System.out.println("mv " + register_name.get(des_num) + ", " + register_name.get(src_num));
        }
        if ((src_num >= 0) && (des_num < 0)) {
          String reg = register_name.get(src_num);
          System.out.println("sw " + reg + ", " + (des_num * 4) + "(s0)");
        }
        if ((src_num < 0) && (des_num >= 0)) {
          String reg = register_name.get(des_num);
          System.out.println("lw " + reg + ", " + (src_num * 4) + "(s0)");
        }
        if((src_num < 0) && (des_num < 0)) {
          System.out.println("lw t0, " + (src_num * 4) + "(s0)");
          System.out.println("sw t0, " + (des_num * 4) + "(s0)");
        }
      }
    }
    System.out.println("j b" + to);
    return;
  }

  @Override
  public void CodePrint() {
    System.out.print("b" + num + ":");
    for (PseudoMove move : moves) {
      System.out.print(move.src + "->" + move.des + ";");
    }
    System.out.println("TO: b" + to);
  }
}
