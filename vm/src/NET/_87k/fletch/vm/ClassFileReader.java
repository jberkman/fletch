package NET._87k.fletch.vm;

class ClassFileReader {
    byte[] bytes;
    int offset;
    int length;

    ClassFileReader(byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        this.offset = offset;
        this.length = offset + length;
    }

    byte readU1() {
        if (offset >= length) {
            throw new IndexOutOfBoundsException();
        }
        return bytes[offset++];
    }

    short readU2() {
        if (offset >= length) {
            throw new IndexOutOfBoundsException();
        }
        int value = (bytes[offset++] & 0xff) << 8;
        value |= bytes[offset++] & 0xff;
        return (short) (value & 0xffff);
    }

    int readU4() {
        if (offset >= length) {
            throw new IndexOutOfBoundsException();
        }
        int value = (bytes[offset++] & 0xff) << 24;
        value |= (bytes[offset++] & 0xff) << 16;
        value |= (bytes[offset++] & 0xff) << 8;
        value |= bytes[offset++] & 0xff;
        return value;
    }

    void readConstantPoolInfo()  {
        byte tag = readU1();
        switch (tag) {
            case ClassFile.CONSTANT_CLASS:
                offset += 2; // name_index
                break;

            case ClassFile.CONSTANT_FIELDREF:
            case ClassFile.CONSTANT_METHODREF:
            case ClassFile.CONSTANT_INTERFACE_METHODREF:
                offset += 4; // class_index, name_and_type_index
                break;

            case ClassFile.CONSTANT_STRING:
                offset += 2; // string_index
                break;

            case ClassFile.CONSTANT_INTEGER:
            case ClassFile.CONSTANT_FLOAT:
                offset += 4; // bytes
                break;

            case ClassFile.CONSTANT_LONG:
            case ClassFile.CONSTANT_DOUBLE:
                offset += 8; // high_bytes, low_bytes
                break;

            case ClassFile.CONSTANT_NAME_AND_TYPE:
                offset += 4; // name_index, descriptor_index
                break;

            case ClassFile.CONSTANT_UTF8:
                int length = readU2() & 0xffff;
                offset += length;
                break;

            default:
                throw new ClassFormatError(Integer.toHexString(tag & 0xff));
        }
        if (offset > length) {
            throw new IndexOutOfBoundsException();
        }
    }

    short[] readFieldOrMethodInfo() throws ClassFormatError {
        offset += 6; // access_flags, name_index, descriptor_index
        int attributesCount = readU2() & 0xffff;
        short[] attributeOffsets = new short[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributeOffsets[i] = (short)offset;
            readAttributeInfo();
        }
        return attributeOffsets;
    }

    void readAttributeInfo() throws ClassFormatError {
        offset += 2; // attribtute_name_index
        int attributeLength = readU4();
        offset += attributeLength;
        if (offset > length) {
            throw new IndexOutOfBoundsException();
        }
    }
}
