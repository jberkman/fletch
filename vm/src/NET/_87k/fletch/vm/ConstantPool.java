package NET._87k.fletch.vm;

final class ConstantPool {
    private final ConstantPoolEntry[] entries;

    ConstantPool(ConstantPoolEntry[] entries) {
        this.entries = entries;
    }

    ConstantValueInfo constantValue(int index) {
        index -= 1; // pool is 1-indexed
        if (index < 0 || index >= entries.length) {
            throw new ClassFormatError();
        }
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ConstantValueInfo)) {
            throw new ClassFormatError();
        }
        return (ConstantValueInfo) entry;
    }

    NameAndTypeInfo nameAndType(int index) {
        index -= 1; // pool is 1-indexed
        if (index < 0 || index >= entries.length) {
            throw new ClassFormatError();
        }
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof NameAndTypeInfo)) {
            throw new ClassFormatError();
        }
        return (NameAndTypeInfo) entry;
    }

    String className(int index) {
        index -= 1; // pool is 1-indexed
        if (index < 0 || index >= entries.length) {
            throw new ClassFormatError();
        }
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError();
        }
        return ((ClassInfo) entry).name();
    }

    String utf8String(int index) {
        index -= 1; // pool is 1-indexed
        if (index < 0 || index >= entries.length) {
            throw new ClassFormatError();
        }
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        return ((Utf8Info) entry).string;
    }
}
