package IRSentence;

public class IRLocal extends IRCode{
  public String name = null;
  public String type = null;
  @Override public void CodePrint() {
    System.out.println(name + " = alloc " + type);
  }
}
