import org.antlr.v4.runtime.tree.ParseTree;

import ASTnodes.ASTNode;
import ASTnodes.ProgNode;
import Composer.Composer;
import Visitor.ASTVisitor;
import basic_grammar.guqinLexer;
import basic_grammar.guqinParser;
import Optimization.*;
import org.antlr.v4.runtime.*;
import java.io.InputStream;
import java.io.FileInputStream;

public class Guqin {

    public static void main(String[] args) throws Exception {
        InputStream is = System.in;

        CharStream input = CharStreams.fromStream(is);
        guqinLexer lexer = new guqinLexer(input);
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                    int charPositionInLine, String msg, RecognitionException e) {
                System.out.println("Invalid Identifier");
                System.exit(-1);
            }
        });
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        guqinParser parser = new guqinParser(tokens);
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                    int charPositionInLine, String msg, RecognitionException e) {
                System.out.println("Invalid Identifier");
                System.exit(-1);
            }
        });
        ParseTree tree = parser.prog();
        ASTVisitor AST = new ASTVisitor();
        ASTNode entry = AST.visit(tree);
        try {
            entry.check();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        Composer Yuchuan = new Composer(AST);
        Yuchuan.translate((ProgNode) entry);
        Optimizor adam = new Optimizor(Yuchuan);
        adam.OptimizedCodegen();
        System.exit(0);
    }
}