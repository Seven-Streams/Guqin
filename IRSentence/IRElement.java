package IRSentence;

public class IRElement extends IRCode{
  public String output = null;
  public String now_type = null;
  public String src = null;
  public int num = 0;
  @Override public void CodePrint() {
    System.out.println(output + " = getelementptr " + now_type + " , ptr " + src + " , i32 " + Integer.toString(num));
    return;
  }
}
