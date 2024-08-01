package nodes;

public class LiterNode extends ExprNode{
  public String value = null;
  @Override public Mypair check() throws Exception {
    is_left = false;
    if(type == "this") {
      if(!in_class) {
        throw new Exception("\" this \" isn't in a class.");
      }
      type = this_class;
      return new Mypair(this_class, 0);
    }
    return new Mypair(type, 0);
  }
}
