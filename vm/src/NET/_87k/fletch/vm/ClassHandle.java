package NET._87k.fletch.vm;

final class ClassHandle extends ObjectHandle {
    private static ClassHandle classClassHandle;
    private static final SystemClassLoader systemClassLoader = new SystemClassLoader();

    final ClassDefinition definition;
    final ClassHandle superHandle;
    private Object[] staticFields;

    final ObjectHandle classLoader;

    ClassHandle(ClassDefinition definition, ClassHandle superHandle, ObjectHandle classLoader) {
        super(classClassHandle);
        this.definition = definition;
        this.superHandle = superHandle;
        this.classLoader = classLoader;
    }

    ClassHandle(ClassDefinition definition, ClassHandle superHandle) {
        this(definition, superHandle, null);
    }

    static ClassHandle forName(String name) throws ClassNotFoundException {
        ClassHandle currentClass = Machine.callStack.currentClass();
        if (currentClass != null && currentClass.classLoader != null) {
            throw new RuntimeException();
        }
        if (name.indexOf('/') == -1) {
            name = name.replace('.', '/');
        }
        return systemClassLoader.loadClass(name);
    }

    void bindClassClassHandle() {
        if (classClassHandle != null) {
            throw new IllegalStateException();
        }
        classClassHandle = this;
        superHandle.setClassHandle(this);
        setClassHandle(this);
    }

    void initialize() {
        if (staticFields != null) {
            return;
        }
        if (superHandle != null) {
            superHandle.initialize();
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

    void invokeStatic(String name, String descriptor) {
        MethodInfo method = definition.staticMethodInfo(name, descriptor);
        if (method == null) {
            throw new NoSuchMethodError();
        }
        Machine.invoke(this, method);
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

    int instanceFieldCount() {
        int count = definition.instanceFields.length;
        if (superHandle != null) {
            count += superHandle.instanceFieldCount();
        }
        return count;
    }

}
