package NET._87k.fletch.vm;

final class Utf8Info implements ConstantPoolEntry {
    final String string;

    Utf8Info(String string) {
        this.string = string;
    }

    public void resolve(ConstantPoolEntry[] pool) {
    }
}
