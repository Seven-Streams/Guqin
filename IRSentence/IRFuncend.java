package IRSentence;

import java.util.HashMap;

import Composer.Composer;

public class IRFuncend extends IRCode {
  public boolean last_return = false;

  @Override
  public void CodePrint() {
    System.out.println("}");
    System.out.println("");
    return;
  }

  @Override
  public void Codegen() {
    if (!last_return) {
      System.out.println("li a0, 0");
      System.out.println("j .return" + func_num);
    }
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
    if (!last_return) {
      buffer.add("li a0, 0");
      buffer.add("j .return" + func_num);
    }
    buffer.add("");
    buffer.add(".return" + func_num + ":");
    if ((sp_length >> 11) == 0) {
      buffer.add("addi sp, sp, " + sp_length);
    } else {
      buffer.add("li t0, " + (sp_length));
      buffer.add("add sp, sp, t0");
    }
    buffer.add("lw ra, -4(sp)");
    if (register_use.get(-func_num) >= 0) {
      buffer.add("lw s0, -8(sp)");
    }
    for (int i = 1; i <= Integer.min(register_use.get(-func_num), 11); i++) {
      buffer.add("lw s" + i + ", " + (-8 - 4 * i) + "(sp)");
    }
    buffer.add("ret");
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRLabel return_value = new IRLabel(now_label.get(0));
    return return_value;
  }
}