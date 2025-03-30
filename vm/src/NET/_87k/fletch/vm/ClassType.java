package NET._87k.fletch.vm;

public class ClassType {
    private ClassType superType;
    private ClassFile file;
    private ClassObjectRef reference;

    public ClassType(ClassType superType, ClassFile file) {
        if (file == null) {
            throw new NullPointerException();
        }
        this.superType = superType;
        this.file = file;
    }

    public void bind(ClassObjectRef reference) {
        if (this.reference != null) {
            throw new IllegalStateException();
        }
        this.reference = reference;
    }

    public ClassType superType() {
        return superType;
    }

    public ClassObjectRef classReference() {
        return reference;
    }

    final static void invokeStaticNative(String methodName, String methodSignature) throws Throwable {
        throw new RuntimeException();
    }

    final static void invokeStaticBytecode(String methodName, String methodSignature) throws Throwable {
        throw new RuntimeException();
    }
}
