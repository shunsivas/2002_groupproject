package restaurant.domain;


public abstract class Customer {
    private final int number;
    private MenuItem choice; 
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

    
    public abstract String typeLabel();
    public abstract int waitingPenalty();
    public abstract int billFor(MenuItem item);
    protected abstract MenuItem chooseDish(java.util.Random rng);
    public abstract String describe();

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
        satisfaction = Math.max(0, satisfaction - waitingPenalty());
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
