package NET._87k.fletch.vm;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

final class BootstrapClassLoader {

    private final Dictionary classObjects = new Hashtable();

    BootstrapClassLoader() {
    }

    private ClassObject defineClassObject(String className, byte[] bytes, int offset, int len) throws ClassNotFoundException {
        ClassFileReader reader = new ClassFileReader(bytes, offset, len);
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

        ClassObject superClass = null;
        if (def.superClass != null) {
            superClass = loadClassObject(def.superClass);
        }
        return new ClassObject(def, superClass);
    }

    ClassObject loadClassObject(String className) throws ClassNotFoundException {
        ClassObject classObject = (ClassObject) classObjects.get(className);
        if (classObject != null) {
            return classObject;
        }
        Slice slice = Machine.classFileLoader.loadClassFile(className);
        classObject = defineClassObject(className, slice.bytes, slice.offset, slice.length);
        classObjects.put(className, classObject);
        return classObject;
    }


}
