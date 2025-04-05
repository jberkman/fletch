package NET._87k.fletch.vm;

final class CallStack {

    private static final int MAX_FRAMES = 32;
    private static final int STACK_SIZE = 1024;

    private final int[] stack = new int[STACK_SIZE];

    private final ClassHandle[] classes = new ClassHandle[MAX_FRAMES];
    private final MethodInfo[] methods = new MethodInfo[MAX_FRAMES];
    private final int[] fps = new int[MAX_FRAMES];

    private int currentFrame;
    private int sp;

    void push(int i) {
        stack[sp++] = i;
    }

    int pop() {
        return stack[--sp];
    }

    int peek() {
        return stack[sp - 1];
    }

    private void checkLocalIndex(int index) {
        if (methods[currentFrame] == null) {
            throw new InternalError();
        }
        if (index < 0 || index >= methods[currentFrame].code.maxLocals) {
            throw new IndexOutOfBoundsException();
        }
    }

    int loadLocal(int index) {
        checkLocalIndex(index);
        return stack[fps[currentFrame] + index];
    }

    void storeLocal(int index, int value) {
        checkLocalIndex(index);
        stack[fps[currentFrame] + index] = value;
    }

    void pushFrame(ClassHandle classHandle, MethodInfo method) {
        fps[currentFrame + 1] = fps[currentFrame];
        if (methods[currentFrame] != null) {
            fps[currentFrame + 1] += methods[currentFrame].code.maxLocals;
        }
        sp = fps[++currentFrame] + method.code.maxLocals;
        classes[currentFrame] = classHandle;
        methods[currentFrame] = method;
    }

    void popFrame() {
        classes[currentFrame] = null;
        methods[currentFrame] = null;
        sp = fps[--currentFrame];
        if (methods[currentFrame] != null) {
            sp += methods[currentFrame].code.maxLocals;
        }
    }

    ClassHandle currentClass() {
        return classes[currentFrame];
    }

    MethodInfo currentMethod() {
        return methods[currentFrame];
    }

}
