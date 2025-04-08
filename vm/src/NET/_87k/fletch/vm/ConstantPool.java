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

final class ConstantPool {
    private final ConstantPoolEntry[] entries;

    ConstantPool(ConstantPoolEntry[] entries) {
        this.entries = entries;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= entries.length) {
            throw new ClassFormatError();
        }
    }

    ConstantValueInfo constantValue(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ConstantValueInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return (ConstantValueInfo) entry;
    }

    NameAndTypeInfo nameAndType(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof NameAndTypeInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return (NameAndTypeInfo) entry;
    }

    String className(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((ClassInfo) entry).name();
    }

    ClassHandle classHandle(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((ClassInfo) entry).handle();
    }

    String utf8String(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError("#" + (index + 1) + " = " + entry.getClass().toString());
        }
        return ((Utf8Info) entry).string;
    }

    String refClassName(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MemberrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((MemberrefInfo) entry).className();
    }

    ClassHandle refClass(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MemberrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((MemberrefInfo) entry).classHandle();
    }

    FieldInfo field(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MethodrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((FieldrefInfo) entry).field();
    }

    FieldInfo staticField(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof FieldrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((FieldrefInfo) entry).staticField();
    }

    MethodInfo methodInfo(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MethodrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((MethodrefInfo) entry).methodInfo();
    }

    ClassHandle virtualMethodClass(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MethodrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((MethodrefInfo) entry).virtualMethodClass();
    }

    MethodInfo staticMethodInfo(int index) throws ClassNotFoundException {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MethodrefInfo)) {
            throw new ClassFormatError(entry.getClass().toString());
        }
        return ((MethodrefInfo) entry).staticMethodInfo();
    }
}
