package IRSentence;

import java.util.ArrayList;

public class IRClass extends IRCode {
  public ArrayList<String> types = new ArrayList<>();
  public String name = null;
  @Override
  public void CodePrint() {
    System.out.print(name + " = type {");
    for(int i = 0; i < (types.size() - 1); i++) {
      System.out.print(types.get(i) + ',');
    }
    if((types.size() - 1) >= 0) {
      System.out.print(types.get(types.size() - 1));
      System.out.println("}");
    }
    return;
  }
}
