package IRSentence;

import java.util.ArrayList;

public class IRFunc extends IRCode{
  String name = null;
  String return_type = null;
  ArrayList<String> types = new ArrayList<>();
  ArrayList<String> names = new ArrayList<>();

  @Override public void CodePrint() {
    System.out.print(types + " " + name + "( ");
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
