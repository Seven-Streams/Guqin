package IRSentence;

import java.util.HashMap;

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
    String addr1 = null;
    String addr2 = null;
    if (op1.equals("true") || op1.equals("false") || op1.equals("null")) {
      if (op1.equals("true")) {
        System.out.println("li a0, 1 ");
      } else {
        System.out.println("li, a0, 0");
      }
    } else {
      try {
        int ins_1 = Integer.parseInt(op1);
        if ((ins_1 >> 12) != 0) {
          System.out.println("lui a0, " + (ins_1 >> 12));
        } else {
          System.out.println("andi a0, a0, 0");
        }
        System.out.println("addi a0, a0, " + (ins_1 & 0x00000fff));
      } catch (NumberFormatException e) {
        addr1 = relative_addr.get(op1);
        System.out.println("lw a0, " + addr1);
      }
    }
    if (op2.equals("true") || op2.equals("false") || op2.equals("null")) {
      if (op2.equals("true")) {
        System.out.println("li a1, 1 ");
      } else {
        System.out.println("li, a1, 0");
      }
    } else {
      try {
        int ins_2 = Integer.parseInt(op2);
        if ((ins_2 >> 12) != 0) {
          System.out.println("lui a1, " + (ins_2 >> 12));
        } else {
          System.out.println("andi a1, a1, 0");
        }
        System.out.println("addi a1, a1, " + (ins_2 & 0x00000fff));
      } catch (NumberFormatException e) {
        addr2 = relative_addr.get(op2);
        System.out.println("lw a1, " + addr2);
      }
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
        System.out.println("xori a3, a2, 1");
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

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
    try {
      Integer.parseInt(op1);
    } catch (NumberFormatException e) {
      if (use.containsKey(op1)) {
        use.put(op1, use.get(op1) + 1);
      } else {
        use.put(op1, 1);
      }
    }
    try {
      Integer.parseInt(op2);
    } catch (NumberFormatException e) {
      if (use.containsKey(op2)) {
        use.put(op2, use.get(op2) + 1);
      } else {
        use.put(op2, 1);
      }
    }
    try {
      Integer.parseInt(target_reg);
    } catch (NumberFormatException e) {
      if (def.containsKey(target_reg)) {
        def.put(target_reg, def.get(target_reg) + 1);
      } else {
        def.put(target_reg, 1);
      }
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
    return !use.containsKey(target_reg);
  }

  @Override
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated) {
    while (replace.containsKey(op1)) {
      op1 = new String(replace.get(op1));
    }
    while (replace.containsKey(op2)) {
      op2 = new String(replace.get(op2));
    }
    while (replace.containsKey(target_reg)) {
      target_reg = new String(replace.get(target_reg));
    }
    return;
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    while (single.containsKey(op1)) {
      op1 = new String(single.get(op1));
    }
    while (single.containsKey(op2)) {
      op2 = new String(single.get(op2));
    }
    while (single.containsKey(target_reg)) {
      target_reg = new String(single.get(target_reg));
    }
    return;
  }
}
