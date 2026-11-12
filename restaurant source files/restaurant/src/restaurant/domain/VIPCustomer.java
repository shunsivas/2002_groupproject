package restaurant.domain;

import java.util.Random;

public class VIPCustomer extends Customer {
    public VIPCustomer(int number, int arrivalTurn) { super(number, arrivalTurn); }

    @Override public String typeLabel() { return "VIP"; }
    @Override public int waitingPenalty() { return 5; }
    @Override public int billFor(MenuItem item) { return item.priceCents() * 90 / 100; }
    @Override protected MenuItem chooseDish(Random rng) {
        return MenuItem.values()[rng.nextInt(MenuItem.values().length)];
    }
    @Override public String describe() { return "loses 5/turn, 10% off"; }
}
