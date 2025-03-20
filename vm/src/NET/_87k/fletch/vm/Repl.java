package NET._87k.fletch.vm;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Repl {

    private static Dictionary getOpCodeLookupTable() {
        Hashtable lut = new Hashtable();
        lut.put("AALOAD", new Integer((int)OpCode.AALOAD));
        lut.put("AASTORE", new Integer((int)OpCode.AASTORE));
        lut.put("ACONST_NULL", new Integer((int)OpCode.ACONST_NULL));
        lut.put("ALOAD", new Integer((int)OpCode.ALOAD));
        lut.put("ALOAD_0", new Integer((int)OpCode.ALOAD_0));
        lut.put("ALOAD_1", new Integer((int)OpCode.ALOAD_1));
        lut.put("ALOAD_2", new Integer((int)OpCode.ALOAD_2));
        lut.put("ALOAD_3", new Integer((int)OpCode.ALOAD_3));
        lut.put("ANEWARRAY", new Integer((int)OpCode.ANEWARRAY));
        lut.put("ARETURN", new Integer((int)OpCode.ARETURN));
        lut.put("ARRAYLENGTH", new Integer((int)OpCode.ARRAYLENGTH));
        lut.put("ASTORE", new Integer((int)OpCode.ASTORE));
        lut.put("ASTORE_0", new Integer((int)OpCode.ASTORE_0));
        lut.put("ASTORE_1", new Integer((int)OpCode.ASTORE_1));
        lut.put("ASTORE_2", new Integer((int)OpCode.ASTORE_2));
        lut.put("ASTORE_3", new Integer((int)OpCode.ASTORE_3));
        lut.put("ATHROW", new Integer((int)OpCode.ATHROW));
        lut.put("BALOAD", new Integer((int)OpCode.BALOAD));
        lut.put("BASTORE", new Integer((int)OpCode.BASTORE));
        lut.put("BIPUSH", new Integer((int)OpCode.BIPUSH));
        lut.put("CALOAD", new Integer((int)OpCode.CALOAD));
        lut.put("CASTORE", new Integer((int)OpCode.CASTORE));
        lut.put("CHECKCAST", new Integer((int)OpCode.CHECKCAST));
        lut.put("D2F", new Integer((int)OpCode.D2F));
        lut.put("D2I", new Integer((int)OpCode.D2I));
        lut.put("D2L", new Integer((int)OpCode.D2L));
        lut.put("DADD", new Integer((int)OpCode.DADD));
        lut.put("DALOAD", new Integer((int)OpCode.DALOAD));
        lut.put("DASTORE", new Integer((int)OpCode.DASTORE));
        lut.put("DCMPG", new Integer((int)OpCode.DCMPG));
        lut.put("DCMPL", new Integer((int)OpCode.DCMPL));
        lut.put("DCONST_0", new Integer((int)OpCode.DCONST_0));
        lut.put("DCONST_1", new Integer((int)OpCode.DCONST_1));
        lut.put("DDIV", new Integer((int)OpCode.DDIV));
        lut.put("DLOAD", new Integer((int)OpCode.DLOAD));
        lut.put("DLOAD_0", new Integer((int)OpCode.DLOAD_0));
        lut.put("DLOAD_1", new Integer((int)OpCode.DLOAD_1));
        lut.put("DLOAD_2", new Integer((int)OpCode.DLOAD_2));
        lut.put("DLOAD_3", new Integer((int)OpCode.DLOAD_3));
        lut.put("DMUL", new Integer((int)OpCode.DMUL));
        lut.put("DNEG", new Integer((int)OpCode.DNEG));
        lut.put("DREM", new Integer((int)OpCode.DREM));
        lut.put("DRETURN", new Integer((int)OpCode.DRETURN));
        lut.put("DSTORE", new Integer((int)OpCode.DSTORE));
        lut.put("DSTORE_0", new Integer((int)OpCode.DSTORE_0));
        lut.put("DSTORE_1", new Integer((int)OpCode.DSTORE_1));
        lut.put("DSTORE_2", new Integer((int)OpCode.DSTORE_2));
        lut.put("DSTORE_3", new Integer((int)OpCode.DSTORE_3));
        lut.put("DSUB", new Integer((int)OpCode.DSUB));
        lut.put("DUP", new Integer((int)OpCode.DUP));
        lut.put("DUP_X1", new Integer((int)OpCode.DUP_X1));
        lut.put("DUP_X2", new Integer((int)OpCode.DUP_X2));
        lut.put("DUP2", new Integer((int)OpCode.DUP2));
        lut.put("DUP2_X1", new Integer((int)OpCode.DUP2_X1));
        lut.put("DUP2_X2", new Integer((int)OpCode.DUP2_X2));
        lut.put("F2D", new Integer((int)OpCode.F2D));
        lut.put("F2I", new Integer((int)OpCode.F2I));
        lut.put("F2L", new Integer((int)OpCode.F2L));
        lut.put("FADD", new Integer((int)OpCode.FADD));
        lut.put("FALOAD", new Integer((int)OpCode.FALOAD));
        lut.put("FASTORE", new Integer((int)OpCode.FASTORE));
        lut.put("FCMPG", new Integer((int)OpCode.FCMPG));
        lut.put("FCMPL", new Integer((int)OpCode.FCMPL));
        lut.put("FCONST_0", new Integer((int)OpCode.FCONST_0));
        lut.put("FCONST_1", new Integer((int)OpCode.FCONST_1));
        lut.put("FCONST_2", new Integer((int)OpCode.FCONST_2));
        lut.put("FDIV", new Integer((int)OpCode.FDIV));
        lut.put("FLOAD", new Integer((int)OpCode.FLOAD));
        lut.put("FLOAD_0", new Integer((int)OpCode.FLOAD_0));
        lut.put("FLOAD_1", new Integer((int)OpCode.FLOAD_1));
        lut.put("FLOAD_2", new Integer((int)OpCode.FLOAD_2));
        lut.put("FLOAD_3", new Integer((int)OpCode.FLOAD_3));
        lut.put("FMUL", new Integer((int)OpCode.FMUL));
        lut.put("FNEG", new Integer((int)OpCode.FNEG));
        lut.put("FREM", new Integer((int)OpCode.FREM));
        lut.put("FRETURN", new Integer((int)OpCode.FRETURN));
        lut.put("FSTORE", new Integer((int)OpCode.FSTORE));
        lut.put("FSTORE_0", new Integer((int)OpCode.FSTORE_0));
        lut.put("FSTORE_1", new Integer((int)OpCode.FSTORE_1));
        lut.put("FSTORE_2", new Integer((int)OpCode.FSTORE_2));
        lut.put("FSTORE_3", new Integer((int)OpCode.FSTORE_3));
        lut.put("FSUB", new Integer((int)OpCode.FSUB));
        lut.put("GETFIELD", new Integer((int)OpCode.GETFIELD));
        lut.put("GETSTATIC", new Integer((int)OpCode.GETSTATIC));
        lut.put("GOTO", new Integer((int)OpCode.GOTO));
        lut.put("GOTO_W", new Integer((int)OpCode.GOTO_W));
        lut.put("I2B", new Integer((int)OpCode.I2B));
        lut.put("I2C", new Integer((int)OpCode.I2C));
        lut.put("I2D", new Integer((int)OpCode.I2D));
        lut.put("I2F", new Integer((int)OpCode.I2F));
        lut.put("I2L", new Integer((int)OpCode.I2L));
        lut.put("I2S", new Integer((int)OpCode.I2S));
        lut.put("IADD", new Integer((int)OpCode.IADD));
        lut.put("IALOAD", new Integer((int)OpCode.IALOAD));
        lut.put("IAND", new Integer((int)OpCode.IAND));
        lut.put("IASTORE", new Integer((int)OpCode.IASTORE));
        lut.put("ICONST_0", new Integer((int)OpCode.ICONST_0));
        lut.put("ICONST_1", new Integer((int)OpCode.ICONST_1));
        lut.put("ICONST_2", new Integer((int)OpCode.ICONST_2));
        lut.put("ICONST_3", new Integer((int)OpCode.ICONST_3));
        lut.put("ICONST_4", new Integer((int)OpCode.ICONST_4));
        lut.put("ICONST_5", new Integer((int)OpCode.ICONST_5));
        lut.put("ICONST_M1", new Integer((int)OpCode.ICONST_M1));
        lut.put("IDIV", new Integer((int)OpCode.IDIV));
        lut.put("IF_ACMPEQ", new Integer((int)OpCode.IF_ACMPEQ));
        lut.put("IF_ACMPNE", new Integer((int)OpCode.IF_ACMPNE));
        lut.put("IF_ICMPEQ", new Integer((int)OpCode.IF_ICMPEQ));
        lut.put("IF_ICMPGE", new Integer((int)OpCode.IF_ICMPGE));
        lut.put("IF_ICMPGT", new Integer((int)OpCode.IF_ICMPGT));
        lut.put("IF_ICMPLE", new Integer((int)OpCode.IF_ICMPLE));
        lut.put("IF_ICMPLT", new Integer((int)OpCode.IF_ICMPLT));
        lut.put("IF_ICMPNE", new Integer((int)OpCode.IF_ICMPNE));
        lut.put("IFEQ", new Integer((int)OpCode.IFEQ));
        lut.put("IFGE", new Integer((int)OpCode.IFGE));
        lut.put("IFGT", new Integer((int)OpCode.IFGT));
        lut.put("IFLE", new Integer((int)OpCode.IFLE));
        lut.put("IFLT", new Integer((int)OpCode.IFLT));
        lut.put("IFNE", new Integer((int)OpCode.IFNE));
        lut.put("IFNONNULL", new Integer((int)OpCode.IFNONNULL));
        lut.put("IFNULL", new Integer((int)OpCode.IFNULL));
        lut.put("IINC", new Integer((int)OpCode.IINC));
        lut.put("ILOAD", new Integer((int)OpCode.ILOAD));
        lut.put("ILOAD_0", new Integer((int)OpCode.ILOAD_0));
        lut.put("ILOAD_1", new Integer((int)OpCode.ILOAD_1));
        lut.put("ILOAD_2", new Integer((int)OpCode.ILOAD_2));
        lut.put("ILOAD_3", new Integer((int)OpCode.ILOAD_3));
        lut.put("IMUL", new Integer((int)OpCode.IMUL));
        lut.put("INEG", new Integer((int)OpCode.INEG));
        lut.put("INSTANCEOF", new Integer((int)OpCode.INSTANCEOF));
        lut.put("INVOKEINTERFACE", new Integer((int)OpCode.INVOKEINTERFACE));
        lut.put("INVOKESPECIAL", new Integer((int)OpCode.INVOKESPECIAL));
        lut.put("INVOKESTATIC", new Integer((int)OpCode.INVOKESTATIC));
        lut.put("INVOKEVIRTUAL", new Integer((int)OpCode.INVOKEVIRTUAL));
        lut.put("IOR", new Integer((int)OpCode.IOR));
        lut.put("IREM", new Integer((int)OpCode.IREM));
        lut.put("IRETURN", new Integer((int)OpCode.IRETURN));
        lut.put("ISHL", new Integer((int)OpCode.ISHL));
        lut.put("ISR", new Integer((int)OpCode.ISR));
        lut.put("ISTORE", new Integer((int)OpCode.ISTORE));
        lut.put("ISTORE_0", new Integer((int)OpCode.ISTORE_0));
        lut.put("ISTORE_1", new Integer((int)OpCode.ISTORE_1));
        lut.put("ISTORE_2", new Integer((int)OpCode.ISTORE_2));
        lut.put("ISTORE_3", new Integer((int)OpCode.ISTORE_3));
        lut.put("ISUB", new Integer((int)OpCode.ISUB));
        lut.put("IUSHR", new Integer((int)OpCode.IUSHR));
        lut.put("IXOR", new Integer((int)OpCode.IXOR));
        lut.put("JSR", new Integer((int)OpCode.JSR));
        lut.put("JSR_W", new Integer((int)OpCode.JSR_W));
        lut.put("L2D", new Integer((int)OpCode.L2D));
        lut.put("L2F", new Integer((int)OpCode.L2F));
        lut.put("L2I", new Integer((int)OpCode.L2I));
        lut.put("LADD", new Integer((int)OpCode.LADD));
        lut.put("LALOAD", new Integer((int)OpCode.LALOAD));
        lut.put("LAND", new Integer((int)OpCode.LAND));
        lut.put("LASTORE", new Integer((int)OpCode.LASTORE));
        lut.put("LCMP", new Integer((int)OpCode.LCMP));
        lut.put("LCONST_0", new Integer((int)OpCode.LCONST_0));
        lut.put("LCONST_1", new Integer((int)OpCode.LCONST_1));
        lut.put("LDC", new Integer((int)OpCode.LDC));
        lut.put("LDC_W", new Integer((int)OpCode.LDC_W));
        lut.put("LDC2_W", new Integer((int)OpCode.LDC2_W));
        lut.put("LDIV", new Integer((int)OpCode.LDIV));
        lut.put("LLOAD", new Integer((int)OpCode.LLOAD));
        lut.put("LLOAD_0", new Integer((int)OpCode.LLOAD_0));
        lut.put("LLOAD_1", new Integer((int)OpCode.LLOAD_1));
        lut.put("LLOAD_2", new Integer((int)OpCode.LLOAD_2));
        lut.put("LLOAD_3", new Integer((int)OpCode.LLOAD_3));
        lut.put("LMUL", new Integer((int)OpCode.LMUL));
        lut.put("LNEG", new Integer((int)OpCode.LNEG));
        lut.put("LOOKUPSWITCH", new Integer((int)OpCode.LOOKUPSWITCH));
        lut.put("LOR", new Integer((int)OpCode.LOR));
        lut.put("LREM", new Integer((int)OpCode.LREM));
        lut.put("LRETURN", new Integer((int)OpCode.LRETURN));
        lut.put("LSHL", new Integer((int)OpCode.LSHL));
        lut.put("LSHR", new Integer((int)OpCode.LSHR));
        lut.put("LSTORE", new Integer((int)OpCode.LSTORE));
        lut.put("LSTORE_0", new Integer((int)OpCode.LSTORE_0));
        lut.put("LSTORE_1", new Integer((int)OpCode.LSTORE_1));
        lut.put("LSTORE_2", new Integer((int)OpCode.LSTORE_2));
        lut.put("LSTORE_3", new Integer((int)OpCode.LSTORE_3));
        lut.put("LSUB", new Integer((int)OpCode.LSUB));
        lut.put("LUSHR", new Integer((int)OpCode.LUSHR));
        lut.put("LXOR", new Integer((int)OpCode.LXOR));
        lut.put("MONITORENTER", new Integer((int)OpCode.MONITORENTER));
        lut.put("MONITOREXIT", new Integer((int)OpCode.MONITOREXIT));
        lut.put("MULTIANEWARRAY", new Integer((int)OpCode.MULTIANEWARRAY));
        lut.put("NEW", new Integer((int)OpCode.NEW));
        lut.put("NEWARRAY", new Integer((int)OpCode.NEWARRAY));
        lut.put("NOP", new Integer((int)OpCode.NOP));
        lut.put("POP", new Integer((int)OpCode.POP));
        lut.put("POP2", new Integer((int)OpCode.POP2));
        lut.put("PUTFIELD", new Integer((int)OpCode.PUTFIELD));
        lut.put("PUTSTATIC", new Integer((int)OpCode.PUTSTATIC));
        lut.put("RET", new Integer((int)OpCode.RET));
        lut.put("RETURN", new Integer((int)OpCode.RETURN));
        lut.put("SALOAD", new Integer((int)OpCode.SALOAD));
        lut.put("SASTORE", new Integer((int)OpCode.SASTORE));
        lut.put("SIPUSH", new Integer((int)OpCode.SIPUSH));
        lut.put("SWAP", new Integer((int)OpCode.SWAP));
        lut.put("TABLESWITCH", new Integer((int)OpCode.TABLESWITCH));
        lut.put("WIDE", new Integer((int)OpCode.WIDE));
        return lut;
    }

    public static void main(String[] _) {
        Machine vm = JavaMachine.newInstance();
        Dictionary lut = getOpCodeLookupTable();
        DataInput in = new DataInputStream(System.in);

        System.out.println("Fletch JVM REPL");
        repl: while (true) {
            System.out.print(">>> ");
            System.out.flush();
            try {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                String[] lines = line.split(" ");
                if (lines.length == 0) {
                    continue;
                }
                Number opCode = (Number)lut.get(lines[0]);
                if (opCode == null) {
                    System.err.println("Invalid opCode: " + lines[0]);
                    continue;
                }
                byte[] args = new byte[lines.length - 1];
                for (int i = 0; i < args.length; i++) {
                    try {
                        int tmp = Integer.parseInt(lines[i + 1]);
                        if (tmp > 0xff) {
                            System.err.println("Invalid argument '" + lines[i+1] + "'");
                            continue repl;
                        }
                        args[i] = (byte)(tmp & 0xff);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid argument '" + lines[i+1] + "'': " + e);
                        continue repl;
                    }
                }
                System.out.print("Executing opCode " + opCode + " with args: [");
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(args[i]);
                }
                System.out.println("]");
                vm.execute(opCode.byteValue(), args);
            } catch (IOException e) {
                System.err.println("IO Error: " + e);
                break;
            }
        }
    }
}
