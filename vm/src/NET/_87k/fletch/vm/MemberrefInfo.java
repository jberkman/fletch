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

import java.util.jar.Attributes.Name;

abstract class MemberrefInfo implements ConstantPoolEntry {

    private final ConstantPool pool;
    private final int classIndex;
    private final int natIndex;

    private String className;
    private ClassHandle classHandle;
    private NameAndTypeInfo nat;

    MemberrefInfo(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
        this.pool = pool;
        this.classIndex = classIndex;
        this.natIndex = nameAndTypeIndex;
    }

    String className() {
        if (className != null) {
            return className;
        }
        return className = pool.utf8String(classIndex);
    }

    ClassHandle classHandle() {
        if (classHandle != null) {
            return classHandle;
        }
        return classHandle; // = ClassObjectHandle.forName(className());
    }

    private NameAndTypeInfo nat() {
        if (nat != null) {
            return nat;
        }
        return nat = pool.nameAndType(natIndex);
    }

    String name() {
        return nat().name();
    }

    String descriptor() {
        return nat().descriptor();
    }
}
