package NET._87k.fletch.vm;

final class ClassObjectRef extends ClassRef {
    private static ClassType classClassType;

    final ClassType objectClassType;

    ClassObjectRef(ClassType objectClassType) {
        super(classClassType);
        this.objectClassType = objectClassType;
    }

    static void bind(ClassType classClassType) {
        if (ClassObjectRef.classClassType != null) {
            throw new IllegalStateException();
        }
        ClassObjectRef.classClassType = classClassType;
    }
}
