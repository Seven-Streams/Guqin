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
				res.dim_expr.put(i, universal_dnode.dim_expr.get(1));
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
				res.dim_expr.put(i, universal_dnode.dim_expr.get(1));
			}
		}
		res.dim = ctx.getChildCount();
		universal_dnode = res;
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

	@Deprecated
	public ASTNode visitReal_type(guqinParser.Real_typeContext ctx) {
		return visitChildren(ctx);
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
		if (args != null) {
			for (int i = 0; i < args.getChildCount(); i++) {
				if (args.getChild(i) instanceof guqinParser.TypepairContext) {
					ASTNode arg = visit(args.getChild(i));
					res.args.add(arg);
				}
			}
		}
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.StatContext
					|| ctx.getChild(i) instanceof guqinParser.ReturnstatContext) {
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
		res.symbol_left = true;
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
				res.calling.add(ctx.getChild(i).getText());
			}
			if (ctx.getChild(i) instanceof guqinParser.Dimensions_chooseContext) {
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
	public SingleNode visitAft(guqinParser.AftContext ctx) {
		SingleNode res = new SingleNode();
		res.symbol_left = false;
		res.symbol = ctx.op.getText();
		res.value = visit(ctx.expr());
		return res;
	}

	@Override
	public ASTNode visitEqualogic(guqinParser.EqualogicContext ctx) {
		DoubleNode res = new DoubleNode();
		res.symbol = ctx.op.getText();
		res.value1 = visit(ctx.expr(0));
		res.value2 = visit(ctx.expr(1));
		return res;
	}

	@Override
	public ASTNode visitNull(guqinParser.NullContext ctx) {
		LiterNode res = new LiterNode();
		res.type = "null";
		res.value = "null";
		res.dim = 0;
		return res;
	}

	@Override
	public ASTNode visitInt_lit(guqinParser.Int_litContext ctx) {
		LiterNode res = new LiterNode();
		res.type = "int";
		res.dim = 0;
		res.value = ctx.getText();
		return res;
	}

	@Override
	public ASTNode visitTrue(guqinParser.TrueContext ctx) {
		LiterNode res = new LiterNode();
		res.type = "bool";
		res.dim = 0;
		res.value = "true";
		return res;
	}

	@Override
	public ASTNode visitFstr(guqinParser.FstrContext ctx) {
		return visit(ctx.format_string());
	}

	@Override
	public ASTNode visitAssign(guqinParser.AssignContext ctx) {
		return visit(ctx.assignexpr());
	}

	@Override
	public ASTNode visitThr(guqinParser.ThrContext ctx) {
		ThreeNode res = new ThreeNode();
		res.condition = visit(ctx.expr(0));
		res.value1 = visit(ctx.expr(1));
		res.value2 = visit(ctx.expr(2));
		return res;
	}

	@Override
	public ASTNode visitAssignexpr(guqinParser.AssignexprContext ctx) {
		AssignNode res = new AssignNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdexprContext) {
				res.ids.add(visit(ctx.getChild(i)));
			}
		}
		res.values = visit(ctx.expr());
		return res;
	}

	@Override
	public ASTNode visitFormat_string(guqinParser.Format_stringContext ctx) {
		if (ctx.FORMAT_ST() != null) {
			LiterNode res = new LiterNode();
			res.type = "string";
			res.dim = 0;
			res.value = ctx.getText().substring(2, ctx.getText().length() - 1);
			return res;
		}
		FstringNode res = new FstringNode();
		res.type = "string";
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				res.exprs.add(visit(ctx.getChild(i)));
			} else {
				LiterNode res_str = new LiterNode();
				res_str.dim = 0;
				res_str.type = "string";
				String f_res_str = ctx.getChild(i).getText();
				if (f_res_str.charAt(0) == 'f') {
					res_str.value = f_res_str.substring(2, f_res_str.length() - 1);
				} else {
					res_str.value = f_res_str.substring(1, f_res_str.length() - 1);
				}
				res.exprs.add(res_str);
			}
		}
		return res;
	}

	@Override
	public ASTNode visitArray_new(guqinParser.Array_newContext ctx) {
		NewNode res = new NewNode();
		res.type = ctx.real_type().getText();
		res.value = visit(ctx.multiarray());
		return res;
	}

	@Override
	public ASTNode visitDim_new(guqinParser.Dim_newContext ctx) {
		NewNode res = new NewNode();
		res.type = ctx.real_type().getText();
		res.dims = visit(ctx.dimensions_declar());
		return res;
	}

	@Override
	public ASTNode visitSingle_new(guqinParser.Single_newContext ctx) {
		NewNode res = new NewNode();
		res.type = ctx.real_type().getText();
		return res;
	}

	@Override
	public ASTNode visitGlobal_declarstat(guqinParser.Global_declarstatContext ctx) {
		DeclarNode res = new DeclarNode();
		res.type = ctx.real_type().getText();
		res.dim_number = visit(ctx.dimensions_declar());
		res.dim = universal_dnode.dim;
		int cnt = 0;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				cnt++;
				res.ID.add(ctx.getChild(i).getText());
			}
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				res.Initial.put(cnt, visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public ASTNode visitLocal_declarstat(guqinParser.Local_declarstatContext ctx) {
		DeclarNode res = new DeclarNode();
		res.type = ctx.real_type().getText();
		res.dim_number = visit(ctx.dimensions_declar());
		res.dim = universal_dnode.dim;
		int cnt = 0;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				res.ID.add(ctx.getChild(i).getText());
				cnt++;
			}
			if (ctx.getChild(i) instanceof guqinParser.ExprContext) {
				res.Initial.put(cnt, visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public ASTNode visitInnercontent(guqinParser.InnercontentContext ctx) {
		StatsNode res = new StatsNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.StatContext) {
				res.stats.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public ASTNode visitLoopinnercontent(guqinParser.LoopinnercontentContext ctx) {
		StatsNode res = new StatsNode();
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.StatContext) {
				res.stats.add(visit(ctx.getChild(i)));
			}
		}
		return res;
	}

	@Override
	public ASTNode visitConditstat(guqinParser.ConditstatContext ctx) {
		IfNode res = new IfNode();
		res.condition = visit(ctx.expr());
		res.branch = visit(ctx.innercontent(0));
		if (ctx.ELSE() != null) {
			res.else_branch = visit(ctx.innercontent(1));
		}
		return res;
	}

	@Override
	public ASTNode visitWhilestat(guqinParser.WhilestatContext ctx) {
		WhileNode res = new WhileNode();
		res.condition = visit(ctx.expr());
		res.stats = visit(ctx.loopinnercontent());
		return res;
	}

	@Override
	public ASTNode visitForstat(guqinParser.ForstatContext ctx) {
		ForNode res = new ForNode();
		if (ctx.stat() != null) {
			res.init = visit(ctx.stat());
		}
		res.condition = visit(ctx.cond());
		if (ctx.expr() != null) {
			res.iterator = visit(ctx.expr());
		}
		res.stats = visit(ctx.loopinnercontent());
		return res;
	}

	@Override
	public ASTNode visitCond(guqinParser.CondContext ctx) {
		ASTNode res = new ASTNode();
		res.dim = 0;
		res.type = "void";
		if (ctx.expr() != null) {
			return visit(ctx.expr());
		} else {
			return res;
		}
	}

	@Override
	public ASTNode visitReturnstat(guqinParser.ReturnstatContext ctx) {
		ReturnNode res = new ReturnNode();
		res.value = visit(ctx.cond());
		return res;
	}

	@Override
	public ASTNode visitContistat(guqinParser.ContistatContext ctx) {
		return new ContinueNode();
	}

	@Override
	public ASTNode visitBreakstat(guqinParser.BreakstatContext ctx) {
		return new BreakNode();
	}

	@Override
	public ASTNode visitExprstat(guqinParser.ExprstatContext ctx) {
		ExprstatNode res = new ExprstatNode();
		res.expr = visit(ctx.expr());
		return res;
	}

	@Override
	public ASTNode visitPint(guqinParser.PintContext ctx) {
		PrintNode res = new PrintNode();
		res.type = "int";
		if (ctx.PRINTINT() != null) {
			res.change_line = true;
		}
		res.value = visit(ctx.expr());
		return res;
	}

	@Override
	public ASTNode visitPstr(guqinParser.PstrContext ctx) {
		PrintNode res = new PrintNode();
		res.type = "string";
		if (ctx.PRINT() != null) {
			res.change_line = true;
		}
		res.value = visit(ctx.expr());
		return res;
	}

	@Override
	public ASTNode visitStat(guqinParser.StatContext ctx) {
		return visit(ctx.getChild(0));
	}

	@Override
	public ASTNode visitArrexpr(guqinParser.ArrexprContext ctx) {
		ASTNode res = visit(ctx.multiarray());
		return res;
	}
	@Override
	public ASTNode visitNewmem(guqinParser.NewmemContext ctx) {
		RightmemNode res = new RightmemNode();
		res.right_value = visit(ctx.newexpr());
		res.right_dim = visit(ctx.dimensions_choose());
		if(ctx.idexpr() != null) {
			guqinParser.IdexprContext to_check = ctx.idexpr();
			for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof guqinParser.IdContext) {
				res.calling.add(ctx.getChild(i).getText());
			}
			if (ctx.getChild(i) instanceof guqinParser.Dimensions_chooseContext) {
				res.dims.add(visit(ctx.getChild(i)));
			}
		}
		}
		if(ctx.funcall() != null) {
			
		}
	}

	@Override
	public ASTNode visitFunmem(guqinParser.FunmemContext ctx) {
		return visitChildren(ctx);
	}
}
printlin