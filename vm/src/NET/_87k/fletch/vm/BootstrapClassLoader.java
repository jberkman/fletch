package NET._87k.fletch.vm;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

final class BootstrapClassLoader {

    private final Dictionary classObjects = new Hashtable();

    BootstrapClassLoader() {
    }

    private ClassObject defineClassObject(String className, AddressRange bytes) throws ClassNotFoundException {
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
        AddressRange bytes = Machine.classFileLoader.loadClassFile(className);
        classObject = defineClassObject(className, bytes);
        classObjects.put(className, classObject);
        return classObject;
    }


}
