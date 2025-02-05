public class InstructionCache {
    private static final int CACHE_SIZE = 8;
    private Word[] cache = new Word[CACHE_SIZE];
    private Word baseAddress = null; // The address of the first word in the cache
    private int cycleCount = 0;

    public InstructionCache() {
        // Initialize cache with empty words
        for (int i = 0; i < CACHE_SIZE; i++) {
            cache[i] = new Word();
        }
    }

    public Word read(Word address) {
        if (isAddressInCache(address)) {
            cycleCount += 10; // Cache hit
            return getWordFromCache(address);
        } 
        else {
            fillCache(address);
            cycleCount += 350; // Cache miss and refill
            return getWordFromCache(address);
        }
    }

    public boolean isAddressInCache(Word address) {
        if (baseAddress == null) {
            return false;
        }
        long addressValue = address.getUnsigned();
        long baseValue = baseAddress.getUnsigned();
        return addressValue >= baseValue && addressValue < baseValue + CACHE_SIZE;
    }

    private Word getWordFromCache(Word address) {
        int offset = (int)(address.getUnsigned() - baseAddress.getUnsigned());
        return cache[offset];
    }

    private void fillCache(Word address) {
        // Cast to int might be required if getUnsigned() returns a larger data type
        int addressValue = (int) address.getUnsigned();
        int baseValue = addressValue - (addressValue % CACHE_SIZE);
        baseAddress = new Word((int) baseValue); // Ensure baseAddress uses the Word(int) constructor

        for (int i = 0; i < CACHE_SIZE; i++) {
            // MainMemory.read() must also be compatible with Word(int)
            cache[i] = MainMemory.read(new Word((int) (baseValue + i)));
        }
    }

    public int getCycleCount() {
        return cycleCount;
    }

    // Reset method for testing or re-initialize
    public void reset() {
        cycleCount = 0;
        baseAddress = null;
        for (int i = 0; i < CACHE_SIZE; i++) {
            cache[i] = new Word();
        }
    }
}