package IRSentence;

import java.util.HashMap;

public class Conditionjmp extends IRCode {
  int label1 = 0;
  int label2 = 0;
  String reg = null;

  @Override
  public void CodePrint() {
    System.out.println("br i1 " + reg + "," + " label %b" + label1 + ", label %b" + label2);
    return;
  }

  public Conditionjmp() {
  }

  public Conditionjmp(int n1, int n2, String _reg) {
    label1 = n1;
    label2 = n2;
    reg = new String(_reg);
  }

  @Override
  public void Codegen() throws Exception {
    if (is_global.get(reg)) {
      throw new Exception("I don't thinl I will use this.");
    } else {
      String addr = relative_addr.get(reg);
      System.out.println("lw a0, " + addr);
      System.out.println("beqz a0, b" + Integer.toString(label2));
      System.out.println("j b" + Integer.toString(label1));
      return;
    }
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
    try {
      Integer.parseInt(reg);
    } catch (NumberFormatException e) {
      if (use.containsKey(reg)) {
        use.put(reg, use.get(reg) + 1);
      } else {
        use.put(reg, 1);
      }
    }
    return;
  }
}
