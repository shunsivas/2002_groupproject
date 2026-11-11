package restaurant.domain;

public enum CustomerType {
    REGULAR("Regular", 8);

    private final String label;
    private final int patiencePenalty;

    CustomerType(String label, int patiencePenalty) {
        this.label = label;
        this.patiencePenalty = patiencePenalty;
    }

    public String label() { return label; }
    public int patiencePenalty() { return patiencePenalty; }
}
