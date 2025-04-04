package NET._87k.fletch.vm;

final class LongInfo implements ConstantPoolEntry, ConstantValueInfo {

    private final Long value;

    LongInfo(int highBytes, int lowBytes) {
        value = new Long(((long)highBytes << 32) | (long)lowBytes);
    }

    public Object value() {
        return value;
    }

}
