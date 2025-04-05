package NET._87k.fletch.vm;

final class ConstantPool {
    private final ConstantPoolEntry[] entries;

    ConstantPool(ConstantPoolEntry[] entries) {
        this.entries = entries;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= entries.length) {
            throw new ClassFormatError();
        }
    }

    ConstantValueInfo constantValue(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ConstantValueInfo)) {
            throw new ClassFormatError();
        }
        return (ConstantValueInfo) entry;
    }

    NameAndTypeInfo nameAndType(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof NameAndTypeInfo)) {
            throw new ClassFormatError();
        }
        return (NameAndTypeInfo) entry;
    }

    String className(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError();
        }
        return ((ClassInfo) entry).name();
    }

    String utf8String(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        return ((Utf8Info) entry).string;
    }

    MethodInfo methodInfo(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MethodrefInfo)) {
            throw new ClassFormatError();
        }
        return null;
        //return ((MethodrefInfo) entry).methodInfo();
    }
}
