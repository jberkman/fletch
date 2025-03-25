package NET._87k.fletch.vm;

abstract class SystemClassLoader {
    protected SystemClassLoader() throws SecurityException {
    }

    protected abstract ClassFile loadClass(String name, boolean resolve) throws ClassNotFoundException;

    protected final ClassFile defineClass(byte data[], int offset, int length)
            throws NullPointerException, IndexOutOfBoundsException, ClassFormatError {
        ClassFile classFile = new ClassFile(data, offset, length);
        classFile.validate();
        return classFile;
    }

    protected final void resolveClass(Class c) throws NullPointerException {
    }

    protected final Class findSystemClass(String name) throws ClassNotFoundException {
        return null;
    }
}
