package restaurant.domain;

public class Table {
    private final int number;
    private Customer occupant;

    public Table(int number) { this.number = number; }

    public int number() { return number; }
    public Customer occupant() { return occupant; }
    public boolean isFree() { return occupant == null; }

    void occupy(Customer c) { this.occupant = c; }
    void free() { this.occupant = null; }
}
