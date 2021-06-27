package com.javanhe.methodtree

import com.javanhe.methodtree.internal.TrackingClassVisitor
import com.javanhe.methodtree.internal.Caller
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Method

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Provides the functionality to determine all points from which specific methods may be called.
 */
class Finder {

    private List<Caller> callers
    private boolean includeIndirect

    static final int SUPPORTED_ASM_VERSION = Opcodes.ASM9

    /**
     * Creates a <tt>Finder</tt> instance.
     *
     * @param includeIndirect determines if indirect method calls are included in the results (default true)
     */
    Finder(boolean includeIndirect = true) {
        this.callers = []
        this.includeIndirect = includeIndirect
    }

    /**
     * Locates all callers of a given class method. The results will contain both direct
     * and indirect callers, unless the <tt>includeIndirect</tt> parameter in the
     * {@link #Finder() constructor} was set to <tt>false</tt>.
     *
     * @param path the path to the class files
     * @param targetMethod a {@link Method} instance representing the method to target
     * @return a List of callers of the method
     */
    List<Caller> findCallingMethods(String path, java.lang.reflect.Method targetMethod) {
        findCallingMethods(path, Type.getInternalName(targetMethod.getDeclaringClass()), Method.getMethod(targetMethod))
    }

    /**
     * Locates all callers of a given class method. The results will contain both direct
     * and indirect callers, unless the <tt>includeIndirect</tt> parameter in the
     * {@link #Finder() constructor} was set to <tt>false</tt>.
     *
     * @param path the path to the class files
     * @param targetClass the class to target, following the ASM naming pattern
     * @param targetMethod an ASM method descriptor representing the method to target
     * @return a List of callers of the method
     */
    List<Caller> findCallingMethods(String path, String targetClass, Method targetMethod) {
        def classVisitor = new TrackingClassVisitor(targetClass, targetMethod)

        Files.walk(Paths.get(path))
            .filter { p ->
                p.toFile().isFile() }.each { file ->
                    if (file.toString().endsWith('.class')) {
                        InputStream stream = new BufferedInputStream(Files.newInputStream(file), 1024)
                        ClassReader reader = new ClassReader(stream)
                        reader.accept(classVisitor, 0)
                        stream.close()
                    }
            }

        classVisitor.callers.each { caller ->
            if (!callers.contains(caller)) {
                callers << caller

                if (includeIndirect) {
                    findCallingMethods(path, caller.className, new Method(caller.methodName, caller.methodDesc))
                }
            }
        }

        callers
    }
}
