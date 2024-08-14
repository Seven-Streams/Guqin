package IRSentence;

import java.util.HashMap;

public class IRCode {
  public void CodePrint(){}
  public void Codegen() throws Exception{}
  public void CheckTime(HashMap<String, Integer> use, HashMap<String, Integer> def){};
  public boolean EmptyStore(HashMap<String, Integer> use){return false;}
  public boolean AssignOnce(HashMap<String, Integer> def) {return false;}
  public void UpdateAssignOnce(HashMap<String, String> replace, HashMap<String, Boolean> deprecated){}
  public boolean AssignOnceRemove(HashMap<String, Boolean> deprecated){return false;}
  public static int now_s0 = 0;
  public static int func_num = 0;
  public static int sp_length = 0;
  public static HashMap<String, Boolean> is_global = new HashMap<>();
  public static HashMap<String, String> relative_addr = new HashMap<>();
}
