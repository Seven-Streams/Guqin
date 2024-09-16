package IRSentence;

import java.util.HashMap;

public class SpecialJmp extends Conditionjmp {
  public String reg2 = null;
  public String symbol = null;

  public SpecialJmp(int n1, int n2, String _reg, String _reg2, String _symbol) {
    label1 = n1;
    label2 = n2;
    reg = (_reg);
    reg2 = (_reg2);
    symbol = (_symbol);
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    try {
      Integer.parseInt(reg);
    } catch (NumberFormatException e) {
      if ((!def.containsKey(reg)) && CheckLit(reg)) {
        use.put(reg, null);
      }
    }
    try {
      Integer.parseInt(reg2);
    } catch (NumberFormatException e) {
      if ((!def.containsKey(reg2)) && CheckLit(reg2)) {
        use.put(reg2, null);
      }
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name) {
    Integer value1 = null;
    Integer value2 = null;
    String reg_str1 = "t0";
    String reg_str2 = "t1";
    try {
      value1 = Integer.parseInt(reg);
    } catch (NumberFormatException e) {
      if (CheckLit(reg)) {
        int addr = registers.get(reg);
        if (addr >= 0) {
          reg_str1 = register_name.get(addr);
        } else {
          addr = now_depth + (addr * 4);
          if ((addr >> 11) == 0) {
            buffer.add("lw t0, " + addr + "(sp)");
          } else {
            buffer.add("li t0, " + addr);
            buffer.add("add t0, t0, sp");
            buffer.add("lw t0, 0(t0)");
          }
        }
      } else {
        value1 = reg.equals("true") ? 1 : 0;
      }
    }
    try {
      value2 = Integer.parseInt(reg2);
    } catch (NumberFormatException e) {
      if (CheckLit(reg2)) {
        int addr = registers.get(reg2);
        if (addr >= 0) {
          reg_str2 = register_name.get(addr);
        } else {
          addr = now_depth + (addr * 4);
          if (((addr) >> 11) == 0) {
            buffer.add("lw t1, " + addr + "(sp)");
          } else {
            buffer.add("li t1, " + addr);
            buffer.add("add t1, t1, sp");
            buffer.add("lw t1, 0(t0)");
          }
        }
      } else {
        value2 = reg2.equals("true") ? 1 : 0;
      }
    }
    if (value1 != null && value2 != null) {
      switch (symbol) {
        case ("=="): {
          if (value1 == value2) {
            buffer.add("j b" + label1);
          } else {
            buffer.add("j b" + label2);
          }
          break;
        }
        case ("!="): {
          if (value1 != value2) {
            buffer.add("j b" + label1);
          } else {
            buffer.add("j b" + label2);
          }
          break;
        }
        case (">="): {
          if (value1 >= value2) {
            buffer.add("j b" + label1);
          } else {
            buffer.add("j b" + label2);
          }
          break;
        }
        case ("<="): {
          if (value1 <= value2) {
            buffer.add("j b" + label1);
          } else {
            buffer.add("j b" + label2);
          }
          break;
        }
        case (">"): {
          if (value1 > value2) {
            buffer.add("j b" + label1);
          } else {
            buffer.add("j b" + label2);
          }
          break;
        }
        case ("<"): {
          if (value1 < value2) {
            buffer.add("j b" + label1);
          } else {
            buffer.add("j b" + label2);
          }
          break;
        }
      }
      return;
    }
    if (value1 != null) {
      if (value1 == 0) {
        reg_str1 = "x0";
      } else {
        buffer.add("li t0, " + value1);
      }
    }
    if (value2 != null) {
      if (value2 == 0) {
        reg_str2 = "x0";
      } else {
        buffer.add("li t1, " + value2);
      }
    }
    switch (symbol) {
      case ("=="): {
        buffer.add("beq " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        buffer.add("j b" + label2);
        buffer.add("cond." + cnt + ":");
        buffer.add("j b" + label1);
        break;
      }
      case ("!="): {
        buffer.add("bne " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        buffer.add("j b" + label2);
        buffer.add("cond." + cnt + ":");
        buffer.add("j b" + label1);
        break;
      }
      case ("<"): {
        buffer.add("blt " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        buffer.add("j b" + label2);
        buffer.add("cond." + cnt + ":");
        buffer.add("j b" + label1);
        break;
      }
      case (">"): {
        buffer.add("blt " + reg_str2 + ", " + reg_str1 + ", cond." + ++cnt);
        buffer.add("j b" + label2);
        buffer.add("cond." + cnt + ":");
        buffer.add("j b" + label1);
        break;
      }
      case (">="): {
        buffer.add("bge " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        buffer.add("j b" + label2);
        buffer.add("cond." + cnt + ":");
        buffer.add("j b" + label1);
        break;
      }
      case ("<="): {
        buffer.add("bge " + reg_str2 + ", " + reg_str1 + ", cond." + ++cnt);
        buffer.add("j b" + label2);
        buffer.add("cond." + cnt + ":");
        buffer.add("j b" + label1);
        break;
      }
    }
    return;
  }
}
