package org.scalameta
package explore

import scala.quoted.*

object ExploreMacros {

  def wildcardImportStaticsImpl(packageName: Expr[String])(using Quotes): Expr[List[String]] = {
    // For now, provide a simple implementation that returns empty list
    // TODO: Implement using Scala 3 reflection when needed
    '{ List.empty[String] }
  }

  def allStaticsImpl(packageName: Expr[String])(using Quotes): Expr[List[String]] = {
    // For now, provide a simple implementation that returns empty list
    // TODO: Implement using Scala 3 reflection when needed
    '{ List.empty[String] }
  }

  def extensionSurfaceImpl(packageName: Expr[String])(using Quotes): Expr[List[String]] = {
    // For now, provide a simple implementation that returns empty list
    // TODO: Implement using Scala 3 reflection when needed
    '{ List.empty[String] }
  }
}