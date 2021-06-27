package com.javanhe.methodtree.util

import org.objectweb.asm.Type

/**
 * Helps with the conversion from ASM types to class types.
 */
class AsmTypeConverter {

    /**
     * Converts a method descriptor to a <tt>List</tt> of classes that represent the
     * method arguments. This method is useful for when reflection is required when
     * using ASM bytecode.
     *
     * @param methodDescriptor the method descriptor to retrieve the parameter class types from
     * @return a <tt>List</tt> of classes representing the method arguments
     */
    static List<Class> getArguments(String methodDescriptor) {
        def types = Type.getArgumentTypes(methodDescriptor)
        def classes = []

        types.each { type ->
            classes << toClass(type)
        }

        return classes
    }

    /**
     * Converts the given {@link Type} to a regular <tt>Class</tt>.
     *
     * @param type the <tt>Type</tt> to convert
     * @return a <tt>Class</tt> representation of the given <tt>Type</tt>
     */
    static Class toClass(Type type) {
        switch(type) {
            case Type.BOOLEAN_TYPE:
                return Boolean.TYPE

            case Type.CHAR_TYPE:
                return Character.TYPE

            case Type.BYTE_TYPE:
                return Byte.TYPE

            case Type.SHORT_TYPE:
                return Short.TYPE

            case Type.INT_TYPE:
                return Integer.TYPE

            case Type.FLOAT_TYPE:
                return Float.TYPE

            case Type.LONG_TYPE:
                return Long.TYPE

            case Type.DOUBLE_TYPE:
                return Double.TYPE

            default:
                return getClass(type)
        }
    }

    private static Class getClass(Type type) {
        isClassName(type) ? Class.forName(type.className) : Class.forName(type.descriptor.replaceAll('/', '.'))
    }

    private static boolean isClassName(Type type) {
        type.descriptor.startsWith('L') && type.descriptor.endsWith(';')
    }
}
