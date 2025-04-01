package NET._87k.fletch.vm;

final class IntegerInfo implements ConstantPoolEntry {

    final int value;

    IntegerInfo(int value) {
        this.value = value;
    }

    public void resolve(ConstantPoolEntry[] pool) {
    }

}
