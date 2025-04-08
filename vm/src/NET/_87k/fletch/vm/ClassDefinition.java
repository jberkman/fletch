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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class ClassDefinition {

    final int accessFlags;
    final ConstantPool constantPool;
    final String thisClass;
    final String superClass;
    final String[] interfaces;
    final FieldInfo[] instanceFields;
    final FieldInfo[] staticFields;
    final MethodInfo[] instanceMethods;
    final MethodInfo[] staticMethods;

    ClassDefinition(int accessFlags, ConstantPool constantPool, String thisClass, String superClass,
            String[] interfaces, FieldInfo[] instanceFields, FieldInfo[] staticFields, MethodInfo[] instanceMethods,
            MethodInfo[] staticMethods) throws ClassFormatError {
        boolean thisIsObject = "java/lang/Object".equals(thisClass);
        boolean superIsNull = superClass == null;

        if ((thisIsObject && !superIsNull) || (superIsNull && !thisIsObject)) {
            throw new ClassFormatError();
        }

        this.accessFlags = accessFlags;
        this.constantPool = constantPool;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.instanceFields = instanceFields;
        this.staticFields = staticFields;
        this.instanceMethods = instanceMethods;
        this.staticMethods = staticMethods;
    }

    private static MethodInfo lookupMethod(MethodInfo[] methods, String name, String descriptor) {
        for (int i = 0; i < methods.length; i++) {
            MethodInfo method = methods[i];
            if (name.equals(method.name) && descriptor.equals(method.descriptor)) {
                return method;
            }
        }
        return null;
    }

    MethodInfo method(String name, String descriptor) {
        return lookupMethod(instanceMethods, name, descriptor);
    }

    MethodInfo staticMethod(String name, String descriptor) {
        return lookupMethod(staticMethods, name, descriptor);
    }

    private static FieldInfo lookupField(FieldInfo[] fields, String name, String descriptor) {
        for (int i = 0; i < fields.length; i++) {
            FieldInfo field = fields[i];
            if (name.equals(field.name) && descriptor.equals(field.descriptor)) {
                return field;
            }
        }
        return null;
    }

    FieldInfo field(String name, String descriptor) {
        return lookupField(instanceFields, name, descriptor);
    }

    FieldInfo staticField(String name, String descriptor) {
        return lookupField(staticFields, name, descriptor);
    }

}
