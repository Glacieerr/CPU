public class Word {
    private final Bit[] bits;
    private int value;
    private String valuee;


    public Word() {
        this.bits = new Bit[32];
        this.value = 0;
        for (int i = 0; i < 32; i++) {
            this.bits[i] = new Bit();
        }
    }
    
    public Word(int value) {
        this(); // Initialize bits array
        this.value = value;

        for (int i = 0; i < 32; i++) {
            boolean bitValue = (value & (1 << i)) != 0;
            bits[31 - i] = new Bit(bitValue); // Set bits[31] is the LSB
        }
    }
    
    public Bit getBit(int i) {
        return new Bit(this.bits[i].getValue());
    }

    public void setBit(int i, Bit value) {
        this.bits[i].set(value.getValue());
    }

    private Word bitwiseOperation(Word other, String operation) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            switch (operation) {
                case "AND":
                    result.setBit(i, this.bits[i].and(other.getBit(i)));
                    break;
                case "OR":
                    result.setBit(i, this.bits[i].or(other.getBit(i)));
                    break;
                case "XOR":
                    result.setBit(i, this.bits[i].xor(other.getBit(i)));
                    break;
            }
        }
        return result;
    }

    public Word and(Word other) {
        return bitwiseOperation(other, "AND");
    }

    public Word or(Word other) {
        return bitwiseOperation(other, "OR");
    }

    public Word xor(Word other) {
        return bitwiseOperation(other, "XOR");
    }

    public Word not() {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            result.setBit(i, this.bits[i].not());
        }
        return result;
    }

    public Word rightShift(int amount) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            if (i + amount < 32) {
                result.setBit(i, this.bits[i + amount]);
            } 
            else {
                result.setBit(i, new Bit(false));
            }
        }
        return result;
    }

    public Word leftShift(int amount) {
        Word result = new Word();
        for (int i = 31; i >= 0; i--) {
            if (i - amount >= 0) {
                result.setBit(i, this.bits[i - amount]);
            } 
            else {
                result.setBit(i, new Bit(false));
            }
        }
        return result;
    }

    public long getUnsigned() {
        long result = 0;
        for (int i = 0; i < 32; i++) {
            if (bits[i].getValue()) {
                result |= (1L << (31 - i));
            }
        }
        return result;
    }

    public int getSigned() {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            if (bits[i].getValue()) {
                result |= (1 << (31 - i));
            }
        }
        return result;
    }

    public void copy(Word other) {
        for (int i = 0; i < 32; i++) {
            this.bits[i].set(other.getBit(i).getValue());
        }
    }

    public void set(int value) {
        for (int i = 0; i < 32; i++) {
            bits[i].set((value & (1 << (31 - i))) != 0);
        }
    }
    
    public void set(long value) {
        for (int i = 0; i < 32; i++) {
            // Extract each bit from the long value and set it in the corresponding Bit object
            boolean bitValue = (value & (1L << i)) != 0;
            bits[31 - i] = new Bit(bitValue); // Set Bit(boolean) constructor
        }
    }
    
    public void increment() {
        boolean carry = true;
        for (int i = 31; i >= 0 && carry; i--) {
            boolean currentBit = this.getBit(i).getValue();
            carry = currentBit && carry; // Determine if there will be a carry
            this.setBit(i, new Bit(currentBit ^ carry)); // Set the new bit value
        }
    }
    
    // Method to determine if the Word's value is negative
    public boolean isNegative() {
        return value < 0;
    }

    // Method to determine if the Word's value is zero
    public boolean isZero() {
        return value == 0;
    }

    public boolean isPositive() {
        return this.value > 0;
    }
    
    // Method to subtract another Word from this Word and return the result as a new Word
    public Word subtract(Word word) {
    	return new Word(this.value - word.value);
	}
    
    // Method to add another Word to this Word and return the result as a new Word
    public Word add(Word other) {
        return new Word(this.value + other.value);
    }

    // Set there's a need to directly set the Word's value
    public void setValue(int value) {
        this.value = value;
    }
    
    public void decrement() {
        this.value -= 1;
    }
    
    public void clearBit(int index) {
        if (index >= 0 && index < bits.length) {
            this.bits[index].set(false); // Set the specified bit to false/0
        } 
        else {
            throw new IllegalArgumentException("Bit index out of range.");
        }
    }
    
	// Method to set a bit to true/1
    public void setBit(int index) {
        if (index >= 0 && index < bits.length) {
            this.bits[index].set(true); // Set the specified bit to true/1
        } 
        else {
            throw new IllegalArgumentException("Bit index out of range.");
        }
    }

    // Method to toggle a bit (if the bit is 0, set it to 1, and vice versa)
    public void toggleBit(int index) {
        if (index >= 0 && index < bits.length) {
            boolean currentValue = this.bits[index].getValue();
            this.bits[index].set(!currentValue); // Toggle the current value
        } 
        else {
            throw new IllegalArgumentException("Bit index out of range.");
        }
    }

    // Method to get the value of a bit
    public boolean getBitValue(int index) {
        if (index >= 0 && index < bits.length) {
            return this.bits[index].getValue();
        } 
        else {
            throw new IllegalArgumentException("Bit index out of range.");
        }
    }
    
    public Word multiply(Word other) {
        long result = (long) this.value * other.value;
        // Cast the result back to int to fit into the Word structure, handling potential overflow
        return new Word((int) result);
    }

    public int getValue() {
        return this.value;
    }
    
    public Word divide(Word word) {
    	if (word.value == 0) {
    		throw new ArithmeticException("Division by zero.");
    	}
    	return new Word(this.value / word.value);
    }
    
    public Word arithmeticRightShift(int numberOfBits) {
        return new Word(this.value >> numberOfBits); // Use the signed right shift operator
    }

    // Method to get the binary string representation of the Word
    public String toBinaryString() {
        StringBuilder binaryString = new StringBuilder(bits.length);
        for (Bit bit : bits) {
            binaryString.append(bit.getValue() ? '1' : '0');
        }
        return binaryString.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(bits[i].toString());
            if (i < 31) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    public int toInt() {
        return this.value;
    }
    
	// Method to convert Word to integer
    public int toInteger() {
        return this.value;
    }
    
	// Method to get the numerical value as an integer
    public int getNumber() {
        // Attempt to parse the string to an integer and return it
        try {
            return Integer.parseInt(valuee);
        } 
        catch (NumberFormatException e) {
            // Handle the case where the string does not contain a parsable integer
            System.err.println("Error: Value '" + value + "' is not a valid number.");
            return 0;
        }
    }
}