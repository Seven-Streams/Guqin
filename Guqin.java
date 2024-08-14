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
        if (args.length > 0) {
            if (args[0].equals("sema")) {
                System.exit(0);
            }
        }
        Composer Yuchuan = new Composer(AST);
        Yuchuan.translate((ProgNode) entry);
        Mem2Reg test = new Mem2Reg(Yuchuan);
        test.Optim();
        Yuchuan.LLVMOutput();
        System.exit(0);
        // Yuchuan.Codegen();
        System.exit(0);
    }
}