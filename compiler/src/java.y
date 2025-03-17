// The Syntactic Grammar

%start CompilationUnit

%%

// Lexical Structure

Id -> Result<Id, ()>:
    'ID' { Ok(Id { span: $span }) }
    ;

Literal -> Result<Literal, ()>:
    "DEC" { Ok(Literal { span: $span, kind: LiteralKind::Decimal($1?) }) }
    | "HEX" { Ok(Literal { span: $span, kind: LiteralKind::Hexadecimal($1?) }) }
    | "OCT" { Ok(Literal { span: $span, kind: LiteralKind::Octal($1?) }) }
    | "FLT" { Ok(Literal { span: $span, kind: LiteralKind::FloatingPoint($1?) }) }
    | "FALSE" { Ok(Literal { span: $span, kind: LiteralKind::Boolean(false) }) }
    | "TRUE" { Ok(Literal { span: $span, kind: LiteralKind::Bolean(true) }) }
    | "CHR" { Ok(Literal { span: $span, kind: LiteralKind::Character($1?) }) }
    | "STR" { Ok(Literal { span: $span, kind: LiteralKind::String($1?) }) }
    | "NULL" { Ok(Literal { span: $span, kind: LiteralKind::Null }) }
    ;

// Types, Values, and Vars

Type -> Result<Type, ()>:
    PrimitiveType { Ok(Type { span: $span, kind: TypeKind::Primitive($1?) }) }
    //| ReferenceType { Ok(Type { span: $span, kind: TypeKind::Reference }) }
    ;

PrimitiveType -> Result<PrimitiveType, ()>:
    NumericType { Ok(PrimitiveType { span: $span, kind: PrimitiveTypeKind::Numeric($1?) }) }
    | 'BOOLEAN' { Ok(PrimitiveType { span: $span, kind: PrimitiveTypeKind::Boolean }) }
    ;

NumericType -> Result<NumericType, ()>:
    IntegralType { Ok(NumericType { span: $span, kind: NumericTypeKind::Integral($1?) }) }
    | FloatingPointType { Ok(NumericType { span: $span, kind: NumericTypeKind::FloatingPoint($1?) }) }
    ;

IntegralType -> Result<IntegralType, ()>:
    'BYTE' { Ok(IntegralType { span: $span, kind: IntegralTypeKind::Byte }) }
    | 'SHORT' { Ok(IntegralType { span: $span, kind: IntegralTypeKind::Short }) }
    | 'INT' { Ok(IntegralType { span: $span, kind: IntegralTypeKind::Int }) }
    | 'LONG' { Ok(IntegralType { span: $span, kind: IntegralTypeKind::Long }) }
    | 'CHAR' { Ok(IntegralType { span: $span, kind: IntegralTypeKind::Char }) }
    ;

FloatingPointType -> Result<FloatingPointType, ()>:
    'FLOAT' { Ok(FloatingPointType { span: $span, kind: FloatingPointTypeKind::Float }) }
    | 'DOUBLE' { Ok(FloatingPointType { span: $span, kind: FloatingPointTypeKind::Double }) }
    ;

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

VarDecltorId -> Result<VarDecltorId, ()>:
    Id { Ok(VarDecltorId { span: $span, kind: VarDecltorIdKind::Id($1?) }) }
    | VarDecltorId 'LBRACKET' 'RBRACKET' { Ok(VarDecltorId { span: $span, kind: VarDecltorIdKind::ArrayOf(Box::new($1?)) }) }
    ;

//// Method Decls

MethodDecl -> Result<MethodDecl, ()>:
    MethodHeader { Ok(MethodDecl { span: $span, header: $1? }) }
    ;

MethodHeader -> Result<MethodHeader, ()>:
    ModifiersOpt 'VOID' MethodDecltor { Ok(MethodHeader { span: $span, modifiers: $1?, decltor: $3? }) }
    ;

MethodDecltor -> Result<MethodDecltor, ()>:
    Id 'LPAREN' FormalParamsOpt 'RPAREN' { Ok(MethodDecltor { span: $span, id: $1?, params: None }) }
    ;

