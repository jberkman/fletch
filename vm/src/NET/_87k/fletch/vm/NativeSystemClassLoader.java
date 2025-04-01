package NET._87k.fletch.vm;

final class NativeSystemClassLoader implements NativeMethod {

    public void invoke(String methodName, String methodSignature) throws Throwable {
        if (methodName == "loadClass") {
            if (methodSignature == "(Ljava/lang/String;Z)Ljava/lang/Class;") {
                boolean resolve = Machine.popBoolean();
                ClassRef className = (ClassRef) Machine.popRef();
                ClassRef instance = (ClassRef) Machine.popRef();
                ClassRef ret = loadClass(instance, className, resolve);
                Machine.push(ret);
                return;
            }
        }
        throw new NoSuchMethodError();
    }

    protected ClassRef loadClass(ClassRef instance, ClassRef className, boolean resolve) throws ClassNotFoundException {
        return null;
    }
}
