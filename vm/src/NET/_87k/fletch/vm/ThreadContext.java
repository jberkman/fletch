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

final class ThreadContext {
    private static final int STACK_SIZE = 1024;
    private static final int NULL = 0xdeadfee7;

    // Only one, for now
    private static ThreadContext current = new ThreadContext();

    private final int[] stack = new int[STACK_SIZE];

    private ClassHandle currentClass;
    private MethodInfo currentMethod;

    // Registers
    private int fp;
    private int sp;
    private int pcBase;
    private int pc;

    static ThreadContext current() {
        return current;
    }

    private ThreadContext() {
    }

    int pc() {
        return pc - pcBase;
    }

    int readPc() {
        return Interpreter.rom.load(pc++);
    }

    int readPc2() {
        int byte1 = readPc();
        int byte2 = readPc();
        return (byte1 << 8) | byte2;
    }

    void push(int i) {
        stack[sp++] = i;
    }

    void push(long j) {
        push((int) (j >> 32));
        push((int) j);
    }

    void push(float f) {
        throw new RuntimeException();
    }

    void push(double d) {
        throw new RuntimeException();
    }

    void push(Boolean b) {
        if (b.booleanValue()) {
            push(1);
        } else {
            push(0);
        }
    }

    void push(Number i) {
        push(i.intValue());
    }

    void pushLong(Number j) {
        push(j.longValue());
    }

    void pushFloat(Number f) {
        push(f.floatValue());
    }

    void pushDouble(Number f) {
        push(f.doubleValue());
    }

    void pushNull() {
        push(NULL);
    }

    void push(Object value, String descriptor) {
        if (descriptor.length() != 1) {
            if (value == null) {
                pushNull();
            } else {
                push(((ObjectHandle) value).id);
            }
        } else {
            switch (descriptor.charAt(0)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                    push((Number) value);
                    break;
                case 'J':
                    pushLong((Number) value);
                    break;
                case 'Z':
                    push((Boolean) value);
                    break;
                case 'D':
                    pushDouble((Number) value);
                    break;
                case 'F':
                    pushFloat((Number) value);
                    break;
                default:
                    throw new IllegalStateException(descriptor);
            }
        }
    }

    int pop() {
        return stack[--sp];
    }

    ObjectHandle popObject() {
        int id = pop();
        if (id == NULL) {
            return null;
        }
        return ObjectHandle.getById((short) id);
    }

    Object pop(String descriptor) {
        if (descriptor.length() != 1) {
            return popObject();
        }
        switch (descriptor.charAt(0)) {
            case 'B':
                return new Byte((byte) pop());
            case 'C':
                return new Character((char) pop());
            case 'I':
                return new Integer(pop());
            case 'S':
                return new Short((short) pop());
            case 'J':
                return new Long(popLong());
            case 'Z':
                return new Boolean(pop() != 0);
            case 'D':
                throw new RuntimeException();
            case 'F':
                throw new RuntimeException();
            default:
                throw new IllegalStateException(descriptor);
        }
    }

    long popLong() {
        int i1 = pop();
        int i2 = pop();
        return (long)(i1 << 32) | (long)i2;
    }

    int peek() {
        return stack[sp - 1];
    }

    private void checkLocalIndex(int index) {
        if (currentMethod == null) {
            throw new InternalError();
        }
        if (index < 0 || index >= currentMethod.code.maxLocals) {
            throw new IndexOutOfBoundsException();
        }
    }

    int loadLocal(int index) {
        checkLocalIndex(index);
        return stack[fp + index];
    }

    void storeLocal(int index, int value) {
        checkLocalIndex(index);
        stack[fp + index] = value;
    }

    ClassHandle currentClass() {
        return currentClass;
    }

    MethodInfo currentMethod() {
        return currentMethod;
    }

    ConstantPool constantPool() {
        return currentClass.definition.constantPool;
    }

    void invoke(ClassHandle classHandle, MethodInfo method) throws Throwable {
        ClassHandle oldClass = currentClass;
        currentClass = classHandle;

        MethodInfo oldMethod = currentMethod;
        currentMethod = method;

        int oldPcBase = pcBase;
        pcBase = method.code.code.base;

        int oldPc = pc;
        pc = pcBase;

        int oldFp = fp;
        fp = sp - method.argumentStackSlots();

        int oldSp = sp;
        sp = fp + method.code.maxLocals;

        classHandle.initialize();

        System.out.println(classHandle.definition.thisClass + "." + method.name
                + method.descriptor + "{");
        OpcodeResult result = Interpreter.run(this, method.code);
        System.out.println("}");

        fp = oldFp;
        sp = oldSp + method.returnValueStackSlots();
        if (result == OpcodeResult.RETURN1) {
            sp++;
        }

        pc = oldPc;
        pcBase = oldPcBase;

        currentMethod = oldMethod;
        currentClass = oldClass;
    }

}
