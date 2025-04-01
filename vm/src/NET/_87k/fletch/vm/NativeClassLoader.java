package NET._87k.fletch.vm;

final class NativeClassLoader implements NativeMethod {

    public void invoke(String methodName, String methodSignature) throws Throwable {
        if (methodName == "defineClass") {
            if (methodSignature == "([BII)Ljava/lang/Class;") {
                int len = Machine.pop();
                int off = Machine.pop();
                short id = (short) Machine.pop();
                byte[] bytes = ((ByteArrayRef) ObjectRef.getById(id)).bytes();
                id = (short) Machine.pop();
                ClassRef self = (ClassRef) ObjectRef.getById(id);

                ClassObjectRef ret = defineClass(self, bytes, off, len);

                Machine.push(ret.id() & 0xffff);
                return;
            }
        } else if (methodName == "findSystemClass") {
            if (methodSignature == "(Ljava/lang/String;)Ljava/lang/Class;") {
                findSystemClass();
                return;
            }
        } else if (methodName == "resolveClass") {
            if (methodSignature == "(Ljava/lang/Class;)") {
                resolveClass();
                return;
            }
        }
        throw new NoSuchMethodError();
    }

    static ClassObjectRef defineClass(ClassRef classLoader, byte[] bytes, int offset, int len) {
        throw new RuntimeException();
    }

    static void findSystemClass() throws ClassNotFoundException {
        throw new RuntimeException();
    }

    static void resolveClass() {
        throw new RuntimeException();
    }

}
