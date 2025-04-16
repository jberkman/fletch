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

use std::{
    collections::BTreeMap,
    fmt::Display,
    fs::File,
    io::Read,
    path::{Path, PathBuf},
    rc::Rc,
};

use fletch_ast::CompilationUnitNode;
use fletch_parser::{lexerdef, parse, token_epp};

#[derive(Debug)]
pub enum ClassLoaderError {
    ClassNotFound,
    Io(std::io::Error),
    Lexer(String),
    Parse(Vec<ClassLoaderError>),
    AstError,
}

impl From<std::io::Error> for ClassLoaderError {
    fn from(e: std::io::Error) -> Self {
        Self::Io(e)
    }
}

impl Display for ClassLoaderError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            Self::AstError => "AST Error".fmt(f),
            Self::ClassNotFound => "Class not found".fmt(f),
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

impl std::error::Error for ClassLoaderError {}

pub struct ClassLoader {
    source_path: Vec<PathBuf>,
    compilation_units: BTreeMap<String, Rc<CompilationUnitNode>>,
}

impl ClassLoader {
    pub fn new(source_path: &str) -> Self {
        let source_path = source_path.split(':').map(|s| PathBuf::from(s)).collect();
        let compilation_units = BTreeMap::new();
        Self {
            source_path,
            compilation_units,
        }
    }

    pub fn load_file(&mut self, path: &Path) -> Result<Rc<CompilationUnitNode>, ClassLoaderError> {
        let mut buf = String::new();
        File::open(path)?.read_to_string(&mut buf)?;

        let lexerdef = lexerdef();
        let lexer = lexerdef.lexer(&buf);
        let (ast, errs) = parse(&lexer);
        if errs.is_empty() {
            ast.unwrap()
                .map(|ast| Rc::new(ast))
                .map_err(|_| ClassLoaderError::AstError)
        } else {
            Err(ClassLoaderError::Parse(
                errs.iter()
                    .map(|e| {
                        ClassLoaderError::Lexer(format!(
                            "{}: {}",
                            path.display(),
                            e.pp(&lexer, &token_epp)
                        ))
                    })
                    .collect(),
            ))
        }
    }

    pub fn load_class(&mut self, class: &str) -> Result<Rc<CompilationUnitNode>, ClassLoaderError> {
        if let Some(ret) = self.compilation_units.get(class) {
            return Ok(ret.clone());
        }
        let class_file = PathBuf::from(class).with_extension("java");
        for path in &self.source_path {
            let full_path = path.join(&class_file);
            if full_path.exists() {
                return self.load_file(full_path.as_path());
            }
        }
        Err(ClassLoaderError::ClassNotFound)
    }
}
