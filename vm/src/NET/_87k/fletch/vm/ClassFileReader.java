package NET._87k.fletch.vm;

import java.io.DataInputStream;
import java.io.IOException;

final class ClassFileReader extends DataInputStream {
    static final int MAGIC = 0xcafebabe;
    static final short MAJOR_VERSION = 45;
    static final short MINOR_VERSION = 3;

    static final byte CONSTANT_CLASS = 7;
    static final byte CONSTANT_FIELDREF = 9;
    static final byte CONSTANT_METHODREF = 10;
    static final byte CONSTANT_INTERFACE_METHODREF = 11;
    static final byte CONSTANT_STRING = 8;
    static final byte CONSTANT_INTEGER = 3;
    static final byte CONSTANT_FLOAT = 4;
    static final byte CONSTANT_LONG = 5;
    static final byte CONSTANT_DOUBLE = 6;
    static final byte CONSTANT_NAME_AND_TYPE = 12;
    static final byte CONSTANT_UTF8 = 1;

    static final short ACC_PUBLIC = 0x1;
    static final short ACC_PRIVATE = 0x2;
    static final short ACC_PROTECTED = 0x4;
    static final short ACC_STATIC = 0x8;

    static final short ACC_FINAL = 0x10;
    static final short ACC_SUPER = 0x20;
    static final short ACC_SYNCHRONIZED = 0x20;
    static final short ACC_VOLATILE = 0x40;
    static final short ACC_TRANSIENT = 0x80;

    static final short ACC_NATIVE = 0x100;
    static final short ACC_INTERFACE = 0x200;
    static final short ACC_ABSTRACT = 0x400;

    private final PosInputStream pos;
    private final byte[] bytes;

    private ClassFileReader(PosInputStream pos, byte[] bytes) {
        super(pos);
        this.pos = pos;
        this.bytes = bytes;
    }

    ClassFileReader(byte[] bytes, int offset, int length) {
        this(new PosInputStream(bytes, offset, length), bytes);
    }

    ClassFileReader(Slice slice) {
        this(new PosInputStream(slice.bytes, slice.offset, slice.length), slice.bytes);
    }

    private void readHeader() throws IOException {
        int magic = readInt();
        if (magic != MAGIC) {
            throw new ClassFormatError(Integer.toHexString(magic));
        }

        int minor = readUnsignedShort();
        int major = readUnsignedShort();
        if (major != MAJOR_VERSION) {
            throw new ClassFormatError(Integer.toHexString(major));
        }
        if (minor != MINOR_VERSION) {
            throw new ClassFormatError(Integer.toHexString(minor));
        }
    }

    private ConstantPoolEntry readConstantPoolEntry() throws IOException {
        int tag = readUnsignedByte();
        switch (tag) {
            case CONSTANT_CLASS:
                return new ClassInfo(readUnsignedShort());

            case CONSTANT_FIELDREF:
                return new FieldrefInfo(readUnsignedShort(), readUnsignedShort());

            case CONSTANT_METHODREF:
                return new MethodrefInfo(readUnsignedShort(), readUnsignedShort());

            case CONSTANT_INTERFACE_METHODREF:
                return new InterfaceMethodrefInfo(readUnsignedShort(), readUnsignedShort());

            case CONSTANT_STRING:
                return new StringInfo(readUnsignedShort());

            case CONSTANT_INTEGER:
                return new IntegerInfo(readInt());

            case CONSTANT_FLOAT:
                return new FloatInfo(readInt());

            case CONSTANT_LONG:
                return new LongInfo(readInt(), readInt());

            case CONSTANT_DOUBLE:
                return new DoubleInfo(readInt(), readInt());

            case CONSTANT_NAME_AND_TYPE:
                return new NameAndTypeInfo(readUnsignedShort(), readUnsignedShort());

            case CONSTANT_UTF8:
                return new Utf8Info(readUTF());

            default:
                throw new ClassFormatError(Integer.toHexString(tag & 0xff));
        }
    }

