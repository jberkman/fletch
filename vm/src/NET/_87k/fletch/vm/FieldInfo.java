package NET._87k.fletch.vm;

import java.util.Dictionary;
import java.util.Hashtable;

final class FieldInfo extends MemberInfo {
    final ConstantValueInfo value;
    private static final Dictionary defaultValues = new Hashtable();

    static {
        defaultValues.put("B", new Byte((byte) 0));
        defaultValues.put("C", new Character((char) 0));
        defaultValues.put("D", new Double(0));
        defaultValues.put("F", new Float(0));
        defaultValues.put("I", new Integer(0));
        defaultValues.put("J", new Long(0));
        defaultValues.put("S", new Short((short) 0));
        defaultValues.put("Z", new Boolean(false));
    }

    FieldInfo(int accessFlags, String name, String descriptor, ConstantValueInfo value) {
        super(accessFlags, name, descriptor);
        this.value = value;
    }

    Object defaultValue() {
        if (value != null) {
            return value.value();
        }
        return defaultValues.get(descriptor);
    }
}
