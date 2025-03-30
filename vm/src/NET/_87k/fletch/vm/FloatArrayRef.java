package NET._87k.fletch.vm;

public final class FloatArrayRef extends ObjectRef {
    private float[] floats;

    public FloatArrayRef(float[] floats) {
        this.floats = floats;
    }

    public float[] floats() {
        return floats;
    }
}
