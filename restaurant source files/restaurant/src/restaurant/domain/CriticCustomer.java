package restaurant.domain;

import java.util.List;
import java.util.Random;

/**
 * Loses 12 per waiting turn and pays full price. Special behaviour (order preference): a Critic
 * always orders the Pasta, the most expensive dish and the one that takes 2 preparation units,
 * so the manager must prioritise the kitchen while the Critic's satisfaction drops quickly.
 */
public class CriticCustomer extends Customer {
    public CriticCustomer(int number, int arrivalTurn) { super(number, arrivalTurn); }

    @Override public String typeLabel() { return "Critic"; }
    @Override public int waitingPenalty() { return 12; }
    @Override public int billFor(MenuEntry item) { return item.priceCents(); }
    @Override protected MenuEntry chooseDish(Random rng) { return MenuItem.PASTA; }
    @Override public String describe() { return "loses 12/turn, always orders Pasta"; }
}
