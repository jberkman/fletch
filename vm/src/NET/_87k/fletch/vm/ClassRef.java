package NET._87k.fletch.vm;

class ClassRef extends ObjectRef {
    final ClassType classType;

    private Object[] fields;

    ClassRef(ClassType classType) {
        this.classType = classType;
    }

    Object getField(int index) {
        return fields[index];
    }

    void setField(int index, Object value) {
        fields[index] = value;
    }
}
