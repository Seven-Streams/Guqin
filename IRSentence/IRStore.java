package IRSentence;

public class IRStore extends IRCode{
  public String name = null;
  public String from = null;
  public String type = null;
  @Override public void CodePrint() {
    System.out.println("store " + type + " " + from + "ptr " + name);
    return;
  }
}
