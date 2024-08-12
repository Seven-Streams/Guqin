package IRSentence;

public class IRBin extends IRCode {
  public String target_reg = null;
  public String op1 = null;
  public String op2 = null;
  public String symbol = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.print(target_reg + " = ");
    switch (symbol) {
      case ("+"): {
        System.out.print("add ");
        break;
      }
      case ("-"): {
        System.out.print("sub ");
        break;
      }
      case ("*"): {
        System.out.print("mul ");
        break;
      }
      case ("/"): {
        System.out.print("sdiv ");
        break;
      }
      case ("%"): {
        System.out.print("srem ");
        break;
      }
      case (">>"): {
        System.out.print("ashr ");
        break;
      }
      case ("<<"): {
        System.out.print("shl ");
        break;
      }
      case ("&"): {
        System.out.print("and ");
        break;
      }
      case ("|"): {
        System.out.print("or ");
        break;
      }
      case ("^"): {
        System.out.print("xor ");
        break;
      }
      default: {
        System.out.println("ERROR in IRBin!");
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
    String addr1 = relative_addr.get(op1);
    String addr2 = relative_addr.get(op2);
    System.out.println("lw a0, " + addr1);
    System.out.println("lw a1, " + addr2);
    switch (symbol) {
      case ("+"): {
        System.out.println("add a2, a0, a1");
        break;
      }
      case ("-"): {
        System.out.println("sub a2, a0, a1");
        break;
      }
      case ("*"): {
        System.out.println("mul a2, a0, a1");
        break;
      }
      case ("/"): {
        System.out.println("div a2, a0, a1");
        break;
      }
      case ("<<"): {
        System.out.println("sll, a2, a0, a1");
        break;
      }
      case (">>"): {
        System.out.println("srl a2, a0, a1");
        break;
      }
      case ("%"): {
        System.out.println("rem a2, a0, a1");
        break;
      }
      case ("&"): {
        System.out.println("and a2, a0, a1");
        break;
      }
      case ("|"): {
        System.out.println("or a2, a0, a1");
        break;
      }
      case ("^"): {
        System.out.println("xor a2, a0, a1");
        break;
      }
      default: {
        throw new Exception("Unexpected Symbol.");
      }
    }
    if(!relative_addr.containsKey(target_reg)) {
      now_s0 += 4;
      relative_addr.put(target_reg, Integer.toString(now_s0) + "(s0)");
    }
    String addr_t = relative_addr.get(target_reg);
    System.out.println("sw a2, " + addr_t);
  }
}
