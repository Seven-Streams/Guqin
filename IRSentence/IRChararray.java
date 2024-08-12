package IRSentence;

public class IRChararray extends IRCode {
  public String value = null;
  public String reg = null;
  public int size = 0;
  //Watch out! This size contains a /00.

  @Override
  public void CodePrint() {
    System.out.print(reg + " = constant [" + Integer.toString(size) + " x i8] c\"");
    for(int i = 0; i < value.length(); i++) {
      if(value.charAt(i) != '\\') {
        System.out.print(value.charAt(i));
      } else {
        System.out.print('\\');
        i++;
        if(value.charAt(i) == '\\') {
          System.out.print('\\');
        }
        if(value.charAt(i) == '\"') {
          System.out.print("22");
        }
        if(value.charAt(i) == 'n') {
          System.out.print("0A");
        }
      }
    }
    System.out.println("\\00\"");
    return;
  }

  @Override
  public void Codegen() {
    is_global.put(reg, true);
    System.out.println(reg.substring(1) + ":");//This part is to avoid "@".
    System.out.println(".asciz " + value);
    return;
  }
}
