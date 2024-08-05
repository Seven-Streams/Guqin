package IRSentence;


public class IRGlobal extends IRCode{
  public String name = null;
  public String type = null;
  public void CodePrint() {
    System.out.println("@" + name + "= global" + type + '0');
  }
}
