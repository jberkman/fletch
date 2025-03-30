package NET._87k.fletch.vm;

public class NativeObject implements NativeMethod {

    public void invoke(String methodName, String methodSignature) throws Throwable {
        if (methodName == "clone") {
            if (methodSignature == "()Ljava/lang/Object;") {
                nativeClone();
                return;
            }
        } else if (methodName == "getClass") {
            if (methodSignature == "()Ljava/lang/Class;") {
                nativeGetClass();
                return;
            }
        }
        throw new NoSuchMethodError();
    }

    private static void nativeClone() {
        throw new RuntimeException();
    }

    private static void nativeGetClass() {
        throw new RuntimeException();
    }

}
