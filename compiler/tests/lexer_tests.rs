use lrlex::lrlex_mod;
use lrpar::{Lexeme, Lexer};

lrlex_mod!("java.l");

macro_rules! test_empty {
    ($name:ident, $input:expr) => {
        #[test]
        fn $name() {
            lex_and_cmp($input, vec![]);
        }
    };
}

macro_rules! test_token {
    ($name:ident, $input:expr, $token:ident) => {
        #[test]
        fn $name() {
            lex_and_cmp($input, vec![java_l::$token]);
        }
    };
}

fn lex_and_cmp(input: &str, expected: Vec<u32>) {
    let lexerdef = java_l::lexerdef();
    let lexer = lexerdef.lexer(input);
    let tokens: Vec<_> = lexer.iter().map(|res| res.unwrap().tok_id()).collect();
    assert_eq!(tokens, expected);
}

#[cfg(test)]
mod sep {
    use super::lex_and_cmp;
    use super::java_l;

    test_token!(test_lparen, "(", T_LPAREN);
    test_token!(test_rparen, ")", T_RPAREN);
    test_token!(test_lbrace, "{", T_LBRACE);
    test_token!(test_rbrace, "}", T_RBRACE);
    test_token!(test_lbracket, "[", T_LBRACKET);
    test_token!(test_rbracket, "]", T_RBRACKET);
    test_token!(test_semic, ";", T_SEMIC);
}

#[cfg(test)]
mod kw {
    use super::lex_and_cmp;
    use super::java_l;

    test_token!(test_abstract, "abstract", T_ABSTRACT);
    test_token!(test_boolean, "boolean", T_BOOLEAN);
    test_token!(test_byte, "byte", T_BYTE);
    test_token!(test_char, "char", T_CHAR);
    test_token!(test_class, "class", T_CLASS);
    test_token!(test_double, "double", T_DOUBLE);
    test_token!(test_final, "final", T_FINAL);
    test_token!(test_float, "float", T_FLOAT);
    test_token!(test_int, "int", T_INT);
    test_token!(test_long, "long", T_LONG);
    test_token!(test_native, "native", T_NATIVE);
    test_token!(test_private, "private", T_PRIVATE);
    test_token!(test_protected, "protected", T_PROTECTED);
    test_token!(test_public, "public", T_PUBLIC);
    test_token!(test_short, "short", T_SHORT);
    test_token!(test_static, "static", T_STATIC);
    test_token!(test_synchronized, "synchronized", T_SYNCHRONIZED);
    test_token!(test_void, "void", T_VOID);
    test_token!(test_volatile, "volatile", T_VOLATILE);

}

#[cfg(test)]
mod id {
    use super::lex_and_cmp;
    use super::java_l;

    test_token!(test_x, "x", T_ID);
    test_token!(test_dollar, "$", T_ID);
    test_token!(test_underscore, "_", T_ID);
    test_token!(test_strange, "_$f00B4r", T_ID);
}
#[cfg(test)]
mod cmnt {
    use super::lex_and_cmp;

    test_empty!(test_trad, "/* foo bar baz */");
    test_empty!(test_trad_with_cmnts, "/* this comment /* // /** ends here:  */");
    test_empty!(test_doc, "/** foo bar baz */");
    test_empty!(test_eol_ff, "// foo bar baz\x0c");
    test_empty!(test_eol_nl, "// foo bar baz\n");
    test_empty!(test_eol_cr, "// foo bar baz\r");
    test_empty!(test_eol_crlf, "// foo bar baz\r\n");
    test_empty!(test_eol_with_trad, "// /* foo bar baz */\n");
}

#[cfg(test)]
mod ws {
    use super::lex_and_cmp;

    test_empty!(test_sp, " ");
    test_empty!(test_ht, "\t");
    test_empty!(test_ff, "\x0c");
    test_empty!(test_lf, "\n");
    test_empty!(test_cr, "\r");
    test_empty!(test_crlf, "\r\n");

    #[test]
    #[should_panic]
    fn test_hex_sp() {
        // TODO: canonicalize ASCII by emitting ASCII Unicode escapes as
        // ASCII
        lex_and_cmp("\\u0020", vec![]);
    }
}

#[test]
fn test_hello() {
    lex_and_cmp(r#"
        class Hello {
            public static void main(String[] args) {
                System.out.println("Hello, World!");
            }
        }
    "#, vec![ 
        java_l::T_CLASS,
        java_l::T_ID,
        java_l::T_LBRACE,
        java_l::T_PUBLIC,
        java_l::T_STATIC,
        java_l::T_VOID,
        java_l::T_ID,
        java_l::T_LPAREN,
        java_l::T_ID,
        java_l::T_LBRACKET,
        java_l::T_RBRACKET,
        java_l::T_ID,
        java_l::T_RPAREN,
        java_l::T_LBRACE,
        java_l::T_ID,
        java_l::T_DOT,
        java_l::T_ID,
        java_l::T_DOT,
        java_l::T_ID,
        java_l::T_LPAREN,
        java_l::T_STR,
        java_l::T_RPAREN,
        java_l::T_SEMIC,
        java_l::T_RBRACE,
        java_l::T_RBRACE,
    ]);
}
