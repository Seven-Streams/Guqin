package IRSentence;

public class IRIcmp extends IRCode{
  public String target_reg = null;
  public String op1 = null;
  public String op2 = null;
  public String symbol = null;
  public String type = null;
  @Override public void CodePrint(){
    System.out.print(target_reg + " =  icmp ");
    switch(symbol){
      case("=="): {
        System.out.print("eq ");
        break;
      }
      case("!="): {
        System.out.print("ne ");
        break;
      }
      case(">="): {
        System.out.print("sge ");
        break;
      }
      case(">"): {
        System.out.print("sgt ");
        break;
      }
      case("<"): {
        System.out.print("slt ");
        break;
      }
      case("<="): {
        System.out.print("sle ");
        break;
      }
      default:{
        System.out.println("ERROR in ICMP!");
      }
    }
    System.out.println(type + " " + op1 + ", " + op2);
    return;
  }
}
