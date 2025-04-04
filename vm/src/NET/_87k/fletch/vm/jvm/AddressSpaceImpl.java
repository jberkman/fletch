package NET._87k.fletch.vm.jvm;

import NET._87k.fletch.vm.AddressSpace;

/**
 * Virtual implementation of 6502 processor, used for VM development.
 */
class AddressSpaceImpl implements AddressSpace {
    final static int ROM_BASE = 0x8000;

    /**
     * Backing memory.
     */
    private final byte[] m = new byte[0x10000];
    private int romBase = 0x8000;

    public int stackBase() {
        return 0x1000;
    }

    public int stackSize() {
        return 0x2000;
    }

    public int load(int addr) {
        return m[addr] & 0xff;
    }

    public void store(int addr, int b) {
        m[addr] = (byte) b;
    }

    public void halt() {
        System.exit(0);
    }

    int flashRom(byte[] bytes, int offset, int length) {
        int ret = romBase;
        for (int i = 0; i < offset; i++) {
            m[romBase++] = bytes[i + offset];
        }
        return ret;
    }
}
