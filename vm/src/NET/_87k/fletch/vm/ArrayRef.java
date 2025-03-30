package NET._87k.fletch.vm;

class ArrayRef extends ObjectRef {
    private final Object array;

    ArrayRef(Object array) {
        this.array = array;
    }

    boolean[] booleans() {
        return (boolean[]) booleans();
    }

    byte[] bytes() {
        return (byte[]) array;
    }

    char[] chars() {
        return (char[]) array;
    }

    double[] doubles() {
        return (double[]) array;
    }

    float[] floats() {
        return (float[]) array;
    }

    int[] ints() {
        return (int[]) array;
    }

    long[] longs() {
        return (long[]) array;
    }

    Object[] objects() {
        return (Object[]) array;
    }

    short[] shorts() {
        return (short[]) array;
    }
}
