package ASTnodes;

import Composer.Composer;
import Composer.Info;
import IRSentence.IRjmp;

public class BreakNode extends StatNode {
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
    res.label = Info.loop.get(Info.loop.size() - 1).end;
    machine.generated.add(res);
    return new Info();
  }
}
