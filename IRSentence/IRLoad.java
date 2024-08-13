package IRSentence;

import java.util.HashMap;

public class IRLoad extends IRCode {
  public String des = null;
  public String src = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.println(des + " = load " + type + ", ptr " + src);
    return;
  }

  @Override
  public void Codegen() {
    if (is_global.get(src)) {
      System.out.println("lui a0, " + "%hi(" + src.substring(1) + ")");
      System.out.println("addi a0, a0, %lo(" + src.substring(1) + ")");
      System.out.println("lw a0, 0(a0)");
    } else {
      String addr_t = relative_addr.get(src);
      System.out.println("lw a0, " + addr_t);
      System.out.println("lw a0, 0(a0)");
    }
    if (!relative_addr.containsKey(des)) {
      is_global.put(des, false);
      now_s0 += 4;
      relative_addr.put(des, Integer.toString(-now_s0) + "(s0)");
    }
    System.out.println("sw a0, " + relative_addr.get(des));
    return;
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def) {
    try {
      Integer.parseInt(src);
    } catch (NumberFormatException e) {
      if (use.containsKey(src)) {
        use.put(src, use.get(src) + 1);
      } else {
        use.put(src, 1);
      }
    }
    try {
      Integer.parseInt(des);
    } catch (NumberFormatException e) {
      if (def.containsKey(des)) {
        def.put(des, def.get(des) + 1);
      } else {
        def.put(des, 1);
      }
    }
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
    return !use.containsKey(des);
  }
}
