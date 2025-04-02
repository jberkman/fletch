package NET._87k.fletch.vm;

final class StringInfo implements ConstantPoolEntry, ConstantValueInfo {

    private final ConstantPool pool;
    private final int stringIndex;
    private String string;

    StringInfo(ConstantPool pool, int stringIndex) {
        this.pool = pool;
        this.stringIndex = stringIndex;
    }

    String value() {
        if (string != null) {
            return string;
        }
        return string = pool.utf8String(stringIndex);
    }

}
