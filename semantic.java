import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class semantic {
    public static void main(String[] args) throws Exception {
        InputStream is =  new FileInputStream("test.txt") ;
        // InputStream is = args.length > 0 ? new FileInputStream(args[0]) : System.in;
        CharStream input = CharStreams.fromStream(is);
        guqinLexer lexer = new guqinLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        guqinParser parser = new guqinParser(tokens);
        MyVisitor visitor = new MyVisitor();
        ParseTree res = parser.prog();
        System.out.println(res.getText());
        Boolean check = visitor.visit(res);
        System.out.println(check);
    }

}