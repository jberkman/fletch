package NET._87k.fletch.vm;

import java.lang.reflect.Field;

final class ClassDefinition {

    final int accessFlags;
    final ConstantPool constantPool;
    final String thisClass;
    final String superClass;
    final String[] interfaces;
    final FieldInfo[] instanceFields;
    final FieldInfo[] staticFields;
    final MethodInfo[] instanceMethods;
    final MethodInfo[] staticMethods;

    ClassDefinition(int accessFlags, ConstantPool constantPool, String thisClass, String superClass,
            String[] interfaces, FieldInfo[] instanceFields, FieldInfo[] staticFields, MethodInfo[] instanceMethods,
            MethodInfo[] staticMethods) throws ClassFormatError {
        boolean thisIsObject = "java/lang/Object".equals(thisClass);
        boolean superIsNull = superClass == null;

        if ((thisIsObject && !superIsNull) || (superIsNull && !thisIsObject)) {
            throw new ClassFormatError();
        }

        this.accessFlags = accessFlags;
        this.constantPool = constantPool;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.instanceFields = instanceFields;
        this.staticFields = staticFields;
        this.instanceMethods = instanceMethods;
        this.staticMethods = staticMethods;
    }

    private static MethodInfo methodInfo(MethodInfo[] methods, String name, String descriptor) {
        for (int i = 0; i < methods.length; i++) {
            MethodInfo method = methods[i];
            if (name.equals(method.name) && descriptor.equals(method.descriptor)) {
                return method;
            }
        }
        return null;
    }

    MethodInfo instanceMethodInfo(String name, String descriptor) {
        return methodInfo(instanceMethods, name, descriptor);
    }

    MethodInfo staticMethodInfo(String name, String descriptor) {
        return methodInfo(staticMethods, name, descriptor);
    }

}
