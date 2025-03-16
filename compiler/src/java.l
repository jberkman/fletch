%%

\{ "LBRACE"
} "RBRACE"
; "SEMIC"

class "class"

[A-Za-z_$\p{Letter}][0-9A-Za-z_$\p{Letter}]* "ID"

/\*[^*].*\*/ ;

/\*\*.*\*/ ;

\/\/([^\f\n\r]|[^\r][^\n])*([\f\n\r]|\r\n) ;

[ \t\f\n\r]|\r\n ;

%%
