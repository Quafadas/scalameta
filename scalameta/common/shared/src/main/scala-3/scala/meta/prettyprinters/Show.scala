package scala.meta
package prettyprinters

import org.scalameta.internal.ScalaCompat.EOL

import scala.language.implicitConversions

trait Show[-T] {
  def apply(t: T): Show.Result
}

private[meta] object Show {
  sealed abstract class Result {
    def desc: String
    override def toString = {
      val sb = new StringBuilder
      var indentation = 0
      def nl(obj: Result) = {
        sb.append(EOL)
        sb.append("  " * indentation)
        loop(obj)
      }
      def loop(obj: Result): Unit = obj match {
        case None => // do nothing
        case obj: Str => sb.append(obj.value)
        case obj: Sequence => obj.xs.foreach(loop)
        case obj: Repeat =>
          val sep = obj.sep
          val sbLenInit = sb.length
          obj.xs.foreach { x =>
            val sbLen = sb.length
            loop(x)
            if (sbLen != sb.length) sb.append(sep)
          }
          val sbLenLast = sb.length
          if (sbLenInit != sbLenLast) sb.setLength(sbLenLast - sep.length)
        case obj: Indent => indentation += 1; nl(obj.res); indentation -= 1
        case obj: Newline => nl(obj.res)
        case obj: Meta => loop(obj.res)
        case obj: Wrap =>
          val sbLenInit = sb.length
          sb.append(obj.prefix)
          val sbLen = sb.length
          loop(obj.res)
          if (sbLen != sb.length) sb.append(obj.suffix) else sb.setLength(sbLenInit)
        case result: Function => loop(result.fn(sb))
      }
      loop(this)
      sb.toString
    }
    final def isEmpty: Boolean = this eq None
    def headChar: Option[Char]
  }

  case object None extends Result {
    override def desc: String = "None"
    def headChar: Option[Char] = Option.empty[Char]
  }
  final case class Str(value: String) extends Result {
    override def desc: String = s"Str($value)"
    def headChar: Option[Char] = if (value.isEmpty) Option.empty else Some(value.head)
  }
  private[meta] case class Sequence(xs: Result*) extends Result {
    override def desc: String = s"Sequence(${xs.map(_.desc).mkString(", ")})"
    def headChar: Option[Char] = xs.find(_ ne None).flatMap(_.headChar)
  }
  private case class Repeat(xs: Seq[Result], sep: String) extends Result {
    override def desc: String = s"Repeat(${xs.map(_.desc).mkString(", ")}, $sep)"
    def headChar: Option[Char] = xs.find(_ ne None).flatMap(_.headChar)
  }
  private case class Indent(res: Result) extends Result {
    override def desc: String = s"Indent(${res.desc})"
    def headChar: Option[Char] = res.headChar
  }
  private case class Newline(res: Result) extends Result {
    override def desc: String = s"Newline(${res.desc})"
    def headChar: Option[Char] = Some('\n')
  }
  private case class Meta(data: Any, res: Result) extends Result {
    override def desc: String = s"Meta($data, ${res.desc})"
    def headChar: Option[Char] = res.headChar
  }
  private case class Wrap(prefix: String, res: Result, suffix: String) extends Result {
    override def desc: String = s"Wrap($prefix, ${res.desc}, $suffix)"
    def headChar: Option[Char] = if (prefix.isEmpty) res.headChar else Some(prefix.head)
  }
  private case class Function(fn: StringBuilder => Result) extends Result {
    override def desc: String = "Function(<fn>)"
    def headChar: Option[Char] = Option.empty[Char]
  }

  implicit def show2result[T](x: T)(implicit show: Show[T]): Result = show(x)

  def sequence[T](xs: T*): Result = {
    val results = xs.toList.map {
      case res: Result => res
      case other => Str(other.toString)
    }
    val res = results.filter(_ ne None)
    res match {
      case Nil => None
      case res => Sequence(res: _*)
    }
  }

  def indent(res: Result): Result = if (res eq None) None else Indent(res)
  def indent[T](x: T)(implicit show: Show[T]): Result = indent(show(x))

  def repeat[T](xs: Seq[T], sep: String = "")(implicit show: Show[T]): Result =
    repeat(sep)(xs.map(show(_)): _*)
  def repeat[T](xs: Seq[T], prefix: String, sep: String, suffix: String)(implicit
      show: Show[T]
  ): Result = wrap(prefix, repeat(xs, sep), suffix)

  def repeat(xs: Result*): Result = repeat("")(xs: _*)
  def repeat(sep: String)(xs: Result*): Result = {
    val results = xs.filter(_ ne None)
    if (results.isEmpty) None else Repeat(results, sep)
  }
  def repeat(prefix: String, sep: String, suffix: String)(xs: Result*): Result =
    wrap(prefix, repeat(sep)(xs: _*), suffix)

  def newline(res: Result): Result = if (res eq None) None else Newline(res)
  def newline[T](x: T)(implicit show: Show[T]): Result = newline(show(x))

  def meta(data: Any, res: Result): Result = if (res eq None) None else Meta(data, res)
  def meta[T](data: Any, xs: T*): Result = {
    // Simple implementation without macros
    val results = xs.toList.map(x => Str(x.toString))
    meta(data, sequence(results: _*))
  }

  // wrap if non-empty
  def wrap[T](x: T, suffix: String)(implicit show: Show[T]): Result = wrap("", x, suffix)
  def wrap[T](prefix: String, x: T)(implicit show: Show[T]): Result = wrap(prefix, x, "")
  def wrap[T](prefix: => String, x: T, suffix: => String)(implicit show: Show[T]): Result = {
    val res = show(x)
    if (res eq None) None else Wrap(prefix, res, suffix)
  }

  def wrap(prefix: => String, res: Result, suffix: => String): Result =
    if (res eq None) None else Wrap(prefix, res, suffix)

  implicit class ResultOps(val res: Result) extends AnyVal {
    def orElse(fallback: => Result): Result = if (res ne None) res else fallback
  }

  // Common show instances
  implicit val stringShow: Show[String] = new Show[String] {
    def apply(s: String): Result = Str(s)
  }
  
  implicit val resultShow: Show[Result] = new Show[Result] {
    def apply(r: Result): Result = r
  }
}