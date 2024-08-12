package IRSentence;

public class IRLoad extends IRCode {
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
    if (is_global.get(src)) {
      System.out.println("lui a0, " + "%ho(" + src.substring(1) + ")");
      System.out.println("addi a0, a0, %lo(" + src.substring(1) + ")");
      System.out.println("lw a0, 0(a0)");
    } else {
      String addr_t = relative_addr.get(src);
      System.out.println("lw a0, " + addr_t);
    }
    if (!relative_addr.containsKey(des)) {
      is_global.put(des, false);
      now_s0 += 4;
      relative_addr.put(des, Integer.toString(-now_s0) + "(s0)");
    }
    System.out.println("sw a0, " + relative_addr.get(des));
    return;
  }
}
