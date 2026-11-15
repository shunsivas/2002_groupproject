package restaurant.domain;

import java.util.Random;


public class CriticCustomer extends Customer {
    public CriticCustomer(int number, int arrivalTurn) { super(number, arrivalTurn); }

    @Override public String typeLabel() { return "Critic"; }
    @Override public int waitingPenalty() { return 12; }
    @Override public int billFor(MenuEntry item) { return item.priceCents(); }
    @Override protected MenuEntry chooseDish(Random rng) { return MenuItem.PASTA; }
    @Override public String describe() { return "loses 12/turn, always orders Pasta"; }
}
