package IRSentence;

public class IRAlloc extends IRCode {
  public String des = null;
  public String type = null;
  public String num = null;

  @Override
  public void CodePrint() {
    if (num != null) {
      System.out.println(des + " = alloca " + type + " , " + num);
    } else {
      System.out.println(des + " = alloca " + type);
    }
    return;
  }
}
