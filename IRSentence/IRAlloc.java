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
    String res = Integer.toString(-now_s0) + "(s0)";
    relative_addr.put(des, res);
    now_s0 += 4;
    System.out.println("addi a0, s0, -" + now_s0);
    System.out.println("sw a0, " + res);
    return;
  }
}
