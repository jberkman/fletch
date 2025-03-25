package NET._87k.fletch.vm;

final class ClassFile {
    final static int MAGIC = 0xcafebabe;
    final static short MAJOR_VERSION = 45;
    final static short MINOR_VERSION = 3;

    final static byte CONSTANT_CLASS = 7;
    final static byte CONSTANT_FIELDREF = 9;
    final static byte CONSTANT_METHODREF = 10;
    final static byte CONSTANT_INTERFACE_METHODREF = 11;
    final static byte CONSTANT_STRING = 8;
    final static byte CONSTANT_INTEGER = 3;
    final static byte CONSTANT_FLOAT = 4;
    final static byte CONSTANT_LONG = 5;
    final static byte CONSTANT_DOUBLE = 6;
    final static byte CONSTANT_NAME_AND_TYPE = 12;
    final static byte CONSTANT_UTF8 = 1;

    final static short ACC_PUBLIC = 0x1;
    final static short ACC_PRIVATE = 0x2;
    final static short ACC_PROTECTED = 0x4;
    final static short ACC_STATIC = 0x8;

    final static short ACC_FINAL = 0x10;
    final static short ACC_SUPER = 0x20;
    final static short ACC_SYNCHRONIZED = 0x20;
    final static short ACC_VOLATILE = 0x40;
    final static short ACC_TRANSIENT = 0x80;

    final static short ACC_NATIVE = 0x100;
    final static short ACC_INTERFACE = 0x200;
    final static short ACC_ABSTRACT = 0x400;

    byte[] bytes;
    short[] constantPoolOffsets;
    short accessFlagsOffset;
    short[] interfaceOffsets;
    short[] fieldOffsets;
    short[][] fieldAttributeOffsets;
    short[] methodOffsets;
    short[][] methodAttributeOffsets;
    short[] attributeOffsets;

    ClassFile(byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        ClassFileReader reader = new ClassFileReader(bytes, offset, length);

        int magic = reader.readU4();
        if (magic != MAGIC) {
            throw new ClassFormatError(Integer.toHexString(magic & 0xffff));
        }

        short minor = reader.readU2();
        short major = reader.readU2();
        if (major != MAJOR_VERSION) {
            throw new ClassFormatError(Integer.toHexString(major & 0xffff));
        }
        if (minor != MINOR_VERSION) {
            throw new ClassFormatError(Integer.toHexString(minor & 0xffff));
        }

        int constantPoolCount = reader.readU2() & 0xffff;
        // constant_pool idx 0 is implied and reserved
        if (constantPoolCount == 0) {
            throw new ClassFormatError();
        }
        constantPoolOffsets = new short[constantPoolCount - 1];
        for (int i = 0; i < constantPoolCount - 1; i++) {
            constantPoolOffsets[i] = (short) reader.offset;
            reader.readConstantPoolInfo();
        }

        accessFlagsOffset = (short) reader.offset;
        // access_flags, this_class, and super_class
        reader.offset += 6;

        int interfacesCount = reader.readU2() & 0xffff;
        interfaceOffsets = new short[interfacesCount];
        for (int i = 0; i < interfacesCount; i++) {
            interfaceOffsets[i] = (short) reader.offset;
            reader.offset += 3; // tag, name_index
        }

        int fieldsCount = reader.readU2() & 0xffff;
        fieldOffsets = new short[fieldsCount];
        fieldAttributeOffsets = new short[fieldsCount][];
        for (int i = 0; i < fieldsCount; i++) {
            fieldOffsets[i] = (short) reader.offset;
            fieldAttributeOffsets[i] = reader.readFieldOrMethodInfo();
        }

        int methodsCount = reader.readU2() & 0xffff;
        methodOffsets = new short[methodsCount];
        methodAttributeOffsets = new short[methodsCount][];
        for (int i = 0; i < methodsCount; i++) {
            methodOffsets[i] = (short) reader.offset;
            methodAttributeOffsets[i] = reader.readFieldOrMethodInfo();
        }

        int attributesCount = reader.readU2() & 0xffff;
        attributeOffsets = new short[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributeOffsets[i] = (short) reader.offset;
            reader.readAttributeInfo();
        }
    }

    private short getU2(int offset) {
        return (short) (((bytes[offset] & 0xff) << 8) | bytes[offset + 1] & 0xff);
    }

    private int getU4(int offset) {
        return ((bytes[offset] & 0xff) << 24) |
                ((bytes[offset + 1] & 0xff) << 16) |
                ((bytes[offset + 2] & 0xff) << 8) |
                (bytes[offset + 3] & 0xff);
    }

