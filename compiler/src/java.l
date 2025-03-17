%%

\( "LPAREN"
\) "RPAREN"
\{ "LBRACE"
} "RBRACE"
\[ "LBRACKET"
] "RBRACKET"
; "SEMIC"

abstract "ABSTRACT"
boolean "BOOLEAN"
byte "BYTE"
char "CHAR"
class "CLASS"
double "DOUBLE"
final "FINAL"
float "FLOAT"
int "INT"
long "LONG"
native "NATIVE"
private "PRIVATE"
protected "PROTECTED"
public "PUBLIC"
short "SHORT"
static "STATIC"
synchronized "SYNCHRONIZED"
transient "TRANSIENT"
void "VOID"
volatile "VOLATILE"

[A-Za-z_$\p{Letter}][0-9A-Za-z_$\p{Letter}]* "ID"

/\*[^*].*\*/ ;

/\*\*.*\*/ ;

\/\/([^\f\n\r]|[^\r][^\n])*([\f\n\r]|\r\n) ;

[ \t\f\n\r]|\r\n ;

%%
