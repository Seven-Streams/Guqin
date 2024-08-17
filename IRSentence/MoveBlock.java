package IRSentence;

import Optimization.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MoveBlock extends IRCode {
  public int to = 0;
  public int num = 0;
  public ArrayList<PseudoMove> moves = new ArrayList<>();
  public ArrayList<IRLoad> loads = new ArrayList<>();
  public ArrayList<IRStore> stores = new ArrayList<>();

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    for (PseudoMove move : moves) {
      def.put(move.des, null);
      use.put(move.src, null);
    }
    for (IRLoad load : loads) {
      def.put(load.des, null);
      use.put(load.src, null);
    }
    for (IRStore store : stores) {
      def.put(store.name, null);
      def.put(store.from, null);
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    for (PseudoMove move : moves) {
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
      System.out.println("mv " + str_src + ", " + str_des);
      if(value_des < 0) {
        System.out.println("sw t1, " + (value_des * 4) + "(s0)");
      }
    }
    return;
  }
}
