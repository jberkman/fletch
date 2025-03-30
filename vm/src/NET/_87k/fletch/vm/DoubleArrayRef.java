package NET._87k.fletch.vm;

public final class DoubleArrayRef extends ObjectRef {
    private double[] doubles;

    public DoubleArrayRef(double[] doubles) {
        this.doubles = doubles;
    }

    public double[] doubles() {
        return doubles;
    }
}
