package NET._87k.fletch.vm;

final class CodeAttribute {
    final int maxStack;
    final int maxLocals;
    final Slice code;
    final ExceptionTableEntry[] exceptionTable;

    CodeAttribute(int maxStack, int maxLocals, Slice code, ExceptionTableEntry[] exceptionTable) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.exceptionTable = exceptionTable;
    }
}
