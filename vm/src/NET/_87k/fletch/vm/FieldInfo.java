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

import java.util.Dictionary;
import java.util.Hashtable;

final class FieldInfo extends MemberInfo {
    private static final Object defaultByte = new Byte((byte) 0);
    private static final Object defaultChar = new Character((char) 0);
    private static final Object defaultDouble = new Double(0d);
    private static final Object defaultFloat = new Float(0f);
    private static final Object defaultInteger = new Integer(0);
    private static final Object defaultLong = new Long(0L);
    private static final Object defaultShort = new Short((short) 0);
    private static final Object defaultBoolean = new Boolean(false);

    int index = -1;
    final ConstantValueInfo value;

    FieldInfo(int accessFlags, String name, String descriptor, ConstantValueInfo value) {
        super(accessFlags, name, descriptor);
        this.value = value;
    }

    Object defaultValue() {
        if (value != null) {
            return value.value();
        }
        if (descriptor.length() > 1) {
            return null;
        }
        switch (descriptor.charAt(0)) {
            case 'B': return defaultByte;
            case 'C': return defaultChar;
            case 'D': return defaultDouble;
            case 'F': return defaultFloat;
            case 'I': return defaultInteger;
            case 'J': return defaultLong;
            case 'S': return defaultShort;
            case 'Z': return defaultBoolean;
            default:
                throw new IllegalStateException(descriptor);
        }
    }
}
