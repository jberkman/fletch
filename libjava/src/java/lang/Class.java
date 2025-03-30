package java.lang;

public final class Class {

    public static native Class forName(String className) throws ClassNotFoundException;

    public native ClassLoader getClassLoader();

    public native Class[] getInterfaces();

    public native String getName();

    public native Class getSuperClass();

    public native boolean isInterface();

    public native Object newInstance() throws InstantiationException, IllegalAccessException;

    public String toString() {
        throw new RuntimeException();
    }
}
