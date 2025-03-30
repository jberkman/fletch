package NET._87k.fletch.vm.jvm;

import NET._87k.fletch.vm.Machine;

public class Jvm {

    public static void main(String[] args) {
        String[] machineArgs = new String[args.length - 1];
        for (int i = 0; i < machineArgs.length; i++) {
            machineArgs[i] = args[i+1];
        }
        Machine.main(new AddressSpaceImpl(), new ClassFileLoaderImpl(args[0]), machineArgs);
    }

    
}
