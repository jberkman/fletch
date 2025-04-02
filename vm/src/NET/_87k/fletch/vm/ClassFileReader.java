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
    private final int length;

    private ClassFileReader(PosInputStream pos, byte[] bytes, int length) {
        super(pos);
        this.pos = pos;
        this.bytes = bytes;
        this.length = length;
    }

    ClassFileReader(byte[] bytes, int offset, int length) {
        this(new PosInputStream(bytes, offset, length), bytes, length);
    }

    ClassFileReader(Slice slice) {
        this(new PosInputStream(slice.bytes, slice.offset, slice.length), slice.bytes, slice.length);
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

    private ConstantPoolEntry readConstantPoolEntry(ConstantPool pool) throws IOException {
        int tag = readUnsignedByte();
        switch (tag) {
            case CONSTANT_CLASS:
                return new ClassInfo(pool, readUnsignedShort());

            case CONSTANT_FIELDREF:
                return new FieldrefInfo(pool, readUnsignedShort(), readUnsignedShort());

            case CONSTANT_METHODREF:
                return new MethodrefInfo(pool, readUnsignedShort(), readUnsignedShort());

            case CONSTANT_INTERFACE_METHODREF:
                return new InterfaceMethodrefInfo(pool, readUnsignedShort(), readUnsignedShort());

            case CONSTANT_STRING:
                return new StringInfo(pool, readUnsignedShort());

            case CONSTANT_INTEGER:
                return new IntegerInfo(readInt());

            case CONSTANT_FLOAT:
                return new FloatInfo(readInt());

            case CONSTANT_LONG:
                return new LongInfo(readInt(), readInt());

            case CONSTANT_DOUBLE:
                return new DoubleInfo(readInt(), readInt());

            case CONSTANT_NAME_AND_TYPE:
                return new NameAndTypeInfo(pool, readUnsignedShort(), readUnsignedShort());

            case CONSTANT_UTF8:
                return new Utf8Info(readUTF());

            default:
                throw new ClassFormatError(Integer.toHexString(tag & 0xff));
        }
    }

    private ConstantPool readConstantpool() throws IOException {
        int constantPoolCount = readUnsignedShort();
        // constant_pool idx 0 is implied and reserved
        if (constantPoolCount == 0) {
            throw new ClassFormatError();
        }
        ConstantPoolEntry[] entries = new ConstantPoolEntry[constantPoolCount - 1];
        ConstantPool pool = new ConstantPool(entries);
        for (int i = 0; i < constantPoolCount - 1; i++) {
            entries[i] = readConstantPoolEntry(pool);
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

    private String readThisClass(ConstantPool pool) throws IOException {
        return pool.className(readUnsignedShort());
    }

    private String readSuperClass(ConstantPool pool) throws IOException {
        int index = readUnsignedShort();
        if (index == 0) {
            return null;
        }
        return pool.className(index);
    }

    private String[] readInterfaces(ConstantPool pool) throws IOException {
        int count = readUnsignedShort();
        String[] interfaces = new String[count];
        for (int i = 0; i < count; i++) {
            interfaces[i] = pool.className(readUnsignedShort());
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

    private FieldInfo readField(ConstantPool pool, boolean isInterface) throws IOException {
        int accessFlags = readFieldAccessFlags(isInterface);
        String name = pool.utf8String(readUnsignedShort());
        String descriptor = pool.utf8String(readUnsignedShort());
        ConstantValueInfo value = null;
        int count = readInt();
        for (int i = 0; i < count; i++) {
            String attributeName = pool.utf8String(readUnsignedShort());
            int length = readInt();
            if (!"ConstantValue".equals(attributeName)) {
                skipBytes(length);
                continue;
            }
            if (length != 2) {
                throw new ClassFormatError();
            }
            // TODO(jcb): check against descriptor
            if (value != null) {
                throw new ClassFormatError();
            }
            value = pool.constantValue(readUnsignedShort());
        }
        return new FieldInfo(accessFlags, name, descriptor, value);
    }

    private FieldInfo[] readFields(ConstantPool pool, boolean isInterface) throws IOException {
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

    private CodeAttribute readCodeAttribute() throws IOException {
        int maxStack = readUnsignedShort();
        int maxLocals = readUnsignedShort();
        int codeLen = readInt();
        Slice code = readSlice(codeLen);
        int count = readUnsignedShort();
        ExceptionTableEntry[] exceptions = new ExceptionTableEntry[count];
        for (int i = 0; i < count; i++) {
            exceptions[i] = new ExceptionTableEntry(readUnsignedShort(), readUnsignedShort(), readUnsignedShort(),
                    readUnsignedShort());
        }

        // Ignore attributes
        count = readUnsignedShort();
        for (int i = 0; i < count; i++) {
            skipBytes(2); // nameIndex
            int len = readInt();
            skipBytes(len); // rest of attribute
        }

        return new CodeAttribute(maxStack, maxLocals, code, exceptions);
    }

    private String[] readExceptions(ConstantPool pool) throws IOException {
        int count = readUnsignedShort();
        String[] exceptions = new String[count];
        for (int i = 0; i < count; i++) {
            int index = readUnsignedShort();
            if (index == 0) {
                continue;
            }
            exceptions[i] = pool.className(index);
        }
        return exceptions;
    }

    private MethodInfo readMethod(ConstantPool pool, boolean isInterface) throws IOException {
        int accessFlags = readMethodAccessFlags(isInterface);
        String name = pool.utf8String(readUnsignedShort());
        String descriptor = pool.utf8String(readUnsignedShort());
        CodeAttribute code = null;
        String[] exceptions = null;
        int count = readUnsignedShort();
        for (int i = 0; i < count; i++) {
            String attributeName = pool.utf8String(readUnsignedShort());
            int length = readInt();
            if ("Code".equals(attributeName)) {
                if (code != null) {
                    throw new ClassFormatError();
                }
                code = readCodeAttribute();
            } else if ("Exceptions".equals(attributeName)) {
                if (exceptions != null) {
                    throw new ClassFormatError();
                }
                exceptions = readExceptions(pool);
            } else {
                skipBytes(length);
            }
        }
        return new MethodInfo(accessFlags, name, descriptor, code, exceptions);
    }

    private MethodInfo[] readMethods(ConstantPool pool, boolean isInterface) throws IOException {
        int count = readUnsignedShort();
        MethodInfo[] methods = new MethodInfo[count];
        for (int i = 0; i < count; i++) {
            methods[i] = readMethod(pool, isInterface);
        }
        return methods;
    }

    ClassDefinition readClassFile() throws IOException {
        readHeader();
        ConstantPool pool = readConstantpool();
        int accessFlags = readAccessFlags();
        boolean isInterface = (accessFlags & ACC_INTERFACE) != 0;
        String thisClass = readThisClass(pool);
        String superClass = readSuperClass(pool);
        String[] interfaces = readInterfaces(pool);
        FieldInfo[] fields = readFields(pool, isInterface);
        MethodInfo[] methods = readMethods(pool, isInterface);

        // Skip attributes
        int count = readUnsignedShort();
        for (int i = 0; i < count; i++) {
            skipBytes(2); // name
            int len = readInt();
            skipBytes(len);
        }

        if (pos.pos() != this.length) {
            throw new ClassFormatError();
        }

        return new ClassDefinition(accessFlags, thisClass, superClass, interfaces, fields, methods);
    }
}
