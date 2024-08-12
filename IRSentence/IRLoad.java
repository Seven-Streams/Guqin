package IRSentence;

public class IRLoad extends IRCode{
  public String des = null;
  public String src = null;
  public String type = null;
  @Override
  public void CodePrint() {
    System.out.println(des + " = load " + type + ", ptr " + src);
    return;
  }

  @Override
  public void Codegen() {
    relative_addr.put(des, relative_addr.get(src));
    return;
  }
}
