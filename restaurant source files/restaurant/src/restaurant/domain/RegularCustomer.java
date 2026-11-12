package restaurant.domain;

import java.util.List;
import java.util.Random;

/** Loses 8 per waiting turn, pays full price, picks uniformly at random (seeded) from the full menu: 3 items + 3 combos. */
public class RegularCustomer extends Customer {
    public RegularCustomer(int number, int arrivalTurn) { super(number, arrivalTurn); }

    @Override public String typeLabel() { return "Regular"; }
    @Override public int waitingPenalty() { return 8; }
    @Override public int billFor(MenuEntry item) { return item.priceCents(); }
    @Override protected MenuEntry chooseDish(Random rng) {
        List<MenuEntry> menu = ComboMeal.fullMenu();
        return menu.get(rng.nextInt(menu.size()));
    }
    @Override public String describe() { return "loses 8/turn, full price"; }
}
