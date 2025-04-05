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
            throw new ClassFormatError();
        }
        return (ConstantValueInfo) entry;
    }

    NameAndTypeInfo nameAndType(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof NameAndTypeInfo)) {
            throw new ClassFormatError();
        }
        return (NameAndTypeInfo) entry;
    }

    String className(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError();
        }
        return ((ClassInfo) entry).name();
    }

    String utf8String(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        return ((Utf8Info) entry).string;
    }

    MethodInfo methodInfo(int index) {
        index -= 1; // pool is 1-indexed
        checkIndex(index);
        ConstantPoolEntry entry = entries[index];
        if (!(entry instanceof MethodrefInfo)) {
            throw new ClassFormatError();
        }
        return null;
        //return ((MethodrefInfo) entry).methodInfo();
    }
}
