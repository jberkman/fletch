package NET._87k.fletch.vm;

public class ClassRef extends ObjectRef {
    private ClassType classType;
    Object[] fields;

    ClassRef(ClassType classType) {
        this.classType = classType;
    }

    final ClassType classType() {
        return classType;
    }

    Object getField(int index) {
        return fields[index];
    }

    void setField(int index, Object value) {
        fields[index] = value;
    }
}
