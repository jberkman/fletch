package java.lang;

public abstract class ClassLoader {
    protected ClassLoader() {}

    protected abstract Class loadClass(String className, boolean resolve) throws ClassNotFoundException;

    protected final Class defineClass(byte[] bytecode, int offset, int length) {
        // This cannot load java.*
        return null;
    }
    
    protected final Class findSystemClass(String className) throws ClassNotFoundException {
        return null;
    }
    
    protected final void resolveClass(Class c) {
    }
}
