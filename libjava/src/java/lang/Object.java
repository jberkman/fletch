package java.lang;

public class Object {

    protected native Object clone();

    public boolean equals(Object obj) {
        throw new RuntimeException();
    }

    protected void finalize() throws Throwable {
    }

    public final native Class getClass();

    public native int hashCode();

    public final native void notify();

    public final native void notifyAll();

    public String toString() {
        throw new RuntimeException();
    }

    public final void wait() throws InterruptedException {
        throw new RuntimeException();
    }

    public final void wait(long timeout) throws InterruptedException {
        throw new RuntimeException();
    }

    public final void wait(long timeout, int nanos) throws InterruptedException {
        throw new RuntimeException();
    }

}
