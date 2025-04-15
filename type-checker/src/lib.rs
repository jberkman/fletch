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

use cfgrammar::span::Span;
use fletch_ast::{
    ClassBodyDecl, ClassBodyDeclNode, ClassDeclNode, CompilationUnitNode, IdentifierNode, MethodDeclNode, Modifier, TypeDecl, TypeDeclNode
};

#[derive(Debug)]
pub enum Error {
    InvalidModifier(Span),
}

fn check_method_decl(method: &MethodDeclNode) -> Result<(), Error> {
    if let Some(mods) = &method.data.modifiers {
        let mut has_non_access = false;
        let mut has_static = false;
        let mut has_visibility = false;
        for modifier in &mods.data {
            match modifier.data {
                Modifier::Abstract | Modifier::Final if !has_non_access => has_non_access = true,
                Modifier::Private | Modifier::Protected | Modifier::Public if !has_visibility => has_visibility = true,
                Modifier::Static if !has_static => has_static = true,
                _ => return Err(Error::InvalidModifier(modifier.span)),
            }
        }
    }
    Ok(())
}

fn check_class_body_decl(decl: &ClassBodyDeclNode) -> Result<(), Error> {
    match &decl.data {
        ClassBodyDecl::Method(method) => check_method_decl(method)?,
    }
    Ok(())
}

fn check_identifier(_: &IdentifierNode) -> Result<(), Error> {
    Ok(())
}

fn check_class_decl(class_decl: &ClassDeclNode) -> Result<(), Error> {
    if let Some(mods) = &class_decl.data.modifiers {
        let mut has_non_access = false;
        let mut has_visibility = false;
        for modifier in &mods.data {
            match modifier.data {
                Modifier::Abstract | Modifier::Final if !has_non_access => has_non_access = true,
                Modifier::Public if !has_visibility => has_visibility = true,
                _ => return Err(Error::InvalidModifier(modifier.span)),
            }
        }
    }
    check_identifier(&class_decl.data.identifier)?;
    if let Some(decls) = &class_decl.data.body.data.decls {
        for decl in &decls.data {
            check_class_body_decl(decl)?;
        }
    }
    Ok(())
}

fn check_type_decl(type_decl: &TypeDeclNode) -> Result<(), Error> {
    if let TypeDecl::Class(class_decl) = &type_decl.data {
        check_class_decl(class_decl)?;
    }
    Ok(())
}

pub fn type_check(ast: &CompilationUnitNode) -> Result<(), Error> {
    if let Some(type_decls) = &ast.data.type_decls {
        for d in &type_decls.data {
            check_type_decl(d)?;
        }
    }
    Ok(())
}
