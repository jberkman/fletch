package NET._87k.fletch.vm;

/**
 * Abstraction layer representing 6502 memory, either Hard (phsycial) or
 * Soft (virtual).
 */
public interface AddressSpace {
    /**
     * Base address of the stack.
     *
     * @return stack base address in RAM (lower 32k).
     * @see AddressSpace#stackSize
     */
    public int stackBase();

    /**
     * Number of bytes allocated in stack.
     *
     * @return Length of stack in bytes.
     * @see AddressSpace#stackBase()
     */
    public int stackSize();

    /**
     * Return memory value at address addr.
     *
     * @param addr unsigned address to access
     * @return Value of memory stored at addr
     * @see AddressSpace#store
     */
    public int load(int addr);

    /**
     * Store value in memory at address addr.
     *
     * @param addr unsigned address to access
     * @param b    Value of memory to be stored
     * @see AddressSpace#load
     */
    public void store(int addr, int b);

    /**
     * Halt execution. This method should not return.
     */
    void halt();
}
