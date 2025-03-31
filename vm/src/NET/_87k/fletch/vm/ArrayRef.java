package NET._87k.fletch.vm;

class ArrayRef extends ObjectRef {
    private final Object array;

    ArrayRef(boolean[] booleans) {
        array = booleans;
    }

    boolean[] booleans() {
        return (boolean[]) booleans();
    }

    ArrayRef(byte[] bytes) {
        array = bytes;
    }

    byte[] bytes() {
        return (byte[]) array;
    }

    ArrayRef(char[] chars) {
        array = chars;
    }

    char[] chars() {
        return (char[]) array;
    }

    ArrayRef(double[] doubles) {
        array = doubles;
    }

    double[] doubles() {
        return (double[]) array;
    }

    ArrayRef(float[] floats) {
        array = floats;
    }

    float[] floats() {
        return (float[]) array;
    }

    ArrayRef(int[] ints) {
        array = ints;
    }

    int[] ints() {
        return (int[]) array;
    }

    ArrayRef(long[] longs) {
        array = longs;
    }

    long[] longs() {
        return (long[]) array;
    }

    ArrayRef(Object[] objects) {
        array = objects;
    }

    Object[] objects() {
        return (Object[]) array;
    }

    ArrayRef(short[] shorts) {
        array = shorts;
    }

    short[] shorts() {
        return (short[]) array;
    }
}
