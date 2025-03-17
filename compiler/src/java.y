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
    ClassDecl { Ok(TypeDecl { span: $span, kind: TypeDeclKind::Class($1?) }) }
    | 'SEMIC' { Ok(TypeDecl { span: $span, kind: TypeDeclKind::EOS }) }
    ;

// Only in LALR(1) Grammer

ModifiersOpt -> Result<Option<Modifiers>, ()>:
    Modifiers { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

Modifiers -> Result<Modifiers, ()>:
    Modifier { Ok(Modifiers { span: $span, items: vec![$1?]}) }
    | Modifiers Modifier {
        let mut v = $1?;
        v.push($2?);
        Ok(v)
    }
    ;

Modifier -> Result<Modifier, ()>:
    'ABSTRACT' { Ok(Modifier { span: $span, kind: ModifierKind::Abstract }) }
    | 'FINAL' { Ok(Modifier { span: $span, kind: ModifierKind::Final }) }
    | 'NATIVE' { Ok(Modifier { span: $span, kind: ModifierKind::Native }) }
    | 'PRIVATE' { Ok(Modifier { span: $span, kind: ModifierKind::Private }) }
    | 'PROTECTED' { Ok(Modifier { span: $span, kind: ModifierKind::Protected }) }
    | 'PUBLIC' { Ok(Modifier { span: $span, kind: ModifierKind::Public }) }
    | 'STATIC' { Ok(Modifier { span: $span, kind: ModifierKind::Static }) }
    | 'SYNCHRONIZED' { Ok(Modifier { span: $span, kind: ModifierKind::Synchronized}) }
    | 'TRANSIENT' { Ok(Modifier { span: $span, kind: ModifierKind::Transient}) }
    | 'VOLATILE' { Ok(Modifier { span: $span, kind: ModifierKind::Volatile }) }
    ;

// Classes

//// Class Decl

ClassDecl -> Result<ClassDecl, ()>:
    'CLASS' Id ClassBody { Ok(ClassDecl { span: $span, id: $2?, body: $3? }) }
    ;

ClassBody -> Result<ClassBody, ()>:
    'LBRACE' ClassBodyDeclsOpt 'RBRACE' { Ok(ClassBody { span: $span }) }
    ;

ClassBodyDeclsOpt -> Result<Option<ClassBodyDecls>, ()>:
    ClassBodyDecls { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

ClassBodyDecls -> Result<ClassBodyDecls, ()>:
    ClassBodyDecl { Ok(ClassBodyDecls { span: $span, items: vec![$1?] }) }
    | ClassBodyDecls ClassBodyDecl {
        let mut v = $1?;
        v.push($2?);
        Ok(v)
    }
    ;

ClassBodyDecl -> Result<ClassBodyDecl, ()>:
    ClassMemberDecl { Ok(ClassBodyDecl { span: $span, kind: ClassBodyDeclKind::ClassMember($1?) }) }
    ;

ClassMemberDecl -> Result<ClassMemberDecl, ()>:
    MethodDecl { Ok(ClassMemberDecl { span: $span, kind: ClassMemberDeclKind::Method($1?) }) }
    ;

//// Field Decls

//// Method Decls

MethodDecl -> Result<MethodDecl, ()>:
    MethodHeader { Ok(MethodDecl { span: $span, header: $1? }) }
    ;

MethodHeader -> Result<MethodHeader, ()>:
    ModifiersOpt 'VOID' MethodDecltor { Ok(MethodHeader { span: $span, modifiers: $1?, decltor: $3? }) }
    ;

MethodDecltor -> Result<MethodDecltor, ()>:
    Id 'LPAREN' 'RPAREN' { Ok(MethodDecltor { span: $span, id: $1?, params: None }) }
    ;

//// Static Inits

//// Ctor Decls

// Interfaces

//// Interface Decls

// Arrays

// Blocks and Statements

// Expressions

%%

use fletch_ast::*;
