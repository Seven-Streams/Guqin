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
	ArrayList<Integer> loops;
	boolean in_class = false;
	String this_type;
	String node_type;
	String func_type;
	HashMap<String, HashMap<String, String>> class_memory;
	HashMap<String, HashMap<String, ArrayList<String>>> class_func_memory;
	HashMap<String, ArrayList<String>> func_memory;
	ArrayList<HashMap<String, String>> variable_memory;

	public MyVisitor() {
		loops = new ArrayList<>();
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

	// Done.
	@Override
	public Boolean visitClassdef(guqinParser.ClassdefContext ctx) {
		in_class = true;
		HashMap<String, String> res = new HashMap<>();
		HashMap<String, ArrayList<String>> res_func_args = new HashMap<>();
		boolean construct = false;
		String id = ctx.getChild(1).getText();
		this_type = id;
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

	// Done.
	@Override
	public Boolean visitFunc(guqinParser.FuncContext ctx) {
		String type = ctx.getChild(0).getText();
		String id = ctx.getChild(1).getText();
		func_type = type;
		if (!class_memory.containsKey(type)) {
			return false;
		}
		if (class_memory.containsKey(id)) {
			return false;
		}
		ParseTree args = ctx.getChild(2);
		HashMap<String, String> variable_res = new HashMap<>();
		for (int i = 0; i < args.getChildCount(); i++) {
			String v_id = args.getChild(1).getText();
			String v_type = args.getChild(0).getText();
			if (class_memory.containsKey(v_type)) {
				return false;
			}
			variable_res.put(v_id, v_type);
		}
		variable_memory.add(variable_res);
		for (int i = 3; i < ctx.getChildCount(); i++) {
			Boolean check = visit(ctx.getChild(i));
			if (!check) {
				variable_memory.remove(variable_memory.size() - 1);
				return false;
			}
		}
		variable_memory.remove(variable_memory.size() - 1);
		return true;
	}

	// Done.
	@Override
	public Boolean visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx) {
		String type = ctx.getChild(0).getText();
		Boolean type_check = class_memory.containsKey(type);
		if (!type_check) {
			return false;
		}
		for (int i = 1; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				boolean value_check = visit(ctx.getChild(i));
				if (!value_check) {
					return false;
				}
				if (node_type != type) {
					return false;
				}
			}
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

	// Done.
	@Override
	public Boolean visitConstruct_func(guqinParser.Construct_funcContext ctx) {
		String type = ctx.getChild(0).getText();
		if (type != this_type) {
			return false;
		}
		variable_memory.add(new HashMap<>());
		for (int i = 3; i < ctx.getChildCount(); i++) {
			Boolean check = visit(ctx.getChild(i));
			if (!check) {
				variable_memory.remove(variable_memory.size() - 1);
				return false;
			}
		}
		variable_memory.remove(variable_memory.size() - 1);
		return true;
	}

//Not Done.
	@Override
	public Boolean visitFuncall(guqinParser.FuncallContext ctx) {
		return visitChildren(ctx);
	}

	// Done.
	@Override
	public Boolean visitPar(guqinParser.ParContext ctx) {
		return visit(ctx.getChild(0));
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

	// Done.
	@Override
	public Boolean visitThis(guqinParser.ThisContext ctx) {
		return in_class;
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

	// Done.
	@Override
	public Boolean visitAssign(guqinParser.AssignContext ctx) {
		return visit(ctx.getChild(0));
	}

	// Done.
	public Boolean visitAssignexpr(guqinParser.AssignexprContext ctx) {
		String id = ctx.getChild(0).getText();
		String type = null;
		for (int i = ctx.getChildCount() - 1; i >= 0; i--) {
			if (variable_memory.get(i).containsKey(id)) {
				type = variable_memory.get(i).get(id);
				break;
			}
		}
		if (type == null) {
			return false;
		}
		boolean check = visit(ctx.expr());
		if (node_type != type) {
			return false;
		}
		return check;
	}

	@Override
	public Boolean visitThr(guqinParser.ThrContext ctx) {
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

	// Done.
	@Override
	public Boolean visitLocal_declarstat(guqinParser.Local_declarstatContext ctx) {
		String type = ctx.real_type().getText();
		if (!class_memory.containsKey(type)) {
			return false;
		}
		for (int i = 1; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				boolean value_check = visit(ctx.getChild(i));
				if (!value_check) {
					return false;
				}
				if (node_type != type) {
					return false;
				}
			}
		}
		for (int i = 1; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				String id = ctx.getChild(i).getText();
				if (variable_memory.get(variable_memory.size() - 1).containsKey(id)) {
					return false;
				} else {
					variable_memory.get(variable_memory.size() - 1).put(id, type);
				}
			}
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitInnercontent(guqinParser.InnercontentContext ctx) {
		variable_memory.add(new HashMap<>());
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (!check) {
				variable_memory.remove(variable_memory.size() - 1);
				return false;
			}
		}
		variable_memory.remove(variable_memory.size() - 1);
		return true;
	}

	// Done.
	@Override
	public Boolean visitConditstat(guqinParser.ConditstatContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				if (node_type != "bool") {
					return false;
				}
			}
			if (!check) {
				return false;
			}
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitWhilestat(guqinParser.WhilestatContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				if (node_type != "bool") {
					return false;
				}
			}
			if (!check) {
				return false;
			}
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitForstat(guqinParser.ForstatContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				if (node_type != "bool") {
					return false;
				}
			}
			if (!check) {
				return false;
			}
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitReturnstat(guqinParser.ReturnstatContext ctx) {
		boolean check = visit(ctx.expr());
		if (node_type != func_type) {
			return false;
		}
		return check;
	}

	// Done.
	@Override
	public Boolean visitContistat(guqinParser.ContistatContext ctx) {
		return true;
	}

	// Done.
	@Override
	public Boolean visitBreakstat(guqinParser.BreakstatContext ctx) {
		return true;
	}

	// Done.
	@Override
	public Boolean visitLoopinnercontent(guqinParser.LoopinnercontentContext ctx) {
		variable_memory.add(new HashMap<>());
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (!check) {
				variable_memory.remove(variable_memory.size() - 1);
				return false;
			}
		}
		variable_memory.remove(variable_memory.size() - 1);
		return true;
	}

	// Done.
	@Override
	public Boolean visitExprstat(guqinParser.ExprstatContext ctx) {
		return visit(ctx.expr());
	}

	// Done.
	@Override
	public Boolean visitStat(guqinParser.StatContext ctx) {
		return visit(ctx.getChild(0));
	}

	// deprecated.
	@Override
	public Boolean visitTypepair(guqinParser.TypepairContext ctx) {
		return visitChildren(ctx);
	}

	// deprecated.
	@Override
	public Boolean visitReal_type(guqinParser.Real_typeContext ctx) {
		return visitChildren(ctx);
	}

	// deprecated.
	@Override
	public Boolean visitArgs(guqinParser.ArgsContext ctx) {
		return visitChildren(ctx);
	}

	// Done.
	@Override
	public Boolean visitBef(guqinParser.BefContext ctx) {
		String type;
		boolean check = visit(ctx.expr());
		type = node_type;
		if (type != "int") {
			return false;
		}
		return check;
	}

	// Done.
	@Override
	public Boolean visitMuldivmod(guqinParser.MuldivmodContext ctx) {
		String type1, type2;
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		type2 = node_type;
		if ((type1 != "int") || (type2 != "int")) {
			return false;
		}
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitInt_lit(guqinParser.Int_litContext ctx) {
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitFalse(guqinParser.FalseContext ctx) {
		node_type = "bool";
		return true;
	}

	// Done.
	@Override
	public Boolean visitStr_lit(guqinParser.Str_litContext ctx) {
		node_type = "string";
		return true;
	}

	// Done.
	@Override
	public Boolean visitTrue(guqinParser.TrueContext ctx) {
		node_type = "bool";
		return true;
	}

	// Done.
	@Override
	public Boolean visitBit(guqinParser.BitContext ctx) {
		String type1, type2;
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		type2 = node_type;
		if ((type1 != "int") || (type2 != "int")) {
			return false;
		}
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitId(guqinParser.IdContext ctx) {
		String id = ctx.getText();
		for (int i = variable_memory.size() - 1; i >= 0; i--) {
			if (variable_memory.get(i).containsKey(id)) {
				node_type = variable_memory.get(i).get(id);
				return true;
			}
		}
		return false;
	}

	// Done.
	@Override
	public Boolean visitEqualogic(guqinParser.EqualogicContext ctx) {
		String type1, type2;
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		type2 = node_type;
		if (type1 != type2) {
			return false;
		}
		node_type = "bool";
		return true;
	}

	// Done.
	@Override
	public Boolean visitBoologic(guqinParser.BoologicContext ctx) {
		String type1, type2;
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		type2 = node_type;
		if ((type1 != "bool") || (type2 != "bool")) {
			return false;
		}
		node_type = "bool";
		return true;
	}
}