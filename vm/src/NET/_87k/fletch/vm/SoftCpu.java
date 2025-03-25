package NET._87k.fletch.vm;

/**
 * Virtual implementation of 6502 processor, used for VM development.
 */
class SoftCpu implements Cpu {
    /**
     * Backing memory.
     */
    private byte[] m = new byte[0x10000];

    public short heapBase() {
        return 0x1000;
    }

    public short heapLength() {
        return 0x2000;
    }

    public byte ld(short addr) {
        return m[addr & 0xffff];
    }

    public void st(short addr, byte b) {
        m[addr & 0xffff] = b;
    }

    public void halt() {
        System.exit(0);
    }

    public static void main(String[] args) {
        String[] machineArgs = new String[args.length - 1];
        for (int i = 0; i < machineArgs.length; i++) {
            machineArgs[i] = args[i+1];
        }
        Machine.main(new SoftCpu(), new SoftClassLoader(args[0]), machineArgs);
    }
}
