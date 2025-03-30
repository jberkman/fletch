package NET._87k.fletch.vm;

public final class IntArrayRef extends ObjectRef {
    private int[] ints;

    public IntArrayRef(int[] ints) {
        this.ints = ints;
    }

    public int[] ints() {
        return ints;
    }
}
