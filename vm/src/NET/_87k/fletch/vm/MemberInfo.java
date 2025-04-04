package NET._87k.fletch.vm;

abstract class MemberInfo {
    final int accessFlags;
    final String name;
    final String descriptor;

    MemberInfo(int accessFlags, String name, String descriptor) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.descriptor = descriptor;
    }

    boolean isStatic() {
        return (accessFlags & AccessFlags.ACC_STATIC) != 0;
    }

    boolean isNative() {
        return (accessFlags & AccessFlags.ACC_NATIVE) != 0;
    }

}
