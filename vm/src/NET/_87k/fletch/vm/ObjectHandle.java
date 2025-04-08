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

import java.util.Dictionary;
import java.util.Hashtable;

class ObjectHandle {
    private static short nextId = 1;
    private static final Dictionary handles = new Hashtable();

    final short id;
    private ClassHandle classHandle;
    private Object[] fields;

    ObjectHandle(ClassHandle classHandle) {
        id = nextId++;
        handles.put(new Integer(id & 0xffff), this);
        if (classHandle != null) {
            setClassHandle(classHandle);
        }
    }

    static ObjectHandle getById(short id) {
        return (ObjectHandle) handles.get(new Integer(id & 0xffff));
    }

    private void initializeInstanceFields(ClassHandle classHandle) {
        if (classHandle.superHandle != null) {
            initializeInstanceFields(classHandle.superHandle);
        }
        for (int i = 0; i < classHandle.definition.instanceFields.length; i++) {
            FieldInfo field = classHandle.definition.instanceFields[i];
            fields[field.index] = field.defaultValue();
        }
    }

    void setClassHandle(ClassHandle classHandle) {
        if (this.classHandle != null) {
            throw new IllegalStateException();
        }
        this.classHandle = classHandle;
        classHandle.initialize();
        fields = new Object[classHandle.fieldCount()];
        this.initializeInstanceFields(classHandle);
    }

    ClassHandle classHandle() {
        return classHandle;
    }

    Object getField(int index) {
        return fields[index];
    }

    void setField(int index, Object value) {
        fields[index] = value;
    }

    void invokeSpecial(String name, String descriptor) throws Throwable {
        MethodInfo method = classHandle.definition.method(name, descriptor);
        if (method == null) {
            throw new NoSuchMethodError();
        }
        if (method.isNative()) {
            throw new ClassFormatError();
        } else {
            ThreadContext.current().invoke(classHandle, method);
        }
    }

}
