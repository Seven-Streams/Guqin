package nodes;

public class BreakNode extends StatNode{
    @Override public Mypair check() throws Exception{
      if(in_loop == 0) {
        throw new Exception("Break Sentence should be in a loop!");
      }
    return new Mypair();
  }
}
