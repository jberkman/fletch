package NET._87k.fletch.vm;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

final class ClassObject {
    final ClassDefinition definition;
    final ClassObject superClass;
    final ClassObjectHandle handle;
    private Object[] staticFields;

    ClassObject(ClassDefinition definition, ClassObject superClass) {
        if (definition == null) {
            throw new NullPointerException();
        }
        this.definition = definition;
        this.superClass = superClass;
        this.handle = new ClassObjectHandle(this);
    }

    void initialize() {
        if (staticFields != null) {
            return;
        }
        if (superClass != null) {
            superClass.initialize();
        }
        System.out.println(definition.thisClass + ".initialize()");
        staticFields = new Object[definition.staticFields.length];
        for (int i = 0; i < staticFields.length; i++) {
            staticFields[i] = definition.staticFields[i].defaultValue();
            System.out.println(definition.thisClass + "." + definition.staticFields[i].name + " = " + staticFields[i]);
        }
        for (int i = 0; i < definition.staticMethods.length; i++) {
            if (!"<clinit>".equals(definition.staticMethods[i].name)) {
                continue;
            }
            System.out.println(definition.thisClass + "." + definition.staticMethods[i].name + definition.staticMethods[i].descriptor);
        }
        //MethodInfo clinit = (MethodInfo) definition.staticMethods.get("<clinit>()V");
        //if (clinit != null) {
        //}
    }

    private Object[] getStaticFields() {
        if (staticFields == null) {
            initialize();
        }
        return staticFields;
    }

    Object getStaticField(int index) {
        return getStaticFields()[index];
    }

    void setStaticField(int index, Object value) {
        getStaticFields()[index] = value;
    }

}
