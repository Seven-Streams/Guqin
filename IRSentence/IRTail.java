package IRSentence;

import java.util.ArrayList;
import java.util.HashMap;

import Optimization.PseudoMove;

public class IRTail extends IRCode {
  public String target_reg = null;
  public String func_name = null;
  public String func_type = null;
  public ArrayList<String> reg = new ArrayList<>();

  @Override
  public void CodePrint() {
    if (target_reg != null) {
      System.out.print(target_reg + " = ");
    }
    System.out.print("tail " + func_type + " @" + func_name + "(");
    for (int i = 0; i < reg.size(); i++) {
      System.out.print(" " + reg.get(i));
      if (i != (reg.size() - 1)) {
        System.out.print(", ");
      }
    }
    System.out.println(")");
    return;
  }

  public IRTail(IRFuncall call) {
    target_reg = call.target_reg == null ? null : (call.target_reg);
    func_name = call.func_name;
    func_type = call.func_type;
    reg = call.reg;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    if (Empty_func.containsKey(func_name)) {
      return;
    }
    if (reg.size() > 8) {
      for (int i = 8; i < reg.size(); i++) {
        String str = "t0";
        try {
          int num = Integer.parseInt(reg.get(i));
          buffer.add("li t0, " + num);
        } catch (NumberFormatException e) {
          if (!is_global.containsKey(reg.get(i))) {
            int value = registers.get(reg.get(i));
            if (value >= 0) {
              str = register_name.get(value);
            } else {
              value = now_depth + (value * 4);
              if ((value >> 11) == 0) {
                buffer.add("lw t0, " + value + "(sp)");
              } else {
                buffer.add("li t0, " + value);
                buffer.add("add t0, t0, sp");
                buffer.add("lw t0, 0(t0)");
              }
            }
          } else {
            buffer.add("lui t0, %hi(" + reg.get(i).substring(1) + ")");
            buffer.add("addi t0, t0, " + "%lo(" + reg.get(i).substring(1) + ")");
            buffer.add("lw t0, 0(t0)");
          }
        }
        buffer.add("sw " + str + ", " + ((i - 8) * 4) + "(sp)");
      }
    }
    int total = Integer.min(8, reg.size());
    HashMap<Integer, Boolean> danger = new HashMap<>();
    for (int i = 0; i < total; i++) {
      try {
        Integer.parseInt(reg.get(i));
      } catch (NumberFormatException e) {
        if (CheckLit(reg.get(i)) && (!is_global.containsKey(reg.get(i)))) {
          if (registers.get(reg.get(i)) >= 0) {
            danger.put(registers.get(reg.get(i)), null);
          }
        }
      }
    }
    ArrayList<PseudoMove> moves = new ArrayList<>();
    for (int i = 0; i < total; i++) {
      String func_in = null;
      try {
        Integer.parseInt(reg.get(i));
      } catch (NumberFormatException e) {
        if (CheckLit(reg.get(i))) {
          if (!is_global.containsKey(reg.get(i))) {
            int value = registers.get(reg.get(i));
            if (value >= 0) {
              danger.remove(value);
            }
          }
        }
      }
      if (!danger.containsKey(27 - i)) {
        func_in = register_name.get(27 - i);
        danger.put(27 - i, null);
      } else {
        for (int j = 12; j < 28; j++) {
          if (!danger.containsKey(j)) {
            func_in = register_name.get(j);
            PseudoMove move = new PseudoMove(func_in, "a" + i);
            danger.put(j, null);
            moves.add(move);
            break;
          }
        }
      }
      try {
        int num = Integer.parseInt(reg.get(i));
        buffer.add("li " + func_in + ", " + num);
      } catch (NumberFormatException e) {
        if (CheckLit(reg.get(i))) {
          if (!is_global.containsKey(reg.get(i))) {
            int value = registers.get(reg.get(i));
            if (value >= 0) {
              if (!func_in.equals(register_name.get(value))) {
                buffer.add("mv " + func_in + ", " + register_name.get(value));
              }
            } else {
              value = now_depth + (value * 4);
              if ((value >> 11) == 0) {
                buffer.add("lw " + func_in + ", " + value + "(sp)");
              } else {
                buffer.add("li " + func_in + ", " + value);
                buffer.add("add " + func_in + ", " + func_in + ", sp");
                buffer.add("lw " + func_in + ", 0(" + func_in + ")");
              }
            }
          } else {
            buffer.add("lui " + func_in + ", %hi(" + reg.get(i).substring(1) + ")");
            buffer.add("addi " + func_in + ", " + func_in + ", %lo(" + reg.get(i).substring(1) + ")");
            buffer.add("lw " + func_in + ", 0(" + func_in + ")");
          }
        } else {
          if (reg.get(i).equals("true")) {
            buffer.add("li " + func_in + ", 1");
          } else {
            buffer.add("li " + func_in + ", 0");
          }
        }
      }
    }
    for (PseudoMove move : moves) {
      if (!move.des.equals(move.src)) {
        buffer.add("mv " + move.des + ", " + move.src);
      }
    }
    buffer.add("j " + func_name + ".tail");
    return;
  }
}
