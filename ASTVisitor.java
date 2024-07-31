import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import nodes.*;

@SuppressWarnings("CheckReturnValue")
public class ASTVisitor extends guqinBaseVisitor<ASTNode> {
	DimensionNode universal_dnode = new DimensionNode();

	@Deprecated
	public ASTNode visitId(guqinParser.IdContext ctx) {
		return visitChildren(ctx);
	}

	// Done.
	@Override
	public ProgNode visitProg(guqinParser.ProgContext ctx) {
		ProgNode res = new ProgNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			res.trees.add(visit(ctx.getChild(i)));
		}
		return res;
	}

	@Override
	public DimensionNode visitDimension(guqinParser.DimensionContext ctx) {
		int cnt = 0;
		DimensionNode res = new DimensionNode();
		if (ctx.expr() != null) {
			cnt++;
			res.dim_expr.put(1, visit(ctx.expr()));
		}
		res.dim = cnt;
		universal_dnode = res;
		return res;
	}

	@Override
	public DimensionNode visitMust_dimension(guqinParser.Must_dimensionContext ctx) {
		DimensionNode res = new DimensionNode();
		res.dim = 1;
		res.dim_expr.put(1, visit(ctx.expr()));
		universal_dnode = res;
		return res;
	}

	@Override
	public ASTNode visitTypepair(guqinParser.TypepairContext ctx) {
		IdNode res = new IdNode();
		res.id = ctx.id().getText();
		res.type = ctx.real_type().getText();
		ASTNode dims = visit(ctx.dimensions());
		res.dim = dims.dim;
		return res;
	}

	@Override
	public DimensionNode visitDimensions(guqinParser.DimensionsContext ctx) {
		DimensionNode res = new DimensionNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			visit(ctx.getChild(i));
			if (universal_dnode.dim_expr.containsKey(1)) {
				res.dim_expr.put(i + 1, universal_dnode.dim_expr.get(1));
			}
		}
		res.dim = ctx.getChildCount();
		return res;
	}

	@Override
	public ASTNode visitDimensions_choose(guqinParser.Dimensions_chooseContext ctx) {
		DimensionNode res = new DimensionNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			visit(ctx.getChild(i));
			if (universal_dnode.dim_expr.containsKey(1)) {
				res.dim_expr.put(i + 1, universal_dnode.dim_expr.get(1));
			}
		}
		res.dim = ctx.getChildCount();
		return res;
	}

	@Override
	public ASTNode visitDimensions_declar(guqinParser.Dimensions_declarContext ctx) {
		DimensionNode res = new DimensionNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			visit(ctx.getChild(i));
			if (universal_dnode.dim_expr.containsKey(1)) {
				res.dim_expr.put(i + 1, universal_dnode.dim_expr.get(1));
			}
		}
		res.dim = ctx.getChildCount();
		return res;
	}

	@Override
	public ArrayNode visitArray(guqinParser.ArrayContext ctx) {
		ArrayNode res = new ArrayNode();
		res.dim = 1;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				res.elements.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public MultiarrayNode visitMultiarray(guqinParser.MultiarrayContext ctx) {
		MultiarrayNode res = new MultiarrayNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.MultiarrayContext ||
					ctx.getChild(i) instanceof guqinParser.ArrayContext) {
				res.elements.add(visit(ctx.getChild(i)));
			}
		}
		res.dim = res.elements.get(0).dim + 1;
		return res;
	}

	@Override
	public ASTNode visitReal_type(guqinParser.Real_typeContext ctx) {
		ASTNode res = new ASTNode();
		res.dim = 0;
		res.type = ctx.getText();
		return res;
	}

	@Deprecated
	public ASTNode visitArgs(guqinParser.ArgsContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public FuncNode visitFunc(guqinParser.FuncContext ctx) {
		FuncNode res = new FuncNode();
		res.dim = 0;
		if (ctx.real_type() == null) {
			res.type = "void";
		} else {
			res.type = ctx.real_type().getText();
			ASTNode check = visit(ctx.dimensions());
			res.dim = check.dim;
		}
		res.id = ctx.id().getText();
		guqinParser.ArgsContext args = ctx.args();
		for (int i = 0; i < args.getChildCount(); i++) {
			if (args.getChild(i) instanceof guqinParser.TypepairContext) {
				ASTNode arg = visit(args.getChild(i));
				res.args.add(arg);
			}
		}
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.StatContext) {
				res.stats.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public FuncNode visitConstruct_func(guqinParser.Construct_funcContext ctx) {
		FuncNode res = new FuncNode();
		res.dim = 0;
		res.type = ctx.id().getText();
		res.id = ctx.id().getText();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.StatContext) {
				res.stats.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public ClassNode visitClassdef(guqinParser.ClassdefContext ctx) {
		ClassNode res = new ClassNode();
		res.name = ctx.id().getText();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.Construct_funcContext) {
				if (res.construct != null) {
					throw new IllegalArgumentException("Re-definition of construct function.");
				} else {
					res.construct = visit(ctx.getChild(i));
				}
			}
			if (ctx.getChild(i) instanceof guqinParser.FuncContext
					|| ctx.getChild(i) instanceof guqinParser.Local_declarstatContext) {
				res.member.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public FuncallNode visitFuncall(guqinParser.FuncallContext ctx) {
		FuncallNode res = new FuncallNode();
		res.name = ctx.id().getText();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				res.args.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public SingleNode visitBef(guqinParser.BefContext ctx) {
		SingleNode res = new SingleNode();
		res.symbol = ctx.op.getText();
		res.value = visit(ctx.expr());
		return res;
	}

	@Override
	public ASTNode visitIdexprs(guqinParser.IdexprsContext ctx) {
		return visit(ctx.idexpr());
	}

	@Override
	public ASTNode visitFuncallexpr(guqinParser.FuncallexprContext ctx) {
		FuncallNode res = new FuncallNode();
		if (ctx.idexpr() != null) {
			res.from = visit(ctx.idexpr());
		}
		guqinParser.FuncallContext to_check = ctx.funcall();
		res.name = to_check.id().getText();
		for (int i = 0; i < to_check.getChildCount(); i++) {
			if (to_check.getChild(i) instanceof guqinParser.ExprContext) {
				res.args.add(visit(to_check.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public DoubleNode visitMuldivmod(guqinParser.MuldivmodContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = ctx.op.getText();
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public DoubleNode visitBor(guqinParser.BorContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = "|";
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public InnerNode visitStrord(guqinParser.StrordContext ctx) {
		InnerNode res = new InnerNode();
		res.name = "ord";
		res.args.add(visit(ctx.expr(0)));
		res.args.add(visit(ctx.expr(1)));
		res.args.add(visit(ctx.expr(2)));
		return res;
	}

	@Override
	public DoubleNode visitShift(guqinParser.ShiftContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = ctx.op.getText();
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public InnerNode visitStrint(guqinParser.StrintContext ctx) {
		InnerNode res = new InnerNode();
		res.name = ctx.op.getText();
		res.args.add(visit(ctx.expr()));
		return res;
	}

	@Override
	public ASTNode visitIdexpr(guqinParser.IdexprContext ctx) {
		MemNode res = new MemNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				res.calling.add(visit(ctx.getChild(i)));
			}
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				res.dims.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public ASTNode visitBan(guqinParser.BanContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = "&";
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitAnd(guqinParser.AndContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = "&&";
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitXor(guqinParser.XorContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = "^";
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitStr_lit(guqinParser.Str_litContext ctx) {
		LiterNode res = new LiterNode();
		res.type = "string";
		res.dim = 0;
		res.value = ctx.getText();
		res.value = res.value.substring(1, res.value.length() - 1);
		return res;
	}

	@Override
	public ASTNode visitOrder(guqinParser.OrderContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = ctx.op.getText();
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitPar(guqinParser.ParContext ctx) {
		return visit(ctx.expr());
	}

	@Override
	public ASTNode visitNew(guqinParser.NewContext ctx) {
		return visit(ctx.newexpr());
	}

	@Override
	public ASTNode visitOr(guqinParser.OrContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = "||";
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitAddmin(guqinParser.AddminContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = ctx.op.getText();
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitFalse(guqinParser.FalseContext ctx) {
		LiterNode res = new LiterNode();
		res.type = "bool";
		res.dim = 0;
		res.value = "false";
		return res;
	}

	@Override
	public ASTNode visitThis(guqinParser.ThisContext ctx) {
		LiterNode res = new LiterNode();
		res.type = "this";
		res.dim = 0;
		res.value = "this";
		return res;
	}

	@Override
	public ASTNode visitSubstr(guqinParser.SubstrContext ctx) {
		InnerNode res = new InnerNode();
		res.name = "substring";
		res.args.add(visit(ctx.expr(0)));
		res.args.add(visit(ctx.expr(1)));
		res.args.add(visit(ctx.expr(2)));
		return res;
	}

	@Override
	public ASTNode visitAft(guqinParser.AftContext ctx) {
		SingleNode res = new SingleNode();
		res.symbol = ctx.op.getText();
		res.value = visit(ctx.expr());
		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitEqualogic(guqinParser.EqualogicContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitNull(guqinParser.NullContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitInt_lit(guqinParser.Int_litContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitTrue(guqinParser.TrueContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitFstr(guqinParser.FstrContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitAssign(guqinParser.AssignContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitThr(guqinParser.ThrContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitAssignexpr(guqinParser.AssignexprContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitFormat_string(guqinParser.Format_stringContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitArray_new(guqinParser.Array_newContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitDim_new(guqinParser.Dim_newContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitSingle_new(guqinParser.Single_newContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitLocal_declarstat(guqinParser.Local_declarstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitInnercontent(guqinParser.InnercontentContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitLoopinnercontent(guqinParser.LoopinnercontentContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitConditstat(guqinParser.ConditstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitWhilestat(guqinParser.WhilestatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitForstat(guqinParser.ForstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitCond(guqinParser.CondContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitReturnstat(guqinParser.ReturnstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitContistat(guqinParser.ContistatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitBreakstat(guqinParser.BreakstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitExprstat(guqinParser.ExprstatContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitPint(guqinParser.PintContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitPstr(guqinParser.PstrContext ctx) {
		return visitChildren(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public ASTNode visitStat(guqinParser.StatContext ctx) {
		return visitChildren(ctx);
	}
}