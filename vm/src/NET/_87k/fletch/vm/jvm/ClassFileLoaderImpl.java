package NET._87k.fletch.vm.jvm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import NET._87k.fletch.vm.AddressRange;
import NET._87k.fletch.vm.AddressSpace;
import NET._87k.fletch.vm.ClassFileLoader;

class ClassFileLoaderImpl implements ClassFileLoader {
    private final String[] classPath;
    private final AddressSpace cpu;
    private int rp = AddressSpaceImpl.ROM_BASE;

    ClassFileLoaderImpl(String classPath, AddressSpace cpu) {
        this.classPath = classPath.split("\\:");
        this.cpu = cpu;
    }

    public AddressRange loadClassFile(String name) throws ClassNotFoundException {
        Throwable ex = null;
        for (int i = 0; i < classPath.length; i++) {
            String fileName = classPath[i] + "/" + name + ".class";
            InputStream file;
            try {
                file = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                ex = e;
                continue;
            }

            try {
                int base = rp;
                while (file.available() > 0) {
                    int b = file.read();
                    cpu.store(rp++, b);
                }
                AddressRange ret = new AddressRange(base, rp - base);
                System.out.println("Loaded " + fileName + ": " + ret);
                return ret;
            } catch (Throwable e) {
                ex = e;
                continue;
            } finally {
                try {
                    file.close();
                } catch (IOException e) {
                }
            }
        }
        throw new ClassNotFoundException(name, ex);
    }
}
