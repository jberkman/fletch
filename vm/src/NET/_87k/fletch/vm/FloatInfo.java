package NET._87k.fletch.vm;

final class FloatInfo implements ConstantPoolEntry, ConstantValueInfo {

    final float value;

    FloatInfo(int intValue) {
        value = (float) intValue;
        throw new RuntimeException();
    }

    public void resolve(ConstantPoolEntry[] pool) {
    }
}
