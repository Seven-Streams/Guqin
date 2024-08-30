package IRSentence;

import java.util.HashMap;

public class SpecialJmp extends Conditionjmp {
  public String reg2 = null;
  public String symbol = null;

  public SpecialJmp(int n1, int n2, String _reg, String _reg2, String _symbol) {
    label1 = n1;
    label2 = n2;
    reg = new String(_reg);
    reg2 = new String(_reg2);
    symbol = new String(_symbol);
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
          if (((-addr) >> 9) == 0) {
            System.out.println("lw t0, " + (addr * 4) + "(s0)");
          } else {
            System.out.println("li t0, " + (addr * 4));
            System.out.println("add t0, t0, s0");
            System.out.println("lw t0, 0(t0)");
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
          if (((addr) >> 9) == 0) {
            System.out.println("lw t1, " + (addr * 4) + "(s0)");
          } else {
            System.out.println("li t1, " + (addr * 4));
            System.out.println("add t1, t1, s0");
            System.out.println("lw t1, 0(t0)");
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
            System.out.println("j b" + label1);
          } else {
            System.out.println("j b" + label2);
          }
          break;
        }
        case ("!="): {
          if (value1 != value2) {
            System.out.println("j b" + label1);
          } else {
            System.out.println("j b" + label2);
          }
          break;
        }
        case (">="): {
          if (value1 >= value2) {
            System.out.println("j b" + label1);
          } else {
            System.out.println("j b" + label2);
          }
          break;
        }
        case ("<="): {
          if (value1 <= value2) {
            System.out.println("j b" + label1);
          } else {
            System.out.println("j b" + label2);
          }
          break;
        }
        case (">"): {
          if (value1 > value2) {
            System.out.println("j b" + label1);
          } else {
            System.out.println("j b" + label2);
          }
          break;
        }
        case ("<"): {
          if (value1 < value2) {
            System.out.println("j b" + label1);
          } else {
            System.out.println("j b" + label2);
          }
          break;
        }
      }
      return;
    }
    if (value1 != null) {
      System.out.println("li t0, " + value1);
    }
    if (value2 != null) {
      System.out.println("li t1, " + value2);
    }
    switch (symbol) {
      case ("=="): {
        System.out.println("beq " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        System.out.println("j b" + label2);
        System.out.println("cond." + cnt + ":");
        System.out.println("j b" + label1);
        break;
      }
      case ("!="): {
        System.out.println("bne " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        System.out.println("j b" + label2);
        System.out.println("cond." + cnt + ":");
        System.out.println("j b" + label1);
        break;
      }
      case ("<"): {
        System.out.println("blt " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        System.out.println("j b" + label2);
        System.out.println("cond." + cnt + ":");
        System.out.println("j b" + label1);
        break;
      }
      case (">"): {
        System.out.println("blt " + reg_str2 + ", " + reg_str1 + ", cond." + ++cnt);
        System.out.println("j b" + label2);
        System.out.println("cond." + cnt + ":");
        System.out.println("j b" + label1);
        break;
      }
      case (">="): {
        System.out.println("bge " + reg_str1 + ", " + reg_str2 + ", cond." + ++cnt);
        System.out.println("j b" + label2);
        System.out.println("cond." + cnt + ":");
        System.out.println("j b" + label1);
        break;
      }
      case ("<="): {
        System.out.println("bge " + reg_str2 + ", " + reg_str1 + ", cond." + ++cnt);
        System.out.println("j b" + label2);
        System.out.println("cond." + cnt + ":");
        System.out.println("j b" + label1);
        break;
      }
    }
    return;
  }
}
