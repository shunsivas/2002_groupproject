package restaurant.domain;

/**
 * Base class for all customers. Type-specific behaviour (waiting loss, bill, dish choice)
 * is dispatched through the abstract methods below, never through type checks elsewhere.
 */
public abstract class Customer {
    private final int number;
    private MenuEntry choice; // chosen at arrival, revealed once the order is placed
    private final int arrivalTurn;
    private int satisfaction = 100;
    private CustomerState state = CustomerState.WAITING;
    private Table table;
    private Order order;
    private int servedTurn = -1;
    private int readyToPayTurn = -1;
    private int paidSatisfaction = -1;

    protected Customer(int number, int arrivalTurn) {
        this.number = number;
        this.arrivalTurn = arrivalTurn;
    }

    // ---- behaviour that differs per customer type ----
    /** Display name of the customer type. */
    public abstract String typeLabel();
    /** Satisfaction points lost per turn while not yet served. */
    public abstract int waitingPenalty();
    /** Bill in cents for the given dish. */
    public abstract int billFor(MenuEntry item);
    /** Picks this customer's dish. Documented rule per type. */
    protected abstract MenuEntry chooseDish(java.util.Random rng);
    /** One-line description of the type's special rule, for the UI. */
    public abstract String describe();

    /** Called once on arrival. */
    public void decideDish(java.util.Random rng) { this.choice = chooseDish(rng); }

    public int number() { return number; }
    public String label() { return "C" + number; }
    public int arrivalTurn() { return arrivalTurn; }
    public int satisfaction() { return satisfaction; }
    public CustomerState state() { return state; }
    public Table table() { return table; }
    public Order order() { return order; }
    public int servedTurn() { return servedTurn; }
    public int readyToPayTurn() { return readyToPayTurn; }
    public int paidSatisfaction() { return paidSatisfaction; }
    MenuEntry choice() { return choice; }

    void seatAt(Table t) { table = t; t.occupy(this); state = CustomerState.SEATED; }

    /** The bill is locked now: customer discount first, then Happy Hour if active. */
    Order placeOrder(HappyHour happyHour) {
        order = new Order(this, choice, happyHour.adjust(billFor(choice)));
        state = CustomerState.ORDERED;
        return order;
    }

    void serve(int turn) {
        order.markServed();
        state = CustomerState.SERVED;
        servedTurn = turn;
    }

    void finishEating(int turn) { state = CustomerState.READY_TO_PAY; readyToPayTurn = turn; }

    void pay() {
        order.markPaid();
        state = CustomerState.PAID;
        paidSatisfaction = satisfaction;
        releaseTable();
    }

    /** Applies the end-of-turn waiting penalty. Returns true if the customer abandons. */
    boolean waitOneTurn() {
        satisfaction = Math.max(0, satisfaction - waitingPenalty());
        return satisfaction == 0;
    }

    void recover(int points) { satisfaction = Math.min(100, satisfaction + points); }

    void abandon() {
        if (order != null) order.cancel();
        state = CustomerState.LEFT;
        releaseTable();
    }

    private void releaseTable() {
        if (table != null) { table.free(); table = null; }
    }
}
