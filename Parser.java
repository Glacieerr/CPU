import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }
    
    // Checks if the current position is at the end of the tokens list
    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    public void parse() {
        while (!isAtEnd()) {
            parseStatement();
        }
    }
    
	// Utility method to advance the current token and return to previous
    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private void parseStatement() {
        if (isAtEnd()) return; // Prevent parsing if no more tokens are available
        Token token = advance();
        switch (token.getType()) {
            case MATH:
                parseMath();
                break;
            case ADD:
                parseAdd();
                break;
            case SUBTRACT:
                parseSubtract();
                break;
            case MULTIPLY:
                parseMultiply();
                break;
            case AND:
                parseAnd();
                break;
            case OR:
                parseOr();
                break;
            case NOT:
                parseNot();
                break;
            case XOR:
                parseXor();
                break;
            case COPY:
                parseCopy();
                break;
            case HALT:
                parseHalt();
                break;
            case BRANCH:
                parseBranch();
                break;
            case JUMP:
                parseJump();
                break;
            case CALL:
                parseCall();
                break;
            case PUSH:
                parsePush();
                break;
            case LOAD:
                parseLoad();
                break;
            case RETURN:
                parseReturn();
                break;
            case STORE:
                parseStore();
                break;
            case PEEK:
                parsePeek();
                break;
            case POP:
                parsePop();
                break;
            case INTERRUPT:
                parseInterrupt();
                break;
            case EQUAL:
                parseEqual();
                break;
            case UNEQUAL:
                parseUnequal();
                break;
            case GREATER:
                parseGreater();
                break;
            case LESS:
                parseLess();
                break;
            case GREATEROREQUAL:
                parseGreaterOrEqual();
                break;
            case LESSOREQUAL:
                parseLessOrEqual();
                break;
            case SHIFT:
                parseShift();
                break;
            case LEFT:
                parseLeft();
                break;
            case RIGHT:
                parseRight();
                break;
            default:
                throw new RuntimeException("Unexpected token: " + token.getType());
        }
    }
    
    // Define methods
    private void parseMath() {
        // More tokens to process for operation type and operands
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input after 'MATH'");

        // Expecting an operation type after MATH
        Token operation = advance();
        switch (operation.getType()) {
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
                // Parse operands for the operation
                Token operand1 = consume(TokenType.REGISTER, "Expect register as first operand.");
                consume(TokenType.COMMA, "Expect ',' between operands.");
                Token operand2 = consume(TokenType.REGISTER, "Expect register as second operand.");
                // Generate output for this MATH operation
                outputMathOperation(operation.getType(), operand1, operand2);
                break;
            default:
                throw new RuntimeException("Unexpected token '" + operation.getType() + "' after 'MATH'");
        }
    }
    
    private void parseAdd() {
        // Expecting two operands for the ADD operation, which could be registers or immediate values
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register as first operand for ADD.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register as second operand for ADD.");
        
        // Destination register for storing the result of the ADD operation
        Token destination = null;
        if (check(TokenType.COMMA)) { // Check if there's a third operand (destination register)
            consume(TokenType.COMMA, "Expected ',' after second operand.");
            destination = consumeExpected(TokenType.REGISTER, "Expected register as destination for ADD.");
        }

        // Handle the parsed ADD operation
        outputAddInstruction(operand1, operand2, destination);
    }

    // Consumes the next token if it matches the expected type, otherwise throws a parsing exception
    private Token consumeExpected(TokenType expected, String errorMessage) {
        if (!check(expected)) {
            throw new RuntimeException(errorMessage);
        }
        return advance();
    }

    // Method to output or process the parsed ADD instruction
    private void outputAddInstruction(Token operand1, Token operand2, Token destination) {
        String instruction = "ADD " + operand1.getValue() + ", " + operand2.getValue();
        if (destination != null) {
            instruction += ", " + destination.getValue(); // Include the destination if present
        }
        System.out.println(instruction);
        }

    private void parseSubtract() {
        // The SUBTRACT instruction syntax 
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register as first operand for SUBTRACT.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register as second operand for SUBTRACT.");
        
        // Handling a possible destination register
        Token destination = null;
        if (check(TokenType.COMMA)) { // Treat it as the destination register
            consume(TokenType.COMMA, "Expected ',' after second operand for destination register.");
            destination = consumeExpected(TokenType.REGISTER, "Expected register as destination for SUBTRACT.");
        }

        // Handling the parsed SUBTRACT operation
        outputSubtractInstruction(operand1, operand2, destination);
    }

    // Utility methods
    private void outputSubtractInstruction(Token operand1, Token operand2, Token destination) {
        String instruction = "SUBTRACT " + operand1.getValue() + ", " + operand2.getValue();
        if (destination != null) {
            instruction += ", " + destination.getValue(); // Including the destination if specified
        }
        System.out.println(instruction);
    }

    private void parseMultiply() {
        // Structure to parseSubtract, adjusted for MULTIPLY
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register as first operand for MULTIPLY.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register as second operand for MULTIPLY.");
        
        // Destination register for the result
        Token destination = null;
        if (check(TokenType.COMMA)) {
            consume(TokenType.COMMA, "Expected ',' after second operand.");
            destination = consumeExpected(TokenType.REGISTER, "Expected register as destination for MULTIPLY.");
        }

        // Generate output or process the parsed MULTIPLY instruction
        outputMultiplyInstruction(operand1, operand2, destination);
    }

    private void outputMultiplyInstruction(Token operand1, Token operand2, Token destination) {
        String instruction = "MULTIPLY " + operand1.getValue() + ", " + operand2.getValue();
        if (destination != null) {
            instruction += ", " + destination.getValue(); // Include the destination if present
        }
        System.out.println(instruction);
    }

    private void parseAnd() {
        // AND operation syntax is AND operand1 and operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register as first operand for AND.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register as second operand for AND.");

        // Handle the parsed AND operation
        outputAndInstruction(operand1, operand2);
    }

    // Output or process the parsed AND instruction
    private void outputAndInstruction(Token operand1, Token operand2) {
        System.out.println("AND " + operand1.getValue() + " " + operand2.getValue());
    }

    private void parseOr() {
        // Check if already at the end of the tokens list to prevent errors
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during OR instruction parsing.");

        // OR operation syntax is OR operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register as first operand for OR.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register as second operand for OR.");

        outputOrInstruction(operand1, operand2);
    }

    // Output or process the parsed OR instruction
    private void outputOrInstruction(Token operand1, Token operand2) {
        System.out.println("OR " + operand1.getValue() + ", " + operand2.getValue());
    }


    private void parseNot() {
        // Check if already at the end of the tokens list to prevent errors
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during NOT instruction parsing.");

        // NOT operation syntax is: NOT operand
        Token operand = consumeExpected(TokenType.REGISTER, "Expected register as operand for NOT.");

        // Handle the parsed NOT operation
        outputNotInstruction(operand);
    }

    // Output or process the parsed NOT instruction
    private void outputNotInstruction(Token operand) {
        System.out.println("NOT " + operand.getValue());
    }


    private void parseXor() {
        // Check for premature end of input
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during XOR instruction parsing.");

        // Expecting two operands for the XOR operation
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register as first operand for XOR.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register as second operand for XOR.");

        // Handle the parsed XOR operation
        outputXorInstruction(operand1, operand2);
    }

    // Output or process the parsed XOR instruction
    private void outputXorInstruction(Token operand1, Token operand2) {
        System.out.println("XOR " + operand1.getValue() + ", " + operand2.getValue());
    }


    private void parseCopy() {
        // Check for premature end of input to prevent errors
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during COPY instruction parsing.");

        // Expecting two operands for the COPY operation
        Token destination = consumeExpected(TokenType.REGISTER, "Expected register as destination operand for COPY.");
        consumeExpected(TokenType.COMMA, "Expected ',' after destination operand.");
        Token source = consumeExpected(TokenType.REGISTER, "Expected register as source operand for COPY.");

        // Handle the parsed COPY operation
        outputCopyInstruction(destination, source);
    }

    // Output or process the parsed COPY instruction
    private void outputCopyInstruction(Token destination, Token source) {
        System.out.println("COPY " + destination.getValue() + ", " + source.getValue());
    }

    private void parseHalt() {
        
        // If anything improperly follows the HALT instruction in the same line
        if (!isAtEnd() && peek().getType() != TokenType.SEMICOLON && peek().getType() != TokenType.EOF) {
            throw new RuntimeException("Unexpected tokens after HALT instruction.");
        }

        // Handle the HALT operation
        outputHaltInstruction();
    }

    // Output or process the parsed HALT instruction
    private void outputHaltInstruction() {
        System.out.println("HALT");
    }


    private void parseBranch() {
        // Check for premature end of input to prevent errors
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during BRANCH instruction parsing.");

        // BRANCH operation syntax is BRANCH condition, targetLabel
        Token condition = consumeExpected(TokenType.CONDITION, "Expected condition after BRANCH.");
        consumeExpected(TokenType.COMMA, "Expected ',' after condition.");
        Token targetLabel = consumeExpected(TokenType.LABEL, "Expected label as target for BRANCH.");

        // Handle the parsed BRANCH operation
        outputBranchInstruction(condition, targetLabel);
    }

    // Output or process the parsed BRANCH instruction
    private void outputBranchInstruction(Token condition, Token targetLabel) {
        System.out.println("BRANCH " + condition.getValue() + ", " + targetLabel.getValue());
    }


    private void parseJump() {
        // Check if already at the end of the tokens list to prevent errors
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during JUMP instruction parsing.");

        // JUMP operation syntax  JUMP targetLabel
        Token targetLabel = consumeExpected(TokenType.LABEL, "Expected label as target for JUMP.");

        // Handle the parsed JUMP operation
        outputJumpInstruction(targetLabel);
    }

    // Output or process the parsed JUMP instruction
    private void outputJumpInstruction(Token targetLabel) {
        System.out.println("JUMP " + targetLabel.getValue());
    }

    private void parseCall() {
        // Check for premature end of input
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during CALL instruction parsing.");

        // CALL operation syntax is CALL targetLabel
        Token targetLabel = consumeExpected(TokenType.LABEL, "Expected label as target for CALL.");

        // Handle the parsed CALL operation
        outputCallInstruction(targetLabel);
    }

    // Output or process the parsed CALL instruction
    private void outputCallInstruction(Token targetLabel) {
        System.out.println("CALL " + targetLabel.getValue());
    }


    private void parsePush() {
        // Check for premature end of input
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during PUSH instruction parsing.");

        // PUSH operation syntax allows for either a register or an immediate value
        Token valueToken = advance(); // Advance without checking type
        
        // Check if valueToken is of an expected type
        if (!(valueToken.getType() == TokenType.REGISTER || valueToken.getType() == TokenType.NUMBER)) {
            throw new RuntimeException("Expected register or immediate value as operand for PUSH.");
        }

        // Handle the parsed PUSH operation
        outputPushInstruction(valueToken);
    }

    private void outputPushInstruction(Token valueToken) {
        System.out.println("PUSH " + valueToken.getValue());
    }


    private void parseLoad() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during LOAD instruction parsing.");
        
        // Expecting a destination register and a memory address/source
        Token destination = consumeExpected(TokenType.REGISTER, "Expected destination register for LOAD.");
        consumeExpected(TokenType.COMMA, "Expected ',' after destination register.");
        Token source = consumeExpected(TokenType.MEMORY_ADDRESS, "Expected memory address as source for LOAD.");
        
        // Handle the parsed LOAD operation
        outputLoadInstruction(destination, source);
    }

    private void outputLoadInstruction(Token destination, Token source) {
        System.out.println("LOAD " + destination.getValue() + ", " + source.getValue());
    }


    private void parseReturn() {
        if (isAtEnd()) throw new RuntimeException("Unexpected token after RETURN instruction.");
        // Handle the parsed RETURN operation
        outputReturnInstruction();
    }

    private void outputReturnInstruction() {
        System.out.println("RETURN");
    }


    private void parseStore() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during STORE instruction parsing.");

        // STORE operation syntax is STORE sourceRegister, memoryAddress
        Token sourceRegister = consumeExpected(TokenType.REGISTER, "Expected register as source for STORE.");
        consumeExpected(TokenType.COMMA, "Expected ',' after source register.");
        Token memoryAddress = consumeExpected(TokenType.ADDRESS, "Expected memory address as destination for STORE.");

        outputStoreInstruction(sourceRegister, memoryAddress);
    }

    private void outputStoreInstruction(Token sourceRegister, Token memoryAddress) {
        System.out.println("STORE " + sourceRegister.getValue() + ", " + memoryAddress.getValue());
    }


    private void parsePeek() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during PEEK instruction parsing.");
        outputPeekInstruction();
    }

    private void outputPeekInstruction() {
        System.out.println("PEEK");
    }

    private void parsePop() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during POP instruction parsing.");

        // POP operation syntax is POP destinationRegister
        Token destinationRegister = consumeExpected(TokenType.REGISTER, "Expected register as destination for POP.");

        outputPopInstruction(destinationRegister);
    }

    private void outputPopInstruction(Token destinationRegister) {
        System.out.println("POP " + destinationRegister.getValue());
    }


    private void parseInterrupt() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during INTERRUPT instruction parsing.");

        // INTERRUPT operation syntax is INTERRUPT interruptNumber
        Token interruptNumber = consumeExpected(TokenType.NUMBER, "Expected interrupt number after INTERRUPT.");

        outputInterruptInstruction(interruptNumber);
    }

    private void outputInterruptInstruction(Token interruptNumber) {
        System.out.println("INTERRUPT " + interruptNumber.getValue());
    }

    
    private void parseEqual() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during EQUAL condition parsing.");

        // EQUAL condition syntax might be EQUAL operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register or value as first operand for EQUAL.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register or value as second operand for EQUAL.");

        outputEqualCondition(operand1, operand2);
    }

    private void outputEqualCondition(Token operand1, Token operand2) {
        System.out.println("EQUAL " + operand1.getValue() + ", " + operand2.getValue());
    }

    
    private void parseUnequal() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during UNEQUAL condition parsing.");

        // UNEQUAL condition syntax are UNEQUAL operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register or value as first operand for UNEQUAL.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register or value as second operand for UNEQUAL.");

        outputUnequalCondition(operand1, operand2);
    }

    private void outputUnequalCondition(Token operand1, Token operand2) {
        System.out.println("UNEQUAL " + operand1.getValue() + ", " + operand2.getValue());
    }

    
    private void parseGreater() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during GREATER condition parsing.");

        // GREATER condition syntax are GREATER operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register or value as first operand for GREATER.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register or value as second operand for GREATER.");

        outputGreaterCondition(operand1, operand2);
    }

    private void outputGreaterCondition(Token operand1, Token operand2) {
        System.out.println("GREATER " + operand1.getValue() + ", " + operand2.getValue());
    }

    
    private void parseLess() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during LESS condition parsing.");

        // LESS condition syntax are LESS operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register or value as first operand for LESS.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register or value as second operand for LESS.");

        outputLessCondition(operand1, operand2);
    }

    private void outputLessCondition(Token operand1, Token operand2) {
        System.out.println("LESS " + operand1.getValue() + ", " + operand2.getValue());
    }

    
    private void parseGreaterOrEqual() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during GREATER OR EQUAL condition parsing.");

        // GREATER_OR_EQUAL operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register or value as first operand for GREATER_OR_EQUAL.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register or value as second operand for GREATER_OR_EQUAL.");

        outputGreaterOrEqualCondition(operand1, operand2);
    }

    private void outputGreaterOrEqualCondition(Token operand1, Token operand2) {
        System.out.println("GREATER_OR_EQUAL " + operand1.getValue() + ", " + operand2.getValue());
    }

    
    private void parseLessOrEqual() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during LESS OR EQUAL condition parsing.");

        // LESS_OR_EQUAL operand1, operand2
        Token operand1 = consumeExpected(TokenType.REGISTER, "Expected register or value as first operand for LESS_OR_EQUAL.");
        consumeExpected(TokenType.COMMA, "Expected ',' after first operand.");
        Token operand2 = consumeExpected(TokenType.REGISTER, "Expected register or value as second operand for LESS_OR_EQUAL.");

        outputLessOrEqualCondition(operand1, operand2);
    }

    private void outputLessOrEqualCondition(Token operand1, Token operand2) {
        System.out.println("LESS_OR_EQUAL " + operand1.getValue() + ", " + operand2.getValue());
    }

    
    private void parseShift() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during SHIFT instruction parsing.");

        // SHIFT direction, register, positions
        Token direction = consumeExpected(TokenType.DIRECTION, "Expected direction (LEFT or RIGHT) for SHIFT.");
        consumeExpected(TokenType.COMMA, "Expected ',' after direction.");
        Token register = consumeExpected(TokenType.REGISTER, "Expected register for SHIFT.");
        consumeExpected(TokenType.COMMA, "Expected ',' after register.");
        Token positions = consumeExpected(TokenType.NUMBER, "Expected number of positions to shift.");

        outputShiftInstruction(direction, register, positions);
    }

    private void outputShiftInstruction(Token direction, Token register, Token positions) {
        System.out.println("SHIFT " + direction.getValue() + ", " + register.getValue() + ", " + positions.getValue());
    }

    
    private void parseLeft() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during LEFT shift parsing.");

        // LEFT destinationRegister, sourceRegister, shiftAmount
        Token destinationRegister = consumeExpected(TokenType.REGISTER, "Expected register as destination for LEFT shift.");
        consumeExpected(TokenType.COMMA, "Expected ',' after destination register.");
        Token sourceRegister = consumeExpected(TokenType.REGISTER, "Expected register as source for LEFT shift.");
        consumeExpected(TokenType.COMMA, "Expected ',' after source register.");
        Token shiftAmount = consumeExpected(TokenType.NUMBER, "Expected numeric shift amount for LEFT shift.");

        outputLeftShiftInstruction(destinationRegister, sourceRegister, shiftAmount);
    }

    private void outputLeftShiftInstruction(Token destinationRegister, Token sourceRegister, Token shiftAmount) {
        System.out.println("LEFT " + destinationRegister.getValue() + ", " + sourceRegister.getValue() + ", " + shiftAmount.getValue());
    }

    
    private void parseRight() {
        if (isAtEnd()) throw new RuntimeException("Unexpected end of input during RIGHT shift parsing.");

        // RIGHT destinationRegister, sourceRegister, shiftAmount
        Token destinationRegister = consumeExpected(TokenType.REGISTER, "Expected register as destination for RIGHT shift.");
        consumeExpected(TokenType.COMMA, "Expected ',' after destination register.");
        Token sourceRegister = consumeExpected(TokenType.REGISTER, "Expected register as source for RIGHT shift.");
        consumeExpected(TokenType.COMMA, "Expected ',' after source register.");
        Token shiftAmount = consumeExpected(TokenType.NUMBER, "Expected numeric shift amount for RIGHT shift.");

        outputRightShiftInstruction(destinationRegister, sourceRegister, shiftAmount);
    }

    private void outputRightShiftInstruction(Token destinationRegister, Token sourceRegister, Token shiftAmount) {
        System.out.println("RIGHT " + destinationRegister.getValue() + ", " + sourceRegister.getValue() + ", " + shiftAmount.getValue());
    }
    

    // Consumes the next token if it's of the expected type, otherwise throws an error
    private Token consume(TokenType type, String errorMessage) {
        if (check(type)) return advance();
        throw new RuntimeException(errorMessage);
    }

    // Checks if the current token is of the expected type
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    // Returns the current token without consuming it
    private Token peek() {
        return tokens.get(current);
    }

    private void outputMathOperation(TokenType operation, Token operand1, Token operand2) {
        System.out.println(operation + " " + operand1.getValue() + ", " + operand2.getValue());
    }
}