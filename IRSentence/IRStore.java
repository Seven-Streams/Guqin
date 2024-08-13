package IRSentence;

import java.util.HashMap;

public class IRStore extends IRCode {
  public String name = null;
  public String from = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.println("store " + type + " " + from + ",ptr " + name);
    return;
  }

  @Override
  public void Codegen() {
    if (!(from.equals("true") || from.equals("false") || from.equals("null"))) {
      try {
        int num = Integer.parseInt(from);
        if ((num >> 12) != 0) {
          System.out.println("lui a1, " + (num >> 12));
        } else {
          System.out.println("andi a1, a1, 0");
        }
        System.out.println("addi a1, a1, " + (num & 0x00000fff));
      } catch (NumberFormatException e) {
        String addr_from = relative_addr.get(from);
        System.out.println("lw a1, " + addr_from);
      }
    } else {
      if (from.equals("true")) {
        System.out.println("li a1, 1");
      } else {
        System.out.println("li a1, 0");
      }
    }
    if (is_global.containsKey(name)) {
      if (is_global.get(name)) {
        System.out.println("lui a0, " + "%hi(" + name.substring(1) + ")");
        System.out.println("addi a0, a0, %lo(" + name.substring(1) + ")");
        System.out.println("sw a1, 0(a0)");
      } else {
        String addr_des = relative_addr.get(name);
        System.out.println("lw a0, " + addr_des);
        System.out.println("sw a1, 0(a0)");
      }
    }
    return;
  }
  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
    if(def.containsKey(name)) {
      def.put(name, def.get(name) + 1);
    }
    if(use.containsKey(from)) {
      use.put(from, use.get(from) + 1);
    }
    return;
  }
  
  @Override
  public boolean EmptyStore(HashMap<String, Boolean> deprecated) {
    return deprecated.containsKey(name);
  }
}
