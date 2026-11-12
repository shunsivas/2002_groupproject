package restaurant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Aggregate holding the whole restaurant state: tables, queue, customers, money and counters. */
public class Restaurant {
    private final List<Table> tables = new ArrayList<>();
    private final List<Customer> queue = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final Random menuRandom;
    private final HappyHour happyHour = new HappyHour();
    private long revenueCents;
    private int servedCount;
    private int unhappyDepartures;
    private int paidCount;
    private long paidSatisfactionSum;

    public Restaurant(int tableCount, long seed) {
        for (int i = 1; i <= tableCount; i++) tables.add(new Table(i));
        this.menuRandom = new Random(seed);
    }

    // ---- read access ----
    public List<Table> tables() { return Collections.unmodifiableList(tables); }
    public List<Customer> queue() { return Collections.unmodifiableList(queue); }
    public List<Customer> customers() { return Collections.unmodifiableList(customers); }
    public HappyHour happyHour() { return happyHour; }
    public long revenueCents() { return revenueCents; }
    public int servedCount() { return servedCount; }
    public int unhappyDepartures() { return unhappyDepartures; }
    public int paidCount() { return paidCount; }

    /** Average satisfaction recorded at payment, or null if nobody has paid. */
    public Double averageSatisfaction() {
        return paidCount == 0 ? null : (double) paidSatisfactionSum / paidCount;
    }

    /** Exact integer check for "average >= threshold". */
    public boolean averageSatisfactionAtLeast(int threshold) {
        return paidCount > 0 && paidSatisfactionSum >= (long) threshold * paidCount;
    }

    public Customer findCustomer(int number) {
        for (Customer c : customers) if (c.number() == number) return c;
        return null;
    }

    public Table findTable(int number) {
        for (Table t : tables) if (t.number() == number) return t;
        return null;
    }

    public Table firstFreeTable() {
        for (Table t : tables) if (t.isFree()) return t;
        return null;
    }

    public List<Order> activeOrders() {
        List<Order> list = new ArrayList<>();
        for (Customer c : customers)
            if (c.order() != null && (c.order().isCookable() || c.order().status() == OrderStatus.READY))
                list.add(c.order());
        return list;
    }

    // ---- mutations (package-private: only domain staff / the turn controller's domain calls use them) ----

    /**
     * A customer of the given kind arrives and joins the queue. The customer decides its own dish
     * (see each subclass); Regular and VIP use the seeded random generator, so runs are reproducible.
     */
    public Customer customerArrives(int turn, CustomerKind kind) {
        Customer c = kind.create(customers.size() + 1, turn);
        c.decideDish(menuRandom);
        customers.add(c);
        queue.add(c);
        return c;
    }

    void removeFromQueue(Customer c) { queue.remove(c); }
    void recordServed() { servedCount++; }

    void recordPayment(Customer c) {
        revenueCents += c.order().billCents();
        paidCount++;
        paidSatisfactionSum += c.satisfaction();
    }

    /** Happy Hour: customers still waiting for a table or food recover satisfaction (max 100). */
    public void applyHappyHourRecovery() {
        for (Customer c : customers)
            if (c.state().losesSatisfaction()) c.recover(HappyHour.RECOVERY);
    }

    /** Customers served on an earlier turn finish eating and become READY_TO_PAY. */
    public void finishEating(int turn, List<String> events) {
        for (Customer c : customers) {
            if (c.state() == CustomerState.SERVED && c.servedTurn() < turn) {
                c.finishEating(turn);
                events.add(c.label() + " finished eating and is ready to pay.");
            }
        }
    }

    public void endOfTurnWaiting(List<String> events) {
        for (Customer c : customers) {
            if (!c.state().losesSatisfaction()) continue;
            if (c.waitOneTurn()) {
                boolean wasQueued = c.state() == CustomerState.WAITING;
                if (wasQueued) queue.remove(c);
                c.abandon();
                unhappyDepartures++;
                events.add(c.label() + " lost patience and left without paying.");
            }
        }
    }
}
