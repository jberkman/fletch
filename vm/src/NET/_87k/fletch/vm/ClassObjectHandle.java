package NET._87k.fletch.vm;

final class ClassObjectHandle extends ObjectHandle {
    private static ClassObject classClassType;

    final ClassObject objectClassType;

    ClassObjectHandle(ClassObject objectClassType) {
        super(classClassType);
        this.objectClassType = objectClassType;
    }

    static void bind(ClassObject classClassType) {
        if (ClassObjectHandle.classClassType != null) {
            throw new IllegalStateException();
        }
        ClassObjectHandle.classClassType = classClassType;
    }
}
