package NET._87k.fletch.libjava;

public class SystemClassLoader extends ClassLoader {

    protected native Class loadClass(String name, boolean resolve) throws ClassNotFoundException;

}
