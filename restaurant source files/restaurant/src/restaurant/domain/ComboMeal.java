package restaurant.domain;

import java.util.List;


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

    public static List<MenuEntry> fullMenu() {
        return List.of(MenuItem.SALAD, MenuItem.BURGER, MenuItem.PASTA, SALAD_BURGER, SALAD_PASTA, BURGER_PASTA);
    }
}
