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

class Aaload implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        ObjectHandle array = ctx.popObject();
        int index = ctx.pop();
        System.out.println((ctx.pc() - 3) + ": aaload " + array + "[" + index + "]");
        ctx.pushNull();
        return OpcodeResult.CONTINUE;
    }
}

class AconstNull implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": aconst_null");
        ctx.pushNull();
        return OpcodeResult.CONTINUE;
    }
}

class Aload0 implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": aload_0");
        ctx.push(ctx.loadLocal(0));
        return OpcodeResult.CONTINUE;
    }
}

class Aload1 implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": aload_1");
        ctx.push(ctx.loadLocal(1));
        return OpcodeResult.CONTINUE;
    }
}

class Areturn implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": areturn");
        ctx.storeLocal(0, ctx.pop());
        return OpcodeResult.RETURN1;
    }
}

class Dup implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": dup");
        ctx.push(ctx.peek());
        return OpcodeResult.CONTINUE;
    }
}

class GetStatic implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) throws ClassNotFoundException {
        int index = ctx.readPc2();
        System.out.println((ctx.pc() - 3) + ": getstatic #" + index);
        ClassHandle fieldClass = ctx.constantPool().refClass(index);
        FieldInfo fieldInfo = ctx.constantPool().staticField(index);
        Object value = fieldClass.getStatic(fieldInfo.index);
        ctx.push(value, fieldInfo.descriptor);
        return OpcodeResult.CONTINUE;
    }
}

class Iconst0 implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": iconst_0");
        ctx.push(0);
        return OpcodeResult.CONTINUE;
    }
}

class Iconst1 implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": iconst_1");
        ctx.push(1);
        return OpcodeResult.CONTINUE;
    }
}

class Iconst4 implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": iconst_4");
        ctx.push(4);
        return OpcodeResult.CONTINUE;
    }
}

class Iconst5 implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": iconst_5");
        ctx.push(5);
        return OpcodeResult.CONTINUE;
    }
}

class InvokeSpecial implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) throws Throwable {
        int index = ctx.readPc2();
        System.out.println((ctx.pc() - 3) + ": invokespecial #" + index);
        ClassHandle methodClass = ctx.constantPool().refClass(index);
        MethodInfo method = ctx.constantPool().methodInfo(index);
        ctx.invoke(methodClass, method);
        // NameAndTypeInfo nat =
        // ctx.currentObjectHandle().classObject.definition.constantPool.method(index);
        // System.out.println(nat.name() + "." + nat.descriptor());

        return OpcodeResult.CONTINUE;
    }
}

class InvokeVirtual implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) throws Throwable {
        int index = ctx.readPc2();
        System.out.println((ctx.pc() - 3) + ": invokevirtual #" + index);
        ClassHandle methodClass = ctx.constantPool().virtualMethodClass(index);
        MethodInfo method = ctx.constantPool().methodInfo(index);
        System.out.println("methodClass: " + methodClass.definition.thisClass + " method: " + method);
        ctx.invoke(methodClass, method);
        return OpcodeResult.CONTINUE;
    }
}

class Ldc implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        int index = ctx.readPc();
        System.out.println((ctx.pc() - 2) + ": ldc #" + index);
        ConstantValueInfo value = ctx.constantPool().constantValue(index);
        ctx.push(value.value(), value.descriptor());
        return OpcodeResult.CONTINUE;
    }
}

class New implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) throws ClassNotFoundException {
        int index = ctx.readPc2();
        System.out.println((ctx.pc() - 3) + ": new #" + index);
        ClassHandle newClass = ctx.constantPool().classHandle(index);
        ObjectHandle object = new ObjectHandle(newClass);
        ctx.push(object.id);
        return OpcodeResult.CONTINUE;
    }
}

class Nop implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": nop");
        return OpcodeResult.CONTINUE;
    }
}

class PutStatic implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) throws ClassNotFoundException {
        int index = ctx.readPc2();
        System.out.println((ctx.pc() - 3) + ": putstatic #" + index);
        ClassHandle fieldClass = ctx.constantPool().refClass(index);
        FieldInfo fieldInfo = ctx.constantPool().staticField(index);
        Object value = ctx.pop(fieldInfo.descriptor);
        fieldClass.setStatic(fieldInfo.index, value);
        return OpcodeResult.CONTINUE;
    }
}

class Return implements Opcode {
    public OpcodeResult execute(ThreadContext ctx) {
        System.out.println((ctx.pc() - 1) + ": return");
        return OpcodeResult.RETURN0;
    }
}

/**
 * A Java Virtual Machine.
 */
public abstract class Interpreter {
    protected static ClassFileLoader classFileLoader;

    protected static Rom rom;
    // static CallStack callStack = new CallStack();

    private static final Opcode[] opcodes = new Opcode[256];

    /**
     * Initialize a table of op codes. Each entry that refers to a valid
     * instruction is initialized with an instance of Opcode that can
     * be executed. Invalid instructions store null.
     *
     * @return opcode lookup table
     * @see Opcode
     */
    static {
        opcodes[0x00] = new Nop();
        opcodes[0x01] = new AconstNull();
        opcodes[0x03] = new Iconst0();
        opcodes[0x04] = new Iconst1();
        opcodes[0x07] = new Iconst4();
        opcodes[0x08] = new Iconst5();
        opcodes[0x12] = new Ldc();
        opcodes[0x2a] = new Aload0();
        opcodes[0x2b] = new Aload1();
        opcodes[0x32] = new Aaload();
        opcodes[0x59] = new Dup();
        opcodes[0xb0] = new Areturn();
        opcodes[0xb1] = new Return();
        opcodes[0xb2] = new GetStatic();
        opcodes[0xb3] = new PutStatic();
        opcodes[0xb6] = new InvokeVirtual();
        opcodes[0xb7] = new InvokeSpecial();
        opcodes[0xbb] = new New();
    }

    static OpcodeResult run(ThreadContext ctx, CodeAttribute code) throws Throwable {
        OpcodeResult result = OpcodeResult.CONTINUE;
        int pc = 0;
        while (pc < code.code.length && result == OpcodeResult.CONTINUE) {
            int opcode = ctx.readPc();
            if (opcodes[opcode] == null) {
                throw new InternalError("Invalid instruction: " + (ctx.pc() - 1) + " 0x" + Integer.toHexString(opcode));
            }
            result = opcodes[opcode].execute(ctx);
        }
        return result;
    }

    public static void boot(String mainClassName, String[] args) {
        try {
            // Create the Class object for java.lang.Class
            ClassHandle classClass = ClassHandle.forName("java.lang.Class");

            // Bind the java.lang.Class object to the class class
            classClass.bindClassClassHandle();

            ArrayHandle argsHandle = new ArrayHandle(new ObjectHandle[0], "java.lang.String");
            ThreadContext.current().push(argsHandle.id);

            ClassHandle mainClass = ClassHandle.forName(mainClassName);
            mainClass.invokeStatic("main", "([Ljava/lang/String;)V");
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
