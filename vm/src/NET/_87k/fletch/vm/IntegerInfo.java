package NET._87k.fletch.vm;

final class IntegerInfo implements ConstantPoolEntry, ConstantValueInfo {

    private final Integer value;

    IntegerInfo(int value) {
        this.value = new Integer(value);
    }

    public Object value() {
        return value;
    }

}
