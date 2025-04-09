// MIT License
//
// Copyright (c) 2025 jacob berkman
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.


use lrpar::{Lexeme, Lexer};

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
            lex_and_cmp($input, vec![fletch_parser::$token]);
        }
    };
}

fn lex_and_cmp(input: &str, expected: Vec<u32>) {
    let lexerdef = fletch_parser::lexerdef();
    let lexer = lexerdef.lexer(input);
    let tokens: Vec<_> = lexer.iter().map(|res| res.unwrap().tok_id()).collect();
    assert_eq!(tokens, expected);
}

#[cfg(test)]
mod sep {
    use super::lex_and_cmp;

    test_token!(test_lparen, "(", T_LPAREN);
    test_token!(test_rparen, ")", T_RPAREN);
    test_token!(test_lbrace, "{", T_LBRACE);
    test_token!(test_rbrace, "}", T_RBRACE);
    test_token!(test_lbracket, "[", T_LBRACKET);
    test_token!(test_rbracket, "]", T_RBRACKET);
    test_token!(test_semic, ";", T_SEMIC);
}

#[cfg(test)]
mod op {
    use super::lex_and_cmp;

    test_token!(test_plus, "+", T_PLUS);
}

#[cfg(test)]
mod kw {
    use super::lex_and_cmp;

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
                System.out.println("Hello, " + args[0] + "!");
            }
        }
    "#, vec![
        fletch_parser::T_CLASS,
        fletch_parser::T_ID,
        fletch_parser::T_LBRACE,
        fletch_parser::T_PUBLIC,
        fletch_parser::T_STATIC,
        fletch_parser::T_VOID,
        fletch_parser::T_ID,
        fletch_parser::T_LPAREN,
        fletch_parser::T_ID,
        fletch_parser::T_LBRACKET,
        fletch_parser::T_RBRACKET,
        fletch_parser::T_ID,
        fletch_parser::T_RPAREN,
        fletch_parser::T_LBRACE,
        fletch_parser::T_ID,
        fletch_parser::T_DOT,
        fletch_parser::T_ID,
        fletch_parser::T_DOT,
        fletch_parser::T_ID,
        fletch_parser::T_LPAREN,
        fletch_parser::T_STR,
        fletch_parser::T_PLUS,
        fletch_parser::T_ID,
        fletch_parser::T_LBRACKET,
        fletch_parser::T_DEC,
        fletch_parser::T_RBRACKET,
        fletch_parser::T_PLUS,
        fletch_parser::T_STR,
        fletch_parser::T_RPAREN,
        fletch_parser::T_SEMIC,
        fletch_parser::T_RBRACE,
        fletch_parser::T_RBRACE,
    ]);
}
