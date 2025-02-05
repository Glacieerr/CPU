public class ALU {
    private Word op1;
    private Word op2;
    private Word result;

    public ALU() {
        this.op1 = new Word();
        this.op2 = new Word();
        this.result = new Word();
    }

    // Getters and Setters for better encapsulation
    public Word getOp1() {
        return op1;
    }

    public void setOp1(Word op1) {
        this.op1 = op1;
    }

    public Word getOp2() {
        return op2;
    }

    public void setOp2(Word op2) {
        this.op2 = op2;
    }

    public Word getResult() {
        return result;
    }

    // Method to perform operation based on the operation code
    public void doOperation(Bit[] operation) {
        String opCode = bitArrayToString(operation);
        
        switch (opCode) {
            case "1000":
                result.copy(op1.and(op2));
                break;
            case "1001":
                result.copy(op1.or(op2));
                break;
            case "1010":
                result.copy(op1.xor(op2));
                break;
            case "1011":
                result.copy(op1.not());
                break;
            case "1100":
                // Left shift operation
                performLeftShift();
                break;
            case "1101":
                // Right shift operation
                performRightShift();
                break;
            case "1110":
                // Addition operation
                result.copy(add(op1, op2));
                break;
            case "1111":
                // Subtraction operation
                result.copy(subtract(op1, op2));
                break;
            case "0111":
                // Multiplication operation
                result.copy(multiply(op1, op2));
                break;
            default:
                System.out.println("Unsupported operation");
        }
    }

    // Converts a Bit array to a binary string representation
    private String bitArrayToString(Bit[] bits) {
        StringBuilder builder = new StringBuilder();
        for (Bit b : bits) {
            builder.append(b.getValue() ? "1" : "0");
        }
        return builder.toString();
    }

    // For the left right shift operations to simplify doOpe
    private void performLeftShift() {
        int shiftAmount = op2.getSigned() & 0x1F;
        result.copy(op1.leftShift(shiftAmount));
    }

    private void performRightShift() {
        int shiftAmount = op2.getSigned() & 0x1F;
        result.copy(op1.rightShift(shiftAmount));
    }

    // Add method with detailed
    public Word add(Word a, Word b) {
        Word result = new Word();
        Bit carry = new Bit(false);
        // Iterates over each bit from LSB to MSB
        for (int i = 31; i >= 0; i--) {
            Bit aBit = a.getBit(i);
            Bit bBit = b.getBit(i);
            // Calculate sum and carry
            Bit sum = aBit.xor(bBit).xor(carry);
            result.setBit(i, sum);
            Bit carryFromAB = aBit.and(bBit);
            Bit carryFromSumAndCarry = aBit.xor(bBit).and(carry);
            carry = carryFromAB.or(carryFromSumAndCarry);
        }
        return result;
    }

    // Detailed subtract
    public Word subtract(Word a, Word b) {
        Word notB = b.not();
        Word one = new Word();
        one.setBit(31, new Bit(true));
        Word negativeB = add(notB, one);
        return add(a, negativeB);
    }

    // Detailed multiply
    private Word multiply(Word a, Word b) {
        Word result = new Word();
        for (int i = 0; i < 32; i++) {
            if (b.getBit(i).getValue()) {
                result = add(result, a.leftShift(i));
            }
        }
        return result;
    }

    public Word add2(Word a, Word b) {
        Word result = new Word();
        Bit carry = new Bit(false);
        for (int i = 31; i >= 0; i--) {
            Bit bitA = a.getBit(i);
            Bit bitB = b.getBit(i);
            // Calculate the sum of bitA and bitB with the carry
            Bit sum = bitA.xor(bitB).xor(carry);
            result.setBit(i, sum);
            
            // Calculate the new carry
            Bit carryAB = bitA.and(bitB);
            Bit carrySumCarry = bitA.xor(bitB).and(carry);
            carry = carryAB.or(carrySumCarry);
        }
        return result;
    }

    public Word add4(Word a, Word b, Word c, Word d) {
        // add a and b, and then c and d
        Word sumAB = add(a, b);
        Word sumCD = add(c, d);

        // add results of the first two additions
        Word result = add(sumAB, sumCD);

        return result;
    }
    
    private Word and(Word op1, Word op2) {
        Word result = new Word(); // set Word initializes to all 0s
        for (int i = 0; i < 32; i++) { // set 32-bit words
            boolean bit1 = op1.getBit(i).getValue(); // set getBit(i) returns a Bit object, and Bit has a getValue() method
            boolean bit2 = op2.getBit(i).getValue();
            result.setBit(i, new Bit(bit1 && bit2)); // set setBit(int index, Bit bit) sets the bit at index
        }
        return result;
    }


    private Word or(Word op1, Word op2) {
        Word result = new Word(); // Set a new Word is initialized with all bits set to false/0
        for (int i = 0; i < 32; i++) { // Set 32-bit words
            // Set getBit(i) returns a Bit object for the ith bit, and Bit has a getValue() method returning a boolean
            boolean bit1 = op1.getBit(i).getValue();
            boolean bit2 = op2.getBit(i).getValue();
            // set setBit(int index, Bit value) method exists to set the value of a bit at a specified index
            result.setBit(i, new Bit(bit1 || bit2)); // Set the result bit to true if either bit1 or bit2 is true
        }
        return result;
    }
    
    private Word xor(Word op1, Word op2) {
        Word result = new Word(); // Set a new Word is initialized with all bits set to f
        for (int i = 0; i < 32; i++) { // set 32-bit words
            // set getBit(i) returns a Bit object for the ith bit, and Bit has a getValue() method returning a boolean
            boolean bit1 = op1.getBit(i).getValue();
            boolean bit2 = op2.getBit(i).getValue();
            // Perform XOR operation on each bit pair and set the result bit
            result.setBit(i, new Bit(bit1 ^ bit2)); // Set the result bit to the XOR of bit1 and bit2
        }
        return result;
    }
    
    public Word doOp(int operationCode, Word operand1, Word operand2) {
        switch (operationCode) {
            case 0x0: // operation code for addition
                return add(operand1, operand2);
            case 0x1: // operation code for subtraction
                return subtract(operand1, operand2);
            case 0x2: // operation code for AND
                return and(operand1, operand2);
            case 0x3: // operation code for OR
                return or(operand1, operand2);
            default:
                throw new IllegalArgumentException("Unsupported operation code: " + operationCode);
        }
    }
}