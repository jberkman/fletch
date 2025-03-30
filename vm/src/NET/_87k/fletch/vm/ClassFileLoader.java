package NET._87k.fletch.vm;

public interface ClassFileLoader {

    ClassFile loadClassFile(String name) throws ClassNotFoundException;

}
