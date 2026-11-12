package restaurant.domain;

/** The three menu items. Prices are stored as integer cents. */
public enum MenuItem implements MenuEntry {
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

    @Override public String label() { return label; }
    @Override public int priceCents() { return priceCents; }
    @Override public int prepUnits() { return prepUnits; }
}
