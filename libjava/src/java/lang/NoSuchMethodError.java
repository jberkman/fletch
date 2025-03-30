package java.lang;

public class NoSuchMethodError extends IncompatibleClassChangeError {
    public NoSuchMethodError() {
    }

    public NoSuchMethodError(String msg) {
        super(msg);
    }
}
