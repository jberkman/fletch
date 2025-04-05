package NET._87k.fletch.vm.jvm;

import NET._87k.fletch.vm.Rom;

/**
 * Virtual implementation of 6502 processor, used for VM development.
 */
class RomImpl implements Rom {
    final static int ROM_BASE = 0x8000;
    final static int ROM_SIZE = 0x8000;

    private int freeBase = 0;

    /**
     * Backing memory.
     */
    private final byte[] rom = new byte[ROM_SIZE];

    public int baseAddress() {
        return ROM_BASE;
    }

    public int size() {
        return ROM_SIZE;
    }

    public int load(int addr) {
        return rom[addr - ROM_BASE] & 0xff;
    }

    void flashByte(int addr, int b) {
        rom[addr - ROM_BASE] = (byte) b;
    }
/*
    int flashRom(byte[] bytes, int offset, int length) {
        int ret = freeBase;
        for (int i = 0; i < offset; i++) {
            rom[freeBase++] = bytes[i + offset];
        }
        return ret;
    }
*/
}
