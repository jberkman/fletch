package NET._87k.fletch.vm;

final class ClassInfo implements ConstantPoolEntry {

    private final ConstantPool pool;
    private final int nameIndex;
    private String name;
    private ClassHandle handle;

    ClassInfo(ConstantPool pool, int nameIndex) {
        this.pool = pool;
        this.nameIndex = nameIndex;
    }

    String name() {
        if (name != null) {
            return name;
        }
        // TODO(jcb): validate name is a valid class name
        return name = pool.utf8String(nameIndex);
    }

    ClassHandle handle() {
        if (handle != null) {
            return handle;
        }
        throw new RuntimeException();
    }

}
