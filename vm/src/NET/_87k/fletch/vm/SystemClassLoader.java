package NET._87k.fletch.vm;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

final class SystemClassLoader {

    private final Dictionary handles = new Hashtable();

    private static final ConstantPool arrayConstantPool = new ConstantPool(new ConstantPoolEntry[0]);

    private static final String[] arrayInterfaces = new String[0];

    private static final FieldInfo[] arrayInstanceFields = {
            new FieldInfo(AccessFlags.ACC_PUBLIC | AccessFlags.ACC_FINAL, "length", "I", null),
    };

    private static final FieldInfo[] arrayStaticFields = new FieldInfo[0];

    private static final MethodInfo[] arrayInstanceMethods = {
            new MethodInfo(AccessFlags.ACC_PUBLIC | AccessFlags.ACC_NATIVE, "clone", "()V", null, new String[0]),
    };

    private static final MethodInfo[] arrayStaticMethods = new MethodInfo[0];

    private ClassHandle defineArrayClass(String className) throws ClassNotFoundException {
        ClassDefinition def = new ClassDefinition(
                AccessFlags.ACC_PUBLIC | AccessFlags.ACC_FINAL | AccessFlags.ACC_SUPER,
                arrayConstantPool, className, "java/lang/Object", arrayInterfaces, arrayInstanceFields,
                arrayStaticFields, arrayInstanceMethods,
                arrayStaticMethods);
        ClassHandle superClass = loadClass(def.superClass);
        return new ClassHandle(def, superClass);
    }

    private ClassHandle defineClass(String className, AddressRange bytes) throws ClassNotFoundException {
        ClassFileInputStream reader = new ClassFileInputStream(bytes);
        ClassDefinition def;
        try {
            def = reader.readClassFile();
            reader.close();
        } catch (IOException e) {
            throw new ClassCastException(e.toString());
        }

        if (!className.equals(def.thisClass)) {
            throw new ClassFormatError();
        }

        ClassHandle superClass = null;
        if (def.superClass != null) {
            superClass = loadClass(def.superClass);
        }
        return new ClassHandle(def, superClass);
    }

    ClassHandle loadClass(String className) throws ClassNotFoundException {
        if (className.length() == 0) {
            throw new ClassNotFoundException();
        }
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1, className.length() - 1);
        }
        ClassHandle handle = (ClassHandle) handles.get(className);
        if (handle != null) {
            return handle;
        }
        if (className.charAt(0) == '[') {
            handle = defineArrayClass(className);
        } else {
            AddressRange bytes = Machine.classFileLoader.loadClassFile(className);
            handle = defineClass(className, bytes);
        }
        System.out.println("Defined class " + className);
        handles.put(className, handle);
        return handle;
    }

}
