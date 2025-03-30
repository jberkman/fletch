package java.lang;

public class IncompatibleClassChangeError extends LinkageError {
    public IncompatibleClassChangeError() {
    }

    public IncompatibleClassChangeError(String msg) {
        super(msg);
    }
}
