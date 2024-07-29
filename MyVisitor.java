import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.HashMap;
import java.util.ArrayList;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
/*notations of types:
 * bool, int, class_name, void, string
 * In the string-pair hashmap, the id is the first, and the type is the second one.
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
		class_memory.put("int", null);
		class_memory.put("string", null);
		class_memory.put("bool", null);
		variable_memory.add(initialMap);
	}

	@Override
	public Boolean visitClassdefv(guqinParser.ClassdefvContext ctx) {
		in_class = true;
		HashMap<String, String> res = new HashMap<>();
		HashMap<String, ArrayList<String>> res_func_args = new HashMap<>();
		boolean construct = false;
		String id = ctx.getChild(1).getText();
		if (class_memory.containsKey(id)) {
			return false;
		}
		for (int i = 2; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.TypepairContext) {
				String type = ctx.getChild(i).getChild(0).getText();
				String mem_id = ctx.getChild(i).getChild(1).getText();
				if (!class_memory.containsKey(type)) {
					in_class = false;
					return false;
				}
				if (res.containsKey(mem_id)) {
					in_class = false;
					return false;
				}
				res.put(mem_id, type);
			}
		} // First, check all the member of the class.
		variable_memory.add(res);
		for (int i = 2; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.Construct_funcContext) {
				if (construct) {
					variable_memory.remove(variable_memory.size() - 1);
					in_class = false;
					return false;
				}
				Boolean check = visit(ctx.getChild(i));
				if (!check) {
					in_class = false;
					variable_memory.remove(variable_memory.size() - 1);
					return false;
				}
				construct = true;
			}
			if (ctx.getChild(i) instanceof guqinParser.FuncContext) {
				Boolean check = visit(ctx.getChild(i));
				String func_id = ctx.getChild(i).getChild(1).getText();
				if (!check) {
					in_class = false;
					variable_memory.remove(variable_memory.size() - 1);
					return false;
				}
				ArrayList<String> arg_types = new ArrayList<>();
				String func_type = ctx.getChild(i).getChild(0).getText();
				if (func_type != "void" && !class_memory.containsKey(func_type)) {
					variable_memory.remove(variable_memory.size() - 1);
					in_class = false;
					return false;
				}
				if (res.containsKey(func_id)) {
					variable_memory.remove(variable_memory.size() - 1);
					in_class = false;
					return false;
				}
				ParseTree func_args = ctx.getChild(i).getChild(2);
				for (int j = 0; j < func_args.getChildCount(); j++) {
					arg_types.add(func_args.getChild(j).getChild(0).getText());
				}
				res.put(func_id, func_type);
				res_func_args.put(func_type, arg_types);
			}
		}
		class_func_memory.put(id, res_func_args);
		class_memory.put(id, res);
		in_class = false;
		variable_memory.remove(variable_memory.size() - 1);
		return true;
	}

	@Override
	public Boolean visitFuncv(guqinParser.FuncvContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitGlobal_declarstatv(guqinParser.Global_declarstatvContext ctx) {
		String type = ctx.getChild(0).getText();
		Boolean type_check = class_memory.containsKey(type);
		if (!type_check) {
			return false;
		}
		for (int i = 1; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				String id = ctx.getChild(i).getText();
				if (variable_memory.get(0).containsKey(id)) {
					return false;
				} else {
					variable_memory.get(0).put(id, type);
				}
			}
		}
		return true;
	}

	@Override
	public Boolean visitTypepair(guqinParser.TypepairContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitArgs(guqinParser.ArgsContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFunc(guqinParser.FuncContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitConstruct_func(guqinParser.Construct_funcContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitClassdef(guqinParser.ClassdefContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFuncall(guqinParser.FuncallContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitPar(guqinParser.ParContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitTostr(guqinParser.TostrContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitStrfunc(guqinParser.StrfuncContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitNew(guqinParser.NewContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitThis(guqinParser.ThisContext ctx) {
		return in_class;
	}

	@Override
	public Boolean visitMemfunc(guqinParser.MemfuncContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitSubstr(guqinParser.SubstrContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitAft(guqinParser.AftContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitSingle(guqinParser.SingleContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitMemnum(guqinParser.MemnumContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitKb(guqinParser.KbContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitMember(guqinParser.MemberContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitAddminus(guqinParser.AddminusContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFstr(guqinParser.FstrContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFuncallv(guqinParser.FuncallvContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitAssign(guqinParser.AssignContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitThr(guqinParser.ThrContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitAssignexpr(guqinParser.AssignexprContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFormat_string(guqinParser.Format_stringContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitNewexpr(guqinParser.NewexprContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitAssignstat(guqinParser.AssignstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitLocal_declarstat(guqinParser.Local_declarstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitInnercontent(guqinParser.InnercontentContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitConditstat(guqinParser.ConditstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitWhilestat(guqinParser.WhilestatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitForstat(guqinParser.ForstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitReturnstat(guqinParser.ReturnstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitContistat(guqinParser.ContistatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitBreakstat(guqinParser.BreakstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitExprstat(guqinParser.ExprstatContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitStat(guqinParser.StatContext ctx) {
		return visitChildren(ctx);
	}
}