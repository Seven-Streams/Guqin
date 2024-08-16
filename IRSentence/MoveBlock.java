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
}
