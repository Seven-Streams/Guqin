package IRSentence;

import java.util.HashMap;

public class IRLabel extends IRCode {
  public int label = 0;
  public Integer cond = null;

  public IRLabel() {
  }

  public IRLabel(int num) {
    label = num;
  }

  @Override
  public void CodePrint() {
    System.out.println("");
    System.out.println("b" + label + ":");
    return;
  }

  @Override
  public void Codegen() {
    System.out.println("");
    System.out.println("b" + label + ":");
    if (cond != null) {
      System.out.println("li a7, " + cond);
    }
  }

  @Override
  public int CheckBlock(HashMap<String, HashMap<Integer, Boolean>> times, int now_block) {
    return label;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    System.out.println("");
    System.out.println("b" + label + ":");
    return;
  }
}
