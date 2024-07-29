grammar guqin;

prog: classdef | func | global_declarstat;
typepair: (INT | BOOL | STRING | ID) ID;
real_type: (INT | BOOL | STRING | ID);
args: (typepair (',' typepair)*)?;
func: (INT | BOOL | STRING | VOID | ID) ID (
		('(' args ')')
		| ('()')
	) '{' (stat | returnstat)* '}';
construct_func: ID '()' '{' stat* '}';
classdef:
	CLASS ID '{' ((typepair ';') | construct_func | func)* '}';
funcall:
	ID ('[' expr ']')? ('.' (ID ('[' expr ']') '.')*) (
		expr (',' expr)*
	)?;
expr:
	INT_VALUE										# int_lit
	| STRING_VALUE									# str_lit
	| TRUE											# true
	| FALSE											# false
	| ID											# id
	| THIS											# this
	| funcall										# funcallv
	| ID '.' ID										# member
	| ID '[' expr ']'								# memnum
	| newexpr										# new
	| expr op = (ADD | MINUS) expr					# addminus
	| expr op = (MUL | DIV | MOD) expr				# muldivmod
	| '(' expr ')'									# par
	| op = (SAD | SMI) expr							# bef
	| expr op = (SAD | SMI)							# aft
	| op = (NOT | BNO | MINUS) expr					# single
	| expr op = (OR | AND) expr						# boologic
	| expr op = (UEQ | EQ) expr						# equalogic
	| expr op = (BAN | BOR | XOR) expr				# bit
	| expr '?' expr ':' expr						# thr
	| assignexpr									# assign
	| format_string									# fstr
	| op = (GETSTRING | GETINT) '()'				# kb
	| TOSTRING '(' expr ')'							# tostr
	| ID '.' op = (LENGTH | PARSEINT | ORD) '()'	# strfunc
	| ID '.' SUBSTRING '(' expr ',' expr ')'		# substr;
assignexpr: ID ASS expr;
format_string:
	'f' '"' (
		('{' (format_string | expr) '}')
		| ( '\\' '"' | ~'"')
	)* '"';
newexpr:
	NEW real_type '[]' '{' expr (',' expr)* '}'
	| NEW real_type ('()')?
	| NEW real_type ('[' INT ']')*;
global_declarstat:
	real_type ID ('=' expr)? (',' ID ('=' expr)?)* ';';
local_declarstat:
	real_type ID ('=' expr)? (',' ID ('=' expr)?)* ';';
innercontent: '{' (stat)* '}' | (stat);
loopinnercontent:
	'{' (stat | breakstat | contistat)* '}'
	| (stat | breakstat | contistat);
conditstat: IF '(' expr ')' innercontent (ELSE innercontent)?;
whilestat:
	WHILE '(' expr ')' '{' (stat | breakstat | contistat)* '}';
forstat:
	FOR '(' (stat | ';') (expr)? ';' expr ')' loopinnercontent;
returnstat: RETURN (expr)? ';';
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