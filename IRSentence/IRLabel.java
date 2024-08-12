package IRSentence;

public class IRLabel extends IRCode{
  public int label = 0;
  public IRLabel() {}
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
    CodePrint();
  }
}
