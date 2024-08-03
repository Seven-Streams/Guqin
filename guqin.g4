grammar guqin;

@lexer::members {
	int left = 0;
}
id: ID;
prog: (classdef | func | global_declarstat)+;
dimension: '[' expr? ']' | '[]';
must_dimension: '[' expr ']';
typepair: real_type dimensions id;
dimensions: (dimension)*;
dimensions_exist: must_dimension+;
dimensions_choose: must_dimension*;
dimensions_declar: dimension*;
array: '{' (expr (',' expr)*)? '}';
multiarray: array | '{' multiarray (',' multiarray)* '}';
real_type: (INT | BOOL | STRING | id);
args: (typepair (',' typepair)*)?;
funcall: id ( '(' (expr (',' expr)*)? ')' | '()');
func: ((real_type dimensions) | VOID) id (('(' args ')') | '()') (
		'{' ( stat)* '}'
	);
newexpr:
	NEW real_type '[]' multiarray		# array_new
	| NEW real_type dimensions_declar	# dim_new
	| NEW real_type ('()')?				# single_new;
construct_func: id '()' '{' stat* '}';
classdef:
	CLASS id '{' (local_declarstat | construct_func | func)* '};';
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
	| expr '.' op = (LENGTH | PARSEINT) '()'			# strint
	| expr '.' ORD '(' expr ')'							# strord
	| expr '.' SUBSTRING '(' expr ',' expr ')'			# substr
	| expr ('.' funcall)								# memfun
	| expr ('.' ID)										# mem
	| THIS												# this
	| newexpr											# new
	| '(' expr ')'										# par
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
	| multiarray										# arrexpr
	| <assoc = right> expr '?' expr ':' expr			# thr;
assignexpr: <assoc = right>expr (',' expr)* ASS expr;
global_declarstat:
	real_type dimensions_declar id ('=' expr)? (
		',' id ('=' expr)?
	)* ';';
local_declarstat:
	real_type dimensions_declar id ('=' expr)? (
		',' id ('=' expr)?
	)* ';';
innercontent: '{' (stat)* '}' | (stat);
loopinnercontent:
	'{' (stat)* '}'
	| (stat);
conditstat: IF '(' expr ')' innercontent (ELSE innercontent)?;
whilestat: WHILE '(' expr ')' loopinnercontent;
forstat:
	FOR '(' (stat | ';') cond ';' (expr | assignexpr)? ')' loopinnercontent;
cond: (expr)?;
returnstat: RETURN cond ';';
contistat: CONTINUE ';';
breakstat: BREAK ';';
exprstat: expr ';';
printstat: (PRINTINT | PRINTLNINT) '(' expr ')' ';'	# pint
	| (PRINTLN | PRINT) '(' expr ')' ';'			# pstr;
stat:
	printstat
	| exprstat
	| assignexpr ';'
	| local_declarstat
	| conditstat
	| whilestat
	| breakstat
	| contistat
	| forstat
	| returnstat
	| scooped_stat
	| empty_stat;
empty_stat: ';';
scooped_stat: '{' (stat+) '}' | ('{' '}');
format_string:
	FORMAT_ST
	| FORMAT_L ((expr? FORMAT_INNER)* expr) FORMAT_R;
LINE_COMMENT: '//' .*? '\r'? '\n' -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
STRING_VALUE: '"' ( '\\' ["] | ~["])* '"';
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
WS: [ \r\n\t]+ -> skip;
fragment CHAR: ~[$"\r\n] | '{{' | '}}';
FORMAT_L: 'f"' CHAR* '$';
FORMAT_R: '$' CHAR* '"';
FORMAT_INNER: '$' CHAR* '$';
FORMAT_ST: 'f"' CHAR* '"';