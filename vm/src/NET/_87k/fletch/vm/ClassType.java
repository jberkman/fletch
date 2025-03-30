package NET._87k.fletch.vm;

class ClassType {
    final ClassType superType;
    private final ClassFile file;
    private ClassObjectRef reference;

    public ClassType(ClassType superType, ClassFile file) {
        if (file == null) {
            throw new NullPointerException();
        }
        this.superType = superType;
        this.file = file;
    }

    public void bind(ClassObjectRef reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        if (this.reference != null) {
            throw new IllegalStateException();
        }
        this.reference = reference;
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
