package sjsonnet

sealed trait Expr
object Expr{
  case class Null(offset: Int) extends Expr
  case class True(offset: Int) extends Expr
  case class False(offset: Int) extends Expr
  case class Self(offset: Int) extends Expr
  case class Super(offset: Int) extends Expr
  case class $(offset: Int) extends Expr

  case class Str(offset: Int, value: String) extends Expr
  case class Num(offset: Int, value: Double) extends Expr
  case class Id(offset: Int, value: String) extends Expr
  case class Arr(offset: Int, value: Seq[Expr]) extends Expr
  case class Obj(offset: Int, value: ObjBody) extends Expr

  sealed trait FieldName

  object FieldName{
    case class Fixed(value: String) extends FieldName
    case class Dyn(expr: Expr) extends FieldName
  }
  sealed trait Member

  object Member{
    case class Field(fieldName: FieldName,
                     plus: Boolean,
                     args: Option[Params],
                     sep: String,
                     rhs: Expr) extends Member
    case class BindStmt(value: Bind) extends Member
    case class AssertStmt(value: Expr, msg: Option[Expr]) extends Member
  }


  case class Parened(offset: Int, value: Expr) extends Expr
  case class Params(args: Seq[(String, Option[Expr])])
  case class Args(args: Seq[(Option[String], Expr)])

  case class UnaryOp(offset: Int, op: UnaryOp.Op, value: Expr) extends Expr
  object UnaryOp{
    sealed trait Op
    object `+` extends Op
    object `-` extends Op
    object `~` extends Op
    object `!` extends Op
  }
  case class BinaryOp(offset: Int, lhs: Expr, op: BinaryOp.Op, rhs: Expr) extends Expr
  object BinaryOp{
    sealed trait Op
    object `*` extends Op
    object `/` extends Op
    object `%` extends Op
    object `+` extends Op
    object `-` extends Op
    object `<<` extends Op
    object `>>` extends Op
    object `<` extends Op
    object `>` extends Op
    object `<=` extends Op
    object `>=` extends Op
    object `in` extends Op
    object `==` extends Op
    object `!=` extends Op
    object `&` extends Op
    object `^` extends Op
    object `|` extends Op
    object `&&` extends Op
    object `||` extends Op
  }
  case class AssertExpr(offset: Int, asserted: Member.AssertStmt, returned: Expr) extends Expr
  case class LocalExpr(offset: Int, bindings: Seq[Bind], returned: Expr) extends Expr

  case class Bind(name: String, args: Option[Params], rhs: Expr)
  case class Import(offset: Int, value: String) extends Expr
  case class ImportStr(offset: Int, value: String) extends Expr
  case class Error(offset: Int, value: Expr) extends Expr
  case class Apply(offset: Int, value: Expr, args: Args) extends Expr
  case class Select(offset: Int, value: Expr, name: String) extends Expr
  case class Lookup(offset: Int, value: Expr, index: Expr) extends Expr
  case class Slice(offset: Int,
                   value: Expr,
                   start: Option[Expr],
                   end: Option[Expr],
                   stride: Option[Expr]) extends Expr
  case class Function(offset: Int, params: Params, body: Expr) extends Expr
  case class IfElse(offset: Int, cond: Expr, then: Expr, `else`: Option[Expr]) extends Expr

  sealed trait CompSpec extends Expr
  case class IfSpec(offset: Int, cond: Expr) extends CompSpec
  case class ForSpec(offset: Int, name: String, cond: Expr) extends CompSpec

  case class Comp(offset: Int, value: Expr, first: ForSpec, rest: Seq[CompSpec]) extends Expr
  case class ObjExtend(offset: Int, base: Expr, ext: ObjBody) extends Expr

  sealed trait ObjBody
  object ObjBody{
    case class MemberList(value: Seq[Member]) extends ObjBody
    case class ObjComp(preLocals: Seq[Member.BindStmt],
                       key: Expr,
                       value: Expr,
                       postLocals: Seq[Member.BindStmt],
                       first: ForSpec,
                       rest: Seq[CompSpec]) extends ObjBody
  }

}