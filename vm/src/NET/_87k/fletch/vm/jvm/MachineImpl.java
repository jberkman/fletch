package NET._87k.fletch.vm.jvm;

import NET._87k.fletch.vm.Machine;

public class MachineImpl extends Machine {

    public static void main(String[] args) {
        cpu = new AddressSpaceImpl();
        classFileLoader = new ClassFileLoaderImpl(args[0]);

        String[] machineArgs = new String[args.length - 2];
        for (int i = 0; i < machineArgs.length; i++) {
            machineArgs[i] = args[i+2];
        }

        boot(args[1], machineArgs);
    }


}
