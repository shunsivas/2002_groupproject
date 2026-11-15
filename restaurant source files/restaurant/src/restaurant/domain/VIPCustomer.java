package restaurant.domain;

import java.util.List;
import java.util.Random;

public class VIPCustomer extends Customer {
    public VIPCustomer(int number, int arrivalTurn) { super(number, arrivalTurn); }

    @Override public String typeLabel() { return "VIP"; }
    @Override public int waitingPenalty() { return 5; }
    @Override public int billFor(MenuEntry item) { return Money.percentOf(item.priceCents(), 90); }
    @Override protected MenuEntry chooseDish(Random rng) {
        List<MenuEntry> menu = ComboMeal.fullMenu();
        return menu.get(rng.nextInt(menu.size()));
    }
    @Override public String describe() { return "loses 5/turn, 10% off"; }
}
