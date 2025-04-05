package NET._87k.fletch.vm;

import java.util.jar.Attributes.Name;

abstract class MemberrefInfo implements ConstantPoolEntry {

    private final ConstantPool pool;
    private final int classIndex;
    private final int natIndex;

    private String className;
    private ClassHandle classHandle;
    private NameAndTypeInfo nat;

    MemberrefInfo(ConstantPool pool, int classIndex, int nameAndTypeIndex) {
        this.pool = pool;
        this.classIndex = classIndex;
        this.natIndex = nameAndTypeIndex;
    }

    String className() {
        if (className != null) {
            return className;
        }
        return className = pool.utf8String(classIndex);
    }

    ClassHandle classHandle() {
        if (classHandle != null) {
            return classHandle;
        }
        return classHandle; // = ClassObjectHandle.forName(className());
    }

    private NameAndTypeInfo nat() {
        if (nat != null) {
            return nat;
        }
        return nat = pool.nameAndType(natIndex);
    }

    String name() {
        return nat().name();
    }

    String descriptor() {
        return nat().descriptor();
    }
}
