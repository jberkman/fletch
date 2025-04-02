package NET._87k.fletch.vm;

final class ClassInfo implements ConstantPoolEntry {

    private final ConstantPool pool;
    private final int nameIndex;
    private String name;

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

}
