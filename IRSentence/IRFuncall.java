package IRSentence;

import java.util.ArrayList;

public class IRFuncall extends IRCode {
  public String target_reg = null;
  public String func_name = null;
  public String func_type = null;
  public ArrayList<String> type = new ArrayList<>();
  public ArrayList<String> reg = new ArrayList<>();

  @Override
  public void CodePrint() {
    if (target_reg != null) {
      System.out.print(target_reg + " = ");
    }
    System.out.print("call " + func_type + " @" + func_name + "(");
    for (int i = 0; i < reg.size(); i++) {
      System.out.print(type.get(i) + " " + reg.get(i));
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
      System.out.println("mv a0, sp");
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
            System.out.println("lw a0, " + addr1);
          } else {
            System.out.println("lui a0, %hi(" + reg.get(i).substring(1) + ")");
            System.out.println("addi a0, a0, " + "%lo(" + reg.get(i).substring(1) + ")");
            System.out.println("lw a0, 0(a0)");
          }
        }
        System.out.println("sw a1, " + ((i - 8) * 4) + "(a0)");
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
}
