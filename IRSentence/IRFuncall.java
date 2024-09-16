package IRSentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeMap;

import Composer.Composer;
import Optimization.NameLabelPair;
import Optimization.PseudoMove;

public class IRFuncall extends IRCode {
  public static TreeMap<Integer, HashMap<Integer, Boolean>> to_save_registers = null;
  public String target_reg = null;
  public String func_name = null;
  public String func_type = null;
  public HashMap<Integer, Boolean> occupied = new HashMap<>();
  public ArrayList<String> type = new ArrayList<>();
  public ArrayList<String> reg = new ArrayList<>();

  @Override
  public void CodePrint() {
    if (target_reg != null) {
      System.out.print(target_reg + " = ");
    }
    System.out.print("call " + func_type + " @" + func_name + "(");
    for (int i = 0; i < reg.size(); i++) {
      System.out.print(" " + reg.get(i));
      if (i != (reg.size() - 1)) {
        System.out.print(", ");
      }
    }
    System.out.println(")");
    return;
  }

  @Override
  public void Codegen() {
    if (reg.size() <= 8) {
      for (int i = 0; i < reg.size(); i++) {
        try {
          int num = Integer.parseInt(reg.get(i));
          if ((num >> 12) != 0) {
            System.out.println("lui a" + i + ", " + (num >> 12));
          } else {
            System.out.println("andi a" + i + ", a" + i + ", 0");
          }
          System.out.println("addi a" + i + ", a" + i + ", " + (num & 0x00000fff));
        } catch (NumberFormatException e) {
          if (!is_global.get(reg.get(i))) {
            String addr1 = relative_addr.get(reg.get(i));
            System.out.println("lw a" + i + ", " + addr1);
          } else {
            System.out.println("lui a" + i + ", %hi(" + reg.get(i).substring(1) + ")");
            System.out.println("addi a" + i + ", a" + i + ", " + "%lo(" + reg.get(i).substring(1) + ")");
            System.out.println("lw a" + i + ", 0(a" + i + ")");
          }
        }
      }
    } else {
      for (int i = 8; i < reg.size(); i++) {
        try {
          int num = Integer.parseInt(reg.get(i));
          if ((num >> 12) != 0) {
            System.out.println("lui a1" + ", " + (num >> 12));
          } else {
            System.out.println("andi a1, a1, 0");
          }
          System.out.println("addi a1, a1, " + (num & 0x00000fff));
        } catch (NumberFormatException e) {
          if (!is_global.get(reg.get(i))) {
            String addr1 = relative_addr.get(reg.get(i));
            System.out.println("lw sp, " + addr1);
          } else {
            System.out.println("lui sp, %hi(" + reg.get(i).substring(1) + ")");
            System.out.println("addi sp, sp, " + "%lo(" + reg.get(i).substring(1) + ")");
            System.out.println("lw sp, 0(sp)");
          }
        }
        System.out.println("sw a1, " + ((i - 8) * 4) + "(sp)");
      }
      for (int i = 0; i < 8; i++) {
        try {
          int num = Integer.parseInt(reg.get(i));
          if ((num >> 12) != 0) {
            System.out.println("lui a" + i + ", " + (num >> 12));
          } else {
            System.out.println("andi a" + i + ", a" + i + ", 0");
          }
          System.out.println("addi a" + i + ", a" + i + ", " + (num & 0x00000fff));
        } catch (NumberFormatException e) {
          String addr1 = relative_addr.get(reg.get(i));
          System.out.println("lw a" + i + ", " + addr1);
        }
      }
    }
    System.out.println("call " + func_name);
    if (target_reg != null) {
      if (!relative_addr.containsKey(target_reg)) {
        is_global.put(target_reg, false);
        now_s0 += 4;
        relative_addr.put(target_reg, Integer.toString(-now_s0) + "(s0)");
      }
      System.out.println("sw a0, " + relative_addr.get(target_reg));
    }
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
    for (String arg : reg) {
      try {
        Integer.parseInt(arg);
      } catch (NumberFormatException e) {
        if (use.containsKey(arg)) {
          use.put(arg, use.get(arg) + 1);
        } else {
          use.put(arg, 1);
        }
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
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated) {
    for (int i = 0; i < reg.size(); i++) {
      while (replace.containsKey(reg.get(i))) {
        reg.set(i, new String(replace.get(reg.get(i))));
      }
    }
    while (replace.containsKey(target_reg)) {
      target_reg = new String(replace.get(target_reg));
    }
    return;
  }

  @Override
  public void UpdateSingleBlock(HashMap<String, String> single) {
    for (int i = 0; i < reg.size(); i++) {
      while (single.containsKey(reg.get(i))) {
        reg.set(i, new String(single.get(reg.get(i))));
      }
    }
    while (single.containsKey(target_reg)) {
      target_reg = new String(single.get(target_reg));
    }
    return;
  }

  @Override
  public void UpdateNames(HashMap<String, Stack<NameLabelPair>> variable_stack, HashMap<String, String> reg_value,
      int now_block) {
    for (int i = 0; i < reg.size(); i++) {
      if (variable_stack.containsKey(reg.get(i))) {
        reg.set(i, new String(variable_stack.get(reg.get(i)).peek().name));
      }
      if (reg_value.containsKey(reg.get(i))) {
        reg.set(i, new String(reg_value.get(reg.get(i))));
      }
    }
    return;
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    for (String res : reg) {
      try {
        Integer.parseInt(res);
      } catch (NumberFormatException e) {
        if (CheckLit(res) && (!is_global.containsKey(res))) {
          if (!def.containsKey(res)) {
            use.put(res, null);
          }
        }
      }
    }
    if (target_reg != null) {
      def.put(target_reg, null);
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    if (Empty_func.containsKey(func_name)) {
      return;
    }
    if (func_name.equals("array_size")) {
      if (!registers.containsKey(reg.get(0))) {
        System.out.println("Warning: try to apply size function to null.");
        return;
      }
      if (!registers.containsKey(target_reg)) {
        return;
      }
      int place = registers.get(reg.get(0));
      String from = place >= 0 ? register_name.get(place) : "t0";
      if (place < 0) {
        place = now_depth + (place * 4);
        buffer.add("lw t0, " + place + "(sp)");
      }
      place = registers.get(target_reg);
      String to = place >= 0 ? register_name.get(place) : "t0";
      buffer.add("lw " + to + ", -4(" + from + ")");
      if (place < 0) {
        place = now_depth + (place * 4);
        buffer.add("sw t0, " + place + "(sp)");
      }
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
    int extra = Integer.max(0, reg.size() - 8);
    ArrayList<String> to_save = new ArrayList<>();
    HashMap<Integer, Boolean> busy_sreg = new HashMap<>();
    for (Integer reg_num : to_save_registers.get(sentence_number).keySet()) {
      if ((reg_num <= 11)) {
        busy_sreg.put(reg_num, null);
        continue;
      } else {
        to_save.add(register_name.get(reg_num));
      }
    }
    for (int i = 0; i < to_save.size(); i++) {
      buffer.add("sw " + to_save.get(i) + "," + ((i + extra) * 4) + "(sp)");
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
    if (func_name.equals("getInt") || func_name.equals("toString") || func_name.equals("print")
        || func_name.equals("printInt") || func_name.equals("printlnInt") || func_name.equals("getString")
        || func_name.equals("string_substring") || func_name.equals("string_ord") || func_name.equals("string_cat")
        || func_name.equals("ptr_array") || func_name.equals("int_array") || func_name.equals("string_copy")) {
      switch (func_name) {
        case ("string_copy"): {
          buffer.add("addi sp, sp, -16");
          if (busy_sreg.containsKey(10)) {
            buffer.add("sw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 4(sp)");
          }
          buffer.add("mv s10, a0");
          buffer.add("call strlen");
          buffer.add("addi a0, a0, 1");
          buffer.add("call malloc");
          buffer.add("mv s11, a0");
          buffer.add("mv a1, s10");
          buffer.add("call strcpy");
          buffer.add("mv a0, s11");
          if (busy_sreg.containsKey(10)) {
            buffer.add("lw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 4(sp)");
          }
          buffer.add("addi sp, sp, 16");
          break;
        }
        case ("int_array"): {
          buffer.add("addi sp, sp, -16");
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 8(sp)");
          }
          buffer.add("mv s11, a0");
          buffer.add("slli a0, a0, 2");
          buffer.add("addi a0, a0, 4");
          buffer.add("call malloc");
          buffer.add("addi a1, a0, 4");
          buffer.add("sw s11, 0(a0)");
          buffer.add("mv a0, a1");
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 8(sp)");
          }
          buffer.add("addi sp, sp, 16");
          break;
        }
        case ("ptr_array"): {
          buffer.add("addi sp, sp, -16");
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 8(sp)");
          }
          buffer.add("mv s11, a0");
          buffer.add("slli a0, a0, 2");
          buffer.add("addi a0, a0, 4");
          buffer.add("call malloc");
          buffer.add("addi a1, a0, 4");
          buffer.add("sw s11, 0(a0)");
          buffer.add("mv a0, a1");
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 8(sp)");
          }
          buffer.add("addi sp, sp, 16");
          break;
        }
        case ("string_cat"): {
          buffer.add(" addi sp, sp, -16");
          if (busy_sreg.containsKey(10)) {
            buffer.add("sw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 4(sp)");
          }
          if (busy_sreg.containsKey(9)) {
            buffer.add("sw s9, 0(sp)");
          }
          buffer.add("mv s9, a1");
          buffer.add("mv s11, a0");
          buffer.add("call strlen");
          buffer.add("mv s10, a0");
          buffer.add("mv a0, s9");
          buffer.add("call strlen");
          buffer.add("add  a0, a0, s10");
          buffer.add("addi a0, a0, 1");
          buffer.add("call malloc");
          buffer.add("mv s10, a0");
          buffer.add("mv a1, s11");
          buffer.add("call strcpy");
          buffer.add("mv a0, s10");
          buffer.add("mv a1, s9");
          buffer.add("call strcat");
          buffer.add("mv a0, s10");
          if (busy_sreg.containsKey(10)) {
            buffer.add("lw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 4(sp)");
          }
          if (busy_sreg.containsKey(9)) {
            buffer.add("lw s9, 0(sp)");
          }
          buffer.add("addi sp, sp, 16");
          break;
        }
        case ("string_ord"): {
          buffer.add("add	a0, a0, a1");
          buffer.add("lbu	a0, 0(a0)");
          break;
        }
        case ("string_substring"): {
          buffer.add("addi sp, sp, -32");
          if (busy_sreg.containsKey(10)) {
            buffer.add("sw s10, 24(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 20(sp)");
          }
          if (busy_sreg.containsKey(9)) {
            buffer.add("sw s9, 16(sp)");
          }
          if (busy_sreg.containsKey(8)) {
            buffer.add("sw s8, 12(sp)");
          }
          buffer.add("mv s9, a2");
          buffer.add("mv s11, a1");
          buffer.add("mv s8, a0");
          buffer.add("li a0, 1024");
          buffer.add("call malloc");
          buffer.add("mv s10, a0");
          buffer.add("add  a1, s8, s11");
          buffer.add("call strcpy");
          buffer.add("sub a0, s9, s11");
          buffer.add("add a0, a0, s10");
          buffer.add("sb zero, 0(a0)");
          buffer.add("mv a0, s10");
          if (busy_sreg.containsKey(10)) {
            buffer.add("lw s10, 24(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 20(sp)");
          }
          if (busy_sreg.containsKey(9)) {
            buffer.add("lw s9, 16(sp)");
          }
          if (busy_sreg.containsKey(8)) {
            buffer.add("lw s8, 12(sp)");
          }
          buffer.add("addi sp, sp, 32");
          break;
        }
        case ("getString"): {
          buffer.add("addi sp, sp, -16");
          if (busy_sreg.containsKey(10)) {
            buffer.add("sw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 4(sp)");
          }
          buffer.add("lui  a0, 1");
          buffer.add("call malloc");
          buffer.add("mv s10, a0");
          buffer.add("lui  a0, %hi(.L.str)");
          buffer.add("addi a0, a0, %lo(.L.str)");
          buffer.add("mv a1, s10");
          buffer.add("call scanf");
          buffer.add("mv a0, s10");
          buffer.add("call strlen");
          buffer.add("addi a0, a0, 1");
          buffer.add("call malloc");
          buffer.add("mv s11, a0");
          buffer.add("mv a1, s10");
          buffer.add("call strcpy");
          buffer.add("mv a0, s10");
          buffer.add("call free");
          buffer.add("mv a0, s11");
          if (busy_sreg.containsKey(10)) {
            buffer.add("lw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 4(sp)");
          }
          buffer.add("addi sp, sp, 16");

          break;
        }
        case ("getInt"): {
          buffer.add("addi	sp, sp, -16");
          buffer.add("lui	a0, %hi(.L.str.1)");
          buffer.add("addi	a0, a0, %lo(.L.str.1)");
          buffer.add("addi	a1, sp, 4");
          buffer.add("call	scanf");
          buffer.add("lw	a0, 4(sp)");
          buffer.add("addi	sp, sp, 16");
          break;
        }
        case ("toString"): {
          buffer.add("addi	sp, sp, -16");
          if (busy_sreg.containsKey(10)) {
            buffer.add("sw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("sw s11, 4(sp)");
          }
          buffer.add("mv  s10, a0");
          buffer.add("li	a0, 15");
          buffer.add("call	malloc");
          buffer.add("mv  s11, a0");
          buffer.add("lui	a1, %hi(.L.str.1)");
          buffer.add("addi	a1, a1, %lo(.L.str.1)");
          buffer.add("mv  a2, s10");
          buffer.add("call	sprintf");
          buffer.add("mv  a0, s11");
          if (busy_sreg.containsKey(10)) {
            buffer.add("lw s10, 8(sp)");
          }
          if (busy_sreg.containsKey(11)) {
            buffer.add("lw s11, 4(sp)");
          }
          buffer.add("addi	sp, sp, 16");
          break;
        }
        case ("print"): {
          buffer.add("mv a1, a0");
          buffer.add("lui	a0, %hi(.L.str)");
          buffer.add("addi	a0, a0, %lo(.L.str)");
          buffer.add("call	printf");
          break;
        }
        case ("printInt"): {
          buffer.add("mv  a1, a0");
          buffer.add("lui	a0, %hi(.L.str.1)");
          buffer.add("addi	a0, a0, %lo(.L.str.1)");
          buffer.add("call	printf");
          break;
        }
        case ("printlnInt"): {
          buffer.add("mv  a1, a0");
          buffer.add("lui	a0, %hi(.L.str.2)");
          buffer.add("addi	a0, a0, %lo(.L.str.2)");
          buffer.add("call	printf");
        }
      }
    } else {
      buffer.add("call " + func_name);
    }
    if (target_reg != null) {
      if (registers.containsKey(target_reg)) {
        int value = registers.get(target_reg);
        if (value >= 0) {
          if (!register_name.get(value).equals("a0")) {
            buffer.add("mv " + register_name.get(value) + ", a0");
          }
        } else {
          value = now_depth + (value * 4);
          if ((value >> 11) == 0) {
            buffer.add("sw a0, " + value + "(sp)");
          } else {
            buffer.add("li t1, " + value);
            buffer.add("add t1, t1, sp");
            buffer.add("sw a0, 0(t1)");
          }
        }
      }
    }
    for (int i = 0; i < to_save.size(); i++) {
      if ((target_reg != null) && registers.containsKey(target_reg) && (registers.get(target_reg) >= 0)
          && to_save.get(i).equals(register_name.get(registers.get(target_reg)))) {
        continue;
      }
      buffer.add("lw " + to_save.get(i) + "," + ((i + extra) * 4) + "(sp)");
    }
    return;
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRFuncall return_value = new IRFuncall();
    return_value.func_name = func_name;
    return_value.func_type = func_type;
    if (target_reg != null) {
      if (is_global.containsKey(target_reg)) {
        return_value.target_reg = target_reg;
      } else {
        if (now_name.containsKey(target_reg)) {
          return_value.target_reg = now_name.get(target_reg);
        } else {
          return_value.target_reg = "%reg$" + (++machine.tmp_time);
          now_name.put(target_reg, return_value.target_reg);
        }
      }
    }
    for (String reg_v : reg) {
      try {
        Integer.parseInt(reg_v);
        return_value.reg.add(reg_v);
      } catch (NumberFormatException e) {
        if ((!CheckLit(reg_v)) || is_global.containsKey(reg_v)) {
          return_value.reg.add(reg_v);
        } else {
          if (now_name.containsKey(reg_v)) {
            return_value.reg.add(now_name.get(reg_v));
          } else {
            String tmp = "%reg$" + (++machine.tmp_time);
            return_value.reg.add(tmp);
            now_name.put(reg_v, tmp);
          }
        }
      }
    }
    return return_value;
  }

  @Override
  public void GlobalConstReplace(HashMap<String, String> mapping) {
    for (int i = 0; i < reg.size(); i++) {
      if (mapping.containsKey(reg.get(i))) {
        reg.set(i, new String(mapping.get(reg.get(i))));
      }
    }
    return;
  }

  @Override
  public String ConstCheck(HashMap<String, String> replace) {
    for (int i = 0; i < reg.size(); i++) {
      if (replace.containsKey(reg.get(i))) {
        reg.set(i, new String(replace.get(reg.get(i))));
      }
    }
    return null;
  }
}