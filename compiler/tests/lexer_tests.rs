use lrlex::lrlex_mod;
use lrpar::{Lexeme, Lexer};

lrlex_mod!("java.l");

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

    #[test]
    fn test_lparen() {
        lex_and_cmp("(", vec![java_l::T_LPAREN]);
    }

    #[test]
    fn test_rparen() {
        lex_and_cmp(")", vec![java_l::T_RPAREN]);
    }

    #[test]
    fn test_lbrace() {
        lex_and_cmp("{", vec![java_l::T_LBRACE]);
    }

    #[test]
    fn test_rbrace() {
        lex_and_cmp("}", vec![java_l::T_RBRACE]);
    }

    #[test]
    fn test_semic() {
        lex_and_cmp(";", vec![java_l::T_SEMIC]);
    }

}

#[cfg(test)]
mod kw {
    use super::lex_and_cmp;
    use super::java_l;

    #[test]
    fn test_class() {
        lex_and_cmp("class", vec![java_l::T_CLASS]);
    }

    #[test]
    fn test_void() {
        lex_and_cmp("void", vec![java_l::T_VOID]);
    }

}

#[cfg(test)]
mod id {
    use super::lex_and_cmp;
    use super::java_l;

    #[test]
    fn test_x() {
        lex_and_cmp("a", vec![java_l::T_ID]);
    }

    #[test]
    fn test_dollar() {
        lex_and_cmp("$", vec![java_l::T_ID]);
    }

    #[test]
    fn test_underscore() {
        lex_and_cmp("_", vec![java_l::T_ID]);
    }

    #[test]
    fn test_strange() {
        lex_and_cmp("_$f00B4r", vec![java_l::T_ID]);
    }
}
#[cfg(test)]
mod cmnt {
    use super::lex_and_cmp;

    #[test]
    fn test_trad() {
        lex_and_cmp("/* foo bar baz */", vec![]);
    }

    #[test]
    fn test_trad_with_cmnts() {
        lex_and_cmp("/* this comment /* // /** ends here:  */", vec![]);
    }

    #[test]
    fn test_doc() {
        lex_and_cmp("/** foo bar baz */", vec![]);
    }

    #[test]
    fn test_eol_ff() {
        lex_and_cmp("// foo bar baz\x0c", vec![]);
    }

    #[test]
    fn test_eol_nl() {
        lex_and_cmp("// foo bar baz\n", vec![]);
    }

    #[test]
    fn test_eol_cr() {
        lex_and_cmp("// foo bar baz\r", vec![]);
    }

    #[test]
    fn test_eol_crlf() {
        lex_and_cmp("// foo bar baz\r\n", vec![]);
    }

    #[test]
    fn test_eol_with_trad() {
        lex_and_cmp("// /* foo bar baz */\n", vec![]);
    }
}

#[cfg(test)]
mod ws {
    use super::lex_and_cmp;

    #[test]
    fn test_sp() {
        lex_and_cmp(" ", vec![]);
    }

    #[test]
    #[should_panic]
    fn test_hex_sp() {
        // TODO: canonicalize ASCII by emitting ASCII Unicode escapes as
        // ASCII
        lex_and_cmp("\\u0020", vec![]);
    }

    #[test]
    fn test_ht() {
        lex_and_cmp("\t", vec![]);
    }

    #[test]
    fn test_ff() {
        lex_and_cmp("\x0c", vec![]);
    }

    #[test]
    fn test_lf() {
        lex_and_cmp("\n", vec![]);
    }

    #[test]
    fn test_cr() {
        lex_and_cmp("\r", vec![]);
    }
    #[test]
    fn test_crlf() {
        lex_and_cmp("\r\n", vec![]);
    }
}
