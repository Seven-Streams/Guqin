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
    for(int i = 0; i < reg.size(); i++) {
      System.out.print(type.get(i) + " " + reg.get(i));
      if(i != (reg.size() - 1)) {
        System.out.print(", ");
      }
    }
    System.out.println(")");
    return;
  }
}
