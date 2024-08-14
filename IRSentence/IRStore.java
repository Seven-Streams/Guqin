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
    try {
      Integer.parseInt(name);
    } catch (NumberFormatException e) {
      if (name.length() <= 4) {
        if (def.containsKey(name)) {
          def.put(name, def.get(name) + 1);
        } else {
          def.put(name, 1);
        }
      } else {
        if (name.contains("$")) {
          if (use.containsKey(name)) {
            use.put(name, use.get(name) + 1);
          } else {
            use.put(name, 1);
          }
        } else {
          if (def.containsKey(name)) {
            def.put(name, def.get(name) + 1);
          } else {
            def.put(name, 1);
          }
        }
      }
    }
    try {
      Integer.parseInt(from);
    } catch (NumberFormatException e) {
      if (use.containsKey(from)) {
        use.put(from, use.get(from) + 1);
      } else {
        use.put(from, 1);
      }
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
    return !use.containsKey(name);
  }

  @Override
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean>deprecated) {
    if(deprecated.containsKey(name)) {
      replace.put(name, from);
    }
    while(replace.containsKey(from)) {
      from = new String(replace.get(from));
    }
    return;
  }

  @Override
  public boolean AssignOnceRemove(HashMap<String, Boolean> deprecated) {
    if(deprecated.containsKey(name)) {
      return true;
    } else {
      return false;
    }
  }
}
