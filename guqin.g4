grammar guqin;

typepair: (INT | BOOL | STRING | ID) ID;
real_type: (INT | BOOL | String | ID);
args: (typepair (',' typepair*))?;
func: (INT | BOOL | STRING | VOID | ID) ID '(' args ')' '{' stat* '}';
construct_func: ID '()' '{' stat* '}';
class: CLASS ID '{' (typepair ';') | construct_func | func '}';
funcall: ID (expr (',' expr)*)?;
expr:
	INT_VALUE
	| STRING_VALUE
	| TRUE
	| FALSE
	| ID
	| THIS
	| funcall
	| ID '.' ID
	| ID '.' funcall
	| ID '[' INT ']'
	| newexpr
	| expr ADD expr
	| expr MINUS expr
	| expr MUL expr
	| expr DIV expr
	| expr MOD expr
	| '(' expr ')'
	| SAD expr
	| expr SAD
	| SMI expr
	| expr SMI
	| NOT expr
	| BNO expr
	| MINUS expr
	| expr EQ expr
	| expr UEQ expr
	| expr AND expr
	| expr OR expr
	| expr '?' expr ':' expr
	| assignexpr
	| format_string;
assignexpr: ID ASS expr;
format_string:
	'f' '"' (('{' (format_string | expr) '}') | CHAR)* '"';
newexpr:
	NEW real_type '[]' '{' expr (',' expr)* '}'
	| NEW real_type ('[' INT ']')*;

ID: [a-zA-Z][a-zA-Z0-9_]*;
LINE_COMMENT: '//' .*? '\r'? '\n' -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
CHAR: ( '\\' ["] | ~["]);
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
PRINTINT: 'printlinInt';
GETSTRING: 'getString';
GETINT: 'getInt';
TOSTRING: 'toString';
ADD: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';
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
INT_VALUE: [0-9]+;
WS: [ \r\n\t]+ -> skip;