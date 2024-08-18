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
          if(move.src.equals("true")) {
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
        int value = Integer.parseInt(move.src);
        if ((value >> 12) != 0) {
          System.out.println("lui t0, " + (value >> 12));
          System.out.println("addi t0, t0, " + (value & 0x00000fff));
        } else {
          System.out.println("li t0, " + value);
        }
        int value_des = registers.get(move.des);
        String str_des = "t1";
        if (value_des >= 0) {
          str_des = register_name.get(value_des);
          System.out.println("mv " + str_des + ", t0");
        } else {
          System.out.println("sw t0, " + (value_des * 4) + "(s0)");
        }
      } catch (NumberFormatException e) {
        int value_src = registers.get(move.src);
        int value_des = registers.get(move.des);
        String str_src = "t0";
        String str_des = "t1";
        if (value_src >= 0) {
          str_src = register_name.get(value_src);
        } else {
          System.out.println("lw t0, " + (value_src * 4) + "(s0)");
        }
        if (value_des >= 0) {
          str_des = register_name.get(value_des);
        }
        System.out.println("mv " + str_des + ", " + str_src);
        if (value_des < 0) {
          System.out.println("sw t1, " + (value_des * 4) + "(s0)");
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
