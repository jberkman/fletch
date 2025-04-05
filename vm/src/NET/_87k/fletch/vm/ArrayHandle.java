package NET._87k.fletch.vm;

final class ArrayHandle extends ObjectHandle {

    private final Object array;

    private ArrayHandle(Object array, int length, String className) throws ClassNotFoundException {
        super(ClassHandle.forName(className));
        this.array = array;
        setField(0, new Integer(length));
    }

    ArrayHandle(boolean[] booleans) throws ClassNotFoundException {
        this(booleans, booleans.length, "[Z");
    }

    boolean[] booleans() {
        return (boolean[]) array;
    }

    ArrayHandle(byte[] bytes) throws ClassNotFoundException {
        this(bytes, bytes.length, "[B");
    }

    byte[] bytes() {
        return (byte[]) array;
    }

    ArrayHandle(char[] chars) throws ClassNotFoundException {
        this(chars, chars.length, "[C");
    }

    char[] chars() {
        return (char[]) array;
    }

    ArrayHandle(double[] doubles) throws ClassNotFoundException {
        this(doubles, doubles.length, "[D");
    }

    double[] doubles() {
        return (double[]) array;
    }

    ArrayHandle(float[] floats) throws ClassNotFoundException {
        this(floats, floats.length, "[F");
    }

    float[] floats() {
        return (float[]) array;
    }

    ArrayHandle(int[] ints) throws ClassNotFoundException {
        this(ints, ints.length, "[I");
    }

    int[] ints() {
        return (int[]) array;
    }

    ArrayHandle(long[] longs) throws ClassNotFoundException {
        this(longs, longs.length, "[J");
    }

    long[] longs() {
        return (long[]) array;
    }

    ArrayHandle(Object[] objects, String objectClass) throws ClassNotFoundException {
        this(objects, objects.length, "[L" + objectClass.replace('.', '/') + ";");
    }

    Object[] objects() {
        return (Object[]) array;
    }

    ArrayHandle(short[] shorts) throws ClassNotFoundException {
        this(shorts, shorts.length, "[S");
    }

    short[] shorts() {
        return (short[]) array;
    }
}
