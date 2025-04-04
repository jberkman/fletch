package NET._87k.fletch.vm;

final class DoubleInfo implements ConstantPoolEntry, ConstantValueInfo {
    private final Double value;

    DoubleInfo(int highBytes, int lowBytes) {
        throw new RuntimeException();
    }

    public Object value() {
        return value;
    }
}
