// MIT License
//
// Copyright (c) 2025 jacob berkman
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.


package NET._87k.fletch.vm;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

final class ClassFileInputStream extends DataInputStream {
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

    private final AddressRangeInputStream in;

    private ClassFileInputStream(AddressRangeInputStream in) {
        super(in);
        this.in = in;
    }

    ClassFileInputStream(AddressRange addressRange) {
        this(new AddressRangeInputStream(addressRange));
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

    private FieldInfo readField(ConstantPool pool, boolean isInterface) throws IOException {
        int accessFlags = readUnsignedShort();
        if (!AccessFlags.areValidForField(accessFlags, isInterface)) {
            throw new ClassFormatError();
        }
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

    private void readFields(ConstantPool pool, boolean isInterface, Vector instanceFields, Vector staticFields)
            throws IOException {
        int count = readUnsignedShort();
        instanceFields.ensureCapacity(count);
        staticFields.ensureCapacity(count);
        for (int i = 0; i < count; i++) {
            FieldInfo fieldInfo = readField(pool, isInterface);
            if (fieldInfo.isStatic()) {
                staticFields.addElement(fieldInfo);
            } else {
                instanceFields.addElement(fieldInfo);
            }
        }
    }

    private CodeAttribute readCodeAttribute() throws IOException {
        int maxStack = readUnsignedShort();
        int maxLocals = readUnsignedShort();
        int codeLen = readInt();
        AddressRange code = new AddressRange(in.pos, codeLen);
        skipBytes(codeLen);
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
        int accessFlags = readUnsignedShort();
        if (!AccessFlags.areValidForMethod(accessFlags, isInterface)) {
            throw new ClassFormatError();
        }
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

    private void readMethods(ConstantPool pool, boolean isInterface, Vector instanceMethods, Vector staticMethods)
            throws IOException {
        int count = readUnsignedShort();
        instanceMethods.ensureCapacity(count);
        staticMethods.ensureCapacity(count);
        for (int i = 0; i < count; i++) {
            MethodInfo method = readMethod(pool, isInterface);
            if (method.isStatic()) {
                staticMethods.addElement(method);
            } else {
                instanceMethods.addElement(method);
            }
        }
    }

    private FieldInfo[] fieldVecToArray(Vector v) {
        FieldInfo[] a = new FieldInfo[v.size()];
        v.copyInto(a);
        return a;
    }

    private MethodInfo[] methodVecToArray(Vector v) {
        MethodInfo[] a = new MethodInfo[v.size()];
        v.copyInto(a);
        return a;
    }

    ClassDefinition readClassFile() throws IOException {
        readHeader();
        ConstantPool pool = readConstantpool();

        int accessFlags = readUnsignedShort();
        if (!AccessFlags.areValidForClass(accessFlags)) {
            throw new ClassFormatError();
        }
        boolean isInterface = (accessFlags & AccessFlags.ACC_INTERFACE) != 0;

        String thisClass = readThisClass(pool);
        String superClass = readSuperClass(pool);
        String[] interfaces = readInterfaces(pool);

        Vector instanceFields = new Vector();
        Vector staticFields = new Vector();
        readFields(pool, isInterface, instanceFields, staticFields);

        Vector instanceMethods = new Vector();
        Vector staticMethods = new Vector();
        readMethods(pool, isInterface, instanceMethods, staticMethods);

        // Skip attributes
        int count = readUnsignedShort();
        for (int i = 0; i < count; i++) {
            skipBytes(2); // name
            skipBytes(readInt());
        }

        if (available() > 0) {
            throw new ClassFormatError();
        }

        return new ClassDefinition(accessFlags, pool, thisClass, superClass, interfaces,
                fieldVecToArray(instanceFields), fieldVecToArray(staticFields), methodVecToArray(instanceMethods),
                methodVecToArray(staticMethods));
    }
}
