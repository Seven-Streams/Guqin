package IRSentence;

import java.util.HashMap;

public class IRGlobal extends IRCode{
  public String name = null;
  public String type = null;
  public String value = null;
  @Override
  public void CheckGlobal() {
    is_global.put(name, null);
  }
  @Override
  public void CodePrint() {
    if(type.equals("ptr")) {
      System.out.println(name + "= global " + type + " null");
    } else {
      if(value == null) {
      System.out.println(name + "= global " + type + " 0");
      } else {
      System.out.println(name + "= global " + type + " " + value);
      }
    }
    return;
  }

  @Override
  public void Codegen() {
    is_global.put(name, true);
    System.out.println(name.substring(1) + ":");
    if(value == null) {
    System.out.println(".word 0");
    } else {
      System.out.println(".word " + value);
    }
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    Codegen();
  }

  @Override
  public void UseDefCheck(HashMap<String, Boolean> def, HashMap<String, Boolean> use) {
    is_global.put(name, null);
  }
}
