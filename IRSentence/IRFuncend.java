package IRSentence;

public class IRFuncend extends IRCode {
  @Override
  public void CodePrint() {
    System.out.println("}");
    System.out.println("");
    return;
  }

  @Override
  public void Codegen() {
    System.out.println("j .return" + func_num);
    System.out.println("");
    System.out.println(".return" + func_num + ":");
    System.out.println("lw ra, " + (sp_length - 4) + "(sp)");
    System.out.println("lw s0, " + (sp_length - 8) + "(sp)");
    System.out.println("addi sp, sp, " + sp_length);
    System.out.println("ret");
    return;
  }
}
