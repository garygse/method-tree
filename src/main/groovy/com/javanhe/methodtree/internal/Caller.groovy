package com.javanhe.methodtree.internal

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Represents the caller of a class method.
 */
@ToString
@EqualsAndHashCode
class Caller {
    String className
    String methodName
    String methodDesc
    String source
}