    private ConstantPoolEntry[] readConstantpool() throws IOException {
        int constantPoolCount = readUnsignedShort();
        // constant_pool idx 0 is implied and reserved
        if (constantPoolCount == 0) {
            throw new ClassFormatError();
        }
        ConstantPoolEntry[] pool = new ConstantPoolEntry[constantPoolCount - 1];
        for (int i = 0; i < constantPoolCount - 1; i++) {
            pool[i] = readConstantPoolEntry();
        }
        return pool;
    }

    private int readAccessFlags() throws IOException {
        int accessFlags = readUnsignedShort();
        accessFlags &= ACC_PUBLIC | ACC_FINAL | ACC_SUPER | ACC_INTERFACE | ACC_ABSTRACT;
        switch (accessFlags & ~ACC_PUBLIC) {
            case ACC_FINAL | ACC_SUPER:
            case ACC_INTERFACE | ACC_ABSTRACT:
            case ACC_SUPER | ACC_ABSTRACT:
            case ACC_SUPER:
                return accessFlags;
            default:
                throw new ClassFormatError();
        }
    }

    private String readThisClass(ConstantPoolEntry[] pool) throws IOException {
        ConstantPoolEntry entry = pool[readUnsignedShort() - 1];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError();
        }
        entry.resolve(pool);
        return ((ClassInfo) entry).name;
    }

    private String readSuperClass(ConstantPoolEntry[] pool) throws IOException {
        int index = readUnsignedShort();
        if (index == 0) {
            return null;
        }
        ConstantPoolEntry entry = pool[index - 1];
        if (!(entry instanceof ClassInfo)) {
            throw new ClassFormatError();
        }
        entry.resolve(pool);
        return ((ClassInfo) entry).name;
    }

    private String[] readInterfaces(ConstantPoolEntry[] pool) throws IOException {
        int count = readUnsignedShort();
        String[] interfaces = new String[count];
        for (int i = 0; i < count; i++) {
            int index = readUnsignedShort();
            ConstantPoolEntry entry = pool[index - i];
            if (!(entry instanceof ClassInfo)) {
                throw new ClassFormatError();
            }
            entry.resolve(pool);
            interfaces[i] = ((Utf8Info) entry).string;
        }
        return interfaces;
    }

    private int readFieldAccessFlags(boolean isInterface) throws IOException {
        int accessFlags = readUnsignedShort();
        accessFlags &= ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL | ACC_VOLATILE | ACC_TRANSIENT;
        if (isInterface) {
            if (accessFlags == (ACC_PUBLIC | ACC_STATIC | ACC_FINAL)) {
                throw new ClassFormatError();
            }
            return accessFlags;
        }
        switch (accessFlags & (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED)) {
            case 0:
            case ACC_PUBLIC:
            case ACC_PRIVATE:
            case ACC_PROTECTED:
                break;

            default:
                throw new ClassFormatError();
        }
        if ((accessFlags & (ACC_FINAL | ACC_VOLATILE)) == (ACC_FINAL | ACC_VOLATILE)) {
            throw new ClassFormatError();
        }
        return accessFlags;
    }

    private Slice readSlice(int length) throws IOException {
        Slice slice = new Slice(bytes, pos.pos(), length);
        skipBytes(length);
        return slice;
    }

    private AttributeInfo readAttribute(ConstantPoolEntry[] pool) throws IOException {
        ConstantPoolEntry entry = pool[readUnsignedShort() - 1];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        String name = ((Utf8Info) entry).string;
        Slice slice = readSlice(readInt());
        return new AttributeInfo(name, slice);
    }

    private AttributeInfo[] readAttributes(ConstantPoolEntry[] pool) throws IOException {
        int count = readUnsignedShort();
        AttributeInfo[] attributes = new AttributeInfo[count];
        for (int i = 0; i < count; i++) {
            attributes[i] = readAttribute(pool);
        }
        return attributes;
    }

    private FieldInfo readField(ConstantPoolEntry[] pool, boolean isInterface) throws IOException {
        int accessFlags = readFieldAccessFlags(isInterface);

        ConstantPoolEntry entry = pool[readUnsignedShort() - 1];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        String name = ((Utf8Info) entry).string;

        entry = pool[readUnsignedShort() - 1];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        String descriptor = ((Utf8Info) entry).string;
        AttributeInfo[] attributes = readAttributes(pool);
        return new FieldInfo(accessFlags, name, descriptor, attributes);
    }

    private FieldInfo[] readFields(ConstantPoolEntry[] pool, boolean isInterface) throws IOException {
        int count = readUnsignedShort();
        FieldInfo[] fields = new FieldInfo[count];
        for (int i = 0; i < count; i++) {
            fields[i] = readField(pool, isInterface);
        }
        return fields;
    }

    private int readMethodAccessFlags(boolean isInterface) throws IOException {
        int accessFlags = readUnsignedShort();
        accessFlags &= ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL | ACC_SYNCHRONIZED | ACC_NATIVE
                | ACC_ABSTRACT;
        if (isInterface) {
            if (accessFlags != (ACC_PUBLIC | ACC_ABSTRACT)) {
                throw new ClassFormatError();
            }
            return accessFlags;
        }
        switch (accessFlags & (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED)) {
            case 0:
            case ACC_PUBLIC:
            case ACC_PRIVATE:
            case ACC_PROTECTED:
                break;
            default:
                throw new ClassFormatError(Integer.toHexString(accessFlags));
        }
        if ((accessFlags & ACC_ABSTRACT) != 0
                && (accessFlags & (ACC_PRIVATE | ACC_STATIC | ACC_FINAL | ACC_NATIVE | ACC_SYNCHRONIZED)) != 0) {
            throw new ClassFormatError(Integer.toHexString(accessFlags));
        }
        return accessFlags;
    }

    private MethodInfo readMethod(ConstantPoolEntry[] pool, boolean isInterface) throws IOException {
        int accessFlags = readMethodAccessFlags(isInterface);

        ConstantPoolEntry entry = pool[readUnsignedShort() - 1];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        String name = ((Utf8Info) entry).string;

        entry = pool[readUnsignedShort() - 1];
        if (!(entry instanceof Utf8Info)) {
            throw new ClassFormatError();
        }
        String descriptor = ((Utf8Info) entry).string;
        AttributeInfo[] attributes = readAttributes(pool);
        return new MethodInfo(accessFlags, name, descriptor, attributes);
    }

    private MethodInfo[] readMethods(ConstantPoolEntry[] pool, boolean isInterface) throws IOException {
        int count = readUnsignedShort();
        MethodInfo[] methods = new MethodInfo[count];
        for (int i = 0; i < count; i++) {
            methods[i] = readMethod(pool, isInterface);
        }
        return methods;
    }

    ClassDefinition readClassFile() throws IOException {
        readHeader();

        ConstantPoolEntry[] pool = readConstantpool();
        int accessFlags = readAccessFlags();
        boolean isInterface = (accessFlags & ACC_INTERFACE) != 0;
        String thisClass = readThisClass(pool);
        String superClass = readSuperClass(pool);

        boolean thisIsObject = "java/lang/Object".equals(thisClass);
        boolean superIsNull = superClass == null;

        if ((thisIsObject && !superIsNull) || (superIsNull && !thisIsObject)) {
            throw new ClassFormatError("thisClass: " + thisClass + " superClass: " + superClass);
        }

        String[] interfaces = readInterfaces(pool);
        FieldInfo[] fields = readFields(pool, isInterface);
        MethodInfo[] methods = readMethods(pool, isInterface);
        AttributeInfo[] attributes = readAttributes(pool);

        return new ClassDefinition(accessFlags, thisClass, superClass, interfaces, fields, methods, attributes);
    }
}
