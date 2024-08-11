// Generated from guqin.g4 by ANTLR 4.13.1
package basic_grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class guqinParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, LINE_COMMENT=3, BLOCK_COMMENT=4, STRING_VALUE=5, VOID=6, 
		BOOL=7, INT=8, STRING=9, NEW=10, CLASS=11, NULL=12, TRUE=13, FALSE=14, 
		THIS=15, IF=16, ELSE=17, FOR=18, WHILE=19, BREAK=20, CONTINUE=21, RETURN=22, 
		PRINT=23, PRINTLN=24, PRINTLNINT=25, PRINTINT=26, LENGTH=27, SUBSTRING=28, 
		PARSEINT=29, ORD=30, INT_VALUE=31, ID=32, MYAND=33, OR=34, NOT=35, GEQ=36, 
		LEQ=37, EQ=38, UEQ=39, RSH=40, LSH=41, SAD=42, SMI=43, BAN=44, BOR=45, 
		XOR=46, BNO=47, LE=48, GE=49, ASS=50, ADD=51, MINUS=52, MUL=53, DIV=54, 
		MOD=55, LP=56, RP=57, LB=58, RB=59, LL=60, RL=61, COM=62, SEG=63, DOT=64, 
		WS=65, FORMAT_L=66, FORMAT_R=67, FORMAT_INNER=68, FORMAT_ST=69;
	public static final int
		RULE_id = 0, RULE_prog = 1, RULE_dimension = 2, RULE_must_dimension = 3, 
		RULE_typepair = 4, RULE_dimensions = 5, RULE_dimensions_exist = 6, RULE_dimensions_choose = 7, 
		RULE_dimensions_declar = 8, RULE_array = 9, RULE_real_type = 10, RULE_args = 11, 
		RULE_funcall = 12, RULE_func = 13, RULE_newexpr = 14, RULE_construct_func = 15, 
		RULE_classdef = 16, RULE_expr = 17, RULE_assignexpr = 18, RULE_global_declarstat = 19, 
		RULE_local_declarstat = 20, RULE_innercontent = 21, RULE_loopinnercontent = 22, 
		RULE_conditstat = 23, RULE_whilestat = 24, RULE_forstat = 25, RULE_cond = 26, 
		RULE_returnstat = 27, RULE_contistat = 28, RULE_breakstat = 29, RULE_exprstat = 30, 
		RULE_printstat = 31, RULE_stat = 32, RULE_empty_stat = 33, RULE_scooped_stat = 34, 
		RULE_format_string = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"id", "prog", "dimension", "must_dimension", "typepair", "dimensions", 
			"dimensions_exist", "dimensions_choose", "dimensions_declar", "array", 
			"real_type", "args", "funcall", "func", "newexpr", "construct_func", 
			"classdef", "expr", "assignexpr", "global_declarstat", "local_declarstat", 
			"innercontent", "loopinnercontent", "conditstat", "whilestat", "forstat", 
			"cond", "returnstat", "contistat", "breakstat", "exprstat", "printstat", 
			"stat", "empty_stat", "scooped_stat", "format_string"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'?'", "':'", null, null, null, "'void'", "'bool'", "'int'", "'string'", 
			"'new'", "'class'", "'null'", "'true'", "'false'", "'this'", "'if'", 
			"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'print'", 
			"'println'", "'printlnInt'", "'printInt'", "'length'", "'substring'", 
			"'parseInt'", "'ord'", null, null, "'&&'", "'||'", "'!'", "'>='", "'<='", 
			"'=='", "'!='", "'>>'", "'<<'", "'++'", "'--'", "'&'", "'|'", "'^'", 
			"'~'", "'<'", "'>'", "'='", "'+'", "'-'", "'*'", "'/'", "'%'", "'('", 
			"')'", "'['", "']'", "'{'", "'}'", "','", "';'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "LINE_COMMENT", "BLOCK_COMMENT", "STRING_VALUE", "VOID", 
			"BOOL", "INT", "STRING", "NEW", "CLASS", "NULL", "TRUE", "FALSE", "THIS", 
			"IF", "ELSE", "FOR", "WHILE", "BREAK", "CONTINUE", "RETURN", "PRINT", 
			"PRINTLN", "PRINTLNINT", "PRINTINT", "LENGTH", "SUBSTRING", "PARSEINT", 
			"ORD", "INT_VALUE", "ID", "MYAND", "OR", "NOT", "GEQ", "LEQ", "EQ", "UEQ", 
			"RSH", "LSH", "SAD", "SMI", "BAN", "BOR", "XOR", "BNO", "LE", "GE", "ASS", 
			"ADD", "MINUS", "MUL", "DIV", "MOD", "LP", "RP", "LB", "RB", "LL", "RL", 
			"COM", "SEG", "DOT", "WS", "FORMAT_L", "FORMAT_R", "FORMAT_INNER", "FORMAT_ST"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "guqin.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public guqinParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(guqinParser.ID, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public List<ClassdefContext> classdef() {
			return getRuleContexts(ClassdefContext.class);
		}
		public ClassdefContext classdef(int i) {
			return getRuleContext(ClassdefContext.class,i);
		}
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public List<Global_declarstatContext> global_declarstat() {
			return getRuleContexts(Global_declarstatContext.class);
		}
		public Global_declarstatContext global_declarstat(int i) {
			return getRuleContext(Global_declarstatContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(77);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(74);
					classdef();
					}
					break;
				case 2:
					{
					setState(75);
					func();
					}
					break;
				case 3:
					{
					setState(76);
					global_declarstat();
					}
					break;
				}
				}
				setState(79); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4294970304L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DimensionContext extends ParserRuleContext {
		public TerminalNode LB() { return getToken(guqinParser.LB, 0); }
		public TerminalNode RB() { return getToken(guqinParser.RB, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DimensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dimension; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDimension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DimensionContext dimension() throws RecognitionException {
		DimensionContext _localctx = new DimensionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_dimension);
		int _la;
		try {
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				match(LB);
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1231888470515971104L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
					{
					setState(82);
					expr(0);
					}
				}

				setState(85);
				match(RB);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(86);
				match(LB);
				setState(87);
				match(RB);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Must_dimensionContext extends ParserRuleContext {
		public TerminalNode LB() { return getToken(guqinParser.LB, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RB() { return getToken(guqinParser.RB, 0); }
		public Must_dimensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_must_dimension; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitMust_dimension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Must_dimensionContext must_dimension() throws RecognitionException {
		Must_dimensionContext _localctx = new Must_dimensionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_must_dimension);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(LB);
			setState(91);
			expr(0);
			setState(92);
			match(RB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypepairContext extends ParserRuleContext {
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public Dimensions_declarContext dimensions_declar() {
			return getRuleContext(Dimensions_declarContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TypepairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typepair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitTypepair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypepairContext typepair() throws RecognitionException {
		TypepairContext _localctx = new TypepairContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typepair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			real_type();
			setState(95);
			dimensions_declar();
			setState(96);
			id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DimensionsContext extends ParserRuleContext {
		public List<DimensionContext> dimension() {
			return getRuleContexts(DimensionContext.class);
		}
		public DimensionContext dimension(int i) {
			return getRuleContext(DimensionContext.class,i);
		}
		public DimensionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dimensions; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDimensions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DimensionsContext dimensions() throws RecognitionException {
		DimensionsContext _localctx = new DimensionsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_dimensions);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(99); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(98);
					dimension();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(101); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Dimensions_existContext extends ParserRuleContext {
		public List<Must_dimensionContext> must_dimension() {
			return getRuleContexts(Must_dimensionContext.class);
		}
		public Must_dimensionContext must_dimension(int i) {
			return getRuleContext(Must_dimensionContext.class,i);
		}
		public Dimensions_existContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dimensions_exist; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDimensions_exist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Dimensions_existContext dimensions_exist() throws RecognitionException {
		Dimensions_existContext _localctx = new Dimensions_existContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_dimensions_exist);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(104); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(103);
					must_dimension();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(106); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Dimensions_chooseContext extends ParserRuleContext {
		public List<Must_dimensionContext> must_dimension() {
			return getRuleContexts(Must_dimensionContext.class);
		}
		public Must_dimensionContext must_dimension(int i) {
			return getRuleContext(Must_dimensionContext.class,i);
		}
		public Dimensions_chooseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dimensions_choose; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDimensions_choose(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Dimensions_chooseContext dimensions_choose() throws RecognitionException {
		Dimensions_chooseContext _localctx = new Dimensions_chooseContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_dimensions_choose);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LB) {
				{
				{
				setState(108);
				must_dimension();
				}
				}
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Dimensions_declarContext extends ParserRuleContext {
		public List<DimensionContext> dimension() {
			return getRuleContexts(DimensionContext.class);
		}
		public DimensionContext dimension(int i) {
			return getRuleContext(DimensionContext.class,i);
		}
		public Dimensions_declarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dimensions_declar; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDimensions_declar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Dimensions_declarContext dimensions_declar() throws RecognitionException {
		Dimensions_declarContext _localctx = new Dimensions_declarContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_dimensions_declar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LB) {
				{
				{
				setState(114);
				dimension();
				}
				}
				setState(119);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COM() { return getTokens(guqinParser.COM); }
		public TerminalNode COM(int i) {
			return getToken(guqinParser.COM, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(LL);
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1231888470515971104L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
				{
				setState(121);
				expr(0);
				setState(126);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COM) {
					{
					{
					setState(122);
					match(COM);
					setState(123);
					expr(0);
					}
					}
					setState(128);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(131);
			match(RL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Real_typeContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(guqinParser.INT, 0); }
		public TerminalNode BOOL() { return getToken(guqinParser.BOOL, 0); }
		public TerminalNode STRING() { return getToken(guqinParser.STRING, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public Real_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_real_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitReal_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Real_typeContext real_type() throws RecognitionException {
		Real_typeContext _localctx = new Real_typeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_real_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				{
				setState(133);
				match(INT);
				}
				break;
			case BOOL:
				{
				setState(134);
				match(BOOL);
				}
				break;
			case STRING:
				{
				setState(135);
				match(STRING);
				}
				break;
			case ID:
				{
				setState(136);
				id();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgsContext extends ParserRuleContext {
		public List<TypepairContext> typepair() {
			return getRuleContexts(TypepairContext.class);
		}
		public TypepairContext typepair(int i) {
			return getRuleContext(TypepairContext.class,i);
		}
		public List<TerminalNode> COM() { return getTokens(guqinParser.COM); }
		public TerminalNode COM(int i) {
			return getToken(guqinParser.COM, i);
		}
		public ArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_args; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgsContext args() throws RecognitionException {
		ArgsContext _localctx = new ArgsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_args);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4294968192L) != 0)) {
				{
				setState(139);
				typepair();
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COM) {
					{
					{
					setState(140);
					match(COM);
					setState(141);
					typepair();
					}
					}
					setState(146);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncallContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COM() { return getTokens(guqinParser.COM); }
		public TerminalNode COM(int i) {
			return getToken(guqinParser.COM, i);
		}
		public FuncallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcall; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitFuncall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncallContext funcall() throws RecognitionException {
		FuncallContext _localctx = new FuncallContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_funcall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			id();
			setState(164);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(150);
				match(LP);
				setState(159);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1231888470515971104L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
					{
					setState(151);
					expr(0);
					setState(156);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COM) {
						{
						{
						setState(152);
						match(COM);
						setState(153);
						expr(0);
						}
						}
						setState(158);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(161);
				match(RP);
				}
				break;
			case 2:
				{
				setState(162);
				match(LP);
				setState(163);
				match(RP);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FuncContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode VOID() { return getToken(guqinParser.VOID, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public Dimensions_declarContext dimensions_declar() {
			return getRuleContext(Dimensions_declarContext.class,0);
		}
		public ArgsContext args() {
			return getRuleContext(ArgsContext.class,0);
		}
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitFunc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case INT:
			case STRING:
			case ID:
				{
				{
				setState(166);
				real_type();
				setState(167);
				dimensions_declar();
				}
				}
				break;
			case VOID:
				{
				setState(169);
				match(VOID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(172);
			id();
			setState(179);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				{
				setState(173);
				match(LP);
				setState(174);
				args();
				setState(175);
				match(RP);
				}
				}
				break;
			case 2:
				{
				setState(177);
				match(LP);
				setState(178);
				match(RP);
				}
				break;
			}
			{
			setState(181);
			match(LL);
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -7991483566204782688L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
				{
				{
				setState(182);
				stat();
				}
				}
				setState(187);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(188);
			match(RL);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NewexprContext extends ParserRuleContext {
		public NewexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newexpr; }
	 
		public NewexprContext() { }
		public void copyFrom(NewexprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Single_newContext extends NewexprContext {
		public TerminalNode NEW() { return getToken(guqinParser.NEW, 0); }
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public Single_newContext(NewexprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitSingle_new(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Dim_newContext extends NewexprContext {
		public TerminalNode NEW() { return getToken(guqinParser.NEW, 0); }
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public DimensionsContext dimensions() {
			return getRuleContext(DimensionsContext.class,0);
		}
		public Dim_newContext(NewexprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDim_new(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Array_newContext extends NewexprContext {
		public TerminalNode NEW() { return getToken(guqinParser.NEW, 0); }
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public TerminalNode LB() { return getToken(guqinParser.LB, 0); }
		public TerminalNode RB() { return getToken(guqinParser.RB, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public Array_newContext(NewexprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitArray_new(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewexprContext newexpr() throws RecognitionException {
		NewexprContext _localctx = new NewexprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_newexpr);
		try {
			setState(206);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				_localctx = new Array_newContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(190);
				match(NEW);
				setState(191);
				real_type();
				setState(192);
				match(LB);
				setState(193);
				match(RB);
				setState(194);
				array();
				}
				break;
			case 2:
				_localctx = new Dim_newContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(196);
				match(NEW);
				setState(197);
				real_type();
				setState(198);
				dimensions();
				}
				break;
			case 3:
				_localctx = new Single_newContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(200);
				match(NEW);
				setState(201);
				real_type();
				setState(204);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(202);
					match(LP);
					setState(203);
					match(RP);
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Construct_funcContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public Construct_funcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_construct_func; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitConstruct_func(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Construct_funcContext construct_func() throws RecognitionException {
		Construct_funcContext _localctx = new Construct_funcContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_construct_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			id();
			setState(209);
			match(LP);
			setState(210);
			match(RP);
			setState(211);
			match(LL);
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -7991483566204782688L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
				{
				{
				setState(212);
				stat();
				}
				}
				setState(217);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(218);
			match(RL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassdefContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(guqinParser.CLASS, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public List<Local_declarstatContext> local_declarstat() {
			return getRuleContexts(Local_declarstatContext.class);
		}
		public Local_declarstatContext local_declarstat(int i) {
			return getRuleContext(Local_declarstatContext.class,i);
		}
		public List<Construct_funcContext> construct_func() {
			return getRuleContexts(Construct_funcContext.class);
		}
		public Construct_funcContext construct_func(int i) {
			return getRuleContext(Construct_funcContext.class,i);
		}
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public ClassdefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classdef; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitClassdef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassdefContext classdef() throws RecognitionException {
		ClassdefContext _localctx = new ClassdefContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_classdef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(CLASS);
			setState(221);
			id();
			setState(222);
			match(LL);
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4294968256L) != 0)) {
				{
				setState(226);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(223);
					local_declarstat();
					}
					break;
				case 2:
					{
					setState(224);
					construct_func();
					}
					break;
				case 3:
					{
					setState(225);
					func();
					}
					break;
				}
				}
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(231);
			match(RL);
			setState(232);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BefContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SAD() { return getToken(guqinParser.SAD, 0); }
		public TerminalNode SMI() { return getToken(guqinParser.SMI, 0); }
		public TerminalNode MINUS() { return getToken(guqinParser.MINUS, 0); }
		public TerminalNode ADD() { return getToken(guqinParser.ADD, 0); }
		public TerminalNode NOT() { return getToken(guqinParser.NOT, 0); }
		public TerminalNode BNO() { return getToken(guqinParser.BNO, 0); }
		public BefContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitBef(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Id_singleContext extends ExprContext {
		public TerminalNode ID() { return getToken(guqinParser.ID, 0); }
		public Id_singleContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitId_single(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DimenContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Dimensions_existContext dimensions_exist() {
			return getRuleContext(Dimensions_existContext.class,0);
		}
		public DimenContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitDimen(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MuldivmodContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MUL() { return getToken(guqinParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(guqinParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(guqinParser.MOD, 0); }
		public MuldivmodContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitMuldivmod(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BorContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BOR() { return getToken(guqinParser.BOR, 0); }
		public BorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitBor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StrordContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode DOT() { return getToken(guqinParser.DOT, 0); }
		public TerminalNode ORD() { return getToken(guqinParser.ORD, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public StrordContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitStrord(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncallexprContext extends ExprContext {
		public FuncallContext funcall() {
			return getRuleContext(FuncallContext.class,0);
		}
		public FuncallexprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitFuncallexpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ShiftContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LSH() { return getToken(guqinParser.LSH, 0); }
		public TerminalNode RSH() { return getToken(guqinParser.RSH, 0); }
		public ShiftContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitShift(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MemfunContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(guqinParser.DOT, 0); }
		public FuncallContext funcall() {
			return getRuleContext(FuncallContext.class,0);
		}
		public MemfunContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitMemfun(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StrintContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(guqinParser.DOT, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public TerminalNode LENGTH() { return getToken(guqinParser.LENGTH, 0); }
		public TerminalNode PARSEINT() { return getToken(guqinParser.PARSEINT, 0); }
		public StrintContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitStrint(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BanContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BAN() { return getToken(guqinParser.BAN, 0); }
		public BanContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitBan(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ThisexprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(guqinParser.DOT, 0); }
		public TerminalNode THIS() { return getToken(guqinParser.THIS, 0); }
		public ThisexprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitThisexpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MemContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(guqinParser.DOT, 0); }
		public TerminalNode ID() { return getToken(guqinParser.ID, 0); }
		public MemContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitMem(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MYAND() { return getToken(guqinParser.MYAND, 0); }
		public AndContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class XorContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode XOR() { return getToken(guqinParser.XOR, 0); }
		public XorContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitXor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Str_litContext extends ExprContext {
		public TerminalNode STRING_VALUE() { return getToken(guqinParser.STRING_VALUE, 0); }
		public Str_litContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitStr_lit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrderContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode GE() { return getToken(guqinParser.GE, 0); }
		public TerminalNode GEQ() { return getToken(guqinParser.GEQ, 0); }
		public TerminalNode LE() { return getToken(guqinParser.LE, 0); }
		public TerminalNode LEQ() { return getToken(guqinParser.LEQ, 0); }
		public OrderContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitOrder(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParContext extends ExprContext {
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public ParContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitPar(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewContext extends ExprContext {
		public NewexprContext newexpr() {
			return getRuleContext(NewexprContext.class,0);
		}
		public NewContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitNew(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OR() { return getToken(guqinParser.OR, 0); }
		public OrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddminContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MINUS() { return getToken(guqinParser.MINUS, 0); }
		public TerminalNode ADD() { return getToken(guqinParser.ADD, 0); }
		public AddminContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitAddmin(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseContext extends ExprContext {
		public TerminalNode FALSE() { return getToken(guqinParser.FALSE, 0); }
		public FalseContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ThisContext extends ExprContext {
		public TerminalNode THIS() { return getToken(guqinParser.THIS, 0); }
		public ThisContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitThis(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrexprContext extends ExprContext {
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrexprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitArrexpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubstrContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode DOT() { return getToken(guqinParser.DOT, 0); }
		public TerminalNode SUBSTRING() { return getToken(guqinParser.SUBSTRING, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public TerminalNode COM() { return getToken(guqinParser.COM, 0); }
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public SubstrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitSubstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AftContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SAD() { return getToken(guqinParser.SAD, 0); }
		public TerminalNode SMI() { return getToken(guqinParser.SMI, 0); }
		public AftContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitAft(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqualogicContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode UEQ() { return getToken(guqinParser.UEQ, 0); }
		public TerminalNode EQ() { return getToken(guqinParser.EQ, 0); }
		public EqualogicContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitEqualogic(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullContext extends ExprContext {
		public TerminalNode NULL() { return getToken(guqinParser.NULL, 0); }
		public NullContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitNull(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class Int_litContext extends ExprContext {
		public TerminalNode INT_VALUE() { return getToken(guqinParser.INT_VALUE, 0); }
		public Int_litContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitInt_lit(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueContext extends ExprContext {
		public TerminalNode TRUE() { return getToken(guqinParser.TRUE, 0); }
		public TrueContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FstrContext extends ExprContext {
		public Format_stringContext format_string() {
			return getRuleContext(Format_stringContext.class,0);
		}
		public FstrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitFstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ThrContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ThrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitThr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				_localctx = new Int_litContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(235);
				match(INT_VALUE);
				}
				break;
			case 2:
				{
				_localctx = new NullContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(236);
				match(NULL);
				}
				break;
			case 3:
				{
				_localctx = new Str_litContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(237);
				match(STRING_VALUE);
				}
				break;
			case 4:
				{
				_localctx = new TrueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(238);
				match(TRUE);
				}
				break;
			case 5:
				{
				_localctx = new FalseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(239);
				match(FALSE);
				}
				break;
			case 6:
				{
				_localctx = new FstrContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(240);
				format_string();
				}
				break;
			case 7:
				{
				_localctx = new Id_singleContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(241);
				match(ID);
				}
				break;
			case 8:
				{
				_localctx = new FuncallexprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(242);
				funcall();
				}
				break;
			case 9:
				{
				_localctx = new ThisContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(243);
				match(THIS);
				}
				break;
			case 10:
				{
				_localctx = new NewContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(244);
				newexpr();
				}
				break;
			case 11:
				{
				_localctx = new ParContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(245);
				match(LP);
				setState(246);
				expr(0);
				setState(247);
				match(RP);
				}
				break;
			case 12:
				{
				_localctx = new BefContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(249);
				((BefContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 6909365428682752L) != 0)) ) {
					((BefContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(250);
				expr(13);
				}
				break;
			case 13:
				{
				_localctx = new ArrexprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(251);
				array();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(326);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(324);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
					case 1:
						{
						_localctx = new MuldivmodContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(254);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(255);
						((MuldivmodContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 63050394783186944L) != 0)) ) {
							((MuldivmodContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(256);
						expr(13);
						}
						break;
					case 2:
						{
						_localctx = new AddminContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(257);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(258);
						((AddminContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==MINUS) ) {
							((AddminContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(259);
						expr(12);
						}
						break;
					case 3:
						{
						_localctx = new ShiftContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(260);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(261);
						((ShiftContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==RSH || _la==LSH) ) {
							((ShiftContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(262);
						expr(11);
						}
						break;
					case 4:
						{
						_localctx = new OrderContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(263);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(264);
						((OrderContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 844631088562176L) != 0)) ) {
							((OrderContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(265);
						expr(10);
						}
						break;
					case 5:
						{
						_localctx = new EqualogicContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(266);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(267);
						((EqualogicContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==UEQ) ) {
							((EqualogicContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(268);
						expr(9);
						}
						break;
					case 6:
						{
						_localctx = new BanContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(269);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(270);
						match(BAN);
						setState(271);
						expr(8);
						}
						break;
					case 7:
						{
						_localctx = new XorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(272);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(273);
						match(XOR);
						setState(274);
						expr(7);
						}
						break;
					case 8:
						{
						_localctx = new BorContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(275);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(276);
						match(BOR);
						setState(277);
						expr(6);
						}
						break;
					case 9:
						{
						_localctx = new AndContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(278);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(279);
						match(MYAND);
						setState(280);
						expr(5);
						}
						break;
					case 10:
						{
						_localctx = new OrContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(281);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(282);
						match(OR);
						setState(283);
						expr(4);
						}
						break;
					case 11:
						{
						_localctx = new ThrContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(284);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(285);
						match(T__0);
						setState(286);
						expr(0);
						setState(287);
						match(T__1);
						setState(288);
						expr(1);
						}
						break;
					case 12:
						{
						_localctx = new DimenContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(290);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(291);
						dimensions_exist();
						}
						break;
					case 13:
						{
						_localctx = new StrintContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(292);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(293);
						match(DOT);
						setState(294);
						((StrintContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==LENGTH || _la==PARSEINT) ) {
							((StrintContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(295);
						match(LP);
						setState(296);
						match(RP);
						}
						break;
					case 14:
						{
						_localctx = new StrordContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(297);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(298);
						match(DOT);
						setState(299);
						match(ORD);
						setState(300);
						match(LP);
						setState(301);
						expr(0);
						setState(302);
						match(RP);
						}
						break;
					case 15:
						{
						_localctx = new SubstrContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(304);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(305);
						match(DOT);
						setState(306);
						match(SUBSTRING);
						setState(307);
						match(LP);
						setState(308);
						expr(0);
						setState(309);
						match(COM);
						setState(310);
						expr(0);
						setState(311);
						match(RP);
						}
						break;
					case 16:
						{
						_localctx = new MemfunContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(313);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						{
						setState(314);
						match(DOT);
						setState(315);
						funcall();
						}
						}
						break;
					case 17:
						{
						_localctx = new MemContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(316);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						{
						setState(317);
						match(DOT);
						setState(318);
						match(ID);
						}
						}
						break;
					case 18:
						{
						_localctx = new ThisexprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(319);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(320);
						match(DOT);
						setState(321);
						match(THIS);
						}
						break;
					case 19:
						{
						_localctx = new AftContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(322);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(323);
						((AftContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SAD || _la==SMI) ) {
							((AftContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(328);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignexprContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ASS() { return getToken(guqinParser.ASS, 0); }
		public List<TerminalNode> COM() { return getTokens(guqinParser.COM); }
		public TerminalNode COM(int i) {
			return getToken(guqinParser.COM, i);
		}
		public AssignexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignexpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitAssignexpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignexprContext assignexpr() throws RecognitionException {
		AssignexprContext _localctx = new AssignexprContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_assignexpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			expr(0);
			setState(334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COM) {
				{
				{
				setState(330);
				match(COM);
				setState(331);
				expr(0);
				}
				}
				setState(336);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(337);
			match(ASS);
			setState(338);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Global_declarstatContext extends ParserRuleContext {
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public Dimensions_declarContext dimensions_declar() {
			return getRuleContext(Dimensions_declarContext.class,0);
		}
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public List<TerminalNode> ASS() { return getTokens(guqinParser.ASS); }
		public TerminalNode ASS(int i) {
			return getToken(guqinParser.ASS, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COM() { return getTokens(guqinParser.COM); }
		public TerminalNode COM(int i) {
			return getToken(guqinParser.COM, i);
		}
		public Global_declarstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_global_declarstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitGlobal_declarstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Global_declarstatContext global_declarstat() throws RecognitionException {
		Global_declarstatContext _localctx = new Global_declarstatContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_global_declarstat);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			real_type();
			setState(341);
			dimensions_declar();
			setState(342);
			id();
			setState(345);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASS) {
				{
				setState(343);
				match(ASS);
				setState(344);
				expr(0);
				}
			}

			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COM) {
				{
				{
				setState(347);
				match(COM);
				setState(348);
				id();
				setState(351);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASS) {
					{
					setState(349);
					match(ASS);
					setState(350);
					expr(0);
					}
				}

				}
				}
				setState(357);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(358);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Local_declarstatContext extends ParserRuleContext {
		public Real_typeContext real_type() {
			return getRuleContext(Real_typeContext.class,0);
		}
		public Dimensions_declarContext dimensions_declar() {
			return getRuleContext(Dimensions_declarContext.class,0);
		}
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public List<TerminalNode> ASS() { return getTokens(guqinParser.ASS); }
		public TerminalNode ASS(int i) {
			return getToken(guqinParser.ASS, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COM() { return getTokens(guqinParser.COM); }
		public TerminalNode COM(int i) {
			return getToken(guqinParser.COM, i);
		}
		public Local_declarstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_local_declarstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitLocal_declarstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Local_declarstatContext local_declarstat() throws RecognitionException {
		Local_declarstatContext _localctx = new Local_declarstatContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_local_declarstat);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			real_type();
			setState(361);
			dimensions_declar();
			setState(362);
			id();
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASS) {
				{
				setState(363);
				match(ASS);
				setState(364);
				expr(0);
				}
			}

			setState(375);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COM) {
				{
				{
				setState(367);
				match(COM);
				setState(368);
				id();
				setState(371);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASS) {
					{
					setState(369);
					match(ASS);
					setState(370);
					expr(0);
					}
				}

				}
				}
				setState(377);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(378);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InnercontentContext extends ParserRuleContext {
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public InnercontentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_innercontent; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitInnercontent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InnercontentContext innercontent() throws RecognitionException {
		InnercontentContext _localctx = new InnercontentContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_innercontent);
		int _la;
		try {
			setState(389);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(380);
				match(LL);
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -7991483566204782688L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
					{
					{
					setState(381);
					stat();
					}
					}
					setState(386);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(387);
				match(RL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(388);
				stat();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LoopinnercontentContext extends ParserRuleContext {
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public LoopinnercontentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopinnercontent; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitLoopinnercontent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopinnercontentContext loopinnercontent() throws RecognitionException {
		LoopinnercontentContext _localctx = new LoopinnercontentContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_loopinnercontent);
		int _la;
		try {
			setState(400);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(391);
				match(LL);
				setState(395);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -7991483566204782688L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
					{
					{
					setState(392);
					stat();
					}
					}
					setState(397);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(398);
				match(RL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(399);
				stat();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditstatContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(guqinParser.IF, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public List<InnercontentContext> innercontent() {
			return getRuleContexts(InnercontentContext.class);
		}
		public InnercontentContext innercontent(int i) {
			return getRuleContext(InnercontentContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(guqinParser.ELSE, 0); }
		public ConditstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitConditstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditstatContext conditstat() throws RecognitionException {
		ConditstatContext _localctx = new ConditstatContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_conditstat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(IF);
			setState(403);
			match(LP);
			setState(404);
			expr(0);
			setState(405);
			match(RP);
			setState(406);
			innercontent();
			setState(409);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(407);
				match(ELSE);
				setState(408);
				innercontent();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhilestatContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(guqinParser.WHILE, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public LoopinnercontentContext loopinnercontent() {
			return getRuleContext(LoopinnercontentContext.class,0);
		}
		public WhilestatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whilestat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitWhilestat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhilestatContext whilestat() throws RecognitionException {
		WhilestatContext _localctx = new WhilestatContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_whilestat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(WHILE);
			setState(412);
			match(LP);
			setState(413);
			expr(0);
			setState(414);
			match(RP);
			setState(415);
			loopinnercontent();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForstatContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(guqinParser.FOR, 0); }
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public List<TerminalNode> SEG() { return getTokens(guqinParser.SEG); }
		public TerminalNode SEG(int i) {
			return getToken(guqinParser.SEG, i);
		}
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public LoopinnercontentContext loopinnercontent() {
			return getRuleContext(LoopinnercontentContext.class,0);
		}
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignexprContext assignexpr() {
			return getRuleContext(AssignexprContext.class,0);
		}
		public ForstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitForstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForstatContext forstat() throws RecognitionException {
		ForstatContext _localctx = new ForstatContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_forstat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(417);
			match(FOR);
			setState(418);
			match(LP);
			setState(421);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(419);
				stat();
				}
				break;
			case 2:
				{
				setState(420);
				match(SEG);
				}
				break;
			}
			setState(423);
			cond();
			setState(424);
			match(SEG);
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(425);
				expr(0);
				}
				break;
			case 2:
				{
				setState(426);
				assignexpr();
				}
				break;
			}
			setState(429);
			match(RP);
			setState(430);
			loopinnercontent();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CondContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cond; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitCond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		CondContext _localctx = new CondContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_cond);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1231888470515971104L) != 0) || _la==FORMAT_L || _la==FORMAT_ST) {
				{
				setState(432);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnstatContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(guqinParser.RETURN, 0); }
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public ReturnstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitReturnstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnstatContext returnstat() throws RecognitionException {
		ReturnstatContext _localctx = new ReturnstatContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_returnstat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(435);
			match(RETURN);
			setState(436);
			cond();
			setState(437);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContistatContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(guqinParser.CONTINUE, 0); }
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public ContistatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contistat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitContistat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContistatContext contistat() throws RecognitionException {
		ContistatContext _localctx = new ContistatContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_contistat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			match(CONTINUE);
			setState(440);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakstatContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(guqinParser.BREAK, 0); }
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public BreakstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitBreakstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakstatContext breakstat() throws RecognitionException {
		BreakstatContext _localctx = new BreakstatContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_breakstat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			match(BREAK);
			setState(443);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprstatContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public ExprstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprstat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitExprstat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprstatContext exprstat() throws RecognitionException {
		ExprstatContext _localctx = new ExprstatContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_exprstat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			expr(0);
			setState(446);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrintstatContext extends ParserRuleContext {
		public PrintstatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_printstat; }
	 
		public PrintstatContext() { }
		public void copyFrom(PrintstatContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PstrContext extends PrintstatContext {
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public TerminalNode PRINTLN() { return getToken(guqinParser.PRINTLN, 0); }
		public TerminalNode PRINT() { return getToken(guqinParser.PRINT, 0); }
		public PstrContext(PrintstatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitPstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PintContext extends PrintstatContext {
		public TerminalNode LP() { return getToken(guqinParser.LP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RP() { return getToken(guqinParser.RP, 0); }
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public TerminalNode PRINTINT() { return getToken(guqinParser.PRINTINT, 0); }
		public TerminalNode PRINTLNINT() { return getToken(guqinParser.PRINTLNINT, 0); }
		public PintContext(PrintstatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitPint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrintstatContext printstat() throws RecognitionException {
		PrintstatContext _localctx = new PrintstatContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_printstat);
		int _la;
		try {
			setState(460);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRINTLNINT:
			case PRINTINT:
				_localctx = new PintContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(448);
				_la = _input.LA(1);
				if ( !(_la==PRINTLNINT || _la==PRINTINT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(449);
				match(LP);
				setState(450);
				expr(0);
				setState(451);
				match(RP);
				setState(452);
				match(SEG);
				}
				break;
			case PRINT:
			case PRINTLN:
				_localctx = new PstrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(454);
				_la = _input.LA(1);
				if ( !(_la==PRINT || _la==PRINTLN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(455);
				match(LP);
				setState(456);
				expr(0);
				setState(457);
				match(RP);
				setState(458);
				match(SEG);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatContext extends ParserRuleContext {
		public PrintstatContext printstat() {
			return getRuleContext(PrintstatContext.class,0);
		}
		public ExprstatContext exprstat() {
			return getRuleContext(ExprstatContext.class,0);
		}
		public AssignexprContext assignexpr() {
			return getRuleContext(AssignexprContext.class,0);
		}
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public Local_declarstatContext local_declarstat() {
			return getRuleContext(Local_declarstatContext.class,0);
		}
		public ConditstatContext conditstat() {
			return getRuleContext(ConditstatContext.class,0);
		}
		public WhilestatContext whilestat() {
			return getRuleContext(WhilestatContext.class,0);
		}
		public BreakstatContext breakstat() {
			return getRuleContext(BreakstatContext.class,0);
		}
		public ContistatContext contistat() {
			return getRuleContext(ContistatContext.class,0);
		}
		public ForstatContext forstat() {
			return getRuleContext(ForstatContext.class,0);
		}
		public ReturnstatContext returnstat() {
			return getRuleContext(ReturnstatContext.class,0);
		}
		public Scooped_statContext scooped_stat() {
			return getRuleContext(Scooped_statContext.class,0);
		}
		public Empty_statContext empty_stat() {
			return getRuleContext(Empty_statContext.class,0);
		}
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitStat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		StatContext _localctx = new StatContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_stat);
		try {
			setState(476);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(462);
				printstat();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(463);
				exprstat();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(464);
				assignexpr();
				setState(465);
				match(SEG);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(467);
				local_declarstat();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(468);
				conditstat();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(469);
				whilestat();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(470);
				breakstat();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(471);
				contistat();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(472);
				forstat();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(473);
				returnstat();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(474);
				scooped_stat();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(475);
				empty_stat();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Empty_statContext extends ParserRuleContext {
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public Empty_statContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_empty_stat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitEmpty_stat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Empty_statContext empty_stat() throws RecognitionException {
		Empty_statContext _localctx = new Empty_statContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_empty_stat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			match(SEG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Scooped_statContext extends ParserRuleContext {
		public TerminalNode LL() { return getToken(guqinParser.LL, 0); }
		public TerminalNode RL() { return getToken(guqinParser.RL, 0); }
		public TerminalNode SEG() { return getToken(guqinParser.SEG, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public Scooped_statContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scooped_stat; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitScooped_stat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Scooped_statContext scooped_stat() throws RecognitionException {
		Scooped_statContext _localctx = new Scooped_statContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_scooped_stat);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			match(LL);
			{
			setState(484);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(481);
					stat();
					}
					} 
				}
				setState(486);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,44,_ctx);
			}
			}
			setState(490);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(487);
				match(RL);
				}
				break;
			case 2:
				{
				setState(488);
				match(RL);
				setState(489);
				match(SEG);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Format_stringContext extends ParserRuleContext {
		public TerminalNode FORMAT_ST() { return getToken(guqinParser.FORMAT_ST, 0); }
		public TerminalNode FORMAT_L() { return getToken(guqinParser.FORMAT_L, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode FORMAT_R() { return getToken(guqinParser.FORMAT_R, 0); }
		public List<TerminalNode> FORMAT_INNER() { return getTokens(guqinParser.FORMAT_INNER); }
		public TerminalNode FORMAT_INNER(int i) {
			return getToken(guqinParser.FORMAT_INNER, i);
		}
		public Format_stringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_format_string; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof guqinVisitor ) return ((guqinVisitor<? extends T>)visitor).visitFormat_string(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Format_stringContext format_string() throws RecognitionException {
		Format_stringContext _localctx = new Format_stringContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_format_string);
		int _la;
		try {
			setState(504);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORMAT_ST:
				enterOuterAlt(_localctx, 1);
				{
				setState(492);
				match(FORMAT_ST);
				}
				break;
			case FORMAT_L:
				enterOuterAlt(_localctx, 2);
				{
				setState(493);
				match(FORMAT_L);
				setState(494);
				expr(0);
				setState(499);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==FORMAT_INNER) {
					{
					{
					setState(495);
					match(FORMAT_INNER);
					setState(496);
					expr(0);
					}
					}
					setState(501);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(502);
				match(FORMAT_R);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 17:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		case 10:
			return precpred(_ctx, 1);
		case 11:
			return precpred(_ctx, 25);
		case 12:
			return precpred(_ctx, 23);
		case 13:
			return precpred(_ctx, 22);
		case 14:
			return precpred(_ctx, 21);
		case 15:
			return precpred(_ctx, 20);
		case 16:
			return precpred(_ctx, 19);
		case 17:
			return precpred(_ctx, 17);
		case 18:
			return precpred(_ctx, 14);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001E\u01fb\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0004"+
		"\u0001N\b\u0001\u000b\u0001\f\u0001O\u0001\u0002\u0001\u0002\u0003\u0002"+
		"T\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002Y\b\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0004\u0005d\b\u0005\u000b\u0005\f\u0005"+
		"e\u0001\u0006\u0004\u0006i\b\u0006\u000b\u0006\f\u0006j\u0001\u0007\u0005"+
		"\u0007n\b\u0007\n\u0007\f\u0007q\t\u0007\u0001\b\u0005\bt\b\b\n\b\f\b"+
		"w\t\b\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t}\b\t\n\t\f\t\u0080\t\t\u0003"+
		"\t\u0082\b\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u008a"+
		"\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u008f\b\u000b\n\u000b"+
		"\f\u000b\u0092\t\u000b\u0003\u000b\u0094\b\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0005\f\u009b\b\f\n\f\f\f\u009e\t\f\u0003\f\u00a0\b"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u00a5\b\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u00ab\b\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0003\r\u00b4\b\r\u0001\r\u0001\r\u0005\r\u00b8\b\r\n\r\f\r\u00bb\t"+
		"\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00cd\b\u000e\u0003"+
		"\u000e\u00cf\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0005\u000f\u00d6\b\u000f\n\u000f\f\u000f\u00d9\t\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0005\u0010\u00e3\b\u0010\n\u0010\f\u0010\u00e6\t\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00fd\b\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u0145\b\u0011\n\u0011\f\u0011"+
		"\u0148\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u014d\b"+
		"\u0012\n\u0012\f\u0012\u0150\t\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u015a\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u0160\b\u0013\u0005\u0013\u0162\b\u0013\n\u0013\f\u0013\u0165\t\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0003\u0014\u016e\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0003\u0014\u0174\b\u0014\u0005\u0014\u0176\b\u0014\n\u0014"+
		"\f\u0014\u0179\t\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0005\u0015\u017f\b\u0015\n\u0015\f\u0015\u0182\t\u0015\u0001\u0015\u0001"+
		"\u0015\u0003\u0015\u0186\b\u0015\u0001\u0016\u0001\u0016\u0005\u0016\u018a"+
		"\b\u0016\n\u0016\f\u0016\u018d\t\u0016\u0001\u0016\u0001\u0016\u0003\u0016"+
		"\u0191\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0003\u0017\u019a\b\u0017\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0003\u0019\u01a6\b\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0003\u0019\u01ac\b\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0003\u001a\u01b2\b\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f"+
		"\u01cd\b\u001f\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0003 \u01dd\b \u0001!\u0001"+
		"!\u0001\"\u0001\"\u0005\"\u01e3\b\"\n\"\f\"\u01e6\t\"\u0001\"\u0001\""+
		"\u0001\"\u0003\"\u01eb\b\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0005#\u01f2"+
		"\b#\n#\f#\u01f5\t#\u0001#\u0001#\u0003#\u01f9\b#\u0001#\u0001\u01e4\u0001"+
		"\"$\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.02468:<>@BDF\u0000\n\u0004\u0000##*+//34\u0001"+
		"\u000057\u0001\u000034\u0001\u0000()\u0002\u0000$%01\u0001\u0000&\'\u0002"+
		"\u0000\u001b\u001b\u001d\u001d\u0001\u0000*+\u0001\u0000\u0019\u001a\u0001"+
		"\u0000\u0017\u0018\u0232\u0000H\u0001\u0000\u0000\u0000\u0002M\u0001\u0000"+
		"\u0000\u0000\u0004X\u0001\u0000\u0000\u0000\u0006Z\u0001\u0000\u0000\u0000"+
		"\b^\u0001\u0000\u0000\u0000\nc\u0001\u0000\u0000\u0000\fh\u0001\u0000"+
		"\u0000\u0000\u000eo\u0001\u0000\u0000\u0000\u0010u\u0001\u0000\u0000\u0000"+
		"\u0012x\u0001\u0000\u0000\u0000\u0014\u0089\u0001\u0000\u0000\u0000\u0016"+
		"\u0093\u0001\u0000\u0000\u0000\u0018\u0095\u0001\u0000\u0000\u0000\u001a"+
		"\u00aa\u0001\u0000\u0000\u0000\u001c\u00ce\u0001\u0000\u0000\u0000\u001e"+
		"\u00d0\u0001\u0000\u0000\u0000 \u00dc\u0001\u0000\u0000\u0000\"\u00fc"+
		"\u0001\u0000\u0000\u0000$\u0149\u0001\u0000\u0000\u0000&\u0154\u0001\u0000"+
		"\u0000\u0000(\u0168\u0001\u0000\u0000\u0000*\u0185\u0001\u0000\u0000\u0000"+
		",\u0190\u0001\u0000\u0000\u0000.\u0192\u0001\u0000\u0000\u00000\u019b"+
		"\u0001\u0000\u0000\u00002\u01a1\u0001\u0000\u0000\u00004\u01b1\u0001\u0000"+
		"\u0000\u00006\u01b3\u0001\u0000\u0000\u00008\u01b7\u0001\u0000\u0000\u0000"+
		":\u01ba\u0001\u0000\u0000\u0000<\u01bd\u0001\u0000\u0000\u0000>\u01cc"+
		"\u0001\u0000\u0000\u0000@\u01dc\u0001\u0000\u0000\u0000B\u01de\u0001\u0000"+
		"\u0000\u0000D\u01e0\u0001\u0000\u0000\u0000F\u01f8\u0001\u0000\u0000\u0000"+
		"HI\u0005 \u0000\u0000I\u0001\u0001\u0000\u0000\u0000JN\u0003 \u0010\u0000"+
		"KN\u0003\u001a\r\u0000LN\u0003&\u0013\u0000MJ\u0001\u0000\u0000\u0000"+
		"MK\u0001\u0000\u0000\u0000ML\u0001\u0000\u0000\u0000NO\u0001\u0000\u0000"+
		"\u0000OM\u0001\u0000\u0000\u0000OP\u0001\u0000\u0000\u0000P\u0003\u0001"+
		"\u0000\u0000\u0000QS\u0005:\u0000\u0000RT\u0003\"\u0011\u0000SR\u0001"+
		"\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000"+
		"UY\u0005;\u0000\u0000VW\u0005:\u0000\u0000WY\u0005;\u0000\u0000XQ\u0001"+
		"\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000Y\u0005\u0001\u0000\u0000"+
		"\u0000Z[\u0005:\u0000\u0000[\\\u0003\"\u0011\u0000\\]\u0005;\u0000\u0000"+
		"]\u0007\u0001\u0000\u0000\u0000^_\u0003\u0014\n\u0000_`\u0003\u0010\b"+
		"\u0000`a\u0003\u0000\u0000\u0000a\t\u0001\u0000\u0000\u0000bd\u0003\u0004"+
		"\u0002\u0000cb\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000ec\u0001"+
		"\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000f\u000b\u0001\u0000\u0000"+
		"\u0000gi\u0003\u0006\u0003\u0000hg\u0001\u0000\u0000\u0000ij\u0001\u0000"+
		"\u0000\u0000jh\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000k\r\u0001"+
		"\u0000\u0000\u0000ln\u0003\u0006\u0003\u0000ml\u0001\u0000\u0000\u0000"+
		"nq\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000op\u0001\u0000\u0000"+
		"\u0000p\u000f\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000rt\u0003"+
		"\u0004\u0002\u0000sr\u0001\u0000\u0000\u0000tw\u0001\u0000\u0000\u0000"+
		"us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000v\u0011\u0001\u0000"+
		"\u0000\u0000wu\u0001\u0000\u0000\u0000x\u0081\u0005<\u0000\u0000y~\u0003"+
		"\"\u0011\u0000z{\u0005>\u0000\u0000{}\u0003\"\u0011\u0000|z\u0001\u0000"+
		"\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000"+
		"~\u007f\u0001\u0000\u0000\u0000\u007f\u0082\u0001\u0000\u0000\u0000\u0080"+
		"~\u0001\u0000\u0000\u0000\u0081y\u0001\u0000\u0000\u0000\u0081\u0082\u0001"+
		"\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083\u0084\u0005"+
		"=\u0000\u0000\u0084\u0013\u0001\u0000\u0000\u0000\u0085\u008a\u0005\b"+
		"\u0000\u0000\u0086\u008a\u0005\u0007\u0000\u0000\u0087\u008a\u0005\t\u0000"+
		"\u0000\u0088\u008a\u0003\u0000\u0000\u0000\u0089\u0085\u0001\u0000\u0000"+
		"\u0000\u0089\u0086\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000"+
		"\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u008a\u0015\u0001\u0000\u0000"+
		"\u0000\u008b\u0090\u0003\b\u0004\u0000\u008c\u008d\u0005>\u0000\u0000"+
		"\u008d\u008f\u0003\b\u0004\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008f"+
		"\u0092\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0001\u0000\u0000\u0000\u0091\u0094\u0001\u0000\u0000\u0000\u0092"+
		"\u0090\u0001\u0000\u0000\u0000\u0093\u008b\u0001\u0000\u0000\u0000\u0093"+
		"\u0094\u0001\u0000\u0000\u0000\u0094\u0017\u0001\u0000\u0000\u0000\u0095"+
		"\u00a4\u0003\u0000\u0000\u0000\u0096\u009f\u00058\u0000\u0000\u0097\u009c"+
		"\u0003\"\u0011\u0000\u0098\u0099\u0005>\u0000\u0000\u0099\u009b\u0003"+
		"\"\u0011\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009b\u009e\u0001\u0000"+
		"\u0000\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009c\u009d\u0001\u0000"+
		"\u0000\u0000\u009d\u00a0\u0001\u0000\u0000\u0000\u009e\u009c\u0001\u0000"+
		"\u0000\u0000\u009f\u0097\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a5\u00059\u0000"+
		"\u0000\u00a2\u00a3\u00058\u0000\u0000\u00a3\u00a5\u00059\u0000\u0000\u00a4"+
		"\u0096\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a5"+
		"\u0019\u0001\u0000\u0000\u0000\u00a6\u00a7\u0003\u0014\n\u0000\u00a7\u00a8"+
		"\u0003\u0010\b\u0000\u00a8\u00ab\u0001\u0000\u0000\u0000\u00a9\u00ab\u0005"+
		"\u0006\u0000\u0000\u00aa\u00a6\u0001\u0000\u0000\u0000\u00aa\u00a9\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00b3\u0003"+
		"\u0000\u0000\u0000\u00ad\u00ae\u00058\u0000\u0000\u00ae\u00af\u0003\u0016"+
		"\u000b\u0000\u00af\u00b0\u00059\u0000\u0000\u00b0\u00b4\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b2\u00058\u0000\u0000\u00b2\u00b4\u00059\u0000\u0000\u00b3"+
		"\u00ad\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b4"+
		"\u00b5\u0001\u0000\u0000\u0000\u00b5\u00b9\u0005<\u0000\u0000\u00b6\u00b8"+
		"\u0003@ \u0000\u00b7\u00b6\u0001\u0000\u0000\u0000\u00b8\u00bb\u0001\u0000"+
		"\u0000\u0000\u00b9\u00b7\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000"+
		"\u0000\u0000\u00ba\u00bc\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000"+
		"\u0000\u0000\u00bc\u00bd\u0005=\u0000\u0000\u00bd\u001b\u0001\u0000\u0000"+
		"\u0000\u00be\u00bf\u0005\n\u0000\u0000\u00bf\u00c0\u0003\u0014\n\u0000"+
		"\u00c0\u00c1\u0005:\u0000\u0000\u00c1\u00c2\u0005;\u0000\u0000\u00c2\u00c3"+
		"\u0003\u0012\t\u0000\u00c3\u00cf\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005"+
		"\n\u0000\u0000\u00c5\u00c6\u0003\u0014\n\u0000\u00c6\u00c7\u0003\n\u0005"+
		"\u0000\u00c7\u00cf\u0001\u0000\u0000\u0000\u00c8\u00c9\u0005\n\u0000\u0000"+
		"\u00c9\u00cc\u0003\u0014\n\u0000\u00ca\u00cb\u00058\u0000\u0000\u00cb"+
		"\u00cd\u00059\u0000\u0000\u00cc\u00ca\u0001\u0000\u0000\u0000\u00cc\u00cd"+
		"\u0001\u0000\u0000\u0000\u00cd\u00cf\u0001\u0000\u0000\u0000\u00ce\u00be"+
		"\u0001\u0000\u0000\u0000\u00ce\u00c4\u0001\u0000\u0000\u0000\u00ce\u00c8"+
		"\u0001\u0000\u0000\u0000\u00cf\u001d\u0001\u0000\u0000\u0000\u00d0\u00d1"+
		"\u0003\u0000\u0000\u0000\u00d1\u00d2\u00058\u0000\u0000\u00d2\u00d3\u0005"+
		"9\u0000\u0000\u00d3\u00d7\u0005<\u0000\u0000\u00d4\u00d6\u0003@ \u0000"+
		"\u00d5\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d9\u0001\u0000\u0000\u0000"+
		"\u00d7\u00d5\u0001\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000"+
		"\u00d8\u00da\u0001\u0000\u0000\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000"+
		"\u00da\u00db\u0005=\u0000\u0000\u00db\u001f\u0001\u0000\u0000\u0000\u00dc"+
		"\u00dd\u0005\u000b\u0000\u0000\u00dd\u00de\u0003\u0000\u0000\u0000\u00de"+
		"\u00e4\u0005<\u0000\u0000\u00df\u00e3\u0003(\u0014\u0000\u00e0\u00e3\u0003"+
		"\u001e\u000f\u0000\u00e1\u00e3\u0003\u001a\r\u0000\u00e2\u00df\u0001\u0000"+
		"\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e2\u00e1\u0001\u0000"+
		"\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000"+
		"\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000\u0000\u00e5\u00e7\u0001\u0000"+
		"\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e7\u00e8\u0005=\u0000"+
		"\u0000\u00e8\u00e9\u0005?\u0000\u0000\u00e9!\u0001\u0000\u0000\u0000\u00ea"+
		"\u00eb\u0006\u0011\uffff\uffff\u0000\u00eb\u00fd\u0005\u001f\u0000\u0000"+
		"\u00ec\u00fd\u0005\f\u0000\u0000\u00ed\u00fd\u0005\u0005\u0000\u0000\u00ee"+
		"\u00fd\u0005\r\u0000\u0000\u00ef\u00fd\u0005\u000e\u0000\u0000\u00f0\u00fd"+
		"\u0003F#\u0000\u00f1\u00fd\u0005 \u0000\u0000\u00f2\u00fd\u0003\u0018"+
		"\f\u0000\u00f3\u00fd\u0005\u000f\u0000\u0000\u00f4\u00fd\u0003\u001c\u000e"+
		"\u0000\u00f5\u00f6\u00058\u0000\u0000\u00f6\u00f7\u0003\"\u0011\u0000"+
		"\u00f7\u00f8\u00059\u0000\u0000\u00f8\u00fd\u0001\u0000\u0000\u0000\u00f9"+
		"\u00fa\u0007\u0000\u0000\u0000\u00fa\u00fd\u0003\"\u0011\r\u00fb\u00fd"+
		"\u0003\u0012\t\u0000\u00fc\u00ea\u0001\u0000\u0000\u0000\u00fc\u00ec\u0001"+
		"\u0000\u0000\u0000\u00fc\u00ed\u0001\u0000\u0000\u0000\u00fc\u00ee\u0001"+
		"\u0000\u0000\u0000\u00fc\u00ef\u0001\u0000\u0000\u0000\u00fc\u00f0\u0001"+
		"\u0000\u0000\u0000\u00fc\u00f1\u0001\u0000\u0000\u0000\u00fc\u00f2\u0001"+
		"\u0000\u0000\u0000\u00fc\u00f3\u0001\u0000\u0000\u0000\u00fc\u00f4\u0001"+
		"\u0000\u0000\u0000\u00fc\u00f5\u0001\u0000\u0000\u0000\u00fc\u00f9\u0001"+
		"\u0000\u0000\u0000\u00fc\u00fb\u0001\u0000\u0000\u0000\u00fd\u0146\u0001"+
		"\u0000\u0000\u0000\u00fe\u00ff\n\f\u0000\u0000\u00ff\u0100\u0007\u0001"+
		"\u0000\u0000\u0100\u0145\u0003\"\u0011\r\u0101\u0102\n\u000b\u0000\u0000"+
		"\u0102\u0103\u0007\u0002\u0000\u0000\u0103\u0145\u0003\"\u0011\f\u0104"+
		"\u0105\n\n\u0000\u0000\u0105\u0106\u0007\u0003\u0000\u0000\u0106\u0145"+
		"\u0003\"\u0011\u000b\u0107\u0108\n\t\u0000\u0000\u0108\u0109\u0007\u0004"+
		"\u0000\u0000\u0109\u0145\u0003\"\u0011\n\u010a\u010b\n\b\u0000\u0000\u010b"+
		"\u010c\u0007\u0005\u0000\u0000\u010c\u0145\u0003\"\u0011\t\u010d\u010e"+
		"\n\u0007\u0000\u0000\u010e\u010f\u0005,\u0000\u0000\u010f\u0145\u0003"+
		"\"\u0011\b\u0110\u0111\n\u0006\u0000\u0000\u0111\u0112\u0005.\u0000\u0000"+
		"\u0112\u0145\u0003\"\u0011\u0007\u0113\u0114\n\u0005\u0000\u0000\u0114"+
		"\u0115\u0005-\u0000\u0000\u0115\u0145\u0003\"\u0011\u0006\u0116\u0117"+
		"\n\u0004\u0000\u0000\u0117\u0118\u0005!\u0000\u0000\u0118\u0145\u0003"+
		"\"\u0011\u0005\u0119\u011a\n\u0003\u0000\u0000\u011a\u011b\u0005\"\u0000"+
		"\u0000\u011b\u0145\u0003\"\u0011\u0004\u011c\u011d\n\u0001\u0000\u0000"+
		"\u011d\u011e\u0005\u0001\u0000\u0000\u011e\u011f\u0003\"\u0011\u0000\u011f"+
		"\u0120\u0005\u0002\u0000\u0000\u0120\u0121\u0003\"\u0011\u0001\u0121\u0145"+
		"\u0001\u0000\u0000\u0000\u0122\u0123\n\u0019\u0000\u0000\u0123\u0145\u0003"+
		"\f\u0006\u0000\u0124\u0125\n\u0017\u0000\u0000\u0125\u0126\u0005@\u0000"+
		"\u0000\u0126\u0127\u0007\u0006\u0000\u0000\u0127\u0128\u00058\u0000\u0000"+
		"\u0128\u0145\u00059\u0000\u0000\u0129\u012a\n\u0016\u0000\u0000\u012a"+
		"\u012b\u0005@\u0000\u0000\u012b\u012c\u0005\u001e\u0000\u0000\u012c\u012d"+
		"\u00058\u0000\u0000\u012d\u012e\u0003\"\u0011\u0000\u012e\u012f\u0005"+
		"9\u0000\u0000\u012f\u0145\u0001\u0000\u0000\u0000\u0130\u0131\n\u0015"+
		"\u0000\u0000\u0131\u0132\u0005@\u0000\u0000\u0132\u0133\u0005\u001c\u0000"+
		"\u0000\u0133\u0134\u00058\u0000\u0000\u0134\u0135\u0003\"\u0011\u0000"+
		"\u0135\u0136\u0005>\u0000\u0000\u0136\u0137\u0003\"\u0011\u0000\u0137"+
		"\u0138\u00059\u0000\u0000\u0138\u0145\u0001\u0000\u0000\u0000\u0139\u013a"+
		"\n\u0014\u0000\u0000\u013a\u013b\u0005@\u0000\u0000\u013b\u0145\u0003"+
		"\u0018\f\u0000\u013c\u013d\n\u0013\u0000\u0000\u013d\u013e\u0005@\u0000"+
		"\u0000\u013e\u0145\u0005 \u0000\u0000\u013f\u0140\n\u0011\u0000\u0000"+
		"\u0140\u0141\u0005@\u0000\u0000\u0141\u0145\u0005\u000f\u0000\u0000\u0142"+
		"\u0143\n\u000e\u0000\u0000\u0143\u0145\u0007\u0007\u0000\u0000\u0144\u00fe"+
		"\u0001\u0000\u0000\u0000\u0144\u0101\u0001\u0000\u0000\u0000\u0144\u0104"+
		"\u0001\u0000\u0000\u0000\u0144\u0107\u0001\u0000\u0000\u0000\u0144\u010a"+
		"\u0001\u0000\u0000\u0000\u0144\u010d\u0001\u0000\u0000\u0000\u0144\u0110"+
		"\u0001\u0000\u0000\u0000\u0144\u0113\u0001\u0000\u0000\u0000\u0144\u0116"+
		"\u0001\u0000\u0000\u0000\u0144\u0119\u0001\u0000\u0000\u0000\u0144\u011c"+
		"\u0001\u0000\u0000\u0000\u0144\u0122\u0001\u0000\u0000\u0000\u0144\u0124"+
		"\u0001\u0000\u0000\u0000\u0144\u0129\u0001\u0000\u0000\u0000\u0144\u0130"+
		"\u0001\u0000\u0000\u0000\u0144\u0139\u0001\u0000\u0000\u0000\u0144\u013c"+
		"\u0001\u0000\u0000\u0000\u0144\u013f\u0001\u0000\u0000\u0000\u0144\u0142"+
		"\u0001\u0000\u0000\u0000\u0145\u0148\u0001\u0000\u0000\u0000\u0146\u0144"+
		"\u0001\u0000\u0000\u0000\u0146\u0147\u0001\u0000\u0000\u0000\u0147#\u0001"+
		"\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000\u0149\u014e\u0003"+
		"\"\u0011\u0000\u014a\u014b\u0005>\u0000\u0000\u014b\u014d\u0003\"\u0011"+
		"\u0000\u014c\u014a\u0001\u0000\u0000\u0000\u014d\u0150\u0001\u0000\u0000"+
		"\u0000\u014e\u014c\u0001\u0000\u0000\u0000\u014e\u014f\u0001\u0000\u0000"+
		"\u0000\u014f\u0151\u0001\u0000\u0000\u0000\u0150\u014e\u0001\u0000\u0000"+
		"\u0000\u0151\u0152\u00052\u0000\u0000\u0152\u0153\u0003\"\u0011\u0000"+
		"\u0153%\u0001\u0000\u0000\u0000\u0154\u0155\u0003\u0014\n\u0000\u0155"+
		"\u0156\u0003\u0010\b\u0000\u0156\u0159\u0003\u0000\u0000\u0000\u0157\u0158"+
		"\u00052\u0000\u0000\u0158\u015a\u0003\"\u0011\u0000\u0159\u0157\u0001"+
		"\u0000\u0000\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u0163\u0001"+
		"\u0000\u0000\u0000\u015b\u015c\u0005>\u0000\u0000\u015c\u015f\u0003\u0000"+
		"\u0000\u0000\u015d\u015e\u00052\u0000\u0000\u015e\u0160\u0003\"\u0011"+
		"\u0000\u015f\u015d\u0001\u0000\u0000\u0000\u015f\u0160\u0001\u0000\u0000"+
		"\u0000\u0160\u0162\u0001\u0000\u0000\u0000\u0161\u015b\u0001\u0000\u0000"+
		"\u0000\u0162\u0165\u0001\u0000\u0000\u0000\u0163\u0161\u0001\u0000\u0000"+
		"\u0000\u0163\u0164\u0001\u0000\u0000\u0000\u0164\u0166\u0001\u0000\u0000"+
		"\u0000\u0165\u0163\u0001\u0000\u0000\u0000\u0166\u0167\u0005?\u0000\u0000"+
		"\u0167\'\u0001\u0000\u0000\u0000\u0168\u0169\u0003\u0014\n\u0000\u0169"+
		"\u016a\u0003\u0010\b\u0000\u016a\u016d\u0003\u0000\u0000\u0000\u016b\u016c"+
		"\u00052\u0000\u0000\u016c\u016e\u0003\"\u0011\u0000\u016d\u016b\u0001"+
		"\u0000\u0000\u0000\u016d\u016e\u0001\u0000\u0000\u0000\u016e\u0177\u0001"+
		"\u0000\u0000\u0000\u016f\u0170\u0005>\u0000\u0000\u0170\u0173\u0003\u0000"+
		"\u0000\u0000\u0171\u0172\u00052\u0000\u0000\u0172\u0174\u0003\"\u0011"+
		"\u0000\u0173\u0171\u0001\u0000\u0000\u0000\u0173\u0174\u0001\u0000\u0000"+
		"\u0000\u0174\u0176\u0001\u0000\u0000\u0000\u0175\u016f\u0001\u0000\u0000"+
		"\u0000\u0176\u0179\u0001\u0000\u0000\u0000\u0177\u0175\u0001\u0000\u0000"+
		"\u0000\u0177\u0178\u0001\u0000\u0000\u0000\u0178\u017a\u0001\u0000\u0000"+
		"\u0000\u0179\u0177\u0001\u0000\u0000\u0000\u017a\u017b\u0005?\u0000\u0000"+
		"\u017b)\u0001\u0000\u0000\u0000\u017c\u0180\u0005<\u0000\u0000\u017d\u017f"+
		"\u0003@ \u0000\u017e\u017d\u0001\u0000\u0000\u0000\u017f\u0182\u0001\u0000"+
		"\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000\u0180\u0181\u0001\u0000"+
		"\u0000\u0000\u0181\u0183\u0001\u0000\u0000\u0000\u0182\u0180\u0001\u0000"+
		"\u0000\u0000\u0183\u0186\u0005=\u0000\u0000\u0184\u0186\u0003@ \u0000"+
		"\u0185\u017c\u0001\u0000\u0000\u0000\u0185\u0184\u0001\u0000\u0000\u0000"+
		"\u0186+\u0001\u0000\u0000\u0000\u0187\u018b\u0005<\u0000\u0000\u0188\u018a"+
		"\u0003@ \u0000\u0189\u0188\u0001\u0000\u0000\u0000\u018a\u018d\u0001\u0000"+
		"\u0000\u0000\u018b\u0189\u0001\u0000\u0000\u0000\u018b\u018c\u0001\u0000"+
		"\u0000\u0000\u018c\u018e\u0001\u0000\u0000\u0000\u018d\u018b\u0001\u0000"+
		"\u0000\u0000\u018e\u0191\u0005=\u0000\u0000\u018f\u0191\u0003@ \u0000"+
		"\u0190\u0187\u0001\u0000\u0000\u0000\u0190\u018f\u0001\u0000\u0000\u0000"+
		"\u0191-\u0001\u0000\u0000\u0000\u0192\u0193\u0005\u0010\u0000\u0000\u0193"+
		"\u0194\u00058\u0000\u0000\u0194\u0195\u0003\"\u0011\u0000\u0195\u0196"+
		"\u00059\u0000\u0000\u0196\u0199\u0003*\u0015\u0000\u0197\u0198\u0005\u0011"+
		"\u0000\u0000\u0198\u019a\u0003*\u0015\u0000\u0199\u0197\u0001\u0000\u0000"+
		"\u0000\u0199\u019a\u0001\u0000\u0000\u0000\u019a/\u0001\u0000\u0000\u0000"+
		"\u019b\u019c\u0005\u0013\u0000\u0000\u019c\u019d\u00058\u0000\u0000\u019d"+
		"\u019e\u0003\"\u0011\u0000\u019e\u019f\u00059\u0000\u0000\u019f\u01a0"+
		"\u0003,\u0016\u0000\u01a01\u0001\u0000\u0000\u0000\u01a1\u01a2\u0005\u0012"+
		"\u0000\u0000\u01a2\u01a5\u00058\u0000\u0000\u01a3\u01a6\u0003@ \u0000"+
		"\u01a4\u01a6\u0005?\u0000\u0000\u01a5\u01a3\u0001\u0000\u0000\u0000\u01a5"+
		"\u01a4\u0001\u0000\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000\u0000\u01a7"+
		"\u01a8\u00034\u001a\u0000\u01a8\u01ab\u0005?\u0000\u0000\u01a9\u01ac\u0003"+
		"\"\u0011\u0000\u01aa\u01ac\u0003$\u0012\u0000\u01ab\u01a9\u0001\u0000"+
		"\u0000\u0000\u01ab\u01aa\u0001\u0000\u0000\u0000\u01ab\u01ac\u0001\u0000"+
		"\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000\u01ad\u01ae\u00059\u0000"+
		"\u0000\u01ae\u01af\u0003,\u0016\u0000\u01af3\u0001\u0000\u0000\u0000\u01b0"+
		"\u01b2\u0003\"\u0011\u0000\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b1\u01b2"+
		"\u0001\u0000\u0000\u0000\u01b25\u0001\u0000\u0000\u0000\u01b3\u01b4\u0005"+
		"\u0016\u0000\u0000\u01b4\u01b5\u00034\u001a\u0000\u01b5\u01b6\u0005?\u0000"+
		"\u0000\u01b67\u0001\u0000\u0000\u0000\u01b7\u01b8\u0005\u0015\u0000\u0000"+
		"\u01b8\u01b9\u0005?\u0000\u0000\u01b99\u0001\u0000\u0000\u0000\u01ba\u01bb"+
		"\u0005\u0014\u0000\u0000\u01bb\u01bc\u0005?\u0000\u0000\u01bc;\u0001\u0000"+
		"\u0000\u0000\u01bd\u01be\u0003\"\u0011\u0000\u01be\u01bf\u0005?\u0000"+
		"\u0000\u01bf=\u0001\u0000\u0000\u0000\u01c0\u01c1\u0007\b\u0000\u0000"+
		"\u01c1\u01c2\u00058\u0000\u0000\u01c2\u01c3\u0003\"\u0011\u0000\u01c3"+
		"\u01c4\u00059\u0000\u0000\u01c4\u01c5\u0005?\u0000\u0000\u01c5\u01cd\u0001"+
		"\u0000\u0000\u0000\u01c6\u01c7\u0007\t\u0000\u0000\u01c7\u01c8\u00058"+
		"\u0000\u0000\u01c8\u01c9\u0003\"\u0011\u0000\u01c9\u01ca\u00059\u0000"+
		"\u0000\u01ca\u01cb\u0005?\u0000\u0000\u01cb\u01cd\u0001\u0000\u0000\u0000"+
		"\u01cc\u01c0\u0001\u0000\u0000\u0000\u01cc\u01c6\u0001\u0000\u0000\u0000"+
		"\u01cd?\u0001\u0000\u0000\u0000\u01ce\u01dd\u0003>\u001f\u0000\u01cf\u01dd"+
		"\u0003<\u001e\u0000\u01d0\u01d1\u0003$\u0012\u0000\u01d1\u01d2\u0005?"+
		"\u0000\u0000\u01d2\u01dd\u0001\u0000\u0000\u0000\u01d3\u01dd\u0003(\u0014"+
		"\u0000\u01d4\u01dd\u0003.\u0017\u0000\u01d5\u01dd\u00030\u0018\u0000\u01d6"+
		"\u01dd\u0003:\u001d\u0000\u01d7\u01dd\u00038\u001c\u0000\u01d8\u01dd\u0003"+
		"2\u0019\u0000\u01d9\u01dd\u00036\u001b\u0000\u01da\u01dd\u0003D\"\u0000"+
		"\u01db\u01dd\u0003B!\u0000\u01dc\u01ce\u0001\u0000\u0000\u0000\u01dc\u01cf"+
		"\u0001\u0000\u0000\u0000\u01dc\u01d0\u0001\u0000\u0000\u0000\u01dc\u01d3"+
		"\u0001\u0000\u0000\u0000\u01dc\u01d4\u0001\u0000\u0000\u0000\u01dc\u01d5"+
		"\u0001\u0000\u0000\u0000\u01dc\u01d6\u0001\u0000\u0000\u0000\u01dc\u01d7"+
		"\u0001\u0000\u0000\u0000\u01dc\u01d8\u0001\u0000\u0000\u0000\u01dc\u01d9"+
		"\u0001\u0000\u0000\u0000\u01dc\u01da\u0001\u0000\u0000\u0000\u01dc\u01db"+
		"\u0001\u0000\u0000\u0000\u01ddA\u0001\u0000\u0000\u0000\u01de\u01df\u0005"+
		"?\u0000\u0000\u01dfC\u0001\u0000\u0000\u0000\u01e0\u01e4\u0005<\u0000"+
		"\u0000\u01e1\u01e3\u0003@ \u0000\u01e2\u01e1\u0001\u0000\u0000\u0000\u01e3"+
		"\u01e6\u0001\u0000\u0000\u0000\u01e4\u01e5\u0001\u0000\u0000\u0000\u01e4"+
		"\u01e2\u0001\u0000\u0000\u0000\u01e5\u01ea\u0001\u0000\u0000\u0000\u01e6"+
		"\u01e4\u0001\u0000\u0000\u0000\u01e7\u01eb\u0005=\u0000\u0000\u01e8\u01e9"+
		"\u0005=\u0000\u0000\u01e9\u01eb\u0005?\u0000\u0000\u01ea\u01e7\u0001\u0000"+
		"\u0000\u0000\u01ea\u01e8\u0001\u0000\u0000\u0000\u01ebE\u0001\u0000\u0000"+
		"\u0000\u01ec\u01f9\u0005E\u0000\u0000\u01ed\u01ee\u0005B\u0000\u0000\u01ee"+
		"\u01f3\u0003\"\u0011\u0000\u01ef\u01f0\u0005D\u0000\u0000\u01f0\u01f2"+
		"\u0003\"\u0011\u0000\u01f1\u01ef\u0001\u0000\u0000\u0000\u01f2\u01f5\u0001"+
		"\u0000\u0000\u0000\u01f3\u01f1\u0001\u0000\u0000\u0000\u01f3\u01f4\u0001"+
		"\u0000\u0000\u0000\u01f4\u01f6\u0001\u0000\u0000\u0000\u01f5\u01f3\u0001"+
		"\u0000\u0000\u0000\u01f6\u01f7\u0005C\u0000\u0000\u01f7\u01f9\u0001\u0000"+
		"\u0000\u0000\u01f8\u01ec\u0001\u0000\u0000\u0000\u01f8\u01ed\u0001\u0000"+
		"\u0000\u0000\u01f9G\u0001\u0000\u0000\u00000MOSXejou~\u0081\u0089\u0090"+
		"\u0093\u009c\u009f\u00a4\u00aa\u00b3\u00b9\u00cc\u00ce\u00d7\u00e2\u00e4"+
		"\u00fc\u0144\u0146\u014e\u0159\u015f\u0163\u016d\u0173\u0177\u0180\u0185"+
		"\u018b\u0190\u0199\u01a5\u01ab\u01b1\u01cc\u01dc\u01e4\u01ea\u01f3\u01f8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}