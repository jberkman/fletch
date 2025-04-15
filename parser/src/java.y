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

// The Syntactic Grammar

%start CompilationUnit
%%

// Lexical Structure

Identifier -> Result<IdentifierNode, ParseError>:
    'ID' { Ok(IdentifierNode::new($span, $span)) }
    ;

Literal -> Result<LiteralNode, ParseError>:
    'DEC' { Ok(Literal::new_decimal_node($span)) }
    | 'HEX' { Ok(Literal::new_hex_node($span)) }
    | 'OCT' { Ok(Literal::new_octal_node($span)) }
    | 'FLT' { Ok(Literal::new_float_node($span)) }
    | 'FALSE' { Ok(Literal::new_bool_node($span, false)) }
    | 'TRUE' { Ok(Literal::new_bool_node($span, true)) }
    | 'CHR' { Ok(Literal::new_char_node($span)) }
    | 'STR' { Ok(Literal::new_string_node($span)) }
    | 'NULL' { Ok(Literal::new_null_node($span)) }
    ;

// Types, Values, and Vars

Type -> Result<TypeNode, ParseError>:
    Type 'LBRACKET' 'RBRACKET' { Ok(Type::new_node($span, Type::Array(Box::new($1?)))) }
    | 'BOOLEAN' { Ok(Type::new_node($span, Type::Boolean)) }
    | 'BYTE' { Ok(Type::new_node($span, Type::Byte)) }
    | 'CHAR' { Ok(Type::new_node($span, Type::Char)) }
    | 'DOUBLE' { Ok(Type::new_node($span, Type::Double)) }
    | 'FLOAT' { Ok(Type::new_node($span, Type::Float)) }
    | 'INT' { Ok(Type::new_node($span, Type::Int)) }
    | 'LONG' { Ok(Type::new_node($span, Type::Long)) }
    | 'SHORT' { Ok(Type::new_node($span, Type::Short)) }
    | Name { Ok(Type::new_node($span, Type::Name($1?))) }
    ;

// Names

Name -> Result<NameNode, ParseError>:
    Identifier { Ok(Name::new_simple_node($span, $1?)) }
    | Name 'DOT' Identifier { Ok(Name::new_qualified_node($span, $1?, $3?)) }
    ;

// Packages

CompilationUnit -> Result<CompilationUnitNode, ParseError>:
    TypeDeclsOpt { Ok(CompilationUnit::new_node($span, $1?)) }
    ;

