package NET._87k.fletch.vm;

import java.util.Dictionary;
import java.util.Hashtable;

class ObjectHandle {
    private static short nextId = 1;
    private static final Dictionary handles = new Hashtable();

    final short id;
    private ClassObjectHandle classHandle;
    private Object[] fields;

    ObjectHandle(ClassObjectHandle classHandle) {
        id = nextId++;
        handles.put(new Integer(id & 0xffff), this);
        if (classHandle != null) {
            setClassHandle(classHandle);
        }
    }

    static ObjectHandle getById(short id) {
        return (ObjectHandle) handles.get(new Integer(id & 0xffff));
    }

    void setClassHandle(ClassObjectHandle classHandle) {
        if (this.classHandle != null) {
            throw new IllegalStateException();
        }
        this.classHandle = classHandle;
        int count = 0;
        while (classHandle != null) {
            count += classHandle.classObject.definition.instanceFields.length;
            classHandle = classHandle.superHandle();
        }
        fields = new Object[count];
        this.classHandle.initializeInstanceFields(fields);
    }

    ClassObjectHandle classHandle() {
        return classHandle;
    }

}
