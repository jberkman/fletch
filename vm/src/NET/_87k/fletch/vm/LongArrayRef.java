package NET._87k.fletch.vm;

public final class LongArrayRef extends ObjectRef {
    private long[] longs;

    public LongArrayRef(long[] longs) {
        this.longs = longs;
    }

    public long[] longs() {
        return longs;
    }
}
