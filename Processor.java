import java.util.List;

public class Processor {
    private static final Word[] registers = new Word[16]; // Registers, R0 to R15
    private static Word currentInstruction; // Holds the current instruction from fetch()
    private static Word op1 = new Word(); // Operand storage for execution phase
    private static Word op2 = new Word();
    private static int destRegIndex; // Include a destination register index, set a 3R format
    private static int functionBits; // Function bits for ALU operation, set a simple format
    private static Bit halted = new Bit(false); // Initialize halted as not halted
    private static Word PC = new Word(); // Word is a class that represents a memory word
    private static ALU alu = new ALU(); // Declaration of the ALU instance.
    private static Word executionResult = new Word(); // To hold the result of ALU operations
    
    private static MainMemory mainMemory = new MainMemory(); // Set a MainMemory 
    //class for memory operations
    private static Word SP = new Word(); // Initialize SP with a default address or value
    
    private int currentClockCycle = 0;
    private InstructionCache instructionCache = new InstructionCache();
    
    
    public Processor() {
        this.currentClockCycle = 0;
    }
    
	// Define condition codes as constants
	    public static final int EQ = 0; // Equals
	    public static final int NE = 1; // Not equals
	    public static final int LT = 2; // Less than
	    public static final int GT = 3; // Greater than
	    public static final int LE = 4; // Less than or equal to
	    public static final int GE = 5; // Greater than or equal to
    
	// Opcode definitions
	    public static final int BRANCH_EQ = 0x01; // opcode for "equals" branch
	    public static final int BRANCH_NE = 0x02; // opcode for "not equals" branch
	    public static final int BRANCH_LT = 0x03; // opcode for "less than" branch
	    public static final int BRANCH_GT = 0x04; // opcode for "greater than" branch
	    public static final int BRANCH_LE = 0x05; // opcode for "less than or equal to" branch
	    public static final int BRANCH_GE = 0x06; // opcode for "greater than or equal to" branch

