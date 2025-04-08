// MIT License
//
// Copyright (c) 2025 jacob berkman
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.


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
        return forNameInternal(name.replace('.', '/'));
    }

    static ClassHandle forNameInternal(String name) throws ClassNotFoundException {
        ClassHandle currentClass = ThreadContext.current().currentClass();
        if (currentClass != null && currentClass.classLoader != null) {
            throw new RuntimeException();
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
        int fieldIndex = 0;
        if (superHandle != null) {
            superHandle.initialize();
            fieldIndex = superHandle.fieldCount();
        }
        System.out.println(definition.thisClass + ".initialize()");
        for (int i = 0; i < definition.instanceFields.length; i++) {
            System.out.println(definition.instanceFields[i].name + ".index = " + fieldIndex);
            definition.instanceFields[i].index = fieldIndex++;
        }
        staticFields = new Object[definition.staticFields.length];
        for (int i = 0; i < staticFields.length; i++) {
            staticFields[i] = definition.staticFields[i].defaultValue();
            System.out.println(definition.thisClass + "." + definition.staticFields[i].name + " = " + staticFields[i]);
        }
        try {
            invokeStatic("<clinit>", "()V");
        } catch (NoSuchMethodError _) {
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    void invokeStatic(String name, String descriptor) throws Throwable {
        MethodInfo method = definition.staticMethod(name, descriptor);
        if (method == null) {
            throw new NoSuchMethodError();
        }
        ThreadContext.current().invoke(this, method);
    }

    private Object[] getStatics() {
        if (staticFields == null) {
            initialize();
        }
        return staticFields;
    }

    Object getStatic(int index) {
        return getStatics()[index];
    }

    void setStatic(int index, Object value) {
        getStatics()[index] = value;
    }

    int fieldCount() {
        int count = definition.instanceFields.length;
        if (superHandle != null) {
            count += superHandle.fieldCount();
        }
        return count;
    }

}
