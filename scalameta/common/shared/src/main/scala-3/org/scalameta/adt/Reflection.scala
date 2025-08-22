package org.scalameta.adt

import scala.reflect.ClassTag
import scala.quoted.*

// Simplified reflection utilities for Scala 3
// These were originally complex reflection-based utilities that used Scala 2 macros
// For now, providing minimal stubs to maintain API compatibility
trait Reflection {
  // In Scala 3, we don't have the same reflection API
  // These are simplified stubs
  
  implicit class XtensionAnnotatedSymbol(sym: Any) {
    def hasAnnotation[T: ClassTag]: Boolean = false
    def getAnnotation[T: ClassTag]: Option[Any] = None
  }
  
  // Additional trait members that were in the original
  trait Root { 
    def sym: Any
    def tpe: Any
    def prefix: String = ""
    def allLeafs: List[Adt] = Nil
  }
  
  trait Branch extends Root {
    def root: Root
  }
  
  trait Leaf extends Branch {
    def fields(isPrivateOK: Boolean): List[Field] = Nil
  }
  
  trait Field {
    def name: String
    def tpe: Any
  }
  
  trait Adt {
    def sym: Any
    def tpe: Any
    def prefix: String
  }
}