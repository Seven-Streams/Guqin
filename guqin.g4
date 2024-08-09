grammar guqin;

id: ID;
prog: (classdef | func | global_declarstat)+;
dimension: LB expr? RB | LB RB;
must_dimension: LB expr RB;
typepair: real_type dimensions_declar id;
dimensions: (dimension)+;
dimensions_exist: must_dimension+;
dimensions_choose: must_dimension*;
dimensions_declar: dimension*;
array: LL (expr (COM expr)*)? RL;
real_type: (INT | BOOL | STRING | id);
args: (typepair (COM typepair)*)?;
funcall: id ( LP (expr (COM expr)*)? RP | LP RP);
func: ((real_type dimensions_declar) | VOID) id ((LP args RP) | LP RP) (
		LL ( stat)* RL
	);
newexpr:
	NEW real_type LB RB array		# array_new
	| NEW real_type dimensions	# dim_new
	| NEW real_type (LP RP)?				# single_new;
construct_func: id LP RP LL stat* RL;
classdef:
	CLASS id LL (local_declarstat | construct_func | func)* RL SEG;
expr:
	INT_VALUE											# int_lit
	| NULL												# null
	| STRING_VALUE										# str_lit
	| TRUE												# true
	| FALSE												# false
	| format_string										# fstr
	| ID												# id_single
	| expr dimensions_exist								# dimen
	| funcall											# funcallexpr
	| expr DOT op = (LENGTH | PARSEINT) LP RP			# strint
	| expr DOT ORD LP expr RP							# strord
	| expr DOT SUBSTRING LP expr COM expr RP			# substr
	| expr (DOT funcall)								# memfun
	| expr (DOT ID)										# mem
	| THIS												# this
	| newexpr											# new
	| LP expr RP										# par
	| expr op = (SAD | SMI)								# aft
	| op = (SAD | SMI | MINUS | ADD | NOT | BNO) expr	# bef
	| expr op = (MUL | DIV | MOD) expr					# muldivmod
	| expr op = (MINUS | ADD) expr						# addmin
	| expr op = (LSH | RSH) expr						# shift
	| expr op = (GE | GEQ | LE | LEQ) expr				# order
	| expr op = (UEQ | EQ) expr							# equalogic
	| expr BAN expr										# ban
	| expr XOR expr										# xor
	| expr BOR expr										# bor
	| expr MYAND expr									# and
	| expr OR expr										# or
	| array #arrexpr
	| <assoc = right> expr '?' expr ':' expr			# thr;
assignexpr: <assoc = right>expr (COM expr)* ASS expr;
global_declarstat:
	real_type dimensions_declar id (ASS expr)? (
		COM id (ASS expr)?
	)* SEG;
local_declarstat:
	real_type dimensions_declar id (ASS expr)? (
		COM id (ASS expr)?
	)* SEG;
innercontent: LL (stat)* RL | (stat);
loopinnercontent: LL (stat)* RL | (stat);
conditstat: IF LP expr RP innercontent (ELSE innercontent)?;
whilestat: WHILE LP expr RP loopinnercontent;
forstat:
	FOR LP (stat | SEG) cond SEG (expr | assignexpr)? RP loopinnercontent;
cond: (expr)?;
returnstat: RETURN cond SEG;
contistat: CONTINUE SEG;
breakstat: BREAK SEG;
exprstat: expr SEG;
printstat: (PRINTINT | PRINTLNINT) LP expr RP SEG	# pint
	| (PRINTLN | PRINT) LP expr RP SEG				# pstr;
stat:
	printstat
	| exprstat
	| assignexpr SEG
	| local_declarstat
	| conditstat
	| whilestat
	| breakstat
	| contistat
	| forstat
	| returnstat
	| scooped_stat
	| empty_stat;
empty_stat: SEG;
scooped_stat: LL (stat*?) (RL | RL SEG);
format_string:
	FORMAT_ST
	| FORMAT_L expr (FORMAT_INNER expr)* FORMAT_R;
LINE_COMMENT: '//' .*? '\r'? '\n' -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
STRING_VALUE:
	'"' ((~["\\]) | '\\"' | '\\\\' | '\\n' | '\\r')* '"';
VOID: 'void';
BOOL: 'bool';
INT: 'int';
STRING: 'string';
NEW: 'new';
CLASS: 'class';
NULL: 'null';
TRUE: 'true';
FALSE: 'false';
THIS: 'this';
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNINT: 'printlnInt';
PRINTINT: 'printInt';
LENGTH: 'length';
SUBSTRING: 'substring';
PARSEINT: 'parseInt';
ORD: 'ord';
INT_VALUE: [0-9]+;
ID: [a-zA-Z][a-zA-Z0-9_]*;
MYAND: '&&';
OR: '||';
NOT: '!';
GEQ: '>=';
LEQ: '<=';
EQ: '==';
UEQ: '!=';
RSH: '>>';
LSH: '<<';
SAD: '++';
SMI: '--';
BAN: '&';
BOR: '|';
XOR: '^';
BNO: '~';
LE: '<';
GE: '>';
ASS: '=';
ADD: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';
LP: '(';
RP: ')';
LB: '[';
RB: ']';
LL: '{';
RL: '}';
COM: ',';
SEG: ';';
DOT: '.';
WS: [ \r\n\t]+ -> skip;
fragment CHAR: ~[$"\r\n] | '{{' | '}}' | '$$';
FORMAT_L: 'f"' CHAR* '$';
FORMAT_R: '$' CHAR* '"';
FORMAT_INNER: '$' CHAR* '$';
FORMAT_ST: 'f"' CHAR* '"';