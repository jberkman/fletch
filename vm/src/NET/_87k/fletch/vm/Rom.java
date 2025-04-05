package NET._87k.fletch.vm;

/**
 * Abstraction layer representing 6502 memory, either Hard (phsycial) or
 * Soft (virtual).
 */
public interface Rom {
    /**
     * Base address of the stack.
     *
     * @return stack base address in RAM (lower 32k).
     * @see Rom#size
     */
    public int baseAddress();

    /**
     * Number of bytes allocated in stack.
     *
     * @return Length of stack in bytes.
     * @see Rom#baseAddress()
     */
    public int size();

    /**
     * Return memory value at address addr.
     *
     * @param addr unsigned address to access
     * @return Value of memory stored at addr
     */
    public int load(int addr);
}
