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

lrlex_mod!("java.l");
lrpar_mod!("java.y");

use fletch_ast::CompilationUnitNode;
pub use java_l::*;
pub use java_y::*;
use lrlex::lrlex_mod;
use lrpar::lrpar_mod;
use std::{
    error::Error as StdError,
    fmt::Display,
    fs::File,
    io::{Error as IoError, Read},
};

#[derive(Debug)]
pub enum Error {
    Io(IoError),
    Lexer(String),
    Parse(Vec<Error>),
}

impl From<IoError> for Error {
    fn from(e: IoError) -> Self {
        Self::Io(e)
    }
}

impl Display for Error {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            Self::Io(e) => e.fmt(f),
            Self::Lexer(s) => s.fmt(f),
            Self::Parse(errs) => {
                for err in errs {
                    err.fmt(f)?;
                }
                Ok(())
            }
        }
    }
}

impl StdError for Error {}

pub fn parse_file(file: &String) -> Result<CompilationUnitNode, Error> {
    let mut buf = String::new();
    File::open(file)?.read_to_string(&mut buf)?;

    let lexerdef = lexerdef();
    let lexer = lexerdef.lexer(&buf);
    let (ast, errs) = parse(&lexer);
    if errs.is_empty() {
        ast.unwrap()
    } else {
        Err(Error::Parse(
            errs.iter()
                .map(|e| Error::Lexer(format!("{}: {}", file, e.pp(&lexer, &token_epp))))
                .collect(),
        ))
    }
}
