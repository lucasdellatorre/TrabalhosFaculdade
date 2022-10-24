public enum Animals {
    EMPTY(0), PIG(1), CHICKEN(2);

    private final int value;

    Animals(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
