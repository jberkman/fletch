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

use cfgrammar::Span;

#[derive(Clone, Debug)]
pub struct Node<T> {
    pub span: Span,
    pub data: T,
}

impl<T> Node<T> {
    pub fn new(span: Span, data: T) -> Self {
        Self { span, data }
    }
}

pub type VecNode<T> = Node<Vec<T>>;

// Lexical Structure

pub type Identifier = Span;

pub type IdentifierNode = Node<Identifier>;
pub type IdentifierList = VecNode<IdentifierNode>;

#[derive(Clone, Debug)]
pub enum Literal {
    Boolean(bool),
    Character(Span),
    Decimal(Span),
    FloatingPoint(Span),
    Hexadecimal(Span),
    Null,
    Octal(Span),
    String(Span),
}

pub type LiteralNode = Node<Literal>;

impl Literal {
    pub fn new_bool_node(span: Span, boolean: bool) -> LiteralNode {
        Node::new(span, Self::Boolean(boolean))
    }

    pub fn new_char_node(span: Span) -> LiteralNode {
        Node::new(span, Self::Character(span))
    }

    pub fn new_decimal_node(span: Span) -> LiteralNode {
        Node::new(span, Self::Decimal(span))
    }

    pub fn new_float_node(span: Span) -> LiteralNode {
        Node::new(span, Self::FloatingPoint(span))
    }

    pub fn new_hex_node(span: Span) -> LiteralNode {
        Node::new(span, Self::Hexadecimal(span))
    }

    pub fn new_null_node(span: Span) -> LiteralNode {
        Node::new(span, Self::Null)
    }

    pub fn new_octal_node(span: Span) -> LiteralNode {
        Node::new(span, Self::Octal(span))
    }

    pub fn new_string_node(span: Span) -> LiteralNode {
        Node::new(span, Self::String(span))
    }
}

// Types, Values, and Vars

#[derive(Clone, Debug)]
pub enum Type {
    Array(Box<TypeNode>),
    Boolean,
    Byte,
    Char,
    Double,
    Float,
    Int,
    Long,
    Name(NameNode),
    Short,
}

pub type TypeNode = Node<Type>;

impl Type {
    pub fn new_node(span: Span, type_: Self) -> TypeNode {
        Node::new(span, type_)
    }
}

// Names

#[derive(Clone, Debug)]
pub enum Name {
    Simple(IdentifierNode),
    Qualified(IdentifierList),
}

pub type NameNode = Node<Name>;

impl Name {
    pub fn new_simple_node(span: Span, name: IdentifierNode) -> NameNode {
        NameNode::new(span, Self::Simple(name))
    }

    pub fn new_qualified_node(span: Span, name: NameNode, identifier: IdentifierNode) -> NameNode {
        match name.data {
            Self::Simple(name) => {
                let list = IdentifierList::new(span, vec![name, identifier]);
                NameNode::new(span, Self::Qualified(list))
            }
            Self::Qualified(name_list) => {
                let mut v = name_list;
                v.data.push(identifier);
                NameNode::new(span, Self::Qualified(v))
            }
        }
    }
}

// Packages

#[derive(Clone, Debug)]
pub struct CompilationUnit {
    pub type_decls: Option<TypeDecls>,
}

pub type CompilationUnitNode = Node<CompilationUnit>;

impl CompilationUnit {
    pub fn new_node(span: Span, type_decls: Option<TypeDecls>) -> CompilationUnitNode {
        Node::new(span, Self { type_decls })
    }
}

#[derive(Clone, Debug)]
pub enum TypeDecl {
    Class(ClassDeclNode),
    Empty,
}

pub type TypeDeclNode = Node<TypeDecl>;
pub type TypeDecls = VecNode<TypeDeclNode>;

impl TypeDecl {
    pub fn new_class_node(span: Span, class_decl: ClassDeclNode) -> TypeDeclNode {
        Node::new(span, Self::Class(class_decl))
    }

    pub fn new_empty_node(span: Span) -> TypeDeclNode {
        Node::new(span, Self::Empty)
    }

    pub fn new_list(span: Span, item: TypeDeclNode) -> TypeDecls {
        Node::new(span, vec![item])
    }
}

// Only in LALR(1) Grammer

#[derive(Clone, Debug)]
pub enum Modifier {
    Abstract,
    Final,
    Native,
    Private,
    Protected,
    Public,
    Static,
    Synchronized,
    Transient,
    Volatile,
}

pub type ModifierNode = Node<Modifier>;
pub type Modifiers = VecNode<ModifierNode>;

impl Modifier {
    pub fn new_node(span: Span, modifier: Self) -> ModifierNode {
        Node::new(span, modifier)
    }

    pub fn new_list(span: Span, item: ModifierNode) -> Modifiers {
        Node::new(span, vec![item])
    }
}

// Classes

//// Class Decl

#[derive(Clone, Debug)]
pub struct ClassDecl {
    pub modifiers: Option<Modifiers>,
    pub identifier: IdentifierNode,
    pub body: ClassBodyNode,
}

