package NET._87k.fletch.vm;

import java.io.IOException;

/**
 *
 */
class Nop implements Opcode {
    public void execute() {
    }
}

/**
 * A Java Virtual Machine.
 */
public final class Machine {
    private static AddressSpace cpu;

    /**
     * Index into heap specifying the first unallocated byte.
     *
     * @see Machine#push
     * @see Machine#pop
     */
    private static short sp = 0;

    /**
     * Program counter register. Contains address of current instruction
     * within the full 64k address space. Due to Java's lack of
     * unsigned, must be anded with 0xffff to access upper 32k ROM
     * addresses.
     *
     * @see Machine#readPC
     */
    private static short pc;

    /**
     * Initialize a table of op codes. Each entry that refers to a valid
     * instruction is initialized with an instance of Opcode that can
     * be executed. Invalid instructions store null.
     *
     * @return opcode lookup table
     * @see Opcode
     */
    static Opcode[] createOpcodeTable() {
        Opcode[] opCodes = new Opcode[256];

        opCodes[0x0] = new Nop();

        return opCodes;
    }

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

    public static void push(ObjectRef obj) {
        push(obj.id() & 0xffff);
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

    public static ObjectRef popRef() {
        return ObjectRef.getById((short) pop());
    }

    public static byte[] popByteArray() {
        return ((ByteArrayRef) popRef()).bytes();
    }

    /**
     * Read the current value of the pc register, incrementing it.
     *
     * @return Next value of program counter.
     * @see Machine#pc
     */
    public static byte readPC() {
        return cpu.load(pc++);
    }

    public static void halt() {
        cpu.halt();
    }

    private static ClassType bootstrapClass(String className, ClassFileLoader classFileLoader) throws ClassNotFoundException {
        try {
            ClassFile classFile = classFileLoader.loadClassFile(className);
            classFile.validate();
            String superClassName = classFile.superClass();
            ClassType superClass = null;
            if (superClassName != null) {
                superClass = bootstrapClass(superClassName, classFileLoader);
            }
            return new ClassType(superClass, classFile);
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }
    }

    public static void main(AddressSpace cpu, ClassFileLoader classFileLoader, String[] args) {
        Machine.cpu = cpu;

        ClassFile mainClass;
        try {
            bootstrapClass("java/lang/Class", classFileLoader);
            bootstrapClass("NET/_87k/fletch/libjava/SystemClassLoader", classFileLoader);
            //mainClass = classFileLoader.loadClassFile(args[0].replace('.', '/'));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // load(args[0])
        // intern(args)
        // pc = args[0].Main

        // Opcode[] ops = createOpcodeTable();
        // while (true) {
        // ops[readPC()].execute();
        // }
    }
}
