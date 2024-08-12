package IRSentence;

public class IRAlloc extends IRCode {
  public String des = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.println(des + " = alloca " + type);
    return;
  }

  @Override
  public void Codegen() {
    is_global.put(des, false);
    now_s0 += 4;
    relative_addr.put(des, now_s0);
    System.out.println("sw a0, " + now_s0 + "(s0)");
    return;
  }
}
