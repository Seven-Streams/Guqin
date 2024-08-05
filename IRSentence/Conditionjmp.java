package IRSentence;

public class Conditionjmp extends IRCode{
  int label1 = 0;
  int label2 = 0;
  String reg = null;
  @Override public void CodePrint() {
    System.out.println("br i1 " + reg + "," + " label %" + label1 + ", label %" + label2);
    return;
  }
  public Conditionjmp(){}
  public Conditionjmp(int n1, int n2, String _reg) {
    label1 = n1;
    label2 = n2;
    reg = new String(_reg);
  }
}
