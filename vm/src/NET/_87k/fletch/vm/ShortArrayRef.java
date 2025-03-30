package NET._87k.fletch.vm;

public final class ShortArrayRef extends ObjectRef {
    private short[] shorts;

    public ShortArrayRef(short[] shorts) {
        this.shorts = shorts;
    }

    public short[] shorts() {
        return shorts;
    }
}
