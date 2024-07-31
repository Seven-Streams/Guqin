grammar guqin;

id: ID;
prog: (classdef | func | global_declarstat)+;
dimension: '[' expr ']' | '[]';
must_dimension: '[' expr ']';
typepair: real_type dimensions id;
dimensions: (dimension)*;
dimensions_choose: must_dimension*;
dimensions_declar: dimension*?;
array: '{' (expr (',' expr)*)? '}';
multiarray: array | '{' multiarray (',' multiarray)* '}';
real_type: (INT | BOOL | STRING | id);
args: (typepair (',' typepair)*)?;
func: ((real_type dimensions) | VOID) id (
		('(' args ')')
		|
		| '()'
	) '{' (stat | returnstat)* '}';
construct_func: id '()' '{' stat* '}';
classdef:
	CLASS id '{' (local_declarstat | construct_func | func)* '};';
expr:
	INT_VALUE												# int_lit
	| NULL													# null
	| STRING_VALUE											# str_lit
	| (FALSE | TRUE)													# bool_lit
	| (id dimensions_choose ('.' id dimensions_choose)*)	# idexpr
	| ((id dimensions_choose ('.' id dimensions_choose)*) '.')? id (
		'(' (expr (',' expr)*)? ')'
		| '()'
	)												# funcall
	| THIS											# this
	| newexpr										# new
	| expr MINUS expr								# minus
	| expr ADD expr									# add
	| expr op = (MUL | DIV | MOD) expr				# muldivmod
	| '(' expr ')'									# par
	| op = (SAD | SMI) expr							# bef
	| expr op = (SAD | SMI)							# aft
	| op = (NOT | BNO | MINUS) expr					# single
	| expr op = (OR | AND) expr						# boologic
	| expr op = (UEQ | EQ) expr						# equalogic
	| expr op = (GE | GEQ | LE | LEQ) expr			# order
	| expr op = (BAN | BOR | XOR | RLH | RSH) expr	# bit
	| expr '?' expr ':' expr						# thr
	| assignexpr									# assign
	| format_string									# fstr
	| GETSTRING '()'								# getstr
	| GETINT '()'									# getint
	| TOSTRING '(' expr ')'							# tostr
	| expr '.' op = (LENGTH | PARSEINT) '()'		# strint
	| expr '.' ORD '(' expr ')'						# strord
	| expr '.' SUBSTRING '(' expr ',' expr ')'		# substr;
assignexpr: id dimensions_choose ASS expr;
format_string:
	id '"' (
		('{' expr (format_string | expr) '}')
		| ( '\\' '"' | ~'"')
	)* '"';
newexpr:
	NEW real_type '[]' array			# array_new
	| NEW real_type dimensions_declar	# dim_new
	| NEW real_type ('()')?				# single_new;
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
printstat: (PRINTINT | PRINTLNINT) '(' expr ')'';'	# pint
	| (PRINTLN | PRINT) '(' expr ')'';'			# pstr;
stat:
	exprstat
	| local_declarstat
	| conditstat
	| whilestat
	| forstat
	| printstat;
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
PRINTINT: 'printint';
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