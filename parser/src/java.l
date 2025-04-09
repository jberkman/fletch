%%

\( "LPAREN"
\) "RPAREN"
\{ "LBRACE"
} "RBRACE"
\[ "LBRACKET"
] "RBRACKET"
; "SEMIC"
, "COMMA"
\. "DOT"

\+ "PLUS"

abstract "ABSTRACT"
boolean "BOOLEAN"
byte "BYTE"
char "CHAR"
class "CLASS"
double "DOUBLE"
false "FALSE"
final "FINAL"
float "FLOAT"
int "INT"
long "LONG"
native "NATIVE"
null "NULL"
private "PRIVATE"
protected "PROTECTED"
public "PUBLIC"
short "SHORT"
static "STATIC"
synchronized "SYNCHRONIZED"
transient "TRANSIENT"
true "TRUE"
void "VOID"
volatile "VOLATILE"

[A-Za-z_$\p{Letter}][0-9A-Za-z_$\p{Letter}]* "ID"

'[^'\\\n\r]|\\([btnfr\"'\\]|[0-3]?[0-9]{0,2})' "CHR"
(0|[1-9][0-9]*)[lL]? "DEC"
(\d+\.\d*|\.\d*|\d+)([eE][+-]?\d+)?[fFdD]? "FLT"
0x[0-9a-fA-F]+[lL]? "HEX"
0[0-7]+[lL]? "OCT"
\"(([^\\\"\n\r]|\\([btnfr\"'\\]|[0-3]?[0-9]{0,2}))*)\" "STR"

/\*[^*].*\*/ ;

/\*\*.*\*/ ;

\/\/([^\f\n\r]|[^\r][^\n])*([\f\n\r]|\r\n) ;

[ \t\f\n\r]|\r\n ;

%%
