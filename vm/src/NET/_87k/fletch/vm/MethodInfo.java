package NET._87k.fletch.vm;

final class MethodInfo extends MemberInfo {
    final CodeAttribute code;
    final String[] exceptions;

    MethodInfo(int accessFlags, String name, String descriptor, CodeAttribute code, String[] exceptions) {
        super(accessFlags, name, descriptor);
        this.code = code;
        this.exceptions = exceptions;
    }

}
