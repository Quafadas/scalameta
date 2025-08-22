package org.scalameta

package object explore {
  inline def wildcardImportStatics(packageName: String): List[String] =
    ${ ExploreMacros.wildcardImportStaticsImpl('packageName) }

  inline def allStatics(packageName: String): List[String] = 
    ${ ExploreMacros.allStaticsImpl('packageName) }

  inline def extensionSurface(packageName: String): List[String] = 
    ${ ExploreMacros.extensionSurfaceImpl('packageName) }
}