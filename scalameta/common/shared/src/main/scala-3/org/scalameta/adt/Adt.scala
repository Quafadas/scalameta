package org.scalameta.adt

import scala.annotation.StaticAnnotation

// The @root, @branch and @leaf annotations for ADT pattern in Scala 3
// For now, these are simplified stubs that maintain API compatibility
// TODO: Implement full macro annotation logic using Scala 3 macros when needed

class root extends StaticAnnotation
class branch extends StaticAnnotation  
class leaf extends StaticAnnotation
class none extends StaticAnnotation

// Placeholder object for typer macros that were used in Scala 2 version
object AdtTyperMacros {
  // These methods are called from generated code but for now we provide stubs
  def hierarchyCheck[T]: Unit = ()
  def immutabilityCheck[T]: Unit = ()
}