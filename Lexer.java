import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String input) {
        this.input = input;
        tokenize();
    }

    private void tokenize() {
        // Extended tokenization logic to include all keywords and patterns for registers, numbers, and symbols
        String keywordsPattern = "\\b(math|add|subtract|multiply|and|or|not|xor|copy|halt|branch|jump|call|push|load|return|store|peek|pop|interrupt|equal|unequal|greater|less|greaterOrEqual|lessOrEqual|shift|left|right)\\b";
        String registerPattern = "\\b(r[0-9]+)\\b";
        String numberPattern = "\\b([0-9]+)\\b";
        String commaPattern = ",";
        String semicolonPattern = ";";
        
        // Combine all patterns
        String tokenPatterns = String.format("(%s)|(%s)|(%s)|(%s)|(%s)", keywordsPattern, registerPattern, numberPattern, commaPattern, semicolonPattern);
        
        Matcher matcher = Pattern.compile(tokenPatterns).matcher(input);
        while (matcher.find()) {
            // Check which group was matched and create the appropriate token
            if (matcher.group(1) != null) {
                // Keywords
                tokens.add(new Token(TokenType.valueOf(matcher.group(1).toUpperCase()), matcher.group(1)));
            } 
            else if (matcher.group(2) != null) {
                // Registers
                tokens.add(new Token(TokenType.REGISTER, matcher.group(2)));
            } 
            else if (matcher.group(3) != null) {
                // Numbers
                tokens.add(new Token(TokenType.NUMBER, matcher.group(3)));
            } 
            else if (matcher.group(4) != null) {
                // Commas
                tokens.add(new Token(TokenType.COMMA, matcher.group(4)));
            } 
            else if (matcher.group(5) != null) {
                // Semicolons
                tokens.add(new Token(TokenType.SEMICOLON, matcher.group(5)));
            }
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }
}