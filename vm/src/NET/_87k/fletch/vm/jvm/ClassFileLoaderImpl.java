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
