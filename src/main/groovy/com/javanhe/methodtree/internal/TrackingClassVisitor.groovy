package com.javanhe.methodtree.internal

import com.javanhe.methodtree.Finder
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.Method

/**
 * A {@link ClassVisitor} that identifies any of its methods that call out to the target method.
 */
class TrackingClassVisitor extends ClassVisitor {

    String source
    String className

    final ArrayList<Caller> callers

    private String targetClass
    private Method targetMethod

    TrackingClassVisitor(String targetClass, Method targetMethod) {
        super(Finder.SUPPORTED_ASM_VERSION)
        this.targetClass = targetClass
        this.targetMethod = targetMethod
        this.callers = []
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name
    }

    @Override
    void visitSource(String source, String debug) {
        this.source = source
    }

    @Override
    MethodVisitor visitMethod(int access, String methodName, String methodDescriptor, String signature, String[] exceptions) {
        new TrackingMethodVisitor(methodName, methodDescriptor, targetClass, targetMethod, this)
    }
}
