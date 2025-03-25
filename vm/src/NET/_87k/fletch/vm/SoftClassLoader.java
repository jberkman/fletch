package NET._87k.fletch.vm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

class SoftClassLoader extends SystemClassLoader {
    private String baseDir;

    SoftClassLoader(String baseDir) {
        super();
        this.baseDir = baseDir;
    }

    protected ClassFile loadClass(String name, boolean resolve) throws ClassNotFoundException {
        name = baseDir + "/" + name.replace('.', '/') + ".class";
        InputStream file;
        try {
            file = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            throw new ClassNotFoundException(name, e);
        }

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(file.available());
            byte[] buf = new byte[1024];
            while (true) {
                int len = file.read(buf);
                if (len < 0) {
                    break;
                }
                buffer.write(buf, 0, len);
            }
            byte[] bytes = buffer.toByteArray();
            ClassFile classFile = defineClass(bytes, 0, bytes.length);
            System.out.println("Loaded " + name);
            return classFile;
        } catch (Throwable e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            try {
                file.close();
            } catch (IOException e) {
            }
        }
    }
}
