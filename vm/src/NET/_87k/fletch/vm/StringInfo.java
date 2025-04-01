package NET._87k.fletch.vm;

final class StringInfo implements ConstantPoolEntry, ConstantValueInfo {

    private final int stringIndex;
    String string;

    StringInfo(int stringIndex) {
        this.stringIndex = stringIndex;
    }

    public void resolve(ConstantPoolEntry[] pool) {
        if (string == null) {
            ConstantPoolEntry entry = pool[stringIndex - 1];
            if (!(entry instanceof Utf8Info)) {
                throw new ClassFormatError();
            }
            string = ((Utf8Info) entry).string;
        }
    }

}
