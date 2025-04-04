package java.lang;

public abstract class ClassLoader {
    protected ClassLoader() throws SecurityException {
    }

    protected abstract Class loadClass(String className, boolean resolve) throws ClassNotFoundException;

    protected native final Class defineClass(byte[] bytecode, int offset, int length);

    protected native final Class findSystemClass(String className) throws ClassNotFoundException;

    protected native final void resolveClass(Class c);
}
