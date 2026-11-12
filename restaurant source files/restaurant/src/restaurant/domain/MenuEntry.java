package restaurant.domain;

/** Anything a customer can order: a single {@link MenuItem} or a {@link ComboMeal}. */
public interface MenuEntry {
    String label();
    int priceCents();
    int prepUnits();
}
