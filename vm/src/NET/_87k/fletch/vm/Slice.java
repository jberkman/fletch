package NET._87k.fletch.vm;

public final class Slice {
    public final byte[] bytes;
    public final int offset;
    public final int length;

    public Slice(byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
    }
}
