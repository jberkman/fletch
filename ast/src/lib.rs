use cfgrammar::Span;
use std::cmp::{max, min};
use fletch_ast_macros::Spanned;

pub trait Spanned {
    fn span(&self) -> Span;
}

#[derive(Clone, Debug)]
pub struct KindOf<T> {
    pub span: Span,
    pub kind: T,
}

impl<T> Spanned for KindOf<T> {
    fn span(&self) -> Span {
        self.span
    }
}

#[derive(Clone, Debug)]
pub struct SpannedVec<T: Spanned> {
    pub span: Span,
    pub items: Vec<T>,
}

impl<T: Spanned> SpannedVec<T> {
    pub fn expand(&mut self, span: Span) {
        self.span = Span::new(
            min(self.span.start(), span.start()),
            max(self.span.end(), span.end()),
        );
    }

    pub fn push(&mut self, item: T) {
        self.expand(item.span());
        self.items.push(item);
    }
}

impl<T: Spanned> Spanned for SpannedVec<T> {
    fn span(&self) -> Span {
        self.span
    }
}

// Lexical Structure

#[derive(Clone, Debug, Spanned)]
pub struct Id {
    pub span: Span,
}

// Names

// Packages

#[derive(Clone, Debug, Spanned)]
pub struct CompilationUnit {
    pub span: Span,
    pub type_decls: Option<TypeDecls>,
}

#[derive(Clone, Debug)]
pub enum TypeDeclKind {
    Class(ClassDecl),
    // Iface(IfaceDecl),
    EOS,
}

pub type TypeDecl = KindOf<TypeDeclKind>;
pub type TypeDecls = SpannedVec<TypeDecl>;

// Only in LALR(1) Grammer

#[derive(Clone, Debug)]
pub enum ModifierKind {
    Public,
    Protected,
    Private,
    Static,
    Abstract,
    Final,
    Native,
    Synchronized,
    Transient,
    Volatile,
}

pub type Modifier = KindOf<ModifierKind>;
pub type Modifiers = SpannedVec<Modifier>;

// Classes

//// Class Decl

#[derive(Clone, Debug, Spanned)]
pub struct ClassDecl {
    pub span: Span,
    pub id: Id,
    pub body: ClassBody,
}

#[derive(Clone, Debug, Spanned)]
pub struct ClassBody {
    pub span: Span,
}

#[derive(Clone, Debug)]
pub enum ClassBodyDeclKind {
    ClassMember(ClassMemberDecl),
}

pub type ClassBodyDecl = KindOf<ClassBodyDeclKind>;
pub type ClassBodyDecls = SpannedVec<ClassBodyDecl>;

#[derive(Clone, Debug)]
pub enum ClassMemberDeclKind {
    Method(MethodDecl),
}

pub type ClassMemberDecl = KindOf<ClassMemberDeclKind>;

//// Field Decls

//// Method Decls

#[derive(Clone, Debug, Spanned)]
pub struct MethodDecl {
    pub span: Span,
    pub header: MethodHeader,
}

#[derive(Clone, Debug, Spanned)]
pub struct MethodHeader {
    pub span: Span,
    pub modifiers: Option<Modifiers>,
    pub decltor: MethodDecltor,
}

#[derive(Clone, Debug, Spanned)]
pub struct MethodDecltor {
    pub span: Span,
    pub id: Id,
    pub params: Option<FormalParams>,
}

#[derive(Clone, Debug, Spanned)]
pub struct FormalParam {
    pub span: Span,
}

pub type FormalParams = SpannedVec<FormalParam>;

//// Static Inits

//// Ctor Decls

// Interfaces

//// Interface Decls

// Arrays

// Blocks and Statements

// Expressions
