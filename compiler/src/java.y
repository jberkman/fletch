// The Syntactic Grammar

%start CompilationUnit

%%

// Lexical Structure

Id -> Result<Id, ()>:
    'ID' { Ok(Id { span: $span }) }
    ;

// Types, Values, and Vars

// Names

// Packages

CompilationUnit -> Result<CompilationUnit, ()>:
    TypeDeclsOpt { Ok(CompilationUnit { span: $span, type_decls: $1? }) }
    ;

TypeDeclsOpt -> Result<Option<TypeDecls>, ()>:
    TypeDecls { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

TypeDecls -> Result<TypeDecls, ()>:
    TypeDecl { Ok(TypeDecls { span: $span, items: vec![$1?] }) }
    | TypeDecls TypeDecl { 
        let mut v = $1?;
        v.push($2?);
        Ok(v)
    }
    ;

TypeDecl -> Result<TypeDecl, ()>:
    ClassDecl { Ok(TypeDecl::Class($1?)) }
    | 'SEMIC' { Ok(TypeDecl::EOS($span)) }
    ;

// Only in LALR(1) Grammer

// Classes

//// Class Decl

ClassDecl -> Result<ClassDecl, ()>:
    'CLASS' Id ClassBody { Ok(ClassDecl { span: $span, id: Box::new($2?), body: Box::new($3?) }) }
    ;

ClassBody -> Result<ClassBody, ()>:
    'LBRACE' 'RBRACE' { Ok(ClassBody { span: $span }) }
    ;

//// Field Decls

//// Method Decls

//// Static Inits

//// Ctor Decls

// Interfaces

//// Interface Decls

// Arrays

// Blocks and Statements

// Expressions

%%

use crate::ast::*;
