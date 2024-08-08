package IRSentence;

import java.util.ArrayList;

public class IRFunc extends IRCode{
  public String name = null;
  public String return_type = null;
  public ArrayList<String> types = new ArrayList<>();
  public ArrayList<String> names = new ArrayList<>();

  @Override public void CodePrint() {;
    System.out.print(return_type + " @" + name + "( ");
    for(int i = 0; i < (types.size() - 1); i++) {
      System.out.print(types.get(i) + " " + names.get(i) + ", ");
    }
    if(!types.isEmpty()) {
      System.out.print(types.get(types.size() - 1) + " " + names.get(types.size() - 1));
    }
    System.out.println(") {");
    return;
  }
}
