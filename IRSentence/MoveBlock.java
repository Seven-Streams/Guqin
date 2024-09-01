package IRSentence;

import Optimization.*;
import java.util.ArrayList;
import java.util.HashMap;

import Composer.Composer;

public class MoveBlock extends IRCode {
  public Integer to = null;
  public Integer num = null;
  public ArrayList<PseudoMove> moves = new ArrayList<>();

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    for (PseudoMove move : moves) {
      def.put(move.des, null);
      try {
        Integer.parseInt(move.src);
      } catch (NumberFormatException e) {
        if (CheckLit(move.src)) {
          if (!def.containsKey(move.src)) {
            use.put(move.src, null);
          }
        } else {
          if (move.src.equals("true")) {
            move.src = "1";
          } else {
            move.src = "0";
          }
        }
      }
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    if (num != null) {
      buffer.add("b" + num + ":");
    }
    for (PseudoMove move : moves) {
      if (move.dead) {
        continue;
      }
      if (!registers.containsKey(move.des)) {
        continue;
      }
      try {
        int src_value = Integer.parseInt(move.src);
        int des_value = registers.get(move.des);
        String target = "t0";
        if (des_value >= 0) {
          target = register_name.get(des_value);
        }
        buffer.add("li " + target + ", " + src_value);
        if (des_value < 0) {
          int res = now_depth + des_value * 4;
          if ((res >> 11) == 0) {
            buffer.add("sw t0, " + res + "(sp)");
          } else {
            buffer.add("li t1, " + res);
            buffer.add("add t1, t1, sp");
            buffer.add("sw t0, 0(t1)");
          }
        }
      } catch (NumberFormatException e) {
        int src_num = registers.get(move.src);
        int des_num = registers.get(move.des);
        if ((src_num >= 0) && (des_num >= 0)) {
          if (des_num == src_num) {
            continue;
          }
          buffer.add("mv " + register_name.get(des_num) + ", " + register_name.get(src_num));
          continue;
        }
        if ((src_num >= 0) && (des_num < 0)) {
          String reg = register_name.get(src_num);
          des_num = now_depth + des_num * 4;
          if ((des_num >> 11) == 0) {
            buffer.add("sw " + reg + ", " + des_num + "(sp)");
          } else {
            buffer.add("li t1, " + des_num);
            buffer.add("add t1, t1, sp");
            buffer.add("sw " + reg + ", 0(t1)");
          }
          continue;
        }
        if ((src_num < 0) && (des_num >= 0)) {
          String reg = register_name.get(des_num);
          src_num = now_depth + (src_num * 4);
          if ((src_num >> 9) == 0) {
            buffer.add("lw " + reg + ", " + src_num + "(sp)");
          } else {
            buffer.add("li t1, " + src_num);
            buffer.add("add t1, t1, sp");
            buffer.add("lw " + reg + ", " + "0(t1)");
          }
          continue;
        }
        if ((src_num < 0) && (des_num < 0)) {
          src_num = now_depth + src_num * 4;
          if ((src_num >> 11) == 0) {
            buffer.add("lw t0, " + src_num + "(sp)");
          } else {
            buffer.add("li t1, " + src_num);
            buffer.add("add t1, t1, sp");
            buffer.add("lw t0, " + "0(t1)");
          }
          des_num = now_depth + des_num * 4;
          if ((des_num >> 11) == 0) {
            buffer.add("sw t0, " + des_num + "(sp)");
          } else {
            buffer.add("li t1, " + des_num);
            buffer.add("add t1, t1, sp");
            buffer.add("sw t0, 0(t1)");
          }
          continue;
        }
      }
    }
    if (to != null) {
      buffer.add("j b" + to);
    }
    return;
  }

  @Override
  public void CodePrint() {
    if (num != null) {
      System.out.print("b" + num + ":");
    }
    for (PseudoMove move : moves) {
      if (!move.dead) {
        System.out.print(move.src + "->" + move.des + ";");
      }
    }
    if (to != null) {
      System.out.println("TO: b" + to);
    }
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    MoveBlock return_value = new MoveBlock();
    if (now_label.containsKey(num)) {
      return_value.num = now_label.get(num);
    } else {
      return_value.num = ++machine.label_number;
      now_label.put(num, return_value.num);
    }
    if (now_label.containsKey(to)) {
      return_value.to = now_label.get(to);
    } else {
      return_value.to = ++machine.label_number;
      now_label.put(to, return_value.to);
    }
    for (PseudoMove move : moves) {
      PseudoMove new_move = new PseudoMove("psnull", "psnull");
      try {
        Integer.parseInt(move.src);
        new_move.src = new String(move.src);
      } catch (NumberFormatException e) {
        if (now_name.containsKey(move.src)) {
          new_move.src = new String(now_name.get(move.src));
        } else {
          if ((!CheckLit(move.src)) || (is_global.containsKey(move.src))) {
            new_move.src = new String(move.src);
          } else {
            new_move.src = new String("%reg$" + (++machine.tmp_time));
            now_name.put(move.src, new_move.src);
          }
        }
      }
      if (now_name.containsKey(move.des)) {
        new_move.des = new String(now_name.get(move.des));
      } else {
        if ((!CheckLit(move.des)) || (is_global.containsKey(move.des))) {
          new_move.des = new String(move.des);
        } else {
          new_move.des = new String("%reg$" + (++machine.tmp_time));
          now_name.put(move.des, new_move.des);
        }
      }
      return_value.moves.add(new_move);
    }
    return return_value;
  }

  @Override
  public void AliveUseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    for (PseudoMove move : moves) {
      if (!use.containsKey(move.des)) {
        move.dead = true;
      } else {
        move.dead = false;
        def.put(move.des, null);
        try {
          Integer.parseInt(move.src);
        } catch (NumberFormatException e) {
          if (CheckLit(move.src)) {
            if (!def.containsKey(move.src)) {
              use.put(move.src, null);
            }
          } else {
            if (move.src.equals("true")) {
              move.src = "1";
            } else {
              move.src = "0";
            }
          }
        }
      }
    }
    return;
  }
}