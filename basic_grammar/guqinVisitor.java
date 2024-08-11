// Generated from guqin.g4 by ANTLR 4.13.1
package basic_grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link guqinParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface guqinVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link guqinParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(guqinParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(guqinParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimension(guqinParser.DimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#must_dimension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMust_dimension(guqinParser.Must_dimensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#typepair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypepair(guqinParser.TypepairContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#dimensions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensions(guqinParser.DimensionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#dimensions_exist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensions_exist(guqinParser.Dimensions_existContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#dimensions_choose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensions_choose(guqinParser.Dimensions_chooseContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#dimensions_declar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimensions_declar(guqinParser.Dimensions_declarContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(guqinParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#real_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReal_type(guqinParser.Real_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(guqinParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#funcall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncall(guqinParser.FuncallContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(guqinParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by the {@code array_new}
	 * labeled alternative in {@link guqinParser#newexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_new(guqinParser.Array_newContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dim_new}
	 * labeled alternative in {@link guqinParser#newexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDim_new(guqinParser.Dim_newContext ctx);
	/**
	 * Visit a parse tree produced by the {@code single_new}
	 * labeled alternative in {@link guqinParser#newexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_new(guqinParser.Single_newContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#construct_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstruct_func(guqinParser.Construct_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#classdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassdef(guqinParser.ClassdefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bef}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBef(guqinParser.BefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id_single}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId_single(guqinParser.Id_singleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dimen}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDimen(guqinParser.DimenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code muldivmod}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMuldivmod(guqinParser.MuldivmodContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bor}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBor(guqinParser.BorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strord}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrord(guqinParser.StrordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funcallexpr}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncallexpr(guqinParser.FuncallexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code shift}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShift(guqinParser.ShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memfun}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemfun(guqinParser.MemfunContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strint}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrint(guqinParser.StrintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ban}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBan(guqinParser.BanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code thisexpr}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisexpr(guqinParser.ThisexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mem}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMem(guqinParser.MemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(guqinParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code xor}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXor(guqinParser.XorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code str_lit}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStr_lit(guqinParser.Str_litContext ctx);
	/**
	 * Visit a parse tree produced by the {@code order}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder(guqinParser.OrderContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar(guqinParser.ParContext ctx);
	/**
	 * Visit a parse tree produced by the {@code new}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew(guqinParser.NewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(guqinParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addmin}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddmin(guqinParser.AddminContext ctx);
	/**
	 * Visit a parse tree produced by the {@code false}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(guqinParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code this}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThis(guqinParser.ThisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrexpr}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrexpr(guqinParser.ArrexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code substr}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstr(guqinParser.SubstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aft}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAft(guqinParser.AftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equalogic}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualogic(guqinParser.EqualogicContext ctx);
	/**
	 * Visit a parse tree produced by the {@code null}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(guqinParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code int_lit}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_lit(guqinParser.Int_litContext ctx);
	/**
	 * Visit a parse tree produced by the {@code true}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(guqinParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fstr}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFstr(guqinParser.FstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code thr}
	 * labeled alternative in {@link guqinParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThr(guqinParser.ThrContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#assignexpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignexpr(guqinParser.AssignexprContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#global_declarstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#local_declarstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocal_declarstat(guqinParser.Local_declarstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#innercontent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnercontent(guqinParser.InnercontentContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#loopinnercontent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopinnercontent(guqinParser.LoopinnercontentContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#conditstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditstat(guqinParser.ConditstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#whilestat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhilestat(guqinParser.WhilestatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#forstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForstat(guqinParser.ForstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(guqinParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#returnstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnstat(guqinParser.ReturnstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#contistat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContistat(guqinParser.ContistatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#breakstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakstat(guqinParser.BreakstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#exprstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprstat(guqinParser.ExprstatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pint}
	 * labeled alternative in {@link guqinParser#printstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPint(guqinParser.PintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pstr}
	 * labeled alternative in {@link guqinParser#printstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPstr(guqinParser.PstrContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(guqinParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#empty_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_stat(guqinParser.Empty_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#scooped_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScooped_stat(guqinParser.Scooped_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link guqinParser#format_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormat_string(guqinParser.Format_stringContext ctx);
}