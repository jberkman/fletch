package NET._87k.fletch.vm;

final class CharArrayRef extends ObjectRef {
    private char[] chars;

    CharArrayRef(char[] chars) {
        this.chars = chars;
    }

    char[] chars() {
        return chars;
    }
}
