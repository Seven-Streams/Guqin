package IRSentence;


public class IRGlobal extends IRCode{
  public String name = null;
  public String type = null;
  @Override
  public void CodePrint() {
    if(type.equals("ptr")) {
      System.out.println(name + "= global " + type + " null");
    } else {
      System.out.println(name + "= global " + type + " 1");
    }
    return;
  }
}
