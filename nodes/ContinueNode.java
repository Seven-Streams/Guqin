package nodes;

public class ContinueNode extends StatNode {
      @Override public Mypair check() throws Exception{
      if(in_loop == 0) {
        throw new Exception("Continue sentence should be in a loop!");
      }
    return new Mypair();
  }
}
