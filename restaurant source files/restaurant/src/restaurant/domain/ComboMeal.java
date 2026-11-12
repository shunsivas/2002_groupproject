package restaurant.domain;

import java.util.List;

/**
 * A combo of two existing menu items. Preparation units are the sum of the two components;
 * the price is 90% of their combined price, rounded to the nearest cent
 * (e.g. Salad + Pasta = ($9 + $18) x 0.9 = $24.30, 3 units).
 */
public final class ComboMeal implements MenuEntry {
    public static final ComboMeal SALAD_BURGER = new ComboMeal(MenuItem.SALAD, MenuItem.BURGER);
    public static final ComboMeal SALAD_PASTA = new ComboMeal(MenuItem.SALAD, MenuItem.PASTA);
    public static final ComboMeal BURGER_PASTA = new ComboMeal(MenuItem.BURGER, MenuItem.PASTA);

    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second) {
        this.first = first;
        this.second = second;
    }

    @Override public String label() { return first.label() + " + " + second.label() + " combo"; }
    @Override public int prepUnits() { return first.prepUnits() + second.prepUnits(); }
    @Override public int priceCents() {
        return Money.percentOf(first.priceCents() + second.priceCents(), 90);
    }

    /** The full menu a Regular or VIP customer chooses from: 3 items + 3 combos. */
    public static List<MenuEntry> fullMenu() {
        return List.of(MenuItem.SALAD, MenuItem.BURGER, MenuItem.PASTA, SALAD_BURGER, SALAD_PASTA, BURGER_PASTA);
    }
}
