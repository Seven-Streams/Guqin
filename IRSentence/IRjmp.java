package IRSentence;

import java.util.HashMap;

import Composer.Composer;

public class IRjmp extends IRCode {
  public int label = 0;
  public IRjmp() {
  }

  public IRjmp(int num) {
    label = num;
  }

  @Override
  public void CodePrint() {
    System.out.println("br label %b" + label);
    return;
  }

  @Override
  public void Codegen() {
    System.out.println("j b" + label);
    return;
  }

  @Override
  public void CodegenWithOptim(HashMap<String, Integer> registers, HashMap<Integer, String> register_name)
      throws Exception {
    Codegen();
  }

  @Override
  public IRCode GetInline(HashMap<String, String> now_name, HashMap<Integer, Integer> now_label, Composer machine)
      throws Exception {
    IRjmp return_value = new IRjmp();
    if(now_label.containsKey(label)) {
      return_value.label = now_label.get(label);
    } else {
      return_value.label = ++machine.label_number;
      now_label.put(label, return_value.label);
    }
    return return_value;
  }
}
