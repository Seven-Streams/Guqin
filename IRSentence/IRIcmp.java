package IRSentence;

public class IRIcmp extends IRCode {
  public String target_reg = null;
  public String op1 = null;
  public String op2 = null;
  public String symbol = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.print(target_reg + " =  icmp ");
    switch (symbol) {
      case ("=="): {
        System.out.print("eq ");
        break;
      }
      case ("!="): {
        System.out.print("ne ");
        break;
      }
      case (">="): {
        System.out.print("sge ");
        break;
      }
      case (">"): {
        System.out.print("sgt ");
        break;
      }
      case ("<"): {
        System.out.print("slt ");
        break;
      }
      case ("<="): {
        System.out.print("sle ");
        break;
      }
      default: {
        System.out.println("ERROR in ICMP!");
      }
    }
    System.out.println(type + " " + op1 + ", " + op2);
    return;
  }

  @Override
  public void Codegen() throws Exception {
    if (is_global.get(op1) || is_global.get(op2)) {
      throw new Exception("Unexpected.");
    }
    String addr1 = null;
    String addr2 = null;
    try {
      int ins_1 = Integer.parseInt(op1);
      System.out.println("lui a0, " + (ins_1 >> 12));
      System.out.println("addi a0, " + (ins_1 & 0x00000fff));
    } catch (NumberFormatException e) {
      addr1 = relative_addr.get(op1);
      System.out.println("lw a0, " + addr1);
    }
    try {
      int ins_2 = Integer.parseInt(op2);
      System.out.println("lui a1, " + (ins_2 >> 12));
      System.out.println("addi a1, " + (ins_2 & 0x00000fff));
    } catch (NumberFormatException e) {
      addr1 = relative_addr.get(op2);
      System.out.println("lw a1, " + addr2);
    }
    switch (symbol) {
      case ("=="): {
        System.out.println("sub a2, a0, a1");
        System.out.println("seqz a3, a2");
        break;
      }
      case ("!="): {
        System.out.println("sub a2, a0, a1");
        System.out.println("snez a3, a2");
        break;
      }
      case (">"): {
        System.out.println("slt a3, a1, a0");
        break;
      }
      case ("<"): {
        System.out.println("slt a3, a0, a1");
        break;
      }
      case (">="): {
        System.out.println("slt a2, a0, a1");
        System.out.println("xori a3, a2, 1");
        break;
      }
      case ("<="): {
        System.out.println("slt a2, a1, a0");
        System.out.println("xori, a3, a2, 1");
        break;
      }
      default: {
        throw new Exception("Unexpected Symbol.");
      }
    }
    if (!relative_addr.containsKey(target_reg)) {
      is_global.put(target_reg, false);
      now_s0 += 4;
      relative_addr.put(target_reg, Integer.toString(-now_s0) + "(s0)");
    }
    String addr_t = relative_addr.get(target_reg);
    System.out.println("sw a3, " + addr_t);
    return;
  }
}
