use cfgrammar::Span;
use std::cmp::{max, min};

pub trait Spanned {
    fn span(&self) -> Span;
}

macro_rules! impl_spanned {
    ($t:ty) => {
        impl Spanned for $t {
            fn span(&self) -> Span {
                self.span
            }
        }
    };
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

#[derive(Clone, Debug)]
pub struct Id {
    pub span: Span,
}

// Names

// Packages

#[derive(Clone, Debug)]
pub struct CompilationUnit {
    pub span: Span,
    pub type_decls: Option<TypeDecls>,
}

impl_spanned!(CompilationUnit);

pub type TypeDecls = SpannedVec<TypeDecl>;

#[derive(Clone, Debug)]
pub enum TypeDecl {
    Class(ClassDecl),
    // Iface(IfaceDecl),
    EOS(Span),
}

impl Spanned for TypeDecl {
    fn span(&self) -> Span {
        match self {
            Self::Class(class) => class.span(),
            // Iface(IfaceDecl) => iface.span(),
            Self::EOS(s) => *s,
        }
    }
}

// Only in LALR(1) Grammer

// Classes

//// Class Decl

#[derive(Clone, Debug)]
pub struct ClassDecl {
    pub span: Span,
    pub id: Box<Id>,
    pub body: Box<ClassBody>,
}

impl_spanned!(ClassDecl);

#[derive(Clone, Debug)]
pub struct ClassBody {
    pub span: Span,
}

impl_spanned!(ClassBody);


//// Field Decls

//// Method Decls

//// Static Inits

//// Ctor Decls

// Interfaces

//// Interface Decls

// Arrays

// Blocks and Statements

// Expressions
