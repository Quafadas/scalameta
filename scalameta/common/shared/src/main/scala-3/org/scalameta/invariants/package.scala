package org.scalameta

import scala.quoted.*
import scala.reflect.ClassTag

package object invariants {
  // This macro behaves like `Predef.require` with an additional twist
  // of taking apart `requirement` and generating out an informative exception message
  // if things does wrong.
  //
  // Things that end up on an error message:
  // 1) Values of local variables.
  // 2) Calls to `org.scalameta.debug` as explained in documentation to that method.
  inline def require(requirement: Boolean): Unit = ${ Macros.require('requirement) }
  inline def require(requirement: Boolean, clue: String): Unit = ${ Macros.requireWithClue('requirement, 'clue) }

  extension [T](x: T)
    // Equivalent to requiring that `x.getClass` is assignable from `U`.
    // Implemented as a macro, because there's no other way to delegate to another macro.
    inline def require[U: ClassTag]: U = ${ Macros.requireCast[T, U]('x) }

  // Provides pretty notation for implications of different kinds.
  // This is surprisingly helpful when writing certain complex `require` calls.
  implicit class XtensionImplication(private val left: Boolean) extends AnyVal {
    def ==>(right: Boolean) = !left || right
    def <==(right: Boolean) = right ==> left
    def <==>(right: Boolean) = left ==> right && right ==> left
  }
}