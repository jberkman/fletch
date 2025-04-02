package NET._87k.fletch.vm;

final class LongInfo implements ConstantPoolEntry, ConstantValueInfo {

    final long value;

    LongInfo(int highBytes, int lowBytes) {
        value = ((long)highBytes << 32) | (long)lowBytes;
    }

}
