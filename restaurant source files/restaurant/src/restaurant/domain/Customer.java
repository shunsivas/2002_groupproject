package restaurant.domain;

public class Customer {
    private final int number;
    private final CustomerType type;
    private final MenuItem choice;
    private final int arrivalTurn;
    private int satisfaction = 100;
    private CustomerState state = CustomerState.WAITING;
    private Table table;
    private Order order;
    private int servedTurn = -1;
    private int readyToPayTurn = -1;
    private int paidSatisfaction = -1;

    public Customer(int number, CustomerType type, MenuItem choice, int arrivalTurn) {
        this.number = number;
        this.type = type;
        this.choice = choice;
        this.arrivalTurn = arrivalTurn;
    }

    public int number() { return number; }
    public String label() { return "C" + number; }
    public CustomerType type() { return type; }
    public int arrivalTurn() { return arrivalTurn; }
    public int satisfaction() { return satisfaction; }
    public CustomerState state() { return state; }
    public Table table() { return table; }
    public Order order() { return order; }
    public int servedTurn() { return servedTurn; }
    public int readyToPayTurn() { return readyToPayTurn; }
    public int paidSatisfaction() { return paidSatisfaction; }
    MenuItem choice() { return choice; }

    void seatAt(Table t) { table = t; t.occupy(this); state = CustomerState.SEATED; }

    Order placeOrder() {
        order = new Order(this, choice);
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

    boolean waitOneTurn() {
        satisfaction = Math.max(0, satisfaction - type.patiencePenalty());
        return satisfaction == 0;
    }

    void abandon() {
        if (order != null) order.cancel();
        state = CustomerState.LEFT;
        releaseTable();
    }

    private void releaseTable() {
        if (table != null) { table.free(); table = null; }
    }
}
