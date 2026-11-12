package restaurant.domain;

public class Order {
    private final Customer customer;
    private final MenuItem item;
    private int remainingUnits;
    private OrderStatus status = OrderStatus.PLACED;

    public Order(Customer customer, MenuItem item) {
        this.customer = customer;
        this.item = item;
        this.remainingUnits = item.prepUnits();
    }

    public Customer customer() { return customer; }
    public MenuItem item() { return item; }
    public int remainingUnits() { return remainingUnits; }
    public OrderStatus status() { return status; }
    public int billCents() { return customer.billFor(item); }

    public boolean isCookable() {
        return status == OrderStatus.PLACED || status == OrderStatus.PREPARING;
    }

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
