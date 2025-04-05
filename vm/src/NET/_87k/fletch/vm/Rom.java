// MIT License
//
// Copyright (c) 2025 jacob berkman
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.


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
