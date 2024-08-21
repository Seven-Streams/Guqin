package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
import Optimization.NameLabelPair;

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
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
    try {
      Integer.parseInt(op1);
    } catch (NumberFormatException e) {
      if (CheckLit(op1)) {
        if (use.containsKey(op1)) {
          use.put(op1, use.get(op1) + 1);
        } else {
          use.put(op1, 1);
        }
      }
    }
    try {
      Integer.parseInt(op2);
    } catch (NumberFormatException e) {
      if (CheckLit(op2)) {
        if (use.containsKey(op2)) {
          use.put(op2, use.get(op2) + 1);
        } else {
          use.put(op2, 1);
        }
      }
    }
    try {
      Integer.parseInt(target_reg);
    } catch (NumberFormatException e) {
      if (CheckLit(target_reg)) {
        if (def.containsKey(target_reg)) {
          def.put(target_reg, def.get(target_reg) + 1);
        } else {
          def.put(target_reg, 1);
        }
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

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    if (reg_value.containsKey(op1)) {
      op1 = new String(reg_value.get(op1));
    }
    if (reg_value.containsKey(op2)) {
      op2 = new String(reg_value.get(op2));
    }
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    try {
      Integer.parseInt(op1);
    } catch (NumberFormatException e) {
      if ((!def.containsKey(op1)) && CheckLit(op1)) {
        use.put(op1, null);
      }
    }
    try {
      Integer.parseInt(op2);
    } catch (NumberFormatException e) {
      if ((!def.containsKey(op2)) && CheckLit(op2)) {
        use.put(op2, null);
      }
    }
    def.put(target_reg, null);
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    String addr1 = "t0";
    String addr2 = "t1";
    if (op1.equals("true") || op1.equals("false") || op1.equals("null")) {
      if (op1.equals("true")) {
        System.out.println("li t0, 1");
      } else {
        System.out.println("li, t0, 0");
      }
    } else {
      try {
        int ins_1 = Integer.parseInt(op1);
        if ((ins_1 >> 12) != 0) {
          System.out.println("lui t0, " + (ins_1 >> 12));
          System.out.println("addi t0, t0, " + (ins_1 & 0x00000fff));
        } else {
          System.out.println("li t0, " + ins_1);
        }
      } catch (NumberFormatException e) {
        int reg_1 = registers.get(op1);
        if (reg_1 >= 0) {
          addr1 = register_name.get(reg_1);
        } else {
          reg_1 = -reg_1;
          if ((reg_1 >> 10) == 0) {
            System.out.println("lw t0, " + (-reg_1 * 4) + "(s0)");
          } else {
            System.out.println("lui t0, " + (reg_1 >> 10));
            System.out.println("addi t0, t0," + ((reg_1 << 2) & 0x00000fff));
            System.out.println("neg t0, t0");
            System.out.println("add t0, t0, s0");
            System.out.println("lw t0, 0(t0)");
          }
        }
      }
    }
    if (op2.equals("true") || op2.equals("false") || op2.equals("null")) {
      if (op2.equals("true")) {
        System.out.println("li t1, 1");
      } else {
        System.out.println("li, t1, 0");
      }
    } else {
      try {
        int ins_2 = Integer.parseInt(op2);
        if ((ins_2 >> 12) != 0) {
          System.out.println("lui t1, " + (ins_2 >> 12));
          System.out.println("addi t1, t1, " + (ins_2 & 0x00000fff));
        } else {
          System.out.println("li t1, " + ins_2);
        }
      } catch (NumberFormatException e) {
        int reg_2 = registers.get(op2);
        if (reg_2 >= 0) {
          addr2 = register_name.get(reg_2);
        } else {
          reg_2 = -reg_2;
          if ((reg_2 >> 10) == 0) {
            System.out.println("lw t1, " + (-reg_2 * 4) + "(s0)");
          } else {
            System.out.println("lui t1, " + (reg_2 >> 10));
            System.out.println("addi t1, t1," + ((reg_2 << 2) & 0x00000fff));
            System.out.println("neg t1, t1");
            System.out.println("add t1, t1, s0");
            System.out.println("lw t1, 0(t1)");
          }
        }
      }
    }
    String target_str = "t0";
    int target_value = registers.get(target_reg);
    if (target_value >= 0) {
      target_str = register_name.get(target_value);
    }
    switch (symbol) {
      case ("=="): {
        System.out.println("sub t0, " + addr1 + ", " + addr2);
        System.out.println("seqz " + target_str + ", t0");
        break;
      }
      case ("!="): {
        System.out.println("sub t0, " + addr1 + ", " + addr2);
        System.out.println("snez " + target_str + ", t0");
        break;
      }
      case (">"): {
        System.out.println("slt " + target_str + ", " + addr2 + ", " + addr1);
        break;
      }
      case ("<"): {
        System.out.println("slt " + target_str + ", " + addr1 + ", " + addr2);
        break;
      }
      case (">="): {
        System.out.println("slt t0, " + addr1 + ", " + addr2);
        System.out.println("xori " + target_str + ", t0, 1");
        break;
      }
      case ("<="): {
        System.out.println("slt t0, " + addr2 + ", " + addr1);
        System.out.println("xori " + target_str + ", t0, 1");
        break;
      }
      default: {
        throw new Exception("Unexpected Symbol.");
      }
    }
    if (target_value < 0) {
      target_value = -target_value;
      if ((target_value >> 10) == 0) {
        System.out.println("sw t0, " + (-target_value * 4) + "(s0)");
      } else {
        System.out.println("lui t1, " + (target_value >> 10));
        System.out.println("addi t1, t1, " + ((target_value << 2) & 0x00000fff));
        System.out.println("neg t1, t1");
        System.out.println("add t1, t1, s0");
        System.out.println("sw t0, 0(t1)");
      }
    }
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRIcmp return_value = new IRIcmp();
    return_value.symbol = new String(symbol);
    try {
      Integer.parseInt(op1);
      return_value.op1 = new String(op1);
    } catch (NumberFormatException e) {
      if ((!is_global.containsKey(op1)) && CheckLit(op1)) {
        if (now_name.containsKey(op1)) {
          return_value.op1 = new String(now_name.get(op1));
        } else {
          return_value.op1 = new String("%reg$" + (++machine.tmp_time));
          now_name.put(op1, return_value.op1);
        }
      } else {
        return_value.op1 = new String(op1);
      }
    }
    try {
      Integer.parseInt(op2);
      return_value.op2 = new String(op2);
    } catch (NumberFormatException e) {
      if (!is_global.containsKey(op2) && CheckLit(op2)) {
        if (now_name.containsKey(op2)) {
          return_value.op2 = new String(now_name.get(op2));
        } else {
          return_value.op2 = new String("%reg$" + (++machine.tmp_time));
          now_name.put(op2, return_value.op2);
        }
      } else {
        return_value.op2 = new String(op2);
      }
    }
    if (!(is_global.containsKey(target_reg))) {
      if (now_name.containsKey(target_reg)) {
        return_value.target_reg = new String(now_name.get(target_reg));
      } else {
        return_value.target_reg = new String("%reg$" + (++machine.tmp_time));
        now_name.put(target_reg, return_value.target_reg);
      }
    }
    return return_value;
  }
}
