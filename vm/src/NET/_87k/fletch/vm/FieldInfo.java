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
    final ConstantValueInfo value;
    private static final Dictionary defaultValues = new Hashtable();

    static {
        defaultValues.put("B", new Byte((byte) 0));
        defaultValues.put("C", new Character((char) 0));
        defaultValues.put("D", new Double(0d));
        defaultValues.put("F", new Float(0f));
        defaultValues.put("I", new Integer(0));
        defaultValues.put("J", new Long(0L));
        defaultValues.put("S", new Short((short) 0));
        defaultValues.put("Z", new Boolean(false));
    }

    FieldInfo(int accessFlags, String name, String descriptor, ConstantValueInfo value) {
        super(accessFlags, name, descriptor);
        this.value = value;
    }

    Object defaultValue() {
        if (value != null) {
            return value.value();
        }
        return defaultValues.get(descriptor);
    }
}
