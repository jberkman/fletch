package NET._87k.fletch.vm;

import java.io.ByteArrayInputStream;

class PosInputStream extends ByteArrayInputStream {

    PosInputStream(byte[] bytes, int off, int len) {
        super(bytes, off, len);
    }

    int pos() {
        return pos;
    }

}
