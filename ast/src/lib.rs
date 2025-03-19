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

pub type LiteralNode = Node<Literal>;

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

impl Type {
    pub fn new_node(span: Span, type_: Self) -> TypeNode {
        Node::new(span, type_)
    }
}

pub type TypeNode = Node<Type>;

// Names

#[derive(Clone, Debug)]
pub enum Name {
    Simple(IdentifierNode),
    Qualified(IdentifierList),
}

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

pub type NameNode = Node<Name>;

// Packages

#[derive(Clone, Debug)]
pub struct CompilationUnit {
    pub type_declarations: Option<TypeDeclarations>,
}

impl CompilationUnit {
    pub fn new_node(span: Span, type_declarations: Option<TypeDeclarations>) -> CompilationUnitNode {
        Node::new(span, Self { type_declarations })
    }
}

pub type CompilationUnitNode = Node<CompilationUnit>;

#[derive(Clone, Debug)]
pub enum TypeDeclaration {
    Class(ClassDeclarationNode),
    Empty,
}

impl TypeDeclaration {
    pub fn new_class_node(span: Span, class_declaration: ClassDeclarationNode) -> TypeDeclarationNode {
        Node::new(span, Self::Class(class_declaration))
    }

    pub fn new_empty_node(span: Span) -> TypeDeclarationNode {
        Node::new(span,  Self::Empty)
    }

    pub fn new_list(span: Span, item: TypeDeclarationNode) -> TypeDeclarations {
        Node::new(span, vec![item])
    }
}

pub type TypeDeclarationNode = Node<TypeDeclaration>;
pub type TypeDeclarations = VecNode<TypeDeclarationNode>;

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

impl Modifier {
    pub fn new_node(span: Span, modifier: Self) -> ModifierNode {
        Node::new(span, modifier)
    }

    pub fn new_list(span: Span, item: ModifierNode) -> Modifiers {
        Node::new(span, vec![item])
    }
}

pub type ModifierNode = Node<Modifier>;
pub type Modifiers = VecNode<ModifierNode>;

// Classes

//// Class Decl

#[derive(Clone, Debug)]
pub struct ClassDeclaration {
    pub modifiers: Option<Modifiers>,
    pub identifier: IdentifierNode,
    pub body: ClassBodyNode,
}

impl ClassDeclaration {
    pub fn new_node(span: Span, modifiers: Option<Modifiers>, identifier: IdentifierNode, body: ClassBodyNode) -> ClassDeclarationNode {
        Node::new(span,  Self { modifiers, identifier, body })
    }
}

pub type ClassDeclarationNode = Node<ClassDeclaration>;

#[derive(Clone, Debug)]
pub struct ClassBody {
    pub declarations: Option<ClassBodyDeclarations>,
}

impl ClassBody {
    pub fn new_node(span: Span, declarations: Option<ClassBodyDeclarations>) -> ClassBodyNode {
        Node::new(span,  Self { declarations })
    }
}
    
pub type ClassBodyNode = Node<ClassBody>;

#[derive(Clone, Debug)]
pub enum ClassBodyDeclaration {
    Method(MethodDeclarationNode),
}

impl ClassBodyDeclaration {
    pub fn new_method_node(span: Span, method: MethodDeclarationNode) -> ClassBodyDeclarationNode {
        Node::new(span, Self::Method(method))
    }

    pub fn new_list(span: Span, item: ClassBodyDeclarationNode) -> ClassBodyDeclarations {
        Node::new(span, vec![item])
    }
}

pub type ClassBodyDeclarationNode = Node<ClassBodyDeclaration>;
pub type ClassBodyDeclarations = VecNode<ClassBodyDeclarationNode>;

//// Field Declarations

#[derive(Clone, Debug)]
pub enum VariableDeclaratorId {
    Singleton(IdentifierNode),
    Array(Box<VariableDeclaratorIdNode>),
}

impl VariableDeclaratorId {
    pub fn new_singleton_node(span: Span, identifier: IdentifierNode) -> VariableDeclaratorIdNode {
        Node::new(span, Self::Singleton(identifier))
    }
    
