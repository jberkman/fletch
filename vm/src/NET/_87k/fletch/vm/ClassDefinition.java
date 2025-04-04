package NET._87k.fletch.vm;

final class ClassDefinition {

    final int accessFlags;
    final String thisClass;
    final String superClass;
    final String[] interfaces;
    final FieldInfo[] instanceFields;
    final FieldInfo[] staticFields;
    final MethodInfo[] instanceMethods;
    final MethodInfo[] staticMethods;

    ClassDefinition(int accessFlags, String thisClass, String superClass, String[] interfaces, FieldInfo[] instanceFields, FieldInfo[] staticFields, MethodInfo[] instanceMethods, MethodInfo[] staticMethods) throws ClassFormatError {
        boolean thisIsObject = "java/lang/Object".equals(thisClass);
        boolean superIsNull = superClass == null;

        if ((thisIsObject && !superIsNull) || (superIsNull && !thisIsObject)) {
            throw new ClassFormatError();
        }

        this.accessFlags = accessFlags;
        this.thisClass = thisClass.intern();
        if (superIsNull) {
            this.superClass = null;
        } else {
            this.superClass = superClass.intern();
        }
        this.interfaces = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            this.interfaces[i] = interfaces[i].intern();
        }
        this.instanceFields = instanceFields;
        this.staticFields = staticFields;
        this.instanceMethods = instanceMethods;
        this.staticMethods = staticMethods;
    }

}
