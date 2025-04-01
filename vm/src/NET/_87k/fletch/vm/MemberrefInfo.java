package NET._87k.fletch.vm;

import java.util.jar.Attributes.Name;

abstract class MemberrefInfo implements ConstantPoolEntry {

    private final int classIndex;
    private final int natIndex;

    String className;
    String name;
    String descriptor;

    MemberrefInfo(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.natIndex = nameAndTypeIndex;
    }

    public void resolve(ConstantPoolEntry[] pool) {
        if (className == null) {
            ConstantPoolEntry classEntry = pool[classIndex - 1];
            ConstantPoolEntry natEntry = pool[natIndex - 1];

            if (!(classEntry instanceof ClassInfo) ||
                    !(natEntry instanceof NameAndTypeInfo)) {
                throw new ClassFormatError();
            }
            classEntry.resolve(pool);
            natEntry.resolve(pool);

            className = ((ClassInfo) classEntry).name;
            name = ((NameAndTypeInfo) natEntry).name;
            descriptor = ((NameAndTypeInfo) natEntry).descriptor;
        }
    }
}
