package NET._87k.fletch.vm;

interface Opcode {
    void execute();
/*
    byte AALOAD = (byte) 0x32;
    byte AASTORE = (byte) 0x53;
    byte ACONST_NULL = (byte) 0x1;
    byte ALOAD = (byte) 0x19;
    byte ALOAD_0 = (byte) 0x2a;
    byte ALOAD_1 = (byte) 0x2b;
    byte ALOAD_2 = (byte) 0x2c;
    byte ALOAD_3 = (byte) 0x2d;
    byte ANEWARRAY = (byte) 0xbd;
    byte ARETURN = (byte) 0xb0;
    byte ARRAYLENGTH = (byte) 0xbe;
    byte ASTORE = (byte) 0x3a;
    byte ASTORE_0 = (byte) 0x4b;
    byte ASTORE_1 = (byte) 0x4c;
    byte ASTORE_2 = (byte) 0x4d;
    byte ASTORE_3 = (byte) 0x4e;
    byte ATHROW = (byte) 0xbf;

    byte BALOAD = (byte) 0x33;
    byte BASTORE = (byte) 0x54;
    byte BIPUSH = (byte) 0x10;

    byte CALOAD = (byte) 0x34;
    byte CASTORE = (byte) 0x55;

    byte CHECKCAST = (byte) 0xc0;

    byte D2F = (byte) 0x90;
    byte D2I = (byte) 0x83;
    byte D2L = (byte) 0x8f;
    byte DADD = (byte) 0x63;
    byte DALOAD = (byte) 0x31;
    byte DASTORE = (byte) 0x52;
    byte DCMPG = (byte) 0x98;
    byte DCMPL = (byte) 0x97;
    byte DCONST_0 = (byte) 0xe;
    byte DCONST_1 = (byte) 0xf;
    byte DDIV = (byte) 0x6f;
    byte DLOAD = (byte) 0x18;
    byte DLOAD_0 = (byte) 0x26;
    byte DLOAD_1 = (byte) 0x27;
    byte DLOAD_2 = (byte) 0x28;
    byte DLOAD_3 = (byte) 0x29;
    byte DMUL = (byte) 0x6b;
    byte DNEG = (byte) 0x77;
    byte DREM = (byte) 0x73;
    byte DRETURN = (byte) 0xaf;
    byte DSTORE = (byte) 0x39;
    byte DSTORE_0 = (byte) 0x47;
    byte DSTORE_1 = (byte) 0x48;
    byte DSTORE_2 = (byte) 0x49;
    byte DSTORE_3 = (byte) 0x4a;
    byte DSUB = (byte) 0x67;

    byte DUP = (byte) 0x59;
    byte DUP_X1 = (byte) 0x5a;
    byte DUP_X2 = (byte) 0x5b;
    byte DUP2 = (byte) 0x5c;
    byte DUP2_X1 = (byte) 0x5d;
    byte DUP2_X2 = (byte) 0x5e;

    byte F2D = (byte) 0x8d;
    byte F2I = (byte) 0x8b;
    byte F2L = (byte) 0x8c;
    byte FADD = (byte) 0x62;
    byte FALOAD = (byte) 0x30;
    byte FASTORE = (byte) 0x51;
    byte FCMPG = (byte) 0x96;
    byte FCMPL = (byte) 0x95;
    byte FCONST_0 = (byte) 0xb;
    byte FCONST_1 = (byte) 0xc;
    byte FCONST_2 = (byte) 0xd;
    byte FDIV = (byte) 0x6e;
    byte FLOAD = (byte) 0x17;
    byte FLOAD_0 = (byte) 0x22;
    byte FLOAD_1 = (byte) 0x23;
    byte FLOAD_2 = (byte) 0x24;
    byte FLOAD_3 = (byte) 0x25;
    byte FMUL = (byte) 0x6a;
    byte FNEG = (byte) 0x76;
    byte FREM = (byte) 0x72;
    byte FRETURN = (byte) 0xae;
    byte FSTORE = (byte) 0x38;
    byte FSTORE_0 = (byte) 0x43;
    byte FSTORE_1 = (byte) 0x44;
    byte FSTORE_2 = (byte) 0x45;
    byte FSTORE_3 = (byte) 0x46;
    byte FSUB = (byte) 0x66;

    byte GETFIELD = (byte) 0xb4;
    byte GETSTATIC = (byte) 0xb2;

    byte GOTO = (byte) 0xa7;
    byte GOTO_W = (byte) 0xc8;

    byte I2B = (byte) 0x91;
    byte I2C = (byte) 0x92;
    byte I2D = (byte) 0x87;
    byte I2F = (byte) 0x86;
    byte I2L = (byte) 0x85;
    byte I2S = (byte) 0x93;
    byte IADD = (byte) 0x60;
    byte IALOAD = (byte) 0x2e;
    byte IAND = (byte) 0x7e;
    byte IASTORE = (byte) 0x4f;
    byte ICONST_M1 = (byte) 0x2;
    byte ICONST_0 = (byte) 0x3;
    byte ICONST_1 = (byte) 0x4;
    byte ICONST_2 = (byte) 0x5;
    byte ICONST_3 = (byte) 0x6;
    byte ICONST_4 = (byte) 0x7;
    byte ICONST_5 = (byte) 0x8;
    byte IDIV = (byte) 0x6c;
    
    byte IF_ACMPEQ = (byte) 0xa5;
    byte IF_ACMPNE = (byte) 0xa6;
    byte IF_ICMPEQ = (byte) 0x9f;
    byte IF_ICMPNE = (byte) 0xa0;
    byte IF_ICMPLT = (byte) 0xa1;
    byte IF_ICMPGE = (byte) 0xa2;
    byte IF_ICMPGT = (byte) 0xa3;
    byte IF_ICMPLE = (byte) 0xa4;
    byte IFEQ = (byte) 0x99;
    byte IFNE = (byte) 0x9a;
    byte IFLT = (byte) 0x9b;
    byte IFGE = (byte) 0x9c;
    byte IFGT = (byte) 0x9d;
    byte IFLE = (byte) 0x9e;
    byte IFNONNULL = (byte) 0xc7;
    byte IFNULL = (byte) 0xc6;
    
    byte IINC = (byte) 0x84;
    byte ILOAD = (byte) 0x15;
    byte ILOAD_0 = (byte) 0x1a;
    byte ILOAD_1 = (byte) 0x1b;
    byte ILOAD_2 = (byte) 0x1c;
    byte ILOAD_3 = (byte) 0x1d;
    byte IMUL = (byte) 0x68;
    byte INEG = (byte) 0x74;

    byte INSTANCEOF = (byte) 0xc1;
    byte INVOKEINTERFACE = (byte) 0xb9;
    byte INVOKESPECIAL = (byte) 0xb7;
    byte INVOKESTATIC = (byte) 0xb8;
    byte INVOKEVIRTUAL = (byte) 0xb6;

    byte IOR = (byte) 0x80;
    byte IREM = (byte) 0x70;
    byte IRETURN = (byte) 0xac;
    byte ISHL = (byte) 0x78;
    byte ISR = (byte) 0x7a;
    byte ISTORE = (byte) 0x36;
    byte ISTORE_0 = (byte) 0x3b;
    byte ISTORE_1 = (byte) 0x3c;
    byte ISTORE_2 = (byte) 0x3d;
    byte ISTORE_3 = (byte) 0x3e;
    byte ISUB = (byte) 0x64;
    byte IUSHR = (byte) 0x7c;
    byte IXOR = (byte) 0x82;

    byte JSR = (byte) 0xa8;
    byte JSR_W = (byte) 0xc9;

    byte L2D = (byte) 0x8a;
    byte L2F = (byte) 0x89;
    byte L2I = (byte) 0x88;
    byte LADD = (byte) 0x61;
    byte LALOAD = (byte) 0x2f;
    byte LAND = (byte) 0x7f;
    byte LASTORE = (byte) 0x50;
    byte LCMP = (byte) 0x94;
    byte LCONST_0 = (byte) 0x9;
    byte LCONST_1 = (byte) 0xa;
    
    byte LDC = (byte) 0x12;
    byte LDC_W = (byte) 0x13;
    byte LDC2_W = (byte) 0x14;

    byte LDIV = (byte) 0x6d;
    byte LLOAD = (byte) 0x16;
    byte LLOAD_0 = (byte) 0x1e;
    byte LLOAD_1 = (byte) 0x1f;
    byte LLOAD_2 = (byte) 0x20;
    byte LLOAD_3 = (byte) 0x21;
    byte LMUL = (byte) 0x69;
    byte LNEG = (byte) 0x75;

    byte LOOKUPSWITCH = (byte) 0xab;

    byte LOR = (byte) 0x81;
    byte LREM = (byte) 0x71;
    byte LRETURN = (byte) 0xad;
    byte LSHL = (byte) 0x79;
    byte LSHR = (byte) 0x7b;
    byte LSTORE = (byte) 0x37;
    byte LSTORE_0 = (byte) 0x3f;
    byte LSTORE_1 = (byte) 0x40;
    byte LSTORE_2 = (byte) 0x41;
    byte LSTORE_3 = (byte) 0x42;
    byte LSUB = (byte) 0x65;
    byte LUSHR = (byte) 0x7d;
    byte LXOR = (byte) 0x83;

    byte MONITORENTER = (byte) 0xc2;
    byte MONITOREXIT = (byte) 0xc3;

    byte MULTIANEWARRAY = (byte) 0xc5;

    byte NEW = (byte) 0xbb;
    byte NEWARRAY = (byte) 0xbc;

    byte NOP = (byte) 0x0;

    byte POP = (byte) 0x57;
    byte POP2 = (byte) 0x58;

    byte PUTFIELD = (byte) 0xb5;
    byte PUTSTATIC = (byte) 0xb3;

    byte RET = (byte) 0xa9;
    byte RETURN = (byte) 0xb1;

    byte SALOAD = (byte) 0x35;
    byte SASTORE = (byte) 0x56;
    byte SIPUSH = (byte) 0x11;

    byte SWAP = (byte) 0x5f;

    byte TABLESWITCH = (byte) 0xaa;

    byte WIDE = (byte) 0xc4;
*/
}
