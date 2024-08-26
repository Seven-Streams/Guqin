package IRSentence;

import java.util.ArrayList;
import java.util.HashMap;

import Composer.Composer;

public class InlineFunc extends IRCode{
  public ArrayList<IRCode> operations = new ArrayList<>();

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    InlineFunc return_value = new InlineFunc();
    for(IRCode code: operations) {
      return_value.operations.add(code.GetInline(now_name, now_label, machine));
    }
    return return_value;
  }
}
