package NET._87k.fletch.vm;

final class ClassInfo implements ConstantPoolEntry {

    private final int nameIndex;
    /* final */ String name;

    ClassInfo(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void resolve(ConstantPoolEntry[] pool) {
        if (name == null) {
            ConstantPoolEntry entry = pool[nameIndex - 1];
            if (!(entry instanceof Utf8Info)) {
                throw new ClassFormatError();
            }
            name = ((Utf8Info) entry).string;
        }
    }

}
