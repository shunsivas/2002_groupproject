package restaurant.domain;

import java.util.Random;

public class RegularCustomer extends Customer {
    public RegularCustomer(int number, int arrivalTurn) { super(number, arrivalTurn); }

    @Override public String typeLabel() { return "Regular"; }
    @Override public int waitingPenalty() { return 8; }
    @Override public int billFor(MenuItem item) { return item.priceCents(); }
    @Override protected MenuItem chooseDish(Random rng) {
        return MenuItem.values()[rng.nextInt(MenuItem.values().length)];
    }
    @Override public String describe() { return "loses 8/turn, full price"; }
}