    static {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Word(); // Initialize registers, ensuring R0 is always 0
        }
    }

    public static void resetState() {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = new Word(); // Reinitialize each register
        }
        currentInstruction = new Word(); // Reset current instruction
        op1 = new Word(); // Reset operand 1
        op2 = new Word(); // Reset operand 2
        destRegIndex = 0; // Reset destination register index
        functionBits = 0; // Reset function bits
        halted = new Bit(false); // Reset halted state
        PC = new Word(); // Reset program counter
        executionResult = new Word(); // Reset execution result
        SP = new Word(0xFFFF); // Set SP to point to the start of the stack
    }
    public static Word getRegisterValue(int index) {
        if (index == 0) {
            return new Word(); // Set the default Word constructor represents 0
        }
        else {
            return registers[index];
        }
    }

    public static void setRegisterValue(int index, Word value) {
        if (index != 0) { // Prevent modifying R0
            registers[index].copy(value); // set copy() method duplicates the value
        }
    }
    
    public static void run() {
        while (!halted.getValue()) {
            fetch();
            decode();
            execute();
            store();
        }
    }

    private static void incrementPC() {
        PC.increment(); // Set the Word class has an increment() method
    }
    
    private static void fetch() {
        // Set MainMemory.read() correctly handles reading from the address in PC
        currentInstruction = MainMemory.read(PC);
        incrementPC();
    }
    
    public static void decode() {
        int rawInstruction = extractRawValue(currentInstruction); // Method correctly extracts the instruction as an integer
        int instructionType = rawInstruction & 0x3; // Extracting the last 2 bits to determine the instruction type

        switch (instructionType) {
            case 0: // "No R" type
                if (rawInstruction == 0) { // Set 0 represents a HALT instruction
                    halted.set(true);
                    System.out.println("Decoded HALT instruction.");
                }
                break;

            case 1: // "1R" type, involves one register
                int regIndex = (rawInstruction >>> 4) & 0xF; // Set bits [7-4] hold the register index
                op1 = getRegisterValue(regIndex); // Set op1 to the value of the specified register
                // Immediate value or function bits can be extracted here if needed
                functionBits = (rawInstruction >>> 8) & 0xFFFFFF; // Set the rest of the bits are for immediate value or function
                System.out.println("Decoded 1R instruction, register: " + regIndex);
                break;

            case 2: // "3R" type, involving three registers
            case 3: // Another "3R" type if needed
                destRegIndex = (rawInstruction >>> 24) & 0xF; // Extract destination register
                int srcRegIndex1 = (rawInstruction >>> 20) & 0xF; // Extract first source register
                int srcRegIndex2 = (rawInstruction >>> 16) & 0xF; // Extract second source register
                functionBits = (rawInstruction >>> 28) & 0xF; // Extract function bits
                op1.copy(getRegisterValue(srcRegIndex1));
                op2.copy(getRegisterValue(srcRegIndex2));
                System.out.println("Decoded 3R instruction with function bits: " + functionBits);
                break;
        }
    }
    
    private static int extractRawValue(Word word) {
        // Directly call the toInt() method on the Word object
        return word.toInt();
    }

    private static void execute() {
    	
    	executionResult = alu.doOp(functionBits, op1, op2); 
        // Check if the processor is halted first to avoid unnecessary execution
        if (halted.getValue()) {
            return;
        }

        final int ADD = 0x0; // Operation code for addition
        final int SUB = 0x1; // Operation code for subtraction
        final int AND = 0x2; // Operation code for bitwise AND
        final int OR = 0x3;  // Operation code for bitwise OR

        switch (functionBits) {
            case ADD:
                // Perform addition
                executionResult = alu.doOp(functionBits, op1, op2);
                System.out.println("Performed addition.");
                break;
            case SUB:
                // Perform subtraction
                executionResult = alu.doOp(functionBits, op1, op2);
                System.out.println("Performed subtraction.");
                break;
            case AND:
                // Perform bitwise AND
                executionResult = alu.doOp(functionBits, op1, op2);
                System.out.println("Performed bitwise AND.");
                break;
            case OR:
                // Perform bitwise OR
                executionResult = alu.doOp(functionBits, op1, op2);
                System.out.println("Performed bitwise OR.");
                break;
                
            default:
                System.out.println("Unknown operation code: " + functionBits);
                // Handle unknown operation codes appropriately
                break;
        }
    }
    
    public static void branchIf(int conditionCode, int regIndex1, int regIndex2, int offset) {
        Word reg1 = registers[regIndex1];
        Word reg2 = registers[regIndex2];
        boolean conditionMet = false;

        // Perform the comparison using the ALU and set conditionMet based on the result
        Word result = alu.subtract(reg1, reg2);
        switch (conditionCode) {
            case EQ: // Equals
                conditionMet = result.isZero(); // isZero() method in Word or result of ALU operation
                break;
            case NE: // Not Equals
                conditionMet = !result.isZero();
                break;
            case LT: // Less Than
                conditionMet = result.isNegative(); // isNegative() method exists
                break;
            case GT: // Greater Than
                conditionMet = !result.isNegative() && !result.isZero(); // GT if result is positive (not negative and not zero)
                break;
            case LE: // Less Than or Equal
                conditionMet = result.isNegative() || result.isZero(); // LE if result is negative or zero
                break;
            case GE: // Greater Than or Equal
                conditionMet = !result.isNegative(); // GE if result is not negative (includes zero and positive)
                break;
        }

        if (conditionMet) {
            // Calculate new PC value by adding offset to current PC
            PC = alu.add(PC, new Word(offset));
        }
    }
    
    public static void executeBranch(int opcode, int regIndex1, int regIndex2, int offset) {
        // opcode handling for a branch instruction
        switch (opcode) {
            case BRANCH_EQ:
                branchIf(EQ, regIndex1, regIndex2, offset);
                break;
            case BRANCH_NE:
                branchIf(NE, regIndex1, regIndex2, offset);
                break;
            case BRANCH_LT:
                branchIf(LT, regIndex1, regIndex2, offset);
                break;
            case BRANCH_GT:
                branchIf(GT, regIndex1, regIndex2, offset);
                break;
            case BRANCH_LE:
                branchIf(LE, regIndex1, regIndex2, offset);
                break;
            case BRANCH_GE:
                branchIf(GE, regIndex1, regIndex2, offset);
                break;

            default:
                System.out.println("Unknown branch opcode: " + opcode);
                // Handle unknown opcode scenario, may throw an exception or simply ignore
                break;
        }
    }
    
    public static void call(int targetAddress) {
        // Push current PC onto the stack
        SP.decrement();
        mainMemory.write(SP, PC);

        // Jump to target address
        PC.setValue(targetAddress); // setValue method exists to directly set PC
    }
    // Return instruction
    public static void returnFromCall() {
        // Pop return address from the stack and set PC
        PC = mainMemory.read(SP);
        SP.increment();
    }
    
    
    public static void load(int regIndex, int baseRegIndex, int offset) {
        Word baseAddress = registers[baseRegIndex];
        Word effectiveAddress = alu.add(baseAddress, new Word(offset));
        registers[regIndex] = mainMemory.read(effectiveAddress);
    }

    // Store instruction
    public static void store(int regIndex, int baseRegIndex, int offset) {
        Word baseAddress = registers[baseRegIndex];
        Word effectiveAddress = alu.add(baseAddress, new Word(offset));
        mainMemory.write(effectiveAddress, registers[regIndex]);
    }

    private static void store() {
        if (destRegIndex != 0) {
            setRegisterValue(destRegIndex, executionResult);
        }
    }
    
    public void executeInstruction(Instruction instruction) {
    	// Cycle counts for different types of instructions
        switch (instruction.getType()) {
            case CACHE_HIT:
                currentClockCycle += 10; // Cache hit cycle count
                break;
            case CACHE_MISS:
                currentClockCycle += 350; // Cache miss cycle count
                break;
            case ALU_OPERATION:
                currentClockCycle += 1; // Cycle count for an ALU operation
                break;
            case LOAD:
                currentClockCycle += 20; // Cycle count for a load operation
                break;
            case STORE:
                currentClockCycle += 20; // Cycle count for a store operation
                break;
            case JUMP:
                currentClockCycle += 5; // Cycle count for a jump instruction
                break;
            case HALT:
                System.out.println("Processor halted at clock cycle: " + currentClockCycle);
                break;
        }
        
        // Check if the instruction is a HALT type to print and reset the clock cycle
        if (instruction.getType() == InstructionType.HALT) {
            System.out.println("Processor halted at clock cycle: " + currentClockCycle);
            // Optionally reset the clock cycle
            currentClockCycle = 0;
        }
    }

    // Method to simulate executing a program (list of instructions)
    public void runProgram(List<Instruction> program) {
        for (Instruction instruction : program) {
            executeInstruction(instruction);
            if (instruction.getType() == InstructionType.HALT) {
                break; // Exit the loop if HALT instruction is encountered
            }
        }
    }

    // Getters and setters for currentClockCycle if needed
    public int getCurrentClockCycle() {
        return currentClockCycle;
    }

    public void setCurrentClockCycle(int currentClockCycle) {
        this.currentClockCycle = currentClockCycle;
    }
    
    public void fetchInstruction(int address) {
        Word instructionAddress = new Word(address);
        Word instruction;
        boolean cacheHit = instructionCache.isAddressInCache(instructionAddress);

        if (cacheHit) {
            instruction = instructionCache.read(instructionAddress);
            currentClockCycle += 10; // Cache hit cycle count
        } 
        else {
            instruction = instructionCache.read(instructionAddress); // read updates the cache and cycle count
            currentClockCycle += 350; // Cache miss cycle count includes cache fill time
        }

        // executeInstruction() executes the fetched instruction
        executeInstruction(instruction);
    }
    
    private InstructionType decodeInstructionType(Word instruction) {
        // Interpret the 'instruction' and return the instruction type
        int instructionCode = instruction.toInteger(); // method to convert Word to integer

        // Map the instruction code to an InstructionType
        switch (instructionCode) {
            case 0:
                return InstructionType.ADD;
            case 1:
                return InstructionType.SUBTRACT;
            default:
                return InstructionType.UNKNOWN; // Consider having an UNKNOWN type for error handling
        }
    }

    private void executeInstruction(Word instruction) {
        // Decode instruction and determine action
        InstructionType type = decodeInstructionType(instruction);
        // operand extraction
        Word operand1 = extractOperand1(instruction);
        Word operand2 = extractOperand2(instruction);
        
        switch (type) {
        case ADD:
            performAdd(operand1, operand2);
            break;
        case SUBTRACT:
            performSubtract(operand1, operand2);
            break;
        }
    }

    // Define performAdd method with two operands
    private void performAdd(Word operand1, Word operand2) {
        // Concatenate the string values of the two Word objects.
        int result = operand1.getValue() + operand2.getValue();
        
        // Print the concatenated result
        System.out.println("Result of Addition: " + result);
    }


    // Definitions for other operations
    private void performSubtract(Word operand1, Word operand2) {
        // Word has a method getNumber() that returns its numerical value as an int.
        int difference = operand1.getNumber() - operand2.getNumber();
        System.out.println("Difference: " + difference);
    }


    // Methods to extract operands from instruction
    private Word extractOperand1(Word instruction) {
        // Extract first operand from instruction
        return new Word(0);
    }

    private Word extractOperand2(Word instruction) {
        // Extract second operand from instruction
        return new Word(0);
    }
}