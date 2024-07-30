grammar guqin;

id: ID;
prog: classdef | func | global_declarstat;
dimension: '[' expr? ']';
must_dimension: '[' expr ']';
typepair: real_type dimensions id;
dimensions: (dimension)*;
dimensions_choose: must_dimension*;
dimensions_declar: (must_dimension dimension*)?;
real_type: (INT | BOOL | STRING | id);
args: (typepair (',' typepair)*)?;
func: (real_type dimensions)
	| VOID id (('(' args ')')) '{' (stat | returnstat)* '}';
construct_func: id '()' '{' stat* '}';
classdef:
	CLASS id '{' (local_declarstat | construct_func | func)* '}';
funcall:
	id '(' (expr (',' expr)*)? ')';
expr:
	INT_VALUE												# int_lit
	| NULL													# null
	| STRING_VALUE											# str_lit
	| TRUE													# true
	| FALSE													# false
	| (id dimensions_choose ('.' id dimensions_choose)*)	# idexpr
	| (id dimensions_choose ('.' id dimensions_choose)*) '.' funcall # memfunc
	| THIS													# this
	| newexpr												# new
	| expr MINUS expr										# minus
	| expr ADD expr											# add
	| expr op = (MUL | DIV | MOD) expr						# muldivmod
	| '(' expr ')'											# par
	| op = (SAD | SMI) expr									# bef
	| expr op = (SAD | SMI)									# aft
	| op = (NOT | BNO | MINUS) expr							# single
	| expr op = (OR | AND) expr								# boologic
	| expr op = (UEQ | EQ) expr								# equalogic
	| expr op = (BAN | BOR | XOR) expr						# bit
	| expr '?' expr ':' expr								# thr
	| assignexpr											# assign
	| format_string											# fstr
	| op = (GETSTRING | GETINT) '()'						# kb
	| TOSTRING '(' expr ')'									# tostr
	| id '.' op = (LENGTH | PARSEINT | ORD) '()'			# strfunc
	| id '.' SUBSTRING '(' expr ',' expr ')'				# substr;
assignexpr: id dimensions_choose ASS expr;
format_string:
	'f' '"' (
		('{' (format_string | expr) '}')
		| ( '\\' '"' | ~'"')
	)* '"';
newexpr:
	NEW real_type '[]' '{' expr (',' expr)* '}'
	| NEW real_type dimensions_declar
	| NEW real_type ('()')?
	| NEW real_type ('[' INT ']') ();
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
	'{' (stat | breakstat | contistat)* '}'
	| (stat | breakstat | contistat);
conditstat: IF '(' expr ')' innercontent (ELSE innercontent)?;
whilestat:
	WHILE '(' expr ')' '{' (stat | breakstat | contistat)* '}';
forstat:
	FOR '(' (stat | ';') cond ';' expr ')' loopinnercontent;
cond: (expr)?;
returnstat: RETURN cond ';';
contistat: CONTINUE ';';
breakstat: BREAK ';';
exprstat: expr ';';
stat:
	exprstat
	| local_declarstat
	| conditstat
	| whilestat
	| forstat;
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
PRINTINT: 'printlnInt';
GETSTRING: 'getString';
GETINT: 'getInt';
TOSTRING: 'toString';
LENGTH: 'length';
SUBSTRING: 'substring';
PARSEINT: 'parseInt';
ORD: 'ord';
INT_VALUE: [0-9]+;
ID: [a-zA-Z][a-zA-Z0-9_]*;
AND: '&&';
OR: '||';
NOT: '!';
GEQ: '>=';
LEQ: '<=';
EQ: '==';
UEQ: '!=';
RSH: '>>';
RLH: '<<';
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