pub type ClassDeclNode = Node<ClassDecl>;

impl ClassDecl {
    pub fn new_node(
        span: Span,
        modifiers: Option<Modifiers>,
        identifier: IdentifierNode,
        body: ClassBodyNode,
    ) -> ClassDeclNode {
        Node::new(
            span,
            Self {
                modifiers,
                identifier,
                body,
            },
        )
    }
}

#[derive(Clone, Debug)]
pub struct ClassBody {
    pub decls: Option<ClassBodyDecls>,
}

pub type ClassBodyNode = Node<ClassBody>;

impl ClassBody {
    pub fn new_node(span: Span, decls: Option<ClassBodyDecls>) -> ClassBodyNode {
        Node::new(span, Self { decls })
    }
}

#[derive(Clone, Debug)]
pub enum ClassBodyDecl {
    Method(MethodDeclNode),
}

pub type ClassBodyDeclNode = Node<ClassBodyDecl>;
pub type ClassBodyDecls = VecNode<ClassBodyDeclNode>;

impl ClassBodyDecl {
    pub fn new_method_node(span: Span, method: MethodDeclNode) -> ClassBodyDeclNode {
        Node::new(span, Self::Method(method))
    }

    pub fn new_list(span: Span, item: ClassBodyDeclNode) -> ClassBodyDecls {
        Node::new(span, vec![item])
    }
}

//// Field Declarations

#[derive(Clone, Debug)]
pub enum VariableDeclaratorId {
    Singleton(IdentifierNode),
    Array(Box<VariableDeclaratorIdNode>),
}

pub type VariableDeclaratorIdNode = Node<VariableDeclaratorId>;

impl VariableDeclaratorId {
    pub fn new_singleton_node(span: Span, identifier: IdentifierNode) -> VariableDeclaratorIdNode {
        Node::new(span, Self::Singleton(identifier))
    }

    pub fn new_array_node(
        span: Span,
        array: Box<VariableDeclaratorIdNode>,
    ) -> VariableDeclaratorIdNode {
        Node::new(span, Self::Array(array))
    }
}

//// Method Declarations

#[derive(Clone, Debug)]
pub struct MethodDecl {
    pub modifiers: Option<Modifiers>,
    pub return_type: Option<TypeNode>,
    pub declarator: MethodDeclaratorNode,
    pub body: MethodBodyNode,
}

pub type MethodDeclNode = Node<MethodDecl>;

impl MethodDecl {
    pub fn new_node(
        span: Span,
        modifiers: Option<Modifiers>,
        return_type: Option<TypeNode>,
        declarator: MethodDeclaratorNode,
        body: MethodBodyNode,
    ) -> MethodDeclNode {
        Node::new(
            span,
            Self {
                modifiers,
                return_type,
                declarator,
                body,
            },
        )
    }
}

#[derive(Clone, Debug)]
pub struct MethodDeclarator {
    pub identifier: IdentifierNode,
    pub parameter_list: Option<FormalParameterList>,
}

pub type MethodDeclaratorNode = Node<MethodDeclarator>;

impl MethodDeclarator {
    pub fn new_node(
        span: Span,
        identifier: IdentifierNode,
        parameter_list: Option<FormalParameterList>,
    ) -> MethodDeclaratorNode {
        Node::new(
            span,
            Self {
                identifier,
                parameter_list,
            },
        )
    }
}

#[derive(Clone, Debug)]
pub struct FormalParameter {
    pub type_: TypeNode,
    pub identifier: VariableDeclaratorIdNode,
}

pub type FormalParameterNode = Node<FormalParameter>;
pub type FormalParameterList = VecNode<FormalParameterNode>;

impl FormalParameter {
    pub fn new_node(
        span: Span,
        type_: TypeNode,
        identifier: VariableDeclaratorIdNode,
    ) -> FormalParameterNode {
        Node::new(span, Self { type_, identifier })
    }

    pub fn new_list(span: Span, item: FormalParameterNode) -> FormalParameterList {
        Node::new(span, vec![item])
    }
}

#[derive(Clone, Debug)]
pub struct MethodBody {
    pub block: Option<BlockNode>,
}

pub type MethodBodyNode = Node<MethodBody>;

impl MethodBody {
    pub fn new_node(span: Span, block: Option<BlockNode>) -> MethodBodyNode {
        Node::new(span, Self { block })
    }
}

//// Static Inits

//// Ctor Declarations

// Interfaces

//// Interface Declarations

// Arrays

// Blocks and Statements

#[derive(Clone, Debug)]
pub struct Block {
    pub statements: Option<BlockStatements>,
}

pub type BlockNode = Node<Block>;

impl Block {
    pub fn new_node(span: Span, statements: Option<BlockStatements>) -> BlockNode {
        Node::new(span, Self { statements })
    }
}

#[derive(Clone, Debug)]
pub enum BlockStatement {
    //Declaration(LocalVariableDeclarationStatementNode),
    Statement(StatementNode),
}

