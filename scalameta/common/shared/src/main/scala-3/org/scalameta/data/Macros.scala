package org.scalameta.data

import scala.quoted.*

object DataTyperMacros {
  // Simplified stubs for Scala 3 - these were originally complex macro implementations
  // For now, we provide minimal implementations to maintain API compatibility
  // TODO: Implement proper null/empty checking logic using Scala 3 reflection when needed
  
  def nullCheck[T](x: T): Unit = ()
  def emptyCheck[T](x: T): Unit = ()
}