package NET._87k.fletch.vm;

final class ArrayHandle extends ObjectHandle {

    ArrayHandle(boolean[] booleans) {
        super(booleans);
    }

    boolean[] booleans() {
        return (boolean[]) booleans();
    }

    ArrayHandle(byte[] bytes) {
        array = bytes;
    }

    byte[] bytes() {
        return (byte[]) array;
    }

    ArrayHandle(char[] chars) {
        array = chars;
    }

    char[] chars() {
        return (char[]) array;
    }

    ArrayHandle(double[] doubles) {
        array = doubles;
    }

    double[] doubles() {
        return (double[]) array;
    }

    ArrayHandle(float[] floats) {
        array = floats;
    }

    float[] floats() {
        return (float[]) array;
    }

    ArrayHandle(int[] ints) {
        array = ints;
    }

    int[] ints() {
        return (int[]) array;
    }

    ArrayHandle(long[] longs) {
        array = longs;
    }

    long[] longs() {
        return (long[]) array;
    }

    ArrayHandle(Object[] objects) {
        array = objects;
    }

    Object[] objects() {
        return (Object[]) array;
    }

    ArrayHandle(short[] shorts) {
        array = shorts;
    }

    short[] shorts() {
        return (short[]) array;
    }
}