    pub fn new_array_node(span: Span, array: Box<VariableDeclaratorIdNode>) -> VariableDeclaratorIdNode {
        Node::new(span, Self::Array(array))
    }
}

pub type VariableDeclaratorIdNode = Node<VariableDeclaratorId>;

//// Method Declarations

#[derive(Clone, Debug)]
pub struct MethodDeclaration {
    pub modifiers: Option<Modifiers>,
    pub return_type: Option<TypeNode>,
    pub declarator: MethodDeclaratorNode,
    pub body: MethodBodyNode,
}


impl MethodDeclaration {
    pub fn new_node(span: Span, modifiers: Option<Modifiers>, return_type: Option<TypeNode>, declarator: MethodDeclaratorNode, body: MethodBodyNode) -> MethodDeclarationNode {
        Node::new(span, Self { modifiers, return_type, declarator, body })
    }
}
    
pub type MethodDeclarationNode = Node<MethodDeclaration>;

#[derive(Clone, Debug)]
pub struct MethodDeclarator {
    pub identifier: IdentifierNode,
    pub parameter_list: Option<FormalParameterList>,
}

impl MethodDeclarator {
    pub fn new_node(span: Span, identifier: IdentifierNode, parameter_list: Option<FormalParameterList>) -> MethodDeclaratorNode {
        Node::new(span, Self { identifier, parameter_list })
    }
}
    
pub type MethodDeclaratorNode = Node<MethodDeclarator>;
#[derive(Clone, Debug)]
pub struct FormalParameter {
    pub type_: TypeNode,
    pub identifier: VariableDeclaratorIdNode,
}

impl FormalParameter {
    pub fn new_node(span: Span, type_: TypeNode, identifier: VariableDeclaratorIdNode) -> FormalParameterNode {
        Node::new(span, Self { type_, identifier })
    }

    pub fn new_list(span: Span, item: FormalParameterNode) -> FormalParameterList {
        Node::new(span, vec![item])
    }
}

pub type FormalParameterNode = Node<FormalParameter>;
pub type FormalParameterList = VecNode<FormalParameterNode>;

#[derive(Clone, Debug)]
pub struct MethodBody {
    pub block: Option<BlockNode>,
}

impl MethodBody {
    pub fn new_node(span: Span, block: Option<BlockNode>) -> MethodBodyNode {
        Node::new(span, Self { block })
    }
}

pub type MethodBodyNode = Node<MethodBody>;

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

impl Block {
    pub fn new_node(span: Span, statements: Option<BlockStatements>) -> BlockNode {
        Node::new(span, Self { statements })
    }
}

pub type BlockNode = Node<Block>;

#[derive(Clone, Debug)]
pub enum BlockStatement {
    //Declaration(LocalVariableDeclarationStatementNode),
    Statement(StatementNode),
}

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

pub type BlockStatementNode = Node<BlockStatement>;
pub type BlockStatements = VecNode<BlockStatementNode>;

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
    Literal(LiteralNode),
    MethodInvocation(MethodInvocationNode),
}

impl Expression {
    pub fn new_literal_node(span: Span, literal: LiteralNode) -> ExpressionNode {
        Node::new(span, Self::Literal(literal))
    }

    pub fn new_method_invocation_node(span: Span, method_invocation: MethodInvocationNode) -> ExpressionNode {
        Node::new(span, Self::MethodInvocation(method_invocation))
    }

    pub fn new_list(span: Span, item: ExpressionNode) -> ExpressionList {
        Node::new(span, vec![item])
    }
}

pub type ExpressionNode = Node<Expression>;
pub type ExpressionList = VecNode<ExpressionNode>;

#[derive(Clone, Debug)]
pub enum MethodInvocation {
    Primary(Box<ExpressionNode>, IdentifierNode, Option<ExpressionList>),
}

impl MethodInvocation {
    pub fn new_primary_node(span: Span, primary: ExpressionNode, identifier: IdentifierNode, argment_list: Option<ExpressionList>) -> MethodInvocationNode {
        Node::new(span, Self::Primary(Box::new(primary), identifier, argment_list))
    }
}

pub type MethodInvocationNode = Node<MethodInvocation>;
