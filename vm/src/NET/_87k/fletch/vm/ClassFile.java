package NET._87k.fletch.vm;

import java.io.IOException;

public final class ClassFile {
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

    private final byte[] bytes;
    private final int length;
    private final short[] constantPoolOffsets;
    private final short accessFlagsOffset;
    private final short[] interfaceOffsets;
    private final short[] fieldOffsets;
    private final short[][] fieldAttributeOffsets;
    private final short[] methodOffsets;
    private final short[][] methodAttributeOffsets;
    private final short[] attributeOffsets;
    private boolean isValid;

    public ClassFile(byte[] bytes, int offset, int length) throws IOException {
        this.bytes = bytes;
        this.length = length;

        ClassFileReader reader = new ClassFileReader(bytes, offset, length);
        try {
            int magic = reader.readInt();
            if (magic != MAGIC) {
                throw new ClassFormatError(Integer.toHexString(magic));
            }

            int minor = reader.readUnsignedShort();
            int major = reader.readUnsignedShort();
            if (major != MAJOR_VERSION) {
                throw new ClassFormatError(Integer.toHexString(major));
            }
            if (minor != MINOR_VERSION) {
                throw new ClassFormatError(Integer.toHexString(minor));
            }

            int constantPoolCount = reader.readUnsignedShort();
            // constant_pool idx 0 is implied and reserved
            if (constantPoolCount == 0) {
                throw new ClassFormatError();
            }
            constantPoolOffsets = new short[constantPoolCount - 1];
            for (int i = 0; i < constantPoolCount - 1; i++) {
                constantPoolOffsets[i] = (short) reader.offset();
                reader.readConstantPoolInfo();
            }

            accessFlagsOffset = (short) reader.offset();
            // access_flags, this_class, and super_class
            reader.skip(6);

            int interfacesCount = reader.readUnsignedShort();
            interfaceOffsets = new short[interfacesCount];
            for (int i = 0; i < interfacesCount; i++) {
                interfaceOffsets[i] = (short) reader.offset();
                reader.skip(3); // tag, name_index
            }

            int fieldsCount = reader.readUnsignedShort();
            fieldOffsets = new short[fieldsCount];
            fieldAttributeOffsets = new short[fieldsCount][];
            for (int i = 0; i < fieldsCount; i++) {
                fieldOffsets[i] = (short) reader.offset();
                fieldAttributeOffsets[i] = reader.readFieldOrMethodInfo();
            }

            int methodsCount = reader.readUnsignedShort();
            methodOffsets = new short[methodsCount];
            methodAttributeOffsets = new short[methodsCount][];
            for (int i = 0; i < methodsCount; i++) {
                methodOffsets[i] = (short) reader.offset();
                methodAttributeOffsets[i] = reader.readFieldOrMethodInfo();
            }

            int attributesCount = reader.readUnsignedShort();
            attributeOffsets = new short[attributesCount];
            for (int i = 0; i < attributesCount; i++) {
                attributeOffsets[i] = (short) reader.offset();
                reader.readAttributeInfo();
            }
        } finally {
            reader.close();
        }
    }

    private short getU2(int offset) {
        return (short) (((bytes[offset] & 0xff) << 8) | bytes[offset + 1] & 0xff);
    }

    private ClassFileReader createConstantPoolInfoReader(int index) {
        if (index == 0) {
            throw new ClassFormatError();
        } else if (index - 1 < constantPoolOffsets.length) {
            return new ClassFileReader(bytes, constantPoolOffsets[index - 1], length);
        } else {
            throw new ClassFormatError(Integer.toString(index));
        }
    }

