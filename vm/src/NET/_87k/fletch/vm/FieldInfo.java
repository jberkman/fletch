package NET._87k.fletch.vm;

final class FieldInfo extends MemberInfo {
    final ConstantValueInfo value;

    FieldInfo(int accessFlags, String name, String descriptor, ConstantValueInfo value) {
        super(accessFlags, name, descriptor);
        this.value = value;
    }
}
