grammar guqin;

prog:
	classdef			# classdefv
	| func				# funcv
	| global_declarstat	# global_declarstatv;
typepair: (INT | BOOL | STRING | ID) ID;
real_type: (INT | BOOL | STRING | ID);
args: (typepair (',' typepair)*)?;
func: (INT | BOOL | STRING | VOID | ID) ID (('(' args ')') |('()')) '{' stat* '}';
construct_func: ID '()' '{' stat* '}';
classdef:
	CLASS ID '{' ((typepair ';')| construct_func| func)* '}';
funcall: ID (expr (',' expr)*)?;
expr:
	(INT_VALUE | STRING_VALUE | TRUE | FALSE)	# liter
	| ID										# id
	| THIS										# this
	| funcall									# funcallv
	| ID '.' ID									# member
	| ID '.' funcall							# memfunc
	| ID '[' INT ']'							# memnum
	| newexpr									# new
	| expr op = (ADD | MINUS) expr				# addminus
	| expr op = (MUL | DIV | MOD) expr			# muldivmod
	| '(' expr ')'								# par
	| op = (SAD | SMI) expr						# bef
	| expr op = (SAD | SMI)						# aft
	| op = (NOT | BNO | MINUS) expr # single
	| expr op = (OR | AND | UEQ | EQ) expr #logic
	| expr op = (BAN | BOR | XOR) expr #bit
	| expr '?' expr ':' expr #thr
	| assignexpr #assign
	| format_string #fstr
	| op = (GETSTRING | GETINT) '()' # kb
	| TOSTRING '(' expr ')' #tostr
	| ID '.' op = (LENGTH | PARSEINT | ORD) '()' # strfunc
	| ID '.' SUBSTRING '(' expr ',' expr ')' #substr;
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
assignstat: assignexpr ';';
global_declarstat:
	real_type ID ('=' expr)? (',' ID ('=' expr)?)* ';';
local_declarstat:
	real_type ID ('=' expr)? (',' ID ('=' expr)?)* ';';
innercontent: '{' (stat | expr)* '}' | (stat | expr);
conditstat: IF '(' expr ')' innercontent (ELSE innercontent)?;
whilestat: WHILE '{' (stat | expr)* '}';
forstat:
	FOR '(' (expr)? ';' (expr)? ';' (expr)? ')' innercontent;
returnstat: RETURN (expr)? ';';
contistat: CONTINUE ';';
breakstat: BREAK ';';
exprstat: expr ';';
stat:
	assignstat
	| exprstat
	| local_declarstat
	| conditstat
	| whilestat
	| forstat
	| returnstat
	| contistat
	| breakstat;
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