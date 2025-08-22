package scala.meta
package internal
package trees

import scala.quoted.*

// Simplified reflection utilities for Scala 3
// These were originally complex reflection-based utilities that used Scala 2 macros
trait TreesReflection {
  // Minimal stub implementations for API compatibility
}

// Common typer macros that were used in Scala 2 version
object CommonTyperMacros {
  def hierarchyCheck[T]: Unit = ()
  def productPrefix[T]: String = ""
  def loadField[T](f: T, s: String): Unit = ()
  def storeField[T](f: T, v: T, s: String): Unit = ()
  def initField[T](f: T): T = f
  def initParam[T](f: T): T = f
  def children[T, U]: List[U] = Nil
}

// Simplified namer macros for Scala 3
trait CommonNamerMacros {
  // Minimal stub implementations for API compatibility
}