package Optimization;

import Composer.Composer;
import IRSentence.IRFunc;
import IRSentence.IRFuncend;

public class RemoveEmptyCall {
  Composer machine = null;
  public RemoveEmptyCall(Composer _machine) {
    machine = _machine;
  }
  public void CheckUnecessaryCalling() {
    for (int i = 0; i < machine.generated.size(); i++) {
      if (machine.generated.get(i) instanceof IRFunc) {
        if (machine.generated.get(i + 1) instanceof IRFuncend) {
          IRFunc.Empty_func.put(((IRFunc) machine.generated.get(i)).name, null);
        }
      }
    }
    return;
  }
}
