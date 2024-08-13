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
    if((sp_length >> 12) == 0) {
    System.out.println("addi sp, sp, "+ sp_length);
    } else {
      System.out.println("lui a0, " + (sp_length >> 12));
      System.out.println("addi a0, a0, " + (sp_length & 0x00000fff));
      System.out.println("add sp, sp, a0");  
    }
    System.out.println("ret");
    return;
  }
}