    private void validateConstantPoolInfo(int index) throws IOException {
        ClassFileReader reader = createConstantPoolInfoReader(index);
        try {
            int tag = reader.readUnsignedByte();
            switch (tag) {
                case CONSTANT_CLASS: {
                    int nameIndex = reader.readUnsignedShort();
                    ClassFileReader nameReader = createConstantPoolInfoReader(nameIndex);
                    try {
                        int nameTag = nameReader.readUnsignedByte();
                        if (nameTag != CONSTANT_UTF8) {
                            throw new ClassFormatError(Integer.toString(nameTag));
                        }
                    } finally {
                        nameReader.close();
                    }
                    break;
                }

                case CONSTANT_INTERFACE_METHODREF:
                case CONSTANT_FIELDREF:
                case CONSTANT_METHODREF: {
                    int classIndex = reader.readUnsignedShort();
                    ClassFileReader classReader = createConstantPoolInfoReader(classIndex);
                    try {
                        int classTag = classReader.readUnsignedByte();
                        if (classTag != CONSTANT_CLASS) {
                            throw new ClassFormatError(Integer.toString(classTag));
                        }
                    } finally {
                        classReader.close();
                    }

                    int nameAndTypeIndex = reader.readUnsignedShort();
                    ClassFileReader nameAndIndexReader = createConstantPoolInfoReader(nameAndTypeIndex);
                    try {
                        int nameAndIndexTag = nameAndIndexReader.readUnsignedByte();
                        if (nameAndIndexTag != CONSTANT_NAME_AND_TYPE) {
                            throw new ClassFormatError(Integer.toString(nameAndIndexTag));
                        }
                    } finally {
                        nameAndIndexReader.close();
                    }
                    break;
                }

                case CONSTANT_STRING: {
                    int stringIndex = reader.readUnsignedShort();
                    ClassFileReader stringReader = createConstantPoolInfoReader(stringIndex);
                    try {
                        int stringTag = stringReader.readUnsignedByte();
                        if (stringTag != CONSTANT_UTF8) {
                            throw new ClassFormatError(Integer.toString(stringTag));
                        }
                    } finally  {
                        stringReader.close();
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
                    int nameIndex = reader.readUnsignedShort();
                    ClassFileReader nameReader = createConstantPoolInfoReader(nameIndex);
                    try {
                        int nameTag = nameReader.readUnsignedByte();
                        if (nameTag != CONSTANT_UTF8) {
                            throw new ClassFormatError(Integer.toString(nameTag));
                        }
                    } finally {
                        nameReader.close();
                    }

                    int descriptorIndex = reader.readUnsignedShort();
                    ClassFileReader descriptorReader = createConstantPoolInfoReader(descriptorIndex);
                    try {
                        int descriptorTag = descriptorReader.readUnsignedByte();
                        if (descriptorTag != CONSTANT_UTF8) {
                            throw new ClassFormatError(Integer.toString(descriptorTag));
                        }
                    } finally {
                        descriptorReader.close();
                    }
                    break;
                }

                case CONSTANT_UTF8: {
                    int length = reader.readUnsignedShort();
                    for (int i = 0; i < length; i++) {
                        int b = reader.readUnsignedByte();
                        if (b == 0 || b >= 0xf0) {
                            throw new ClassFormatError(Integer.toString(b));
                        }
                    }
                    break;
                }
                default:
                    throw new ClassFormatError(Integer.toString(tag));
            }
        } finally {
            reader.close();
        }
    }

    private static boolean areFileAccessFlagsValid(int accessFlags) {
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

    private static boolean areFieldAccessFlagsValid(int accessFlags, boolean isInterface) {
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

    private static boolean areMethodAccessFlagsValid(int accessFlags, boolean isInterface) {
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

    private void validateField(int index, boolean isInterface) throws IOException {
        ClassFileReader reader = new ClassFileReader(bytes, fieldOffsets[index], bytes.length);
        try {
            int accessFlags = reader.readUnsignedShort();
            if (!areFieldAccessFlagsValid(accessFlags, isInterface)) {
                throw new ClassFormatError(Integer.toHexString(accessFlags));
            }

            int nameIndex = reader.readUnsignedShort();
            ClassFileReader nameReader = createConstantPoolInfoReader(nameIndex);
            try {
                int nameTag = nameReader.readUnsignedByte();
                if (nameTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(nameTag));
                }
            } finally {
                nameReader.close();
            }

            int descriptorIndex = reader.readUnsignedShort();
            ClassFileReader descriptorReader = createConstantPoolInfoReader(descriptorIndex);
            try {
                int descriptorTag = descriptorReader.readUnsignedByte();
                if (descriptorTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(descriptorTag));
                }
            } finally {
                descriptorReader.close();
            }

            // just ignore attributes for now
        } finally {
            reader.close();
        }
    }

    private void validateMethod(int index, boolean isInterface) throws IOException {
        ClassFileReader reader = new ClassFileReader(bytes, methodOffsets[index], bytes.length);
        try {
            int accessFlags = reader.readUnsignedShort();
            if (!areMethodAccessFlagsValid(accessFlags, isInterface)) {
                throw new ClassFormatError(Integer.toHexString(accessFlags));
            }

            int nameIndex = reader.readUnsignedShort();
            ClassFileReader nameReader = createConstantPoolInfoReader(nameIndex);
            try {
                int nameTag = nameReader.readUnsignedByte();
                if (nameTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(nameTag));
                }
            } finally {
                nameReader.close();
            }

            int descriptorIndex = reader.readUnsignedShort();
            ClassFileReader descriptorReader = createConstantPoolInfoReader(descriptorIndex);
            try {
                int descriptorTag = descriptorReader.readUnsignedByte();
                if (descriptorTag != CONSTANT_UTF8) {
                    throw new ClassFormatError(Integer.toString(descriptorTag));
                }
            } finally {
                descriptorReader.close();
            }

            // just ignore attributes for now
        } finally {
            reader.close();
        }
    }

    public void validate() throws IOException {
        // Magic, major, and minor are validated in ClassFile()

        // Index 0 is implied
        for (int i = 1; i < constantPoolOffsets.length + 1; i++) {
            validateConstantPoolInfo(i);
        }

        short accessFlags = getU2(accessFlagsOffset);
        if (!areFileAccessFlagsValid(accessFlags)) {
            throw new ClassFormatError(Integer.toHexString(accessFlags));
        }

        short thisClass = getU2(accessFlagsOffset + 2);
        ClassFileReader thisReader = createConstantPoolInfoReader(thisClass);
        int thisTag = thisReader.readUnsignedByte();
        thisReader.close();
        if (thisTag != CONSTANT_CLASS) {
            throw new ClassFormatError(Integer.toString(thisTag * 0xff));
        }
        // TODO: Check that it's for this class

        short superClass = getU2(accessFlagsOffset + 4);
        // TODO: if superClass == 0, check that this is java.lang.Object
        if (superClass != 0) {
            ClassFileReader superReader = createConstantPoolInfoReader(superClass);
            int superTag = superReader.readUnsignedByte();
            superReader.close();
            if (superTag != CONSTANT_CLASS) {
                throw new ClassFormatError(Integer.toString(superTag));
            }
            // TODO: Verify that no superclasses are final
            // TODO: Verify that interface super is Object
        }

        for (int i = 0; i < interfaceOffsets.length; i++) {
            short interfaceIndex = getU2(interfaceOffsets[i]);
            ClassFileReader interfaceReader = createConstantPoolInfoReader(interfaceIndex);
            int interfaceTag = interfaceReader.readUnsignedByte();
            interfaceReader.close();
            if (interfaceTag != CONSTANT_CLASS) {
                throw new ClassFormatError(Integer.toString(interfaceTag));
            }
            // TODO: Verify that type is an interface
        }

        for (int i = 0; i < fieldOffsets.length; i++) {
            validateField(i, (accessFlags & ACC_INTERFACE) != 0);
        }

        for (int i = 0; i < methodOffsets.length; i++) {
            validateMethod(i, (accessFlags & ACC_INTERFACE) != 0);
        }

        isValid = true;
    }

    public String thisClass() throws IOException {
        int index = getU2(accessFlagsOffset + 2) & 0xffff;
        ClassFileReader reader = createConstantPoolInfoReader(index);
        try {
            int tag = reader.readUnsignedByte();
            if (tag != CONSTANT_CLASS) {
                throw new IllegalStateException(Integer.toString(tag));
            }
            index = reader.readUnsignedShort();
        } finally {
            reader.close();
        }

        reader = createConstantPoolInfoReader(index);
        try {
            int tag = reader.readUnsignedByte();
            if (tag != CONSTANT_UTF8) {
                throw new IllegalStateException(Integer.toString(tag));
            }
            return reader.readUTF();
        } finally {
            reader.close();
        }
    }

    public String superClass() throws IOException {
        int index = getU2(accessFlagsOffset + 4);
        if (index == 0) {
            return null;
        }

        ClassFileReader reader = createConstantPoolInfoReader(index);
        try {
            int tag = reader.readUnsignedByte();
            if (tag != CONSTANT_CLASS) {
                throw new IllegalStateException(Integer.toString(tag));
            }
            index = reader.readUnsignedShort();
        } finally {
            reader.close();
        }

        reader = createConstantPoolInfoReader(index);
        try {
            int tag = reader.readUnsignedByte();
            if (tag != CONSTANT_UTF8) {
                throw new IllegalStateException(Integer.toString(tag));
            }
            return reader.readUTF();
        } finally {
            reader.close();
        }
    }
}
