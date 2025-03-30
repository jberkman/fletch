package NET._87k.fletch.vm;

final class ByteArrayRef extends ObjectRef {
    private byte[] bytes;

    ByteArrayRef(byte[] bytes) {
        this.bytes = bytes;
    }

    byte[] bytes() {
        return bytes;
    }
}
