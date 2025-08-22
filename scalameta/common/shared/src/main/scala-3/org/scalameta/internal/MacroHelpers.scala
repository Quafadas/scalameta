package org.scalameta
package internal

import scala.quoted.*

// Simplified macro helpers for Scala 3
// These were originally complex macro utilities that used Scala 2 reflection
// For now, providing minimal stubs to maintain API compatibility

trait MacroHelpers extends DebugFinder with MacroCompat with FreeLocalFinder with ImplTransformers {
  // Minimal stub implementations
}

trait DebugFinder {
  // Stub for debug finding functionality
}

trait FreeLocalFinder {
  // Stub for free local variable finding functionality  
}

trait ImplTransformers {
  // Stub for implementation transformers
}