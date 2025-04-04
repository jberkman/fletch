package NET._87k.fletch.vm;

public class AddressRange {
    public final int base;
    public final int length;

    public AddressRange(int base, int length) {
        this.base = base;
        this.length = length;
    }

    public String toString() {
        return length + " B @ 0x" + Integer.toHexString(base);
    }
}
