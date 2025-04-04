package NET._87k.fletch.vm;

final class FloatInfo implements ConstantPoolEntry, ConstantValueInfo {
    private final Float value;

    FloatInfo(int intValue) {
        throw new RuntimeException();
    }

    public Object value() {
        return value;
    }
}
