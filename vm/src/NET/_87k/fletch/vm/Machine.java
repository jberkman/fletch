package NET._87k.fletch.vm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 */
class Nop implements Opcode {
    public OpcodeResult execute(DataInputStream bytecode) {
        System.out.println("nop");
        return OpcodeResult.CONTINUE;
    }
}

class Aload0 implements Opcode {
    public OpcodeResult execute(DataInputStream bytecode) {
        System.out.println("aload_0");
        return OpcodeResult.CONTINUE;
    }
}

class InvokeSpecial implements Opcode {
    public OpcodeResult execute(DataInputStream bytecode) throws IOException {
        int index = bytecode.readUnsignedShort();
        System.out.println("invokespecial " + index);
        return OpcodeResult.CONTINUE;
    }
}

class Return implements Opcode {
    public OpcodeResult execute(DataInputStream bytecode) throws IOException {
        System.out.println("return");
        return OpcodeResult.RETURN;
    }
}

/**
 * A Java Virtual Machine.
 */
public abstract class Machine {
    protected static AddressSpace cpu;
    protected static ClassFileLoader classFileLoader;

    static ObjectHandle systemClassLoader;
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
        opcodes[0x2a] = new Aload0();
        opcodes[0xb1] = new Return();
        opcodes[0xb7] = new InvokeSpecial();
    }

    /**
     * Index into heap specifying the first unallocated byte.
     *
     * @see Machine#push
     * @see Machine#pop
     */
    private static short sp;

    /**
     * Program counter register. Contains address of current instruction
     * within the full 64k address space. Due to Java's lack of
     * unsigned, must be anded with 0xffff to access upper 32k ROM
     * addresses.
     *
     * @see Machine#readPc
     */
    private static short pc;

    /**
     * Push a 32-bit value onto the stack, in little-endian byteorder.
     * The sp register is incremented by 4.
     *
     * @param i value pushed onto the stack.
     * @see Machine#pop
     * @see Machine#sp
     */
    public static void push(int i) {
        cpu.store(sp++, (byte) ((i >> 0) & 0xff));
        cpu.store(sp++, (byte) ((i >> 8) & 0xff));
        cpu.store(sp++, (byte) ((i >> 16) & 0xff));
        cpu.store(sp++, (byte) ((i >> 24) & 0xff));
    }

    public static void push(byte b) {
        push(b & 0xffff);
    }

    public static void push(short s) {
        push(s & 0xffff);
    }

    public static void push(ObjectHandle obj) {
        push(obj.id & 0xffff);
    }

    /**
     * Pop a 32-bit value from the stack, in little-endian byteorder.
     * The sp register is decremented by 4;
     *
     * @return The value popped from the stack.
     * @see Machine#push
     * @see Machine#sp
     */
    public static int pop() {
        int i = (0xff & cpu.load(--sp)) << 24;
        i |= (0xff & cpu.load(--sp)) << 16;
        i |= (0xff & cpu.load(--sp)) << 8;
        i |= (0xff & cpu.load(--sp));
        return i;
    }

    public static ObjectHandle popRef() {
        return ObjectHandle.getById((short) pop());
    }

    // public static byte[] popByteArray() {
    // return ((ArrayHandle) popRef()).bytes();
    // }

    public static boolean popBoolean() {
        return pop() != 0;
    }

    /**
     * Read the current value of the pc register, incrementing it.
     *
     * @return Next value of program counter.
     * @see Machine#pc
     */
    public static byte readPc() {
        return cpu.load(pc++);
    }

    public static void halt() {
        cpu.halt();
    }

    public static void invokeBytecode(DataInputStream bytecode) throws IOException {
        OpcodeResult result = OpcodeResult.CONTINUE;
        while (bytecode.available() > 0 && result == OpcodeResult.CONTINUE) {
            int opcode = bytecode.readUnsignedByte();
            if (opcodes[opcode] == null) {
                throw new InternalError("Invalid instruction: 0x" + Integer.toHexString(opcode));
            }
            result = opcodes[opcode].execute(bytecode);
        }
    }

    public static void boot(String mainClass, String[] args) {
        BootstrapClassLoader classLoader = new BootstrapClassLoader();
        try {
            // Create the Class object for java.lang.Class
            ClassObjectHandle classClassHandle = classLoader.loadClassObject("java/lang/Class").handle;

            // Bind the java.lang.Class object to the class class
            classClassHandle.bindClassClassHandle();

            ClassObjectHandle loaderClassHandle = classLoader
                    .loadClassObject("NET/_87k/fletch/libjava/SystemClassLoader").handle;
            systemClassLoader = new ObjectHandle(loaderClassHandle);

            loaderClassHandle.bindClassLoader(systemClassLoader);
            NativeClassLoader.adopt(loaderClassHandle);

            classClassHandle.bindClassLoader(systemClassLoader);
            NativeClassLoader.adopt(classClassHandle);

            loaderClassHandle.invokeSpecial("<init>", "()V");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
