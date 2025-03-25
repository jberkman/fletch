package NET._87k.fletch.vm;

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
class Machine {
    private static Cpu cpu;
    private static SystemClassLoader classLoader;

    /**
     * Index into heap specifying the first unallocated byte.
     * 
     * @see Machine#push
     * @see Machine#pop
     * @see Machine#heap
     */
    static private short sp = 0;

    /**
     * Program counter register. Contains address of current instruction
     * within the full 64k address space. Due to Java's lack of
     * unsigned, must be anded with 0xffff to access upper 32k ROM
     * addresses.
     */
    static private short pc;

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
    static void push(int i) {
        cpu.st(sp++, (byte) (0xff & i));
        cpu.st(sp++, (byte) (0xff & i >> 8));
        cpu.st(sp++, (byte) (0xff & i >> 16));
        cpu.st(sp++, (byte) (0xff & i >> 24));
    }

    /**
     * Pop a 32-bit value from the stack, in little-endian byteorder.
     * The sp register is decremented by 4;
     * 
     * @return The value popped from the stack.
     * @see Machine#push
     * @see Machine#sp
     */
    static int pop() {
        int i = (0xff & cpu.ld(--sp)) << 24;
        i |= (0xff & cpu.ld(--sp)) << 16;
        i |= (0xff & cpu.ld(--sp)) << 8;
        i |= (0xff & cpu.ld(--sp));
        return i;
    }

    /**
     * Read the current value of the pc register, incrementing it.
     * 
     * @return Next value of program counter.
     * @see Machine#pc
     */
    static byte readPC() {
        return cpu.ld(pc++);
    }

    static void halt() {
        cpu.halt();
    }

    static void main(Cpu cpu, SystemClassLoader classLoader, String[] args) {
        Machine.cpu = cpu;
        Machine.classLoader = classLoader;
        
        ClassFile mainClass;
        try {
            classLoader.loadClass("java.lang.Object", true);
            classLoader.loadClass("java.lang.Class", true);
            classLoader.loadClass("java.lang.String", true);
            classLoader.loadClass("java.lang.Cloneable", true);
            //mainClass = classLoader.loadClass(args[0], true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // load(args[0])
        // intern(args)
        // pc = args[0].Main

        //Opcode[] ops = createOpcodeTable();
        //while (true) {
        //    ops[readPC()].execute();
        //}
    }
}
