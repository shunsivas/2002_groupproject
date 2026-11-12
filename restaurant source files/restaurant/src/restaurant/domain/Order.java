package restaurant.domain;

public class Order {
    private final Customer customer;
    private final MenuEntry item;
    private final int billCents; // locked when the order is taken
    private int remainingUnits;
    private OrderStatus status = OrderStatus.PLACED;

    Order(Customer customer, MenuEntry item, int billCents) {
        this.customer = customer;
        this.billCents = billCents;
        this.item = item;
        this.remainingUnits = item.prepUnits();
    }

    public Customer customer() { return customer; }
    public MenuEntry item() { return item; }
    public int remainingUnits() { return remainingUnits; }
    public OrderStatus status() { return status; }
    public int billCents() { return billCents; }

    public boolean isCookable() {
        return status == OrderStatus.PLACED || status == OrderStatus.PREPARING;
    }

    /** One preparation unit. Becomes READY when the last unit is done. */
    void advance() {
        remainingUnits--;
        status = remainingUnits == 0 ? OrderStatus.READY : OrderStatus.PREPARING;
    }

    void markServed() { status = OrderStatus.SERVED; }
    void markPaid() { status = OrderStatus.PAID; }
    void cancel() { status = OrderStatus.CANCELLED; }

    @Override
    public String toString() {
        String prog = item.prepUnits() > 1
                ? " (" + (item.prepUnits() - remainingUnits) + "/" + item.prepUnits() + ")" : "";
        return item.label() + " " + status + prog;
    }
}
