package NET._87k.fletch.vm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

final class ClassObjectHandle extends ObjectHandle {
    private static ClassObjectHandle classClassHandle;

    final ClassObject classObject;
    ObjectHandle classLoaderHandle;

    ClassObjectHandle(ClassObject classObject) {
        super(classClassHandle);
        this.classObject = classObject;
    }

    void bindClassClassHandle() {
        if (classClassHandle != null) {
            throw new IllegalStateException();
        }
        classClassHandle = this;
        classObject.superClass.handle.setClassHandle(this);
        setClassHandle(this);
    }

    void bindClassLoader(ObjectHandle loaderHandle) {
        if (classLoaderHandle != null) {
            throw new IllegalStateException();
        }
        classLoaderHandle = loaderHandle;
        if (classObject.superClass != null && classObject.superClass.handle.classLoaderHandle == null) {
            classObject.superClass.handle.bindClassLoader(loaderHandle);
        }
    }

    ClassObjectHandle superHandle() {
        if (classObject.superClass == null) {
            return null;
        }
        return classObject.superClass.handle;
    }

    int initializeInstanceFields(Object[] fields) {
        int i = 0;
        if (classObject.superClass != null) {
            i = classObject.superClass.handle.initializeInstanceFields(fields);
        }
        for (int j = 0; j < classObject.definition.instanceFields.length; i++, j++) {
            fields[i] = classObject.definition.instanceFields[j].defaultValue();
        }
        return i;
    }

    final static void invokeStaticNative(String methodName, String methodSignature) throws Throwable {
        throw new RuntimeException();
    }

    final static void invokeStaticBytecode(String methodName, String methodSignature) throws Throwable {
        throw new RuntimeException();
    }

    void invokeSpecial(String name, String descriptor) {
        MethodInfo[] methods = classObject.definition.instanceMethods;
        for (int i = 0; i < methods.length; i++) {
            MethodInfo method = methods[i];
            if (!name.equals(method.name) || !descriptor.equals(method.descriptor)) {
                continue;
            }
            if (method.isNative()) {
                throw new ClassFormatError();
            } else {
                Machine.invokeBytecode(method.code.code);
            }
            return;
        }
        throw new NoSuchMethodError();
    }

}
