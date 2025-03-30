package NET._87k.fletch.vm;

import java.util.Dictionary;

abstract class ObjectRef {
    private static short nextId = 1;
    private static Dictionary instances;

    final short id;

    public ObjectRef() {
        id = nextId++;
        instances.put(new Integer(id & 0xffff), this);
    }

    public static ObjectRef getById(short id) {
        return (ObjectRef) instances.get(new Integer(id & 0xffff));
    }
}
