package org.scalameta

import scala.quoted.*

object UnreachableMacros {
  
  inline def unreachable: Nothing = ${ unreachableImpl }
  
  inline def unreachableWithDebug(inline dsl: Any): Nothing = ${ unreachableWithDebugImpl('dsl) }
  
  inline def unreachableWithDebugAndClue(inline dsl: Any, inline clue: Any): Nothing = 
    ${ unreachableWithDebugAndClueImpl('dsl, 'clue) }

  def unreachableImpl(using Quotes): Expr[Nothing] = {
    import quotes.reflect.*
    
    '{ org.scalameta.UnreachableError.raise(Map.empty[String, Any]) }
  }

  def unreachableWithDebugImpl(dsl: Expr[Any])(using Quotes): Expr[Nothing] = {
    import quotes.reflect.*
    
    // For now, provide a simple implementation
    '{ org.scalameta.UnreachableError.raise(Map.empty[String, Any]) }
  }

  def unreachableWithDebugAndClueImpl(dsl: Expr[Any], clue: Expr[Any])(using Quotes): Expr[Nothing] = {
    import quotes.reflect.*
    
    // For now, provide a simple implementation
    '{ org.scalameta.UnreachableError.raise(Map.empty[String, Any], ${clue}.toString) }
  }
}