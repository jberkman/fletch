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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import NET._87k.fletch.vm.AddressRange;
import NET._87k.fletch.vm.ClassFileLoader;

class ClassFileLoaderImpl implements ClassFileLoader {
    private final String[] classPath;
    private final RomImpl rom;
    private int rp = RomImpl.ROM_BASE;

    ClassFileLoaderImpl(String classPath, RomImpl rom) {
        this.classPath = classPath.split("\\:");
        this.rom = rom;
    }

    public AddressRange loadClassFile(String name) throws ClassNotFoundException {
        for (int i = 0; i < classPath.length; i++) {
            String fileName = classPath[i] + "/" + name + ".class";
            InputStream file;
            try {
                file = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                continue;
            }

            try {
                int base = rp;
                while (file.available() > 0) {
                    int b = file.read();
                    rom.flashByte(rp++, b);
                }
                AddressRange ret = new AddressRange(base, rp - base);
                System.out.println(Integer.toHexString(base) + ": Loaded " + fileName + " (" + ret.length + " B)");
                return ret;
            } catch (Throwable e) {
                throw new ClassNotFoundException(name, e);
            } finally {
                try {
                    file.close();
                } catch (IOException e) {
                }
            }
        }
        throw new ClassNotFoundException(name);
    }
}
