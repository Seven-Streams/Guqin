package IRSentence;

public class IRReturn extends IRCode {
  public String reg = null;
  public String return_type = null;

  @Override
  public void CodePrint() {
    if (reg == null) {
      System.out.println("ret void");
    } else {
      System.out.println("ret " + return_type + " " + reg);
    }
    return;
  }

  @Override
  public void Codegen() {
    if (reg != null) {
      String return_value = relative_addr.get(reg);
      System.out.println("sw a0, " + return_value);
    }
    System.out.println("j .return" + func_num);
    return;
  }
}
