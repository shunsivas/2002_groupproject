package restaurant.control;

import restaurant.domain.*;

import java.util.ArrayList;
import java.util.List;


public class GameController {
    private final GameConfig config;
    private final Restaurant restaurant;
    private final Waiter waiter = new Waiter();
    private final Chef chef = new Chef();
    private final Cashier cashier = new Cashier();
    private int turn = 0;
    private boolean turnOpen = false;

    public GameController(GameConfig config) {
        this.config = config;
        this.restaurant = new Restaurant(config.tables(), config.seed());
    }

    public GameConfig config() { return config; }
    public Restaurant restaurant() { return restaurant; }
    public Waiter waiter() { return waiter; }
    public Chef chef() { return chef; }
    public int turn() { return turn; }
    public boolean isOver() { return turn >= config.maxTurns() && !turnOpen; }

    // ---- Step 1: arrivals ----
    public List<String> beginTurn() {
        if (turnOpen) throw new IllegalStateException("Turn already open");
        if (isOver()) throw new IllegalStateException("Game is over");
        turn++;
        turnOpen = true;
        waiter.startTurn();
        chef.startTurn();
        cashier.startTurn();
        List<String> events = new ArrayList<>();
        if (config.arrivalTurns().contains(turn)) {
            Customer c = restaurant.customerArrives(turn);
            events.add(c.label() + " (" + c.type().label() + ") arrives and joins the queue with satisfaction 100.");
        }
        return events;
    }

    // ---- Steps 2-3: manager choices (the Waiter acts first) ----
    public ActionResult waiterSeat(int customerNo, Integer tableNo) {
        return waiterGuard() != null ? waiterGuard() : waiter.seat(restaurant, customerNo, tableNo);
    }

    public ActionResult waiterTakeOrder(int customerNo) {
        return waiterGuard() != null ? waiterGuard() : waiter.takeOrder(restaurant, customerNo);
    }

    public ActionResult waiterServe(int customerNo) {
        return waiterGuard() != null ? waiterGuard() : waiter.serve(restaurant, customerNo, turn);
    }

    public ActionResult chefCook(int customerNo) {
        if (!turnOpen) return ActionResult.fail("No turn is in progress.");
        return chef.cook(restaurant, customerNo);
    }

    private ActionResult waiterGuard() {
        if (!turnOpen) return ActionResult.fail("No turn is in progress.");
        if (chef.hasActed())
            return ActionResult.fail("The Waiter acts before the Chef; the Chef has already acted.");
        return null;
    }

    /** Currently possible Waiter tasks, as command hints (for display only). */
    public List<String> availableWaiterTasks() {
        List<String> tasks = new ArrayList<>();
        if (waiter.hasActed()) return tasks;
        Table free = restaurant.firstFreeTable();
        for (Customer c : restaurant.queue())
            if (free != null) tasks.add("seat " + c.number() + " [table]   - seat " + c.label());
        for (Customer c : restaurant.customers()) {
            if (c.state() == CustomerState.SEATED)
                tasks.add("take " + c.number() + "   - take " + c.label() + "'s order");
            if (c.state() == CustomerState.ORDERED && c.order().status() == OrderStatus.READY)
                tasks.add("serve " + c.number() + "   - serve " + c.order().item().label() + " to " + c.label());
        }
        return tasks;
    }

    /** Currently possible Chef tasks, as command hints (for display only). */
    public List<String> availableChefTasks() {
        List<String> tasks = new ArrayList<>();
        if (chef.hasActed()) return tasks;
        for (Order o : restaurant.activeOrders())
            if (o.isCookable())
                tasks.add("cook " + o.customer().number() + "   - advance " + o.customer().label() + "'s "
                        + o.item().label() + " (" + o.remainingUnits() + " unit(s) left)");
        return tasks;
    }

    // ---- Steps 4-5 (and 6 after the last turn) ----
    public List<String> endTurn() {
        if (!turnOpen) throw new IllegalStateException("No turn open");
        List<String> events = new ArrayList<>();
        // Step 4: payment of customers who were ready on an earlier turn, then eating progress.
        Customer paid = cashier.collectPayment(restaurant, turn);
        if (paid != null) {
            events.add("Cashier collects " + Money.format(paid.order().billCents()) + " from "
                    + paid.label() + " (satisfaction " + paid.paidSatisfaction() + "). Table freed.");
        }
        restaurant.finishEating(turn, events);
        // Step 5: waiting and abandonment.
        restaurant.endOfTurnWaiting(events);
        turnOpen = false;
        return events;
    }

    /** Step 6: only valid once all turns have been played. */
    public Outcome result() {
        if (!isOver()) throw new IllegalStateException("Game not finished");
        boolean paidOk = restaurant.paidCount() >= config.minPaid();
        boolean revOk = restaurant.revenueCents() >= config.minRevenueCents();
        boolean satOk = restaurant.averageSatisfactionAtLeast(config.minAvgSatisfaction());
        return new Outcome(paidOk && revOk && satOk, paidOk, revOk, satOk);
    }
}
