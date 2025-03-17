%%

\( "LPAREN"
\) "RPAREN"
\{ "LBRACE"
} "RBRACE"
; "SEMIC"

abstract "ABSTRACT"
class "CLASS"
final "FINAL"
native "NATIVE"
private "PRIVATE"
protected "PROTECTED"
public "PUBLIC"
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
