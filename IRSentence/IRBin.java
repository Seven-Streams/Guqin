package IRSentence;

import java.util.HashMap;
import java.util.Stack;

import Composer.Composer;
import Optimization.NameLabelPair;

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
    String addr1 = null;
    String addr2 = null;
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
        System.out.println("sll a2, a0, a1");
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
    if (!relative_addr.containsKey(target_reg)) {
      is_global.put(target_reg, false);
      now_s0 += 4;
      relative_addr.put(target_reg, Integer.toString(-now_s0) + "(s0)");
    }
    String addr_t = relative_addr.get(target_reg);
    System.out.println("sw a2, " + addr_t);
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
    int target = registers.get(target_reg);
    boolean is_int1 = false;
    int value1;
    try {
      value1 = Integer.parseInt(op1);
      is_int1 = true;
    } catch (NumberFormatException e) {
      value1 = registers.get(op1);
    }
    boolean is_int2 = false;
    int value2;
    try {
      value2 = Integer.parseInt(op2);
      is_int2 = true;
    } catch (NumberFormatException e) {
      value2 = registers.get(op2);
    }
    if (is_int1 && is_int2) {
      int result = 0;
      switch (symbol) {
        case ("+"): {
          result = value1 + value2;
          break;
        }
        case ("-"): {
          result = value1 - value2;
          break;
        }
        case ("*"): {
          result = value1 * value2;
          break;
        }
        case ("/"): {
          if (value2 == 0) {
            return; // It's an undefined behavior.
          } else {
            result = value1 / value2;
          }
          break;
        }
        case ("<<"): {
          result = value1 << value2;
          break;
        }
        case (">>"): {
          result = value1 >> value2;
          break;
        }
        case ("%"): {
          result = value1 % value2;
          break;
        }
        case ("&"): {
          result = value1 & value2;
          break;
        }
        case ("|"): {
          result = value1 | value2;
          break;
        }
        case ("^"): {
          result = value1 ^ value2;
          break;
        }
        default: {
          throw new Exception("Unexpected Symbol.");
        }
      }
      if (target >= 0) {
        System.out.println("li " + register_name.get(target) + ", " + result);
      } else {
        System.out.println("li t0, " + result);
        System.out.println("li t1, " + (target * 4));
        System.out.println("add t1, t1, s0");
        System.out.println("sw t0, 0(t1)");
      }
      return;
    }
    String reg_1 = null;
    String reg_2 = null;
    if (is_int1) {
      System.out.println("li t0, " + value1);
      reg_1 = "t0";
    } else {
      if (value1 < 0) {
        if ((value1 >> 9) == 0) {
          System.out.println("lw t0, " + (value1 * 4) + "(s0)");
        } else {
          System.out.println("li t0, " + (value1 * 4));
          System.out.println("add t0, t0, s0");
          System.out.println("lw t0, 0(t0)");
        }
        reg_1 = "t0";
      }
    }
    if (is_int2) {
      System.out.println("li t1, " + value2);
      reg_2 = "t1";
    } else {
      if (value2 < 0) {
        System.out.println("li t1, " + (value2 * 4));
        System.out.println("add t1, t1, s0");
        System.out.println("lw t1, 0(t1)");
        reg_2 = "t1";
      }
    }
    if (reg_1 == null) {
      reg_1 = register_name.get(value1);
    }
    if (reg_2 == null) {
      reg_2 = register_name.get(value2);
    }
    String target_name = null;
    if (target < 0) {
      target_name = "t0";
    } else {
      target_name = register_name.get(target);
    }
    switch (symbol) {
      case ("+"): {
        System.out.println("add " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("-"): {
        System.out.println("sub " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("*"): {
        System.out.println("mul " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("/"): {
        System.out.println("div " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("<<"): {
        System.out.println("sll " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case (">>"): {
        System.out.println("srl " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("%"): {
        System.out.println("rem " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("&"): {
        System.out.println("and " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("|"): {
        System.out.println("or " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("^"): {
        System.out.println("xor " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      default: {
        System.out.println(symbol);
        throw new Exception("Unexpected Symbol.");
      }
    }
    if (target < 0) {
      if ((target >> 9) == 0) {
        System.out.println("sw t0, " + (target * 4) + "(s0)");
      } else {
        System.out.println("li t1, " + (target * 4));
        System.out.println("add t1, t1, s0");
        System.out.println("sw t0, 0(t1)");
      }
    }
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRBin return_value = new IRBin();
    return_value.symbol = new String(symbol);
    return_value.type = new String(type);
    try {
      Integer.parseInt(op1);
      return_value.op1 = new String(op1);
    } catch (NumberFormatException e) {
      if (!is_global.containsKey(op1)) {
        if (now_name.containsKey(op1)) {
          return_value.op1 = new String(now_name.get(op1));
        } else {
          return_value.op1 = new String("%reg$" + (++machine.tmp_time));
          now_name.put(op1, return_value.op1);
        }
      }
    }
    try {
      Integer.parseInt(op2);
      return_value.op2 = new String(op2);
    } catch (NumberFormatException e) {
      if (!is_global.containsKey(op2)) {
        if (now_name.containsKey(op2)) {
          return_value.op2 = new String(now_name.get(op2));
        } else {
          return_value.op2 = new String("%reg$" + (++machine.tmp_time));
          now_name.put(op2, return_value.op2);
        }
      }
    }
    if (!(is_global.containsKey(target_reg))) {
      if (now_name.containsKey(target_reg)) {
        return_value.target_reg = new String(now_name.get(target_reg));
      } else {
        return_value.target_reg = new String("%reg$" + (++machine.tmp_time));
        now_name.put(target_reg, return_value.target_reg);
      }
    } else {
      target_reg = new String(target_reg);
    }
    return return_value;
  }

  @Override
  public void AliveUseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    if (!use.containsKey(target_reg)) {
      dead = true;
    } else {
      dead = false;
      UseDefCheck(def, use);
    }
  }
}