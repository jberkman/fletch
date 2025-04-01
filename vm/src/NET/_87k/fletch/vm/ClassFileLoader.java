package NET._87k.fletch.vm;

public interface ClassFileLoader {

    Slice loadClassFile(String name) throws ClassNotFoundException;

}
