import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


/*notations of types:
 * bool, int, class_name, void, string
 */

public class MyVisitor extends guqinBaseVisitor<Boolean> {
	boolean in_class = false;
	HashMap<String, HashMap<String, String>> class_memory;
	HashMap<String, HashMap<String, ArrayList<String>>> class_func_memory;
	HashMap<String, ArrayList<String>> func_memory; 
	ArrayList<HashMap<String, String>> variable_memory;
	public MyVisitor() {
        class_memory = new HashMap<>();
				class_func_memory = new HashMap<>();
				func_memory = new HashMap<>();
        variable_memory = new ArrayList<>();
        HashMap<String, String> initialMap = new HashMap<>();
        variable_memory.add(initialMap);
    }
	@Override public Boolean visitClassdefv(guqinParser.ClassdefvContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFuncv(guqinParser.FuncvContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitGlobal_declarstatv(guqinParser.Global_declarstatvContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitTypepair(guqinParser.TypepairContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitReal_type(guqinParser.Real_typeContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitArgs(guqinParser.ArgsContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFunc(guqinParser.FuncContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitConstruct_func(guqinParser.Construct_funcContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitClassdef(guqinParser.ClassdefContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFuncall(guqinParser.FuncallContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitPar(guqinParser.ParContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitTostr(guqinParser.TostrContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitStrfunc(guqinParser.StrfuncContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitNew(guqinParser.NewContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitBef(guqinParser.BefContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitMuldivmod(guqinParser.MuldivmodContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitLiter(guqinParser.LiterContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitThis(guqinParser.ThisContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitMemfunc(guqinParser.MemfuncContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitBit(guqinParser.BitContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitSubstr(guqinParser.SubstrContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitAft(guqinParser.AftContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitSingle(guqinParser.SingleContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitMemnum(guqinParser.MemnumContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitKb(guqinParser.KbContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitMember(guqinParser.MemberContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitAddminus(guqinParser.AddminusContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitId(guqinParser.IdContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFstr(guqinParser.FstrContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitLogic(guqinParser.LogicContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFuncallv(guqinParser.FuncallvContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitAssign(guqinParser.AssignContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitThr(guqinParser.ThrContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitAssignexpr(guqinParser.AssignexprContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFormat_string(guqinParser.Format_stringContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitNewexpr(guqinParser.NewexprContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitAssignstat(guqinParser.AssignstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitLocal_declarstat(guqinParser.Local_declarstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitInnercontent(guqinParser.InnercontentContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitConditstat(guqinParser.ConditstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitWhilestat(guqinParser.WhilestatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitForstat(guqinParser.ForstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitReturnstat(guqinParser.ReturnstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitContistat(guqinParser.ContistatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitBreakstat(guqinParser.BreakstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitExprstat(guqinParser.ExprstatContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitStat(guqinParser.StatContext ctx) { return visitChildren(ctx); }
}