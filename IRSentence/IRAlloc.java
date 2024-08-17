package IRSentence;

import java.util.HashMap;

import Composer.Composer;

public class IRAlloc extends IRCode {
  public String des = null;
  public String type = null;

  @Override
  public void CodePrint() {
    System.out.println(des + " = alloca " + type);
    return;
  }

  @Override
  public void Codegen() {
    is_global.put(des, false);
    now_s0 += 4;
    String res = Integer.toString(-now_s0) + "(s0)";
    relative_addr.put(des, res);
    now_s0 += 4;
    System.out.println("addi a0, s0, -" + now_s0);
    System.out.println("sw a0, " + res);
    return;
  }

  @Override
  public boolean EmptyStore(HashMap<String, Integer> use) {
   return !use.containsKey(des);
  }

  @Override
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def, Composer machine) {
    if(def.containsKey(des)) {
      def.put(des, def.get(des) + 1);
    } else {
      def.put(des, 1);
    }
  }

  @Override 
  public boolean AssignOnce(HashMap<String, Integer> def) {
    return (def.get(des) == 1);
  }

  @Override
  public boolean SingleBlockRemove(HashMap<String, String> single) {
    return single.containsKey(des);
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name) {
    System.out.println("There shouldn't be allocs!");
  }
}
