package NET._87k.fletch.vm.jvm;

import NET._87k.fletch.vm.AddressSpace;
import NET._87k.fletch.vm.Machine;

/**
 * Virtual implementation of 6502 processor, used for VM development.
 */
class AddressSpaceImpl implements AddressSpace {
    /**
     * Backing memory.
     */
    private byte[] m = new byte[0x10000];

    public short stackBase() {
        return 0x1000;
    }

    public short stackSize() {
        return 0x2000;
    }

    public byte load(short addr) {
        return m[addr & 0xffff];
    }

    public void store(short addr, byte b) {
        m[addr & 0xffff] = b;
    }

    public void halt() {
        System.exit(0);
    }
}
