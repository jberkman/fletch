package NET._87k.fletch.vm;

final class JavaNop implements OpCode {
    public void execute(Machine machine, byte[] args) {}
}

public class JavaMachine implements Machine {
    private int pc;
    private OpCode[] opCodes;

    private JavaMachine() {
        opCodes = new OpCode[256];
        
        opCodes[OpCode.NOP] = new JavaNop();
    }

    public static Machine newInstance() {
        return new JavaMachine();
    }

    public short getPC() {
        return (short)(pc & 0xffff);
    }

    public void setPC(short pc) {
        this.pc = pc;
    }

    public void incPC() {
        ++pc;        
    }

    public void execute(byte opCode, byte[] args) {
        opCodes[opCode & 0xff].execute(this, args);
    }
}
