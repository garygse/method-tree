package com.javanhe.methodtree.internal

import com.javanhe.methodtree.Finder
import com.javanhe.methodtree.util.AsmTypeConverter
import org.codehaus.groovy.classgen.asm.BytecodeHelper
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.Method

/**
 * A {@link MethodVisitor} that works in tandem with a provided
 * {@link TrackingClassVisitor} to help keep track of any methods in the
 * <tt>TrackingClassVisitor</tt> that call the specific target method.
 */
class TrackingMethodVisitor extends MethodVisitor {

    final TrackingClassVisitor classVisitor

    private final String visitedMethodName
    private final String visitedMethodDescriptor
    private final String targetClass
    private final Method targetMethod

    private boolean callsTarget

    TrackingMethodVisitor(String visitedMethodName, String visitedMethodDescriptor, String targetClass, Method targetMethod, TrackingClassVisitor classVisitor) {
        super(Finder.SUPPORTED_ASM_VERSION)
        this.visitedMethodName = visitedMethodName
        this.visitedMethodDescriptor = visitedMethodDescriptor
        this.targetClass = targetClass
        this.targetMethod = targetMethod
        this.classVisitor = classVisitor
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        def actualClassName = owner

        try {
            // use reflection on owner/name to determine if inheritance is at play here
            def methodArguments = AsmTypeConverter.getArguments(descriptor)
            def className = BytecodeHelper.formatNameForClassLoading(owner)
            actualClassName = name == '<init>' ?
                    Class.forName(className).getConstructor(methodArguments as Class[]).declaringClass.name.replaceAll(/\./, '/') :
                    Class.forName(className).getMethod(name, methodArguments as Class[]).declaringClass.name.replaceAll(/\./, '/')
        } catch (anything) {
            // do nothing, we don't care
        }

        if (actualClassName == targetClass && name == targetMethod.name && descriptor == targetMethod.descriptor) {
            callsTarget = true
        }
    }

    @Override
    void visitCode() {
        callsTarget = false
    }

    @Override
    void visitEnd() {
        if (callsTarget) {
            classVisitor.callers << new Caller(
                    className : classVisitor.className,
                    methodName: visitedMethodName,
                    methodDesc: visitedMethodDescriptor,
                    source    : classVisitor.source)
        }
    }
}