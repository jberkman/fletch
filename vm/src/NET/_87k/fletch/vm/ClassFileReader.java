package NET._87k.fletch.vm;

import java.io.DataInputStream;
import java.io.IOException;

class ClassFileReader extends DataInputStream {
    private PosInputStream pos;

    private ClassFileReader(PosInputStream pos) {
        super(pos);
        this.pos = pos;
    }

    ClassFileReader(byte[] bytes, int offset, int length) {
        this(new PosInputStream(bytes, offset, length));
    }

    int offset() {
        return pos.pos();
    }

    void readConstantPoolInfo() throws IOException {
        int tag = readUnsignedByte();
        switch (tag) {
            case ClassFile.CONSTANT_CLASS:
                skipBytes(2); // name_index
                break;

            case ClassFile.CONSTANT_FIELDREF:
            case ClassFile.CONSTANT_METHODREF:
            case ClassFile.CONSTANT_INTERFACE_METHODREF:
                skipBytes(4); // class_index, name_and_type_index
                break;

            case ClassFile.CONSTANT_STRING:
                skipBytes(2); // string_index
                break;

            case ClassFile.CONSTANT_INTEGER:
            case ClassFile.CONSTANT_FLOAT:
                skipBytes(4); // bytes
                break;

            case ClassFile.CONSTANT_LONG:
            case ClassFile.CONSTANT_DOUBLE:
                skipBytes(8); // high_bytes, low_bytes
                break;

            case ClassFile.CONSTANT_NAME_AND_TYPE:
                skipBytes(4); // name_index, descriptor_index
                break;

            case ClassFile.CONSTANT_UTF8:
                int length = readUnsignedShort();
                skipBytes(length);
                break;

            default:
                throw new ClassFormatError(Integer.toHexString(tag & 0xff));
        }
    }

    short[] readFieldOrMethodInfo() throws IOException {
        skipBytes(6); // access_flags, name_index, descriptor_index
        int attributesCount = readUnsignedShort();
        short[] attributeOffsets = new short[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributeOffsets[i] = (short)offset();
            readAttributeInfo();
        }
        return attributeOffsets;
    }

    void readAttributeInfo() throws IOException {
        skipBytes(2); // attribtute_name_index
        int attributeLength = readInt();
        skipBytes(attributeLength);
    }
}
