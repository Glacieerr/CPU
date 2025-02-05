public class MainMemory {
    private static final int SIZE = 1024; // Memory size
    private static Word[] memory = new Word[SIZE]; // Memory array

    // Ensures memory is initialized
    private static void initializeMemory() {
        for (int i = 0; i < SIZE; i++) {
            if (memory[i] == null) {
                memory[i] = new Word();
            }
        }
    }

    public static Word read(Word address) {
        initializeMemory(); // Ensure memory is initialized
        int index = (int)address.getUnsigned(); // Cast to int
        Word newWord = new Word();
        newWord.set((int)memory[index].getUnsigned());
        return newWord;
    }

    public static void write(Word address, Word value) {
        initializeMemory(); // Ensure memory is initialized
        int index = (int)address.getUnsigned(); // Cast to int
        memory[index].set(value.getUnsigned()); // Set method accepts long
    }

    public static void load(String[] data) {
        initializeMemory(); // Ensure memory is initialized
        for (int i = 0; i < data.length && i < SIZE; i++) {
            for (int bit = 0; bit < 32; bit++) {
                memory[i].setBit(bit, new Bit(data[i].charAt(bit) == '1'));
            }
        }
    }
}