    private ClassFileReader getConstantPoolInfoReader(int index) {
        if (index == 0) {
            throw new ClassFormatError();
        } else if (index - 1 < constantPoolOffsets.length) {
            return new ClassFileReader(bytes, constantPoolOffsets[index - 1], bytes.length);
        } else {
            throw new ClassFormatError(Integer.toString(index));
        }
    }

    private void validateConstantPoolInfo(int index) {
        ClassFileReader reader = getConstantPoolInfoReader(index);
        byte tag = reader.readU1();
        switch (tag) {
            case CONSTANT_CLASS: {
                short nameIndex = reader.readU2();
                ClassFileReader nameReader = getConstantPoolInfoReader(nameIndex & 0xffff);
                byte nameTag = nameReader.readU1();
                if (nameTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(nameTag & 0xff));
                }
                break;
            }

            case CONSTANT_INTERFACE_METHODREF:
            case CONSTANT_FIELDREF:
            case CONSTANT_METHODREF: {
                short classIndex = reader.readU2();
                ClassFileReader classReader = getConstantPoolInfoReader(classIndex & 0xffff);
                byte classTag = classReader.readU1();
                if (classTag != CONSTANT_CLASS) {
                    throw new ClassFormatError(Integer.toString(classTag & 0xff));
                }

                short nameAndTypeIndex = reader.readU2();
                ClassFileReader nameAndIndexReader = getConstantPoolInfoReader(nameAndTypeIndex & 0xffff);
                byte nameAndIndexTag = nameAndIndexReader.readU1();
                if (nameAndIndexTag != CONSTANT_NAME_AND_TYPE) {
                    throw new ClassFormatError(Integer.toString(nameAndIndexTag & 0xff));
                }
                break;
            }

            case CONSTANT_STRING: {
                short stringIndex = reader.readU2();
                ClassFileReader stringReader = getConstantPoolInfoReader(stringIndex & 0xffff);
                byte stringTag = stringReader.readU1();
                if (stringTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(stringTag & 0xff));
                }
                break;
            }

            case CONSTANT_INTEGER:
            case CONSTANT_FLOAT:
            case CONSTANT_LONG:
            case CONSTANT_DOUBLE:
                // No validation needed
                break;

            case CONSTANT_NAME_AND_TYPE: {
                short nameIndex = reader.readU2();
                ClassFileReader nameReader = getConstantPoolInfoReader(nameIndex & 0xffff);
                byte nameTag = nameReader.readU1();
                if (nameTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(nameTag & 0xff));
                }