TypeDeclsOpt -> Result<Option<TypeDecls>, ParseError>:
    TypeDecls { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

TypeDecls -> Result<TypeDecls, ParseError>:
    TypeDecl { Ok(TypeDecl::new_list($span, $1?)) }
    | TypeDecls TypeDecl {
        let mut v = $1?;
        v.span = $span;
        v.data.push($2?);
        Ok(v)
    }
    ;

TypeDecl -> Result<TypeDeclNode, ParseError>:
    ClassDecl { Ok(TypeDecl::new_class_node($span, $1?)) }
    | 'SEMIC' { Ok(TypeDecl::new_empty_node($span)) }
    ;

// Only in LALR(1) Grammer

ModifiersOpt -> Result<Option<Modifiers>, ParseError>:
    Modifiers { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

Modifiers -> Result<Modifiers, ParseError>:
    Modifier { Ok(Modifier::new_list($span, $1?)) }
    | Modifiers Modifier {
        let mut v = $1?;
        v.span = $span;
        v.data.push($2?);
        Ok(v)
    }
    ;

Modifier -> Result<ModifierNode, ParseError>:
    'ABSTRACT' { Ok(Modifier::new_node($span, Modifier::Abstract)) }
    | 'FINAL' { Ok(Modifier::new_node($span, Modifier::Final)) }
    | 'NATIVE' { Ok(Modifier::new_node($span, Modifier::Native)) }
    | 'PRIVATE' { Ok(Modifier::new_node($span, Modifier::Private)) }
    | 'PROTECTED' { Ok(Modifier::new_node($span, Modifier::Protected)) }
    | 'PUBLIC' { Ok(Modifier::new_node($span, Modifier::Public)) }
    | 'STATIC' { Ok(Modifier::new_node($span, Modifier::Static)) }
    | 'SYNCHRONIZED' { Ok(Modifier::new_node($span, Modifier::Synchronized)) }
    | 'TRANSIENT' { Ok(Modifier::new_node($span, Modifier::Transient)) }
    | 'VOLATILE' { Ok(Modifier::new_node($span, Modifier::Volatile)) }
    ;

// Classes

//// Class Decl

ClassDecl -> Result<ClassDeclNode, ParseError>:
    ModifiersOpt 'CLASS' Identifier ClassBody { Ok(ClassDecl::new_node($span, $1?, $3?, $4?)) }
    ;

ClassBody -> Result<ClassBodyNode, ParseError>:
    'LBRACE' ClassBodyDeclsOpt 'RBRACE' { Ok(ClassBody::new_node($span, $2?)) }
    ;

ClassBodyDeclsOpt -> Result<Option<ClassBodyDecls>, ParseError>:
    ClassBodyDecls { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

ClassBodyDecls -> Result<ClassBodyDecls, ParseError>:
    ClassBodyDecl { Ok(ClassBodyDecl::new_list($span, $1?)) }
    | ClassBodyDecls ClassBodyDecl {
        let mut v = $1?;
        v.span = $span;
        v.data.push($2?);
        Ok(v)
    }
    ;

ClassBodyDecl -> Result<ClassBodyDeclNode, ParseError>:
    MethodDecl { Ok(ClassBodyDecl::new_method_node($span, $1?)) }
    ;

//// Field Decls

VariableDeclaratorId -> Result<VariableDeclaratorIdNode, ParseError>:
    Identifier { Ok(VariableDeclaratorId::new_singleton_node($span, $1?)) }
    | VariableDeclaratorId 'LBRACKET' 'RBRACKET' { Ok(VariableDeclaratorId::new_array_node($span, Box::new($1?))) }
    ;

//// Method Decls

MethodDecl -> Result<MethodDeclNode, ParseError>:
    ModifiersOpt 'VOID' MethodDeclarator MethodBody { Ok(MethodDecl::new_node($span, $1?, None, $3?, $4?)) }
    ;

MethodDeclarator -> Result<MethodDeclaratorNode, ParseError>:
    Identifier 'LPAREN' FormalParameterListOpt 'RPAREN' { Ok(MethodDeclarator::new_node($span, $1?, $3?)) }
    ;

FormalParameterListOpt -> Result<Option<FormalParameterList>, ParseError>:
    FormalParameterList { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

FormalParameterList -> Result<FormalParameterList, ParseError>:
    FormalParameter { Ok(FormalParameter::new_list($span, $1?) )}
    | FormalParameterList FormalParameter {
        let mut v = $1?;
        v.span = $span;
        v.data.push($2?);
        Ok(v)
    }
    ;

FormalParameter -> Result<FormalParameterNode, ParseError>:
    Type VariableDeclaratorId { Ok(FormalParameter::new_node($span, $1?, $2?)) }
    ;

MethodBody -> Result<MethodBodyNode, ParseError>:
    Block { Ok(MethodBody::new_node($span, Some($1?))) }
    | 'SEMIC' { Ok(MethodBody::new_node($span, None)) }
    ;

//// Static Inits

//// Ctor Decls

// Interfaces

//// Interface Decls

// Arrays

// Blocks and Statements

Block -> Result<BlockNode, ParseError>:
    'LBRACE' BlockStatementsOpt 'RBRACE' { Ok(Block::new_node($span, $2?)) }
    ;

BlockStatementsOpt -> Result<Option<BlockStatements>, ParseError>:
    BlockStatements { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

BlockStatements -> Result<BlockStatements, ParseError>:
    BlockStatement { Ok(BlockStatement::new_list($span, $1?)) }
    | BlockStatements BlockStatement {
        let mut v = $1?;
        v.span = $span;
        v.data.push($2?);
        Ok(v)
    }
    ;

BlockStatement -> Result<BlockStatementNode, ParseError>:
    // LclVarDeclstatement
    Statement { Ok(BlockStatement::new_statement_node($span, $1?)) }
    ;

Statement -> Result<StatementNode, ParseError>:
    StatementWithoutTrailingSubstatement { $1 }
    ;

StatementWithoutTrailingSubstatement -> Result<StatementNode, ParseError>:
    ExpressionStatement { $1 }
    ;

ExpressionStatement -> Result<StatementNode, ParseError>:
    StatementExpression 'SEMIC' { Ok(Statement::new_expression_node($span, $1?)) }
    ;

StatementExpression -> Result<ExpressionNode, ParseError>:
    MethodInvocation { Ok(Expression::new_method_invocation_node($span, $1?)) }
    ;

// Expressions

Primary -> Result<ExpressionNode, ParseError>:
    PrimaryNoNewArray { $1 }
    ;

PrimaryNoNewArray -> Result<ExpressionNode, ParseError>:
    Literal { Ok(Expression::new_literal_node($span, $1?)) }
    | FieldAccess { Ok(Expression::new_field_access_node($span, $1?)) }
    | MethodInvocation { Ok(Expression::new_method_invocation_node($span, $1?)) }
    | ArrayAccess { Ok(Expression::new_array_access_node($span, $1?)) }
    ;

ArgumentListOpt -> Result<Option<ExpressionList>, ParseError>:
    ArgumentList { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

ArgumentList -> Result<ExpressionList, ParseError>:
    Expression { Ok(Expression::new_list($span, $1?)) }
    | ArgumentList 'COMMA' Expression {
        let mut v = $1?;
        v.span = $span;
        v.data.push($3?);
        Ok(v)
    }
    ;

FieldAccess -> Result<FieldAccessNode, ParseError>:
    Primary 'DOT' Identifier { Ok(FieldAccess::new_primary_node($span, $1?, $3?)) }
    ;

MethodInvocation -> Result<MethodInvocationNode, ParseError>:
    Name 'LPAREN' ArgumentListOpt 'RPAREN' { Ok(MethodInvocation::new_name_node($span, $1?, $3?)) }
    | Primary 'DOT' Identifier 'LPAREN' ArgumentListOpt 'RPAREN' { Ok(MethodInvocation::new_primary_node($span, $1?, $3?, $5?)) }
    ;

ArrayAccess -> Result<ArrayAccessNode, ParseError>:
    Name 'LBRACKET' Expression 'RBRACKET' { Ok(ArrayAccess::new_name_node($span, $1?, $3?)) }
    | PrimaryNoNewArray 'LBRACKET' Expression 'RBRACKET' { Ok(ArrayAccess::new_primary_node($span, $1?, $3?)) }
    ;

PostfixExpression -> Result<ExpressionNode, ParseError>:
    Primary { $1 }
    ;

UnaryExpressionNotPlusMinus -> Result<ExpressionNode, ParseError>:
    PostfixExpression { $1 }
    ;

UnaryExpression -> Result<ExpressionNode, ParseError>:
    UnaryExpressionNotPlusMinus { $1 }
    ;

MultiplicativeExpression -> Result<ExpressionNode, ParseError>:
    UnaryExpression { $1 }
    ;

AdditiveExpression -> Result<ExpressionNode, ParseError>:
    MultiplicativeExpression { $1 }
    | AdditiveExpression 'PLUS' MultiplicativeExpression { Ok(Expression::new_addition_node($span, $1?, $3?)) }
    ;

ShiftExpression -> Result<ExpressionNode, ParseError>:
    AdditiveExpression { $1 }
    ;

RelationalExpression -> Result<ExpressionNode, ParseError>:
    ShiftExpression { $1 }
    ;

EqualityExpression -> Result<ExpressionNode, ParseError>:
    RelationalExpression { $1 }
    ;

AndExpression -> Result<ExpressionNode, ParseError>:
    EqualityExpression { $1 }
    ;

ExclusiveOrExpression -> Result<ExpressionNode, ParseError>:
    AndExpression { $1 }
    ;

InclusiveOrExpression -> Result<ExpressionNode, ParseError>:
    ExclusiveOrExpression { $1 }
    ;

ConditionalAndExpression -> Result<ExpressionNode, ParseError>:
    InclusiveOrExpression { $1 }
    ;

ConditionalOrExpression -> Result<ExpressionNode, ParseError>:
    ConditionalAndExpression { $1 }
    ;

ConditionalExpression -> Result<ExpressionNode, ParseError>:
    ConditionalOrExpression { $1 }
    ;

AssignmentExpression -> Result<ExpressionNode, ParseError>:
    ConditionalExpression { $1 }
    ;

Expression -> Result<ExpressionNode, ParseError>:
    AssignmentExpression { $1 }
    ;

%%

use fletch_ast::*;
use crate::Error as ParseError;
