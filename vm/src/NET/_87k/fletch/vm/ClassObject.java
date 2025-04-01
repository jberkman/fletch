package NET._87k.fletch.vm;

import java.io.IOException;

final class ClassObject {
    final ClassDefinition definition;
    final ClassObject superClass;
    ClassObjectHandle handle;

    private static ClassObject bootstrapClass(String className, ClassFileLoader loader) throws ClassNotFoundException {
        Slice slice = loader.loadClassFile(className);
        ClassFileReader reader = new ClassFileReader(slice);
        ClassDefinition def;
        try {
            def = reader.readClassFile();
            reader.close();
        } catch (IOException e) {
            throw new ClassCastException(e.toString());
        }

        ClassObject superClass = null;
        if (def.superClass != null) {
            superClass = bootstrapClass(def.superClass, loader);
        }
        return new ClassObject(def, superClass);
    }

    static void bootstrap(ClassFileLoader loader) throws ClassNotFoundException {
        ClassObject classObject = bootstrapClass("java/lang/Class", loader);
        ClassObject classLoader = bootstrapClass("NET/_87k/fletch/libjava/SystemClassLoader", loader);
    }

    ClassObject(ClassDefinition definition, ClassObject superClass) {
        if (definition == null) {
            throw new NullPointerException();
        }
        this.definition = definition;
        this.superClass = superClass;
    }

    void initialize() {
    }

    final static void invokeStaticNative(String methodName, String methodSignature) throws Throwable {
        throw new RuntimeException();
    }

    final static void invokeStaticBytecode(String methodName, String methodSignature) throws Throwable {
        throw new RuntimeException();
    }
}
