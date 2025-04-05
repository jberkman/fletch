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

final class AccessFlags {
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

    static boolean areValidForClass(int accessFlags) {
        accessFlags &= ACC_PUBLIC | ACC_FINAL | ACC_SUPER | ACC_INTERFACE | ACC_ABSTRACT;
        switch (accessFlags & ~ACC_PUBLIC) {
            case ACC_FINAL | ACC_SUPER:
            case ACC_INTERFACE | ACC_ABSTRACT:
            case ACC_SUPER | ACC_ABSTRACT:
            case ACC_SUPER:
                return true;
            default:
                return false;
        }
    }

    static boolean areValidForField(int accessFlags, boolean isInterface) {
        accessFlags &= ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL | ACC_VOLATILE | ACC_TRANSIENT;
        if (isInterface) {
            return accessFlags != (ACC_PUBLIC | ACC_STATIC | ACC_FINAL);
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

    static boolean areValidForMethod(int accessFlags, boolean isInterface) {
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
        return ((accessFlags & ACC_ABSTRACT) == 0) ||
                ((accessFlags & (ACC_PRIVATE | ACC_STATIC | ACC_FINAL | ACC_NATIVE | ACC_SYNCHRONIZED)) == 0);
    }

}
