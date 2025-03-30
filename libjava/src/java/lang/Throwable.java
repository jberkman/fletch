package java.lang;

import java.io.PrintStream;

public class Throwable {
    private String message;

    public Throwable() {
        super();
    }

    public Throwable(String message) {
        this.message = message;
    }

    public native Throwable fillInStackTrace();

    public String getMessage() {
        return message;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s) {
        throw new RuntimeException();
    }
}
