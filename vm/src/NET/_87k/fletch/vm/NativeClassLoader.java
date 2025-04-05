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

final class NativeClassLoader implements NativeMethod {

    public void invoke(String methodName, String methodSignature) throws Throwable {
        /*
        if (methodName == "defineClass") {
            if (methodSignature == "([BII)Ljava/lang/Class;") {
                int len = Machine.pop();
                int off = Machine.pop();
                short id = (short) Machine.pop();
                byte[] bytes = ((ByteArrayRef) ObjectHandle.getById(id)).bytes();
                id = (short) Machine.pop();
                ClassObjectHandle self = (ClassObjectHandle) ObjectHandle.getById(id);

                ClassObjectHandle ret = defineClass(self, bytes, off, len);

                Machine.push(ret.id() & 0xffff);
                return;
            }
        } else if (methodName == "findSystemClass") {
            if (methodSignature == "(Ljava/lang/String;)Ljava/lang/Class;") {
                findSystemClass();
                return;
            }
        } else if (methodName == "resolveClass") {
            if (methodSignature == "(Ljava/lang/Class;)") {
                resolveClass();
                return;
            }
        }
        */
        throw new NoSuchMethodError();
    }

    static ClassHandle defineClass(ClassHandle classLoader, byte[] bytes, int offset, int len) {
        throw new RuntimeException();
    }

    static void findSystemClass() throws ClassNotFoundException {
        throw new RuntimeException();
    }

    static void resolveClass() {
        throw new RuntimeException();
    }

}
