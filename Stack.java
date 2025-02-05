public class Stack {
    private static Word SP = new Word(); // Word can represent addresses
    private static final int STACK_SIZE = 1024; // Stack size
    private static Word[] memory = new Word[STACK_SIZE]; // Simulated stack memory

    static {
        // Initialize stack memory and SP to point to the top of the stack
        for (int i = 0; i < STACK_SIZE; i++) {
            memory[i] = new Word(); // Word has a default constructor
        }
        SP = new Word(STACK_SIZE - 1); // Stack Pointer initialization
    }

    // Method to set SP's value
    public static void setSP(Word value) {
        SP = value;
    }

    // Method to get SP's value
    public static Word getSP() {
        return SP;
    }

    // Push a word onto the stack
    public static void push(Word value) {
        if (SP.toInt() <= 0) { // Check for stack overflow
            throw new StackOverflowError("Stack overflow");
        }
        SP = SP.subtract(new Word(1)); // Decrement SP
        memory[SP.toInt()] = value; // Store the value at the new SP location
    }

    // Pop a word from the stack
    public static Word pop() {
        if (SP.toInt() >= STACK_SIZE - 1) { // Check for stack underflow
            throw new StackUnderflowError("Stack underflow");
        }
        Word value = memory[SP.toInt()]; // Retrieve the value at SP location
        SP = SP.add(new Word(1)); // Increment SP
        return value;
    }

    // Method to check for stack overflow
    public static boolean isStackFull() {
        return SP.toInt() == 0;
    }

    // Method to check for stack underflow
    public static boolean isStackEmpty() {
        return SP.toInt() == STACK_SIZE - 1;
    }

    // Inner class for StackUnderflowError
    public static class StackUnderflowError extends RuntimeException {
        public StackUnderflowError(String message) {
            super(message);
        }
    }

    // Inner class for StackOverflowError
    public static class StackOverflowError extends RuntimeException {
        public StackOverflowError(String message) {
            super(message);
        }
    }
}