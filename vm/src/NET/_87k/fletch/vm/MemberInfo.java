package NET._87k.fletch.vm;

abstract class MemberInfo {
    final int accessFlags;
    final String name;
    final String descriptor;
    final AttributeInfo[] attributes;

    MemberInfo(int accessFlags, String name, String descriptor, AttributeInfo[] attributes) {
        this.accessFlags = accessFlags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes = attributes;
    }
}
