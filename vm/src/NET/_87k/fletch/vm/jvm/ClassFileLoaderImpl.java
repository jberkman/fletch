package NET._87k.fletch.vm.jvm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import NET._87k.fletch.vm.ClassFile;
import NET._87k.fletch.vm.ClassFileLoader;

class ClassFileLoaderImpl implements ClassFileLoader {
    private String[] classPath;

    ClassFileLoaderImpl(String classPath) {
        this.classPath = classPath.split("\\:");
    }

    public ClassFile loadClassFile(String name) throws ClassNotFoundException {
        Throwable ex = null;
        byte[] buf = new byte[8192];
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
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(file.available());
                while (true) {
                    int len = file.read(buf);
                    if (len < 0) {
                        break;
                    }
                    buffer.write(buf, 0, len);
                }
                byte[] bytes = buffer.toByteArray();
                System.out.println("Loaded " + fileName);
                return new ClassFile(bytes, 0, bytes.length);
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
