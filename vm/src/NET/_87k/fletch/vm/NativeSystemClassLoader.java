package NET._87k.fletch.vm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public final class NativeSystemClassLoader implements NativeMethod {

    public void invoke(String methodName, String methodSignature) throws Throwable {
        if (methodName == "loadClass") {
            if (methodSignature == "(Ljava/lang/String;Z)Ljava/lang/Class;") {
                ClassRef
                loadClass();
                return;
            }
        }
        throw new NoSuchMethodError();
    }

    protected void loadClass() throws ClassNotFoundException {
    }
}
