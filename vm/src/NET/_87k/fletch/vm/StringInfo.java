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

final class StringInfo implements ConstantPoolEntry, ConstantValueInfo {

    private final ConstantPool pool;
    private final int stringIndex;
    private String string;
    private ObjectHandle value;

    StringInfo(ConstantPool pool, int stringIndex) {
        this.pool = pool;
        this.stringIndex = stringIndex;
    }

    String string() {
        if (string != null) {
            return string;
        }
        return string = pool.utf8String(stringIndex);
    }

    public String descriptor() {
        return "Ljava/lang/String;";
    }

    public Object value() {
        if (value != null) {
            return value;
        }
        try {
            return value = new ObjectHandle(ClassHandle.forNameInternal("java/lang/String"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
