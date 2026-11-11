package restaurant.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Cashier extends Staff {
    public Cashier() { super("Cashier"); }

    public Customer collectPayment(Restaurant r, int turn) {
        List<Customer> eligible = new ArrayList<>();
        for (Customer c : r.customers())
            if (c.state() == CustomerState.READY_TO_PAY && c.readyToPayTurn() < turn) eligible.add(c);
        if (eligible.isEmpty()) return null;
        eligible.sort(Comparator.comparingInt(Customer::readyToPayTurn).thenComparingInt(Customer::number));
        Customer c = eligible.get(0);
        r.recordPayment(c); 
        c.pay();
        markActed();
        return c;
    }
}