                short descriptorIndex = reader.readU2();
                ClassFileReader descriptorReader = getConstantPoolInfoReader(descriptorIndex & 0xffff);
                byte descriptorTag = descriptorReader.readU1();
                if (descriptorTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(descriptorTag & 0xff));
                }
                break;
            }

            case CONSTANT_UTF8: {
                int length = reader.readU2() & 0xffff;
                for (int i = 0; i < length; i++) {
                    int b = reader.readU1() & 0xff;
                    if (b == 0 || b >= 0xf0) {
                        throw new ClassFormatError(Integer.toString(b & 0xff));
                    }
                }
                break;
            }
            default:
                throw new ClassFormatError(Integer.toString(tag));
        }
    }

    private static boolean areFileAccessFlagsValid(short accessFlags) {
        accessFlags &= /* ACC_PUBLIC | */ ACC_FINAL | ACC_SUPER | ACC_INTERFACE | ACC_ABSTRACT;
        switch (accessFlags) {
            case ACC_FINAL | ACC_SUPER:
            case ACC_INTERFACE | ACC_ABSTRACT:
            case ACC_SUPER | ACC_ABSTRACT:
            case ACC_SUPER:
                return true;
            default:
                return false;
        }
    }

    private static boolean areFieldAccessFlagsValid(short accessFlags, boolean isInterface) {
        accessFlags &= ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL | ACC_VOLATILE | ACC_TRANSIENT;
        if (isInterface) {
            return accessFlags == (ACC_PUBLIC | ACC_STATIC | ACC_FINAL);
        }
        switch (accessFlags & (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED)) {
            case 0:
            case ACC_PUBLIC:
            case ACC_PRIVATE:
            case ACC_PROTECTED:
                break;
            default:
                return false;
        }
        return (accessFlags & (ACC_FINAL | ACC_VOLATILE)) != (ACC_FINAL | ACC_VOLATILE);
    }

    private static boolean areMethodAccessFlagsValid(short accessFlags, boolean isInterface) {
        accessFlags &= ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL | ACC_SYNCHRONIZED | ACC_NATIVE
                | ACC_ABSTRACT;
        if (isInterface) {
            return accessFlags == (ACC_PUBLIC | ACC_ABSTRACT);
        }
        switch (accessFlags & (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED)) {
            case 0:
            case ACC_PUBLIC:
            case ACC_PRIVATE:
            case ACC_PROTECTED:
                break;
            default:
                return false;
        }
        return (accessFlags & ACC_ABSTRACT) == 0
                || (accessFlags & (ACC_PRIVATE | ACC_STATIC | ACC_FINAL | ACC_NATIVE | ACC_SYNCHRONIZED)) == 0;
    }

    private void validateField(int index, boolean isInterface) {
        ClassFileReader reader = new ClassFileReader(bytes, fieldOffsets[index], bytes.length);

        short accessFlags = reader.readU2();
        if (!areFieldAccessFlagsValid(accessFlags, isInterface)) {
            throw new ClassFormatError(Integer.toHexString(accessFlags & 0xffff));
        }

        short nameIndex = reader.readU2();
        ClassFileReader nameReader = getConstantPoolInfoReader(nameIndex);
        byte nameTag = nameReader.readU1();
        if (nameTag != CONSTANT_UTF8) {
            throw new ClassFormatError(Integer.toString(nameTag & 0xff));
        }

        short descriptorIndex = reader.readU2();
        ClassFileReader descriptorReader = getConstantPoolInfoReader(descriptorIndex & 0xffff);
        byte descriptorTag = descriptorReader.readU1();
        if (descriptorTag != CONSTANT_UTF8) {
            throw new ClassFormatError(Integer.toString(descriptorTag & 0xff));
        }

        // just ignore attributes for now
    }

    private void validateMethod(int index, boolean isInterface) {
        ClassFileReader reader = new ClassFileReader(bytes, methodOffsets[index], bytes.length);

        short accessFlags = reader.readU2();
        if (!areMethodAccessFlagsValid(accessFlags, isInterface)) {
            throw new ClassFormatError(Integer.toHexString(accessFlags & 0xffff));
        }

        short nameIndex = reader.readU2();
        ClassFileReader nameReader = getConstantPoolInfoReader(nameIndex);
        byte nameTag = nameReader.readU1();
        if (nameTag != CONSTANT_UTF8) {
            throw new ClassFormatError(Integer.toString(nameTag & 0xff));
        }

        short descriptorIndex = reader.readU2();
        ClassFileReader descriptorReader = getConstantPoolInfoReader(descriptorIndex & 0xffff);
        byte descriptorTag = descriptorReader.readU1();
        if (descriptorTag != CONSTANT_UTF8) {
            throw new ClassFormatError(Integer.toString(descriptorTag & 0xff));
        }

        // just ignore attributes for now
    }

    void validate() {
        // Magic, major, and minor are validated in ClassFile()

        // Index 0 is implied
        for (int i = 1; i < constantPoolOffsets.length + 1; i++) {
            validateConstantPoolInfo(i);
        }

        short accessFlags = getU2(accessFlagsOffset);
        if (!areFileAccessFlagsValid(accessFlags)) {
            throw new ClassFormatError(Integer.toHexString(accessFlags & 0xffff));
        }

        short thisClass = getU2(accessFlagsOffset + 2);
        ClassFileReader thisReader = getConstantPoolInfoReader(thisClass & 0xffff);
        byte thisTag = thisReader.readU1();
        if (thisTag != CONSTANT_CLASS) {
            throw new ClassFormatError(Integer.toString(thisTag * 0xff));
        }
        // TODO: Check that it's for this class

        short superClass = getU2(accessFlagsOffset + 4);
        // TODO: if superClass == 0, check that this is java.lang.Object
        if (superClass != 0) {
            ClassFileReader superReader = getConstantPoolInfoReader(superClass & 0xffff);
            byte superTag = superReader.readU1();
            if (superTag != CONSTANT_CLASS) {
                throw new ClassFormatError(Integer.toString(superTag & 0xff));
            }
            // TODO: Verify that no superclasses are final
            // TODO: Verify that interface super is Object
        }

        for (int i = 0; i < interfaceOffsets.length; i++) {
            short interfaceIndex = getU2(interfaceOffsets[i] & 0xffff);
            ClassFileReader interfaceReader = getConstantPoolInfoReader(interfaceIndex);
            byte interfaceTag = interfaceReader.readU1();
            if (interfaceTag != CONSTANT_CLASS) {
                throw new ClassFormatError(Integer.toString(interfaceTag & 0xff));
            }
            // TODO: Verify that type is an interface
        }

        for (int i = 0; i < fieldOffsets.length; i++) {
            validateField(i, (accessFlags & ACC_INTERFACE) != 0);
        }

        for (int i = 0; i < methodOffsets.length; i++) {
            validateMethod(i, (accessFlags & ACC_INTERFACE) != 0);
        }
    }
}
