package IRSentence;

import java.util.HashMap;

public class IRFuncend extends IRCode {
  @Override
  public void CodePrint() {
    System.out.println("}");
    System.out.println("");
    return;
  }

  @Override
  public void Codegen() {
    System.out.println("li a0, 0");
    System.out.println("j .return" + func_num);
    System.out.println("");
    System.out.println(".return" + func_num + ":");
    System.out.println("lw ra, " + (sp_length - 4) + "(sp)");
    System.out.println("lw s0, " + (sp_length - 8) + "(sp)");
    if ((sp_length >> 12) == 0) {
      System.out.println("addi sp, sp, " + sp_length);
    } else {
      System.out.println("lui a1, " + (sp_length >> 12));
      System.out.println("addi a1, a1, " + (sp_length & 0x00000fff));
      System.out.println("add sp, sp, a1");
    }
    System.out.println("ret");
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    System.out.println("li a0, 0");
    System.out.println("j .return" + func_num);
    System.out.println("");
    System.out.println(".return" + func_num + ":");
    System.out.println("lw ra, " + (sp_length - 4) + "(sp)");
    System.out.println("lw s0, " + (sp_length - 8) + "(sp)");
    if ((sp_length >> 12) == 0) {
      System.out.println("addi sp, sp, " + sp_length);
    } else {
      System.out.println("li t0, " + (sp_length));
      System.out.println("add sp, sp, t0");
    }
    for (int i = 0; i <= Integer.min(register_use.get(-func_num), 10); i++) {
      System.out.println("lw s" + (i + 1) + ", " + (- 12 - 4 * i) + "(sp)");
    }
    System.out.println("ret");
  }
}