FormalParamsOpt -> Result<Option<FormalParams>, ()>:
    FormalParams { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

FormalParams -> Result<FormalParams, ()>:
    FormalParam { Ok(FormalParams { span: $span, items: vec![$1?]} )}
    | FormalParams FormalParam {
        let mut v = $1?;
        v.push($2?);
        Ok(v)
    }
    ;

FormalParam -> Result<FormalParam, ()>:
    Type VarDecltorId { Ok(FormalParam { span: $span, type_: $1?, var_decltor_id: $2? }) }
    ;

//// Static Inits

//// Ctor Decls

// Interfaces

//// Interface Decls

// Arrays

// Blocks and Statements

// Expressions

Primary -> Result<Primary, ()>:
    PrimaryNoNewArray { Ok(Primary { span: $span, kind: PrimaryKind::NoNewArray($1?) }) }
    ;

PrimaryNoNewArray -> Result<PrimaryNoNewArray, ()>:
    Literal { Ok(PrimaryNoNewArray { span: $span, kind: PrimaryNoNewArrayKind::Literal($1?) }) }
    | MethodInvocation { Ok(PrimaryNoNewArray { span: $span, kind: PrimaryNoNewArrayKind::MethodInvocation($1?) }) }
    ;

ArgumentListOpt -> Result<Option<ArgumentList>, ()>:
    ArgumentList { Ok(Some($1?)) }
    | /* opt */ { Ok(None) }
    ;

ArgumentList -> Result<ArgumentList, ()>:
    Expression { Ok(ArgumentList { span: $span, items: vec![$1?] }) }
    | ArgumentList 'COMMA' Expression {
        let mut v = $1?;
        v.push($3?);
        Ok(v)
    }
    ;

MethodInvocation -> Result<MethodInvocation, ()>:
    Primary 'DOT' Id 'LPAREN' ArgumentListOpt 'RPAREN' { 
        Ok(MethodInvocation { span: $span, kind: MethodInvocationKind::Primary($1?, $2?, $3?) })
    }
    ;

PostfixExpression -> Result<PostfixExpression, ()>:
    Primary { Ok(PostfixExpression { span: $span, kind: PostfixExpressionKind::Primary($1?) }) }
    ;

UnaryExpressionNotPlusMinus -> Result<UnaryExpressionNotPlusMinus, ()>:
    PostfixExpression { Ok(UnaryExpressionNotPlusMinus { span: $span, kind: UnaryExpressionNotPlusMinusKind::Postfix($1?) }) }
    ;

UnaryExpression -> Result<UnaryExpression, ()>:
    UnaryExpressionNotPlusMinus { Ok(UnaryExpression { span: $span, kind: UnaryExpressionKind::NotPlusMinus($1?) }) }
    ;

MultiplicativeExpression -> Result<MultiplicativeExpression, ()>:
    UnaryExpression { Ok(MultiplicativeExpression { span: $span, kind: MultiplicativeExpressionKind::Unary($1?) }) }
    ;

AdditiveExpression -> Result<AdditiveExpression, ()>:
    MultiplicativeExpression { Ok(AdditiveExpression { span: $span, kind: AdditiveExpressionKind::Multiplicative($1?) }) }
    ;

ShiftExpression -> Result<ShiftExpression, ()>:
    AdditiveExpression { Ok(ShiftExpression { span: $span, kind: ShiftExpressionKind::Additive($1?) }) }
    ;

RelationalExpression -> Result<RelationalExpression, ()>:
    ShiftExpression { Ok(RelationalExpression { span: $span, kind: RelationalExpressionKind::Shift($1?) }) }
    ;

EqualityExpression -> Result<EqualityExpression, ()>:
    RelationalExpression { Ok(EqualityExpression { span: $span, kind: EqualityExpressionKind::Relational($1?) }) }
    ;

AndExpression -> Result<AndExpression, ()>:
    EqualityExpression { Ok(AndExpression { span: $span, kind: AndExpressionKind::Equality($1?) }) }
    ;

ExclusiveOrExpression -> Result<ExclusiveOrExpression, ()>:
    AndExpression { Ok(ExclusiveOrExpression { span: $span, kind: ExclusiveOrExpressionKind::And($1?) }) }
    ;

InclusiveOrExpression -> Result<InclusiveOrExpression, ()>:
    ExclusiveOrExpression { Ok(InclusiveOrExpression { span: $span, kind: InclusiveOrExpressionKind::ExclusiveOr($1?) }) }
    ;

ConditionalAndExpression -> Result<ConditionalAndExpression, ()>:
    InclusiveOrExpression { Ok(ConditionalAndExpression { span: $span, kind: ConditionalAndExpressionKind::InclusiveOr($1?) }) }
    ;

ConditionalOrExpression -> Result<ConditionalOrExpression, ()>:
    ConditionalAndExpression { Ok(ConditionalOrExpression { span: $span, kind: ConditionalOrExpressionKind::ConditionalAnd($1?) }) }
    ;

ConditionalExpression -> Result<ConditionalExpression, ()>:
    ConditionalOrExpression { Ok(ConditionalExpression { span: $span, kind: ConditionalExpressionKind::ConitionalOr($1?) }) }
    ;

AssignmentExpression -> Result<AssignmentExpression, ()>:
    ConditionalExpression { Ok(AssignmentAxpression { span: $span, kind: AssignmentExpressionKind::Conditional($1?) }) }
    ;

Expression -> Result<Expression, ()>:
    AssignmentExpression { Ok(Expression { span: $span, kind: ExpressionKind::Assignment($1?) }) }
    ;

%%

use fletch_ast::*;
