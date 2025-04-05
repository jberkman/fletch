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
