package IRSentence;

public class IRReturn extends IRCode {
  public String reg = null;
  public String return_type = null;
  @Override
  public void CodePrint() {
    if(reg == null) {
      System.out.println("ret void");
    } else {
      System.out.println("ret " + return_type + " " + reg);
    }
    return;
  }
}
