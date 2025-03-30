package NET._87k.fletch.vm;

final class BooleanArrayRef extends ObjectRef {
    private boolean[] booleans;

    BooleanArrayRef(boolean[] booleans) {
        this.booleans = booleans;
    }

    boolean[] booleans() {
        return booleans;
    }
}
