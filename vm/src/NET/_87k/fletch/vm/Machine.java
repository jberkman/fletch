package NET._87k.fletch.vm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 */
class Nop implements Opcode {
    public OpcodeResult execute() {
        System.out.println(Integer.toHexString(Machine.pc - 1) + ": nop");
        return OpcodeResult.CONTINUE;
    }
}

class Aload0 implements Opcode {
    public OpcodeResult execute() {
        System.out.println(Integer.toHexString(Machine.pc - 1) + ": aload_0");
        Machine.callStack.push(Machine.callStack.loadLocal(0));
        return OpcodeResult.CONTINUE;
    }
}

class InvokeSpecial implements Opcode {
    public OpcodeResult execute() {
        int index = Machine.readPc2();
        System.out.println(Integer.toHexString(Machine.pc - 3) + ": invokespecial " + index);
        //NameAndTypeInfo nat = Machine.callStack.currentObjectHandle().classObject.definition.constantPool.method(index);
        //System.out.println(nat.name() + "." + nat.descriptor());

        return OpcodeResult.CONTINUE;
    }
}

class Return implements Opcode {
    public OpcodeResult execute() {
        System.out.println(Integer.toHexString(Machine.pc - 1) + ": return");
        return OpcodeResult.RETURN;
    }
}

/**
 * A Java Virtual Machine.
 */
public abstract class Machine {
    protected static Rom rom;
    protected static ClassFileLoader classFileLoader;

    static CallStack callStack = new CallStack();

    private static final Opcode[] opcodes = new Opcode[256];

    /**
     * Program counter register. Contains address of current instruction
     * within the full 64k address space. Due to Java's lack of
     * unsigned, must be anded with 0xffff to access upper 32k ROM
     * addresses.
     *
     * @see Machine#readPc
     */
    static int pc;

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
        opcodes[0x2a] = new Aload0();
        opcodes[0xb1] = new Return();
        opcodes[0xb7] = new InvokeSpecial();
    }

    /**
     * Read the current value of the pc register, incrementing it.
     *
     * @return Next value of program counter.
     * @see Machine#pc
     */
    public static int readPc() {
        return rom.load(pc++);
    }

    public static int readPc2() {
        int byte1 = readPc();
        int byte2 = readPc();
        return (byte1 << 8) | byte2;
    }

    public static void invoke(ClassHandle classHandle, MethodInfo method) {
        int oldPc = pc;
        OpcodeResult result = OpcodeResult.CONTINUE;
        pc = method.code.code.base;
        int end = pc + method.code.code.length;
        callStack.pushFrame(classHandle, method);
        while (pc < end && result == OpcodeResult.CONTINUE) {
            int opcode = readPc();
            if (opcodes[opcode] == null) {
                throw new InternalError("Invalid instruction: 0x" + Integer.toHexString(opcode));
            }
            result = opcodes[opcode].execute();
        }
        callStack.popFrame();
        pc = oldPc;
    }

    public static void boot(String mainClassName, String[] args) {
        try {
            // Create the Class object for java.lang.Class
            ClassHandle classClass = ClassHandle.forName("java.lang.Class");

            // Bind the java.lang.Class object to the class class
            classClass.bindClassClassHandle();

            ArrayHandle argsHandle = new ArrayHandle(new ObjectHandle[0], "java.lang.String");
            ClassHandle mainClass = ClassHandle.forName(mainClassName);

            callStack.push(argsHandle.id);
            mainClass.invokeStatic("main", "([Ljava/lang/String;)V");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
