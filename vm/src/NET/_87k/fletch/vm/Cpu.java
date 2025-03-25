package NET._87k.fletch.vm;

/**
 * Abstraction layer representing a 6502 CPU, either Hard (phsycial) or
 * Soft (virtual).
 */
interface Cpu {
    /**
     * Base address of the heap.
     * 
     * @return heap base address in RAM (lower 32k).
     * @see Cpu#heapLength
     */
    short heapBase();

    /**
     * Number of bytes allocated in heap.
     * 
     * @return Length of heap in bytes.
     * @see Cpu#heapBase()
     */
    short heapLength();

    /**
     * Return memory value at address addr.
     * 
     * @param addr unsigned address to access
     * @return Value of memory stored at addr
     * @see Cpu#st
     */
    byte ld(short addr);

    /**
     * Store value in memory at address addr.
     * 
     * @param addr unsigned address to access
     * @param b    Value of memory to be stored
     * @see Cpu#ld
     */
    void st(short addr, byte b);

    /**
     * Halt execution. This method should not return.
     */
    void halt();
}
