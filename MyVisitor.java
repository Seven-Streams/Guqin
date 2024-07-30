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
	String funcall_type;
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
		func_dim = 0;
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

	// Done.
	@Override
	public Boolean visitClassdef(guqinParser.ClassdefContext ctx) {
		in_class = true;
		boolean construct = false;
		this_type = ctx.id().getText();
		if (class_memory.containsKey(this_type)) {
			return false;
		}
		HashMap<String, Mypair> res_var = new HashMap<>();
		HashMap<String, Mypair> res_func = new HashMap<>();
		HashMap<String, ArrayList<Mypair>> res_args = new HashMap<>();
		variable_memory.add(res_var);
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.Local_declarstatContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					variable_memory.remove(variable_memory.size() - 1);
					return false;
				}
			}
		}
		class_memory.put(this_type, res_var);
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.Construct_funcContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (construct) {
					return false;
				}
				construct = true;
				construction.put(this_type, null);
			}
			if (ctx.getChild(i) instanceof guqinParser.FuncContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (res_var.containsKey(node_id)) {
					return false;
				}
				res_args.put(node_id, function_args);
				Mypair to_push = new Mypair(func_type, dim);
				res_func.put(func_type, to_push);
			}
		}
		variable_memory.remove(variable_memory.size() - 1);
		class_memory.put(this_type, res_var);
		class_func_memory.put(this_type, res_func);
		class_func_args.put(this_type, res_args);
		in_class = false;
		return true;
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
		node_id = func_id;
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

	// Done.
	@Override
	public Boolean visitTostr(guqinParser.TostrContext ctx) {
		boolean check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		node_type = "string";
		return true;
	}

	// Done.
	@Override
	public Boolean visitStrint(guqinParser.StrintContext ctx) {
		boolean check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (node_type != "string") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		node_type = "int";
		dim = 0;
		return true;
	}

	// Done.
	@Override
	public Boolean visitStrord(guqinParser.StrordContext ctx) {
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		if (node_type != "string") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		node_type = "int";
		dim = 0;
		return true;
	}

	@Override
	public Boolean visitNew(guqinParser.NewContext ctx) {
		return visitChildren(ctx);
	}

	// Done.
	@Override
	public Boolean visitThis(guqinParser.ThisContext ctx) {
		node_type = this_type;
		dim = 0;
		return in_class;
	}

	// Done.
	@Override
	public Boolean visitSubstr(guqinParser.SubstrContext ctx) {
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		if (node_type != "string") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		check = visit(ctx.expr(2));
		if (!check) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		node_type = "string";
		dim = 0;
		return true;
	}

	// Done.
	@Override
	public Boolean visitAft(guqinParser.AftContext ctx) {
		String type;
		boolean check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type = node_type;
		if (type != "int") {
			return false;
		}
		dim = 0;
		return check;
	}

	// Done.
	@Override
	public Boolean visitSingle(guqinParser.SingleContext ctx) {
		String type;
		boolean check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type = node_type;
		if (type != "int") {
			return false;
		}
		dim = 0;
		return check;
	}

	// Done.
	@Override
	public Boolean visitGetint(guqinParser.GetintContext ctx) {
		dim = 0;
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitGetstr(guqinParser.GetstrContext ctx) {
		dim = 0;
		node_type = "string";
		return true;
	}

	// Done.
	@Override
	public Boolean visitAdd(guqinParser.AddContext ctx) {
		String type1, type2;
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type2 = node_type;
		if (type1 != type2) {
			return false;
		}
		if ((type1 != "int") && (type1 != "string")) {
			return false;
		}
		dim = 0;
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
		if (dim != 0) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type2 = node_type;
		if ((type1 != "int") || (type2 != "int")) {
			return false;
		}
		node_type = "int";
		dim = 0;
		return true;
	}

	// Done.
	@Override
	public Boolean visitFstr(guqinParser.FstrContext ctx) {
		dim = 0;
		node_type = "string";
		return visitChildren(ctx);
	}

	// Done.
	@Override
	public Boolean visitMemfunc(guqinParser.MemfuncContext ctx) {
		Mypair res = new Mypair(null, 0);
		String id;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				if (res.dim != 0) {
					return false;
				}
				id = ctx.getChild(i).getText();
				for (int j = variable_memory.size() - 1; j >= 0; j--) {
					if (variable_memory.get(j).containsKey(id)) {
						res.type = variable_memory.get(j).get(id).type;
						res.dim = variable_memory.get(j).get(id).dim;
						break;
					}
				}
			}
			if (ctx.getChild(i) instanceof guqinParser.Dimensions_chooseContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				res.dim -= dim;
			}
		}
		if (dim != 0) {
			return false;
		}
		func_type = res.type;
		boolean check = visit(ctx.funcall());
		if (!check) {
			return false;
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitAssign(guqinParser.AssignContext ctx) {
		return visit(ctx.getChild(0));
	}

	@Override
	public Boolean visitNull(guqinParser.NullContext ctx) {
		return visit(ctx.getChild(0));
	}

	// Done.
	public Boolean visitAssignexpr(guqinParser.AssignexprContext ctx) {
		Mypair l_type = new Mypair(null, 0);
		String id = ctx.id().getText();
		boolean exist = false;
		for (int i = variable_memory.size() - 1; i >= 0; i--) {
			if (variable_memory.get(i).containsKey(id)) {
				l_type = new Mypair(variable_memory.get(i).get(id).type, variable_memory.get(i).get(id).dim);
				exist = true;
				break;
			}
		}
		if (!exist) {
			return false;
		}
		boolean check = visit(ctx.dimensions_choose());
		if (!check) {
			return false;
		}
		int reduced_dim = dim;
		check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (node_type != l_type.type) {
			return false;
		}
		if ((l_type.dim - reduced_dim) != dim) {
			return false;
		}
		return true;
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
		if (dim != 0) {
			return false;
		}
		String type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		int res_dim = dim;
		type = node_type;
		check = visit(ctx.expr(2));
		if (!check) {
			return false;
		}
		if (res_dim != dim) {
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
		node_type = type;
		dim = res_dim;
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
		if (!check) {
			return false;
		}
		type = node_type;
		if (type != "int") {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		dim = 0;
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
		if (dim != 0) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type2 = node_type;
		if ((type1 != "int") || (type2 != "int")) {
			return false;
		}
		dim = 0;
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitInt_lit(guqinParser.Int_litContext ctx) {
		dim = 0;
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitFalse(guqinParser.FalseContext ctx) {
		node_type = "bool";
		dim = 0;
		return true;
	}

	// Done.
	@Override
	public Boolean visitStr_lit(guqinParser.Str_litContext ctx) {
		node_type = "string";
		dim = 0;
		return true;
	}

	// Done.
	@Override
	public Boolean visitTrue(guqinParser.TrueContext ctx) {
		dim = 0;
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
		dim = 0;
		node_type = "int";
		return true;
	}

	// Done.
	@Override
	public Boolean visitIdexpr(guqinParser.IdexprContext ctx) {
		Mypair res = new Mypair(null, 0);
		String id;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				if (res.dim != 0) {
					return false;
				}
				id = ctx.getChild(i).getText();
				for (int j = variable_memory.size() - 1; j >= 0; j--) {
					if (variable_memory.get(j).containsKey(id)) {
						res.type = variable_memory.get(j).get(id).type;
						res.dim = variable_memory.get(j).get(id).dim;
						break;
					}
				}
			}
			if (ctx.getChild(i) instanceof guqinParser.Dimensions_chooseContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				res.dim -= dim;
			}
		}
		node_type = res.type;
		dim = res.dim;
		return true;
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
		int res_dim = dim;
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		type2 = node_type;
		if (type1 != type2) {
			return false;
		}
		if (res_dim != dim) {
			return false;
		}
		dim = 0;
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
		if (dim != 0) {
			return false;
		}
		type1 = node_type;
		check = visit(ctx.expr(1));
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		type2 = node_type;
		if ((type1 != "bool") || (type2 != "bool")) {
			return false;
		}
		dim = 0;
		node_type = "bool";
		return true;
	}

	public Boolean visitDimension(guqinParser.DimensionContext ctx) {
		if (ctx.getChildCount() == 0) {
			node_type = "int";
			dim = 0;
			return true;
		}
		boolean check = visit(ctx.getChild(0));
		if (!check) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		if (dim != 0) {
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
		int cnt = 0;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (node_type != "int") {
					return false;
				}
				if (dim != 0) {
					return false;
				}
				cnt++;
			}
		}
		dim = cnt;
		return true;
	}

	// Done.
	public Boolean visitDimensions_declar(guqinParser.Dimensions_declarContext ctx) {
		int cnt = 0;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (node_type != "int") {
					return false;
				}
				if (dim != 0) {
					return false;
				}
				cnt++;
			}
		}
		dim = cnt;
		return true;
	}

	// Done.
	public Boolean visitDimensions_choose(guqinParser.Dimensions_chooseContext ctx) {
		int cnt = 0;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (node_type != "int") {
					return false;
				}
				if (dim != 0) {
					return false;
				}
				cnt++;
			}
		}
		dim = cnt;
		return true;
	}

	// Done.
	@Override
	public Boolean visitOrder(guqinParser.OrderContext ctx) {
		String type1, type2;
		boolean check = visit(ctx.expr(0));
		if (!check) {
			return false;
		}
		if (dim != 0) {
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
		if (dim != 0) {
			return false;
		}
		if ((node_type != "int") && (node_type != "string")) {
			return false;
		}
		dim = 0;
		node_type = "bool";
		return true;
	}

	@Override
	public Boolean visitPint(guqinParser.PintContext ctx) {
		boolean check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		if (node_type != "int") {
			return false;
		}
		return true;
	}

	@Override
	public Boolean visitPstr(guqinParser.PstrContext ctx) {
		boolean check = visit(ctx.expr());
		if (!check) {
			return false;
		}
		if (dim != 0) {
			return false;
		}
		if (node_type != "string") {
			return false;
		}
		return true;
	}

	// Done.
	@Override
	public Boolean visitArray(guqinParser.ArrayContext ctx) {
		String type = null;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (dim != 0) {
					return false;
				}
				if (type == null) {
					type = node_type;
				}
				if (type != node_type) {
					return false;
				}
			}
		}
		dim = 1;
		node_type = type;
		return true;
	}

	// Done.
	@Override
	public Boolean visitMultiarray(guqinParser.MultiarrayContext ctx) {
		String type = null;
		int res_dim = -1;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ArrayContext
					|| ctx.getChild(i) instanceof guqinParser.MultiarrayContext) {
				boolean check = visit(ctx.getChild(i));
				if (!check) {
					return false;
				}
				if (res_dim == -1) {
					res_dim = dim;
				}
				if (type == null) {
					type = node_type;
				}
				if (res_dim != dim) {
					return false;
				}
				if (type != node_type) {
					return false;
				}
			}
		}
		dim = res_dim + 1;
		node_type = type;
		return true;
	}
}