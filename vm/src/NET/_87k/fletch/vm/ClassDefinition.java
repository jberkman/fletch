package NET._87k.fletch.vm;

final class ClassDefinition {

    final int accessFlags;
    final String thisClass;
    final String superClass;
    final String[] interfaces;
    final FieldInfo[] fields;
    final MethodInfo[] methods;
    final AttributeInfo[] attributes;

    ClassDefinition(int accessFlags, String thisClass, String superClass, String[] interfaces, FieldInfo[] fields, MethodInfo[] methods, AttributeInfo[] attributes) {
        this.accessFlags = accessFlags;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
    }

}
