package NET._87k.fletch.vm;

import java.util.Dictionary;

abstract class ObjectHandle {
    private static short nextId = 1;
    private static Dictionary handles;

    final short id;
    final ClassObject classType;

    public ObjectHandle(ClassObject classType) {
        id = nextId++;
        this.classType = classType;
        handles.put(new Integer(id & 0xffff), this);
    }

    public static ObjectHandle getById(short id) {
        return (ObjectHandle) handles.get(new Integer(id & 0xffff));
    }
}
