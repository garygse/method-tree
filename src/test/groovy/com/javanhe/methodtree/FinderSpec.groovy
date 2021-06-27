package com.javanhe.methodtree

import com.javanhe.methodtree.internal.Caller
import com.javanhe.methodtree.test.B
import org.objectweb.asm.commons.Method
import spock.lang.Specification

class FinderSpec extends Specification {

    def 'Ensure direct and indirect method calls are found when requested'() {
        given: 'a Finder instance that will look for indirect method calls'
        def finder = new Finder()

        and: 'a method within a class to work on'
        def path = 'build/classes'
        def targetClass = 'com/javanhe/methodtree/test/B'
        def targetMethod = Method.getMethod'void b1()'

        when: 'the finder is invoked'
        def callers = finder.findCallingMethods(path, targetClass, targetMethod)

        then: 'all direct and indirect method calls will be found'
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/A', methodName: 'a1', methodDesc: '()V', source: 'A.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/A', methodName: 'a2', methodDesc: '()V', source: 'A.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/B', methodName: 'b2', methodDesc: '()V', source: 'B.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/C', methodName: '<init>', methodDesc: '()V', source: 'C.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/component/GreetingComponent', methodName: 'getMessage', methodDesc: '()Ljava/lang/String;', source: 'GreetingComponent.java'))
        callers.size() == 5
    }

    def 'Ensure only direct method calls are found when the indirect flag is not set'() {
        given: 'a Finder instance that does not include indirect calls'
        def finder = new Finder(false)

        and: 'a method within a class to work on'
        def path = 'build/classes'
        def targetClass = 'com/javanhe/methodtree/test/B'
        def targetMethod = Method.getMethod'void b1()'

        when: 'the finder is invoked'
        def callers = finder.findCallingMethods(path, targetClass, targetMethod)

        then: 'only the direct method calls will be found'
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/A', methodName: 'a1', methodDesc: '()V', source: 'A.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/B', methodName: 'b2', methodDesc: '()V', source: 'B.java'))
        callers.size() == 2
    }

    def 'Ensure calls to functional interfaces are found'() {
        given: 'a Finder instance'
        def finder = new Finder(false) // this test is only interested in direct calls

        and: 'a method within a class to work on'
        def path = 'build/classes'
        def targetClass = 'java/util/function/Supplier'
        def targetMethod = Method.getMethod'Object get()'

        when: 'the finder is invoked'
        def callers = finder.findCallingMethods(path, targetClass, targetMethod)

        then: 'the correct method is found'
        callers.contains(new Caller(
                className : 'com/javanhe/methodtree/test/function/FunctionalTest',
                methodName: 'squareLazy',
                methodDesc: '(Ljava/util/function/Supplier;)Ljava/math/BigInteger;',
                source    : 'FunctionalTest.java'))
        callers.size() == 1
    }

    def 'Ensure methods provided via Reflection works'() {
        given: 'a Finder instance that will look for indirect method calls'
        def finder = new Finder()

        and: 'a method within a class to work on'
        def path = 'build/classes'
        def targetMethod = B.class.getMethod('b1')

        when: 'the finder is invoked'
        def callers = finder.findCallingMethods(path, targetMethod)

        then: 'all direct and indirect method calls will be found'
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/A', methodName: 'a1', methodDesc: '()V', source: 'A.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/A', methodName: 'a2', methodDesc: '()V', source: 'A.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/B', methodName: 'b2', methodDesc: '()V', source: 'B.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/C', methodName: '<init>', methodDesc: '()V', source: 'C.java'))
        callers.contains(new Caller(className: 'com/javanhe/methodtree/test/component/GreetingComponent', methodName: 'getMessage', methodDesc: '()Ljava/lang/String;', source: 'GreetingComponent.java'))
        callers.size() == 5
    }
}
