package NET._87k.fletch.vm;

public interface ClassFileLoader {

    AddressRange loadClassFile(String name) throws ClassNotFoundException;

}
