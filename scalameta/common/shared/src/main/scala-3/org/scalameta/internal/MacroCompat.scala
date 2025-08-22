package org.scalameta.internal

import scala.quoted.*

trait MacroCompat {
  def qctx: Quotes
  given quotes: Quotes = qctx
  
  // In Scala 3, we don't need separate types for AssignOrNamedArg
  import quotes.reflect.*
  type AssignOrNamedArg = NamedArg
}

object MacroCompat {
  val productFieldNamesAvailable = true
}