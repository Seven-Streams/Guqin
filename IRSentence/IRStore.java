package IRSentence;

public class IRStore extends IRCode {
  public String name = null;
  public String from = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.println("store " + type + " " + from + ",ptr " + name);
    return;
  }

  @Override
  public void Codegen() {
    String addr_t = relative_addr.get(from);
    System.out.println("lw a1, " + addr_t);
    if (is_global.get(name)) {
      System.out.println("lui a0, " + "%ho(" + name.substring(1) + ")");
      System.out.println("addi a0, a0, %lo(" + name.substring(1) + ")");
      System.out.println("sw a1, 0(a0)");
    } else {
      String addr_des = relative_addr.get(name);
      System.out.println("sw a1, " + addr_des);
    }
    return;
  }
}
