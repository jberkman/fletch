package NET._87k.fletch.vm;

final class NameAndTypeInfo implements ConstantPoolEntry {

    private final int nameIndex;
    private final int descriptorIndex;

    String name;
    String descriptor;

    NameAndTypeInfo(int nameIndex, int descriptorIndex) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public void resolve(ConstantPoolEntry[] pool) {
        if (name == null) {
            ConstantPoolEntry nameEntry = pool[nameIndex - 1];
            ConstantPoolEntry descEntry = pool[descriptorIndex - 1];
            if (!(nameEntry instanceof Utf8Info) || !(descEntry instanceof Utf8Info)) {
                throw new ClassFormatError();
            }
            name = ((Utf8Info) nameEntry).string;
            descriptor = ((Utf8Info) descEntry).string;
        }
    }

}
