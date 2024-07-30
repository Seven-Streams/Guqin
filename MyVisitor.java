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
	boolean in_class;
	String this_type;
	String node_type;
	String func_type;
	String node_id;
	int dim;
	int func_dim;
	ArrayList<Mypair> function_args;
	HashMap<String, HashMap<String, Mypair>> class_memory;
	HashMap<String, HashMap<String, Mypair>> class_func_memory;
	HashMap<String, HashMap<String, ArrayList<Mypair>>> class_func_args;
	HashMap<String, ArrayList<Mypair>> func_memory;
	HashMap<String, Mypair> func_return;
	ArrayList<HashMap<String, Mypair>> variable_memory;
	HashMap<String, Boolean> construction;

	public MyVisitor() {
		function_args = new ArrayList<>();
		in_class = false;
		dim = 0;
		construction = new HashMap<>();
		class_memory = new HashMap<>();
		class_func_memory = new HashMap<>();
		class_func_args = new HashMap<>();
		func_return = new HashMap<>();
		func_memory = new HashMap<>();
		variable_memory = new ArrayList<>();
		HashMap<String, Mypair> initialMap = new HashMap<>();
		variable_memory.add(initialMap);
		class_memory.put("int", null);
		class_memory.put("string", null);
		class_memory.put("bool", null);
	}

	// refac.
	@Override
	public Boolean visitClassdef(guqinParser.ClassdefContext ctx) {
	}

	// Done.
	@Override
	public Boolean visitFunc(guqinParser.FuncContext ctx) {
		if (ctx.VOID() != null) {
			func_type = "void";
		} else {
			func_type = ctx.real_type().getText();
			if (!class_memory.containsKey(func_type)) {
				return false;
			}
			boolean check = visit(ctx.dimensions());
			if (!check) {
				return false;
			}
			func_dim = dim;
		}
		String func_id = ctx.id().getText();
		HashMap<String, Mypair> res_var = new HashMap<>();
		ArrayList<Mypair> res_args = new ArrayList<>();
		ParseTree arg_check = ctx.args();
		for (int i = 0; i < arg_check.getChildCount(); i++) {
			boolean check = visit(arg_check.getChild(i));
			if (!check) {
				return false;
			}
			if (!class_memory.containsKey(node_type)) {
				return false;
			}
			if (res_var.containsKey(node_id)) {
				return false;
			}
			Mypair to_put = new Mypair(node_type, dim);
			res_var.put(func_type, to_put);
		}
		variable_memory.add(res_var);
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.StatContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					variable_memory.remove(variable_memory.size() - 1);
					return false;
				}
			}
		}
		if (in_class) {
			function_args = res_args;
		} else {
			func_memory.put(func_id, res_args);
			Mypair function_type = new Mypair(func_type, dim);
			func_return.put(func_id, function_type);
		}
		variable_memory.remove(variable_memory.size() - 1);
		return true;
	}

	// Done.
	@Override
	public Boolean visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx) {
		String type = ctx.real_type().getText();
		if (!class_memory.containsKey(type)) {
			return false;
		}
		boolean check = visit(ctx.dimensions_declar());
		if (!check) {
			return false;
		}
		int res_dim = dim;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				String id = ctx.getChild(i).getText();
				if (variable_memory.get(variable_memory.size() - 1).containsKey(id)) {
					return false;
				}
				Mypair res = new Mypair(type, res_dim);
				variable_memory.get(variable_memory.size() - 1).put(id, res);
			} else {
				check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (node_type != type) {
					return false;
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

	// Not Done.
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

	// Done.
	@Override
	public Boolean visitAdd(guqinParser.AddContext ctx) {
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
		if ((type1 != "int") && (type1 != "string")) {
			return false;
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitMinus(guqinParser.MinusContext ctx) {
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

	// refac.
	public Boolean visitAssignexpr(guqinParser.AssignexprContext ctx) {
	}

	// Done.
	@Override
	public Boolean visitThr(guqinParser.ThrContext ctx) {
		boolean check;
		check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		if (node_type != "bool") {
			return false;
		}
		String type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		type = node_type;
		check = visit(ctx.expr(2));
		if (!check) {
			return false;
		}
		if (type != node_type) {
			return false;
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitFormat_string(guqinParser.Format_stringContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (!check) {
				return false;
			}
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				if ((node_type != "int") && (node_type != "string") && (node_type != "bool")) {
					return false;
				}
			}
		}
		return true;
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
		boolean check = visit(ctx.dimensions_declar());
		if (!check) {
			return false;
		}
		int res_dim = dim;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				String id = ctx.getChild(i).getText();
				if (variable_memory.get(variable_memory.size() - 1).containsKey(id)) {
					return false;
				}
				Mypair res = new Mypair(type, res_dim);
				variable_memory.get(variable_memory.size() - 1).put(id, res);
			} else {
				check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (node_type != type) {
					return false;
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
			if (!check) {
				return false;
			}
		}
		return true;
	}

	// Done.
	public Boolean visitCond(guqinParser.CondContext ctx) {
		if (ctx.isEmpty()) {
			node_type = "void";
			return true;
		}
		boolean check = visit(ctx.expr());
		if (node_type != "bool") {
			return false;
		}
		return check;
	}

	// Done.
	@Override
	public Boolean visitReturnstat(guqinParser.ReturnstatContext ctx) {
		boolean check = true;
		if (ctx.getChildCount() != 0) {
			check = visit(ctx.getChild(0));
		} else {
			node_type = "void";
		}
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

	// Done.
	@Override
	public Boolean visitTypepair(guqinParser.TypepairContext ctx) {
		node_type = ctx.real_type().getText();
		visit(ctx.dimensions());
		node_id = ctx.id().getText();
		return true;
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
	public Boolean visitIdexpr(guqinParser.IdexprContext ctx) {
		String id = ctx.getText();
		for (int i = variable_memory.size() - 1; i >= 0; i--) {
			if (variable_memory.get(i).containsKey(id)) {
				node_type = variable_memory.get(i).get(id).type;
				dim = variable_memory.get(i).get(id).dim;
				return true;
			}
		}
		return false;
	}

	// Done.
	@Override
	public Boolean visitId(guqinParser.IdContext ctx) {
		String id = ctx.getText();
		for (int i = variable_memory.size() - 1; i >= 0; i--) {
			if (variable_memory.get(i).containsKey(id)) {
				node_type = variable_memory.get(i).get(id).type;
				dim = variable_memory.get(i).get(id).dim;
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

	public Boolean visitDimension(guqinParser.DimensionContext ctx) {
		if (ctx.getChildCount() == 0) {
			return true;
		}
		boolean check = visit(ctx.getChild(0));
		if (!check) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		return true;
	}

	public Boolean visitMust_dimension(guqinParser.Must_dimensionContext ctx) {
		boolean check = visit(ctx.getChild(0));
		if (!check) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		return true;
	}

	public Boolean visitDimensions(guqinParser.DimensionsContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (!check) {
				return false;
			}
		}
		dim = ctx.getChildCount();
		return true;
	}

	public Boolean visitDimensions_declar(guqinParser.Dimensions_declarContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (!check) {
				return false;
			}
		}
		dim = ctx.getChildCount();
		return true;
	}

	public Boolean visitDimensions_choose(guqinParser.Dimensions_chooseContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			boolean check = visit(ctx.getChild(i));
			if (!check) {
				return false;
			}
		}
		dim = ctx.getChildCount();
		return true;
	}
}