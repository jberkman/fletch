package NET._87k.fletch.vm;

final class NameAndTypeInfo implements ConstantPoolEntry {

    private final ConstantPool pool;
    private final int nameIndex;
    private final int descriptorIndex;

    private String name;
    private String descriptor;

    NameAndTypeInfo(ConstantPool pool, int nameIndex, int descriptorIndex) {
        this.pool = pool;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    String name() {
        if (name != null) {
            return name;
        }
        return name = pool.utf8String(nameIndex);
    }

    String descriptor() {
        if (descriptor != null) {
            return descriptor;
        }
        return descriptor = pool.utf8String(descriptorIndex);
    }

}
