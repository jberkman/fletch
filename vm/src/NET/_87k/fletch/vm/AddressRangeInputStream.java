package NET._87k.fletch.vm;

import java.io.IOException;
import java.io.InputStream;

class AddressRangeInputStream extends InputStream {

    int pos;
    int remaining;

    AddressRangeInputStream(AddressRange addressRange) {
        pos = addressRange.base;
        remaining = addressRange.length;
    }

    public int read() throws IOException {
        if (remaining-- == 0) {
            return -1;
        }
        return Machine.cpu.load(pos++);
    }

    public int available() throws IOException {
        return remaining;
    }

}
