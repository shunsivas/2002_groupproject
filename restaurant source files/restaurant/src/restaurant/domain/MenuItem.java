package restaurant.domain;

public enum MenuItem {
    SALAD("Salad", 900, 1),
    BURGER("Burger", 1500, 1),
    PASTA("Pasta", 1800, 2);

    private final String label;
    private final int priceCents;
    private final int prepUnits;

    MenuItem(String label, int priceCents, int prepUnits) {
        this.label = label;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    public String label() { return label; }
    public int priceCents() { return priceCents; }
    public int prepUnits() { return prepUnits; }
}