pub type BlockStatementNode = Node<BlockStatement>;
pub type BlockStatements = VecNode<BlockStatementNode>;

impl BlockStatement {
    //pub fn new_declaration_node(span: Span, declaration: LocalVariableDeclarationStatementNode) -> //BlockStatementNode {
    //    Node::new(span, Self::Declaration(declaration))
    //}

    pub fn new_statement_node(span: Span, statement: StatementNode) -> BlockStatementNode {
        Node::new(span, Self::Statement(statement))
    }

    pub fn new_list(span: Span, item: BlockStatementNode) -> BlockStatements {
        Node::new(span, vec![item])
    }
}

#[derive(Clone, Debug)]
pub enum Statement {
    //Labeled(IdentifierNode, Box<StatementNode>),
    //If(ExpressionNode, Box<StatementNode>, Option<Box<StatementNode>>),
    //While(ExpressionNode, Box<StatementNode>),
    //For(Option<ForInitNode>, Option<ExpressionNode>, Option<ForUpdateNode>, Box<StatementNode>),
    Expression(ExpressionNode),
}

pub type StatementNode = Node<Statement>;

impl Statement {
    pub fn new_expression_node(span: Span, expression: ExpressionNode) -> StatementNode {
        Node::new(span, Self::Expression(expression))
    }
}

// Expressions

#[derive(Clone, Debug)]
pub enum Expression {
    Addition(Box<ExpressionNode>, Box<ExpressionNode>),
    ArrayAccess(ArrayAccessNode),
    FieldAccess(FieldAccessNode),
    Literal(LiteralNode),
    MethodInvocation(MethodInvocationNode),
}

pub type ExpressionNode = Node<Expression>;
pub type ExpressionList = VecNode<ExpressionNode>;

impl Expression {
    pub fn new_addition_node(
        span: Span,
        additive_expression: ExpressionNode,
        multiplicative_expression: ExpressionNode,
    ) -> ExpressionNode {
        Node::new(
            span,
            Self::Addition(
                Box::new(additive_expression),
                Box::new(multiplicative_expression),
            ),
        )
    }

    pub fn new_array_access_node(span: Span, array_access: ArrayAccessNode) -> ExpressionNode {
        Node::new(span, Self::ArrayAccess(array_access))
    }

    pub fn new_field_access_node(span: Span, field_access: FieldAccessNode) -> ExpressionNode {
        Node::new(span, Self::FieldAccess(field_access))
    }

    pub fn new_literal_node(span: Span, literal: LiteralNode) -> ExpressionNode {
        Node::new(span, Self::Literal(literal))
    }

    pub fn new_method_invocation_node(
        span: Span,
        method_invocation: MethodInvocationNode,
    ) -> ExpressionNode {
        Node::new(span, Self::MethodInvocation(method_invocation))
    }

    pub fn new_list(span: Span, item: ExpressionNode) -> ExpressionList {
        Node::new(span, vec![item])
    }
}

#[derive(Clone, Debug)]
pub enum FieldAccess {
    Primary(Box<ExpressionNode>, IdentifierNode),
}

pub type FieldAccessNode = Node<FieldAccess>;

impl FieldAccess {
    pub fn new_primary_node(
        span: Span,
        primary: ExpressionNode,
        identifier: IdentifierNode,
    ) -> FieldAccessNode {
        Node::new(span, Self::Primary(Box::new(primary), identifier))
    }
}

#[derive(Clone, Debug)]
pub enum MethodInvocation {
    Name(NameNode, Option<ExpressionList>),
    Primary(Box<ExpressionNode>, IdentifierNode, Option<ExpressionList>),
}

pub type MethodInvocationNode = Node<MethodInvocation>;

impl MethodInvocation {
    pub fn new_name_node(
        span: Span,
        name: NameNode,
        argument_list: Option<ExpressionList>,
    ) -> MethodInvocationNode {
        Node::new(span, Self::Name(name, argument_list))
    }

    pub fn new_primary_node(
        span: Span,
        primary: ExpressionNode,
        identifier: IdentifierNode,
        argment_list: Option<ExpressionList>,
    ) -> MethodInvocationNode {
        Node::new(
            span,
            Self::Primary(Box::new(primary), identifier, argment_list),
        )
    }
}

#[derive(Clone, Debug)]
pub enum ArrayAccess {
    Name(NameNode, Box<ExpressionNode>),
    Primary(Box<ExpressionNode>, Box<ExpressionNode>),
}

pub type ArrayAccessNode = Node<ArrayAccess>;

impl ArrayAccess {
    pub fn new_name_node(span: Span, name: NameNode, index: ExpressionNode) -> ArrayAccessNode {
        Node::new(span, Self::Name(name, Box::new(index)))
    }

    pub fn new_primary_node(
        span: Span,
        primary: ExpressionNode,
        index: ExpressionNode,
    ) -> ArrayAccessNode {
        Node::new(span, Self::Primary(Box::new(primary), Box::new(index)))
    }
}
