package IRSentence;

public class IRjmp extends IRCode{
  public int label = 0;
  public IRjmp() {}
  public IRjmp(int num) {
    label = num;
  }
  @Override
  public void CodePrint() {
    System.out.println("br label %" + label);
    return;
  }
}
