package ASTnodes;

import Composer.*;
import IRSentence.IRjmp;

public class ContinueNode extends StatNode {
  @Override
  public Mypair check() throws Exception {
    if (in_loop == 0) {
      throw new Exception("Invalid Control Flow");
    }
    return new Mypair();
  }

  @Override
  public Info GenerateIR(Composer machine) {
    IRjmp res = new IRjmp();
    res.label = Info.loop.get(Info.loop.size() - 1).iteration;
    machine.generated.add(res);
    return new Info();
  }
}
