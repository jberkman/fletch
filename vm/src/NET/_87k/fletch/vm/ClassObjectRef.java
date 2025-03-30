package NET._87k.fletch.vm;

public final class ClassObjectRef extends ClassRef {
    private static ClassType classClassType;
    private ClassType objectClassType;

    public ClassObjectRef(ClassType objectClassType) {
        super(classClassType);
        this.objectClassType = objectClassType;
    }

    public static void bind(ClassType classClassType) {
        if (ClassObjectRef.classClassType != null) {
            throw new IllegalStateException();
        }
        ClassObjectRef.classClassType = classClassType;
    }

    public ClassType objectClassType() {
        return objectClassType;
    }
}
