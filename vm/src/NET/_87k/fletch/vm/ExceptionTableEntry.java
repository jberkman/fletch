package NET._87k.fletch.vm;

final class ExceptionTableEntry {
    final int startPc;
    final int endPc;
    final int handlerPc;
    final int catchType;

    ExceptionTableEntry(int startPc, int endPc, int handlerPc, int catchType) {
        this.startPc = startPc;
        this.endPc = endPc;
        this.handlerPc = handlerPc;
        this.catchType = catchType;
    }
}
