public class Bit {
    private boolean value;

    public Bit() {
        this.value = false;
    }

    public Bit(boolean value) {
        this.value = value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }

    public void set() {
        this.value = true;
    }

    public void clear() {
        this.value = false;
    }

    public boolean getValue() {
        return this.value;
    }

    public Bit and(Bit other) {
        return new Bit(this.value && other.value);
    }

    public Bit or(Bit other) {
        return new Bit(this.value || other.value);
    }

    public Bit xor(Bit other) {
        return new Bit(this.value ^ other.value);
    }

    public Bit not() {
        return new Bit(!this.value);
    }

    @Override
    public String toString() {
        return this.value ? "t" : "f";
    }
}