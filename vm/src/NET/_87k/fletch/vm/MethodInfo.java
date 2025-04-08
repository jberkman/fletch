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

final class MethodInfo extends MemberInfo {
    final CodeAttribute code;
    final String[] exceptions;

    MethodInfo(int accessFlags, String name, String descriptor, CodeAttribute code, String[] exceptions) {
        super(accessFlags, name, descriptor);
        this.code = code;
        this.exceptions = exceptions;
    }

    int argumentStackSlots() {
        int ret;
        if (isStatic()) {
            ret = 0;
        } else {
            ret = 1;
        }
        for (int i = 1; i < descriptor.length(); i++) {
            switch (descriptor.charAt(i)) {
                case 'L':
                case '[':
                    while (descriptor.charAt(++i) != ';') {
                    }
                case 'B':
                case 'C':
                case 'F':
                case 'I':
                case 'S':
                case 'Z':
                    ++ret;
                    break;
                case 'D':
                case 'J':
                    ret += 2;
                    break;
                case ')':
                    return ret;
                default:
                    throw new ClassFormatError(descriptor);
            }
        }
        throw new ClassFormatError(descriptor);
    }

    int returnValueStackSlots() {
        int i = descriptor.indexOf(')');
        switch (descriptor.charAt(i + 1)) {
            case 'L':
            case 'B':
            case 'C':
            case 'F':
            case 'I':
            case 'S':
            case 'Z':
                return 1;
            case 'D':
            case 'J':
                return 2;
            case 'V':
                return 0;
            default:
                throw new ClassFormatError(descriptor);
        }
    }

}
