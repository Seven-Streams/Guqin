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

	@Override
	public ASTNode visitArgs(guqinParser.ArgsContext ctx) {
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
	public ASTNode visitFunc(guqinParser.FuncContext ctx) {
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
	public ASTNode visitConstruct_func(guqinParser.Construct_funcContext ctx) {
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
	public ASTNode visitClassdef(guqinParser.ClassdefContext ctx) {
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
	public ASTNode visitTostr(guqinParser.TostrContext ctx) {
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
	public ASTNode visitFuncall(guqinParser.FuncallContext ctx) {
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
	public ASTNode visitBef(guqinParser.BefContext ctx) {
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
	public ASTNode visitMuldivmod(guqinParser.MuldivmodContext ctx) {
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
	public ASTNode visitBor(guqinParser.BorContext ctx) {
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
	public ASTNode visitStrord(guqinParser.StrordContext ctx) {
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
	public ASTNode visitShift(guqinParser.ShiftContext ctx) {
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
	public ASTNode visitStrint(guqinParser.StrintContext ctx) {
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
	public ASTNode visitIdexpr(guqinParser.IdexprContext ctx) {
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
	public ASTNode visitBan(guqinParser.BanContext ctx) {
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
	public ASTNode visitGetint(guqinParser.GetintContext ctx) {
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
	public ASTNode visitGetstr(guqinParser.GetstrContext ctx) {
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
	public ASTNode visitAnd(guqinParser.AndContext ctx) {
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
	public ASTNode visitXor(guqinParser.XorContext ctx) {
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
	public ASTNode visitStr_lit(guqinParser.Str_litContext ctx) {
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
	public ASTNode visitOrder(guqinParser.OrderContext ctx) {
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
	public ASTNode visitPar(guqinParser.ParContext ctx) {
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
	public ASTNode visitNew(guqinParser.NewContext ctx) {
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
	public ASTNode visitOr(guqinParser.OrContext ctx) {
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
	public ASTNode visitAddmin(guqinParser.AddminContext ctx) {
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
	public ASTNode visitFalse(guqinParser.FalseContext ctx) {
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
	public ASTNode visitThis(guqinParser.ThisContext ctx) {
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
	public ASTNode visitSubstr(guqinParser.SubstrContext ctx) {
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
	public ASTNode visitAft(guqinParser.AftContext ctx) {
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