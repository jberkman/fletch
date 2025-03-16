use lrlex::lrlex_mod;
use lrpar::{Lexeme, Lexer, NonStreamingLexer};

lrlex_mod!("java.l");

#[test]
fn test_lexer_empty_class() {
    let lexerdef = java_l::lexerdef();
    let lexer = lexerdef.lexer("class Hello{}");

    let lexemes: Vec<_> = lexer.iter().collect::<Result<_, _>>().expect("Lexing failed");

    assert_eq!(lexer.span_str(lexemes[0].span()), "class");
    assert_eq!(lexer.span_str(lexemes[1].span()), "Hello");
    assert_eq!(lexer.span_str(lexemes[2].span()), "{");
    assert_eq!(lexer.span_str(lexemes[3].span()), "}");
}
