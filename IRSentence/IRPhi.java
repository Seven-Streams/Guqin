package IRSentence;

import java.util.ArrayList;

public class IRPhi extends IRCode {
  public String type = null;
  public String target = null;
  public ArrayList<Integer> labels = new ArrayList<>();
  public ArrayList<String> values = new ArrayList<>();

  @Override
  public void CodePrint() {
    System.out.print(target + " = phi " + type + " ");
    for (int i = 0; i < labels.size(); i++) {
      System.out.print("[" + values.get(i) + " , " + "%b" + Integer.toString(labels.get(i)) + "]");
      if(i != (labels.size() - 1)) {
        System.out.print(",");
      }
    }
    System.out.println("");
    return;
  }
}
