package IRSentence;

public class IRElement extends IRCode {
  public String output = null;
  public String now_type = null;
  public String src = null;
  public String num1 = "0";
  public String num2 = null;

  @Override
  public void CodePrint() {
    System.out.print(output + " = getelementptr " + now_type + " , ptr " + src + " , i32 " + num1);
    if (num2 != null) {
      System.out.println(", i32 " + num2);
    } else {
      System.out.println("");
    }
    return;
  }

  @Override
  public void Codegen() throws Exception {
    if (is_global.get(src)) {
      throw new Exception("Unexpected ptr.");
    }
    String src_addr = relative_addr.get(src);
    System.out.println("lw a0, " + src_addr);
    if (num2 != null) {
      try {
        int num_i = Integer.parseInt(num2);
        if ((num_i >> 10) == 0) {
          System.out.println("addi a1, a0, " + Integer.toString(num_i * 4));
        } else {
          System.out.println("lui a1, " + (num_i >> 10));
          System.out.println("addi a1, a1, " + ((num_i << 2) & 0x00000fff));
          System.out.println("addi a1, a0, a1");
        }
      } catch (NumberFormatException e) {
        String num_addr = relative_addr.get(num2);
        System.out.println("lw a2, " + num_addr);
        System.out.println("slli a2, a2, 2");
        System.out.println("add a1, a0, a2");
      }
    } else {
      try {
        int num_i = Integer.parseInt(num1);
        System.out.println("addi a1, a0, " + Integer.toString(num_i * 4));
      } catch (NumberFormatException e) {
        String num_addr = relative_addr.get(num1);
        System.out.println("lw a2, " + num_addr);
        System.out.println("slli a2, a2, 2");
        System.out.println("add a1, a0, a2");
      }
    }
    if (!relative_addr.containsKey(output)) {
      is_global.put(output, false);
      now_s0 += 4;
      relative_addr.put(output, Integer.toString(-now_s0) + "(s0)");
    }
    String out_reg = relative_addr.get(output);
    System.out.println("sw a1, " + out_reg);
    return;
  }
}
