public class Instruction {
    private InstructionType type;
    
    public Instruction(InstructionType type) {
        this.type = type;
    }

    public InstructionType getType() {
        return type;
    }
}

enum InstructionType {
    // Define instruction types
    CACHE_HIT, CACHE_MISS, HALT, ALU_OPERATION, LOAD, STORE, JUMP, ADD, SUBTRACT, UNKNOWN, 
    MULTIPLY, DIVIDE, MODULUS
}