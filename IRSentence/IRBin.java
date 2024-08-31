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
    if (!registers.containsKey(target_reg)) {
      return;
    }
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
        buffer.add("li " + register_name.get(target) + ", " + result);
      } else {
        target = now_depth + (target * 4);
        buffer.add("li t0, " + result);
        buffer.add("li t1, " + target);
        buffer.add("add t1, t1, sp");
        buffer.add("sw t0, 0(t1)");
      }
      return;
    }
    String reg_1 = null;
    String reg_2 = null;
    if ((((is_int1) && ((value1 >> 9) == 0)) || ((is_int2) && ((value2 >> 9) == 0)))
        && (symbol.equals("+") || symbol.equals("&") || symbol.equals("|") || symbol.equals("^"))) {
      int const_value = is_int1 ? value1 : value2;
      int reg_value = is_int1 ? value2 : value1;
      String reg = null;
      if (reg_value < 0) {
        int res = now_depth + reg_value * 4;
        buffer.add("li t1, " + res);
        buffer.add("add t1, t1, sp");
        buffer.add("lw t1, 0(t1)");
        reg = "t1";
      } else {
        reg = register_name.get(reg_value);
      }
      String target_name = null;
      if (target < 0) {
        target_name = "t0";
      } else {
        target_name = register_name.get(target);
      }
      switch (symbol) {
        case ("+"): {
          buffer.add("addi " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        case ("&"): {
          buffer.add("andi " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        case ("|"): {
          buffer.add("ori " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        case ("^"): {
          buffer.add("xori " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        default: {
          buffer.add(symbol);
          throw new Exception("Unexpected Symbol.");
        }
      }
      if (target < 0) {
        target = now_depth + (target * 4);
        if ((target >> 11) == 0) {
          buffer.add("sw t0, " + target + "(sp)");
        } else {
          buffer.add("li t1, " + target);
          buffer.add("add t1, t1, sp");
          buffer.add("sw t0, 0(t1)");
        }
      }
      return;
    }
    if ((((is_int1) && ((value1 >> 9) == 0)) || ((is_int2) && ((value2 >> 9) == 0)))
        && (symbol.equals("*") || symbol.equals("/"))) {
      int const_value = is_int1 ? value1 : value2;
      int reg_value = is_int1 ? value2 : value1;
      boolean power = true;
      int cnt = 0;
      boolean symbol_const = false;
      if (const_value < 0) {
        symbol_const = true;
        const_value = -const_value;
      }
      int test = const_value;
      while (test != 0) {
        cnt++;
        test >>= 1;
        if (((test & 1) != 0) && (test != 1)) {
          power = false;
          break;
        }
      }
      String target_name = null;
      if (target < 0) {
        target_name = "t0";
      } else {
        target_name = register_name.get(target);
      }
      if (const_value == 0) {
        buffer.add("li " + target_name + ", 0");
        if (target < 0) {
          target = now_depth + (target * 4);
          if ((target >> 11) == 0) {
            buffer.add("sw t0, " + target + "(sp)");
          } else {
            buffer.add("li t1, " + target);
            buffer.add("add t1, t1, sp");
            buffer.add("sw t0, 0(t1)");
          }
        }
        return;
      }
      if (power) {
        String reg = null;
        if (reg_value < 0) {
          int value = now_depth + (reg_value * 4);
          buffer.add("li t1, " + value);
          buffer.add("add t1, t1, sp");
          buffer.add("lw t1, 0(t1)");
          reg = "t1";
        } else {
          reg = register_name.get(reg_value);
        }
        switch (symbol) {
          case ("*"): {
            buffer.add("slli " + target_name + ", " + reg + ", " + (cnt - 1));
            break;
          }
          case ("/"): {
            buffer.add("srai " + target_name + ", " + reg + ", 31");
            buffer.add("srli " + target_name + ", " + target_name + ", " + (33 - cnt));
            buffer.add("add " + target_name + ", " + reg + ", " + target_name);
            buffer.add("srai " + target_name + ", " + target_name + ", " + (cnt - 1));
            break;
          }
          default:
            break;
        }
        if (symbol_const) {
          buffer.add("neg " + target_name + ", " + target_name);
        }
        if (target < 0) {
          target = now_depth + (target * 4);
          if ((target >> 11) == 0) {
            buffer.add("sw t0, " + target + "(sp)");
          } else {
            buffer.add("li t1, " + target);
            buffer.add("add t1, t1, sp");
            buffer.add("sw t0, 0(t1)");
          }
        }
        return;
      }
    }
    if (((is_int2) && ((value2 >> 9) == 0))
        && (symbol.equals("-") || symbol.equals(">>") || symbol.equals("<<"))) {
      int const_value = is_int1 ? value1 : value2;
      int reg_value = is_int1 ? value2 : value1;
      String reg = null;
      if (reg_value < 0) {
        int value = now_depth + (reg_value * 4);
        buffer.add("li t1, " + value);
        buffer.add("add t1, t1, sp");
        buffer.add("lw t1, 0(t1)");
        reg = "t1";
      } else {
        reg = register_name.get(reg_value);
      }
      String target_name = null;
      if (target < 0) {
        target_name = "t0";
      } else {
        target_name = register_name.get(target);
      }
      switch (symbol) {
        case ("<<"): {
          buffer.add("slli " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        case (">>"): {
          buffer.add("srli " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        case ("-"): {
          const_value = -const_value;
          buffer.add("addi " + target_name + ", " + reg + ", " + const_value);
          break;
        }
        default: {
          buffer.add(symbol);
          throw new Exception("Unexpected Symbol.");
        }
      }
      if (target < 0) {
        target = now_depth + (target * 4);
        if ((target >> 11) == 0) {
          buffer.add("sw t0, " + target + "(sp)");
        } else {
          buffer.add("li t1, " + target);
          buffer.add("add t1, t1, sp");
          buffer.add("sw t0, 0(t1)");
        }
      }
      return;
    }
    if (is_int1) {
      buffer.add("li t0, " + value1);
      reg_1 = "t0";
    } else {
      if (value1 < 0) {
        int res_value = now_depth + (value1 * 4);
        if ((res_value >> 11) == 0) {
          buffer.add("lw t0, " + res_value + "(sp)");
        } else {
          buffer.add("li t0, " + res_value);
          buffer.add("add t0, t0, sp");
          buffer.add("lw t0, 0(t0)");
        }
        reg_1 = "t0";
      }
    }
    if (is_int2) {
      buffer.add("li t1, " + value2);
      reg_2 = "t1";
    } else {
      if (value2 < 0) {
        int res_value = now_depth + (value2 * 4);
        if ((res_value >> 11) == 0) {
          buffer.add("lw t1, " + res_value + "(sp)");
        } else {
          buffer.add("li t1, " + res_value);
          buffer.add("add t1, t1, sp");
          buffer.add("lw t1, 0(t1)");
        }
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
        buffer.add("add " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("-"): {
        buffer.add("sub " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("*"): {
        buffer.add("mul " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("/"): {
        buffer.add("div " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("<<"): {
        buffer.add("sll " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case (">>"): {
        buffer.add("srl " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("%"): {
        buffer.add("rem " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("&"): {
        buffer.add("and " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("|"): {
        buffer.add("or " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      case ("^"): {
        buffer.add("xor " + target_name + ", " + reg_1 + ", " + reg_2);
        break;
      }
      default: {
        buffer.add(symbol);
        throw new Exception("Unexpected Symbol.");
      }
    }
    if (target < 0) {
      target = now_depth + target * 4;
      if ((target >> 11) == 0) {
        System.out.println("sw t0, " + target + "(sp)");
      } else {
        System.out.println("li t1, " + target);
        System.out.println("add t1, t1, sp");
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

  @Override
  public void GlobalConstReplace(HashMap<String, String> mapping) {
    if (mapping.containsKey(op1)) {
      op1 = new String(mapping.get(op1));
    }
    if (mapping.containsKey(op2)) {
      op2 = new String(mapping.get(op2));
    }
    return;
  }

  @Override
  public String ConstCheck(HashMap<String, String> replace) {
    if (dead) {
      return null;
    }
    boolean ok = true;
    Integer value1 = null;
    Integer value2 = null;
    try {
      value1 = Integer.parseInt(op1);
    } catch (NumberFormatException e) {
      if (!replace.containsKey(op1)) {
        ok = false;
      } else {
        value1 = Integer.parseInt(replace.get(op1));
        op1 = new String(replace.get(op1));
      }
    }
    try {
      value2 = Integer.parseInt(op2);
    } catch (NumberFormatException e) {
      if (!replace.containsKey(op2)) {
        ok = false;
      } else {
        value2 = Integer.parseInt(replace.get(op2));
        op2 = new String(replace.get(op2));
      }
    }
    int result = 0;
    if(!ok) {
      return null;
    }
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
          result = 114514;
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
        if (value2 == 0) {
          result = 114514;
        } else {
          result = value1 % value2;
        }
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
        System.out.println("Error! Unexpected symbol!");
      }
    }
    replace.put(target_reg, Integer.toString(result));
    dead = true;
    return target_reg;
  }
}