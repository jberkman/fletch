package NET._87k.fletch.vm;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

final class NativeSystemClassLoader implements NativeMethod {

    public void invoke(String methodName, String methodSignature) throws Throwable {
        /*
        if (methodName == "loadClass") {
            if (methodSignature == "(Ljava/lang/String;Z)Ljava/lang/Class;") {
                boolean resolve = Machine.popBoolean();
                ClassTypeHandle className = (ClassTypeHandle) Machine.popRef();
                ClassTypeHandle instance = (ClassTypeHandle) Machine.popRef();
                ClassTypeHandle ret = loadClass(instance, className, resolve);
                Machine.push(ret);
                return;
            }
        }
        */
        throw new NoSuchMethodError();
    }

    /*
    protected ClassTypeHandle loadClass(ClassTypeHandle instance, ClassTypeHandle className, boolean resolve) throws ClassNotFoundException {
        return null;
    }
    */

}

