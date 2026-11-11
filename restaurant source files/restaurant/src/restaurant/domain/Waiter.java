package restaurant.domain;

public class Waiter extends Staff {
    public Waiter() { super("Waiter"); }

    public ActionResult seat(Restaurant r, int customerNo, Integer tableNo) {
        if (hasActed()) return alreadyActed();
        Customer c = r.findCustomer(customerNo);
        if (c == null) return ActionResult.fail("There is no customer C" + customerNo + ".");
        if (c.state() != CustomerState.WAITING)
            return ActionResult.fail(c.label() + " is not in the waiting queue (state: " + c.state() + ").");
        Table t;
        if (tableNo == null) {
            t = r.firstFreeTable();
            if (t == null) return ActionResult.fail("No free table: both tables are occupied.");
        } else {
            t = r.findTable(tableNo);
            if (t == null) return ActionResult.fail("There is no table " + tableNo + ".");
            if (!t.isFree()) return ActionResult.fail("Table " + tableNo + " is occupied by "
                    + t.occupant().label() + ".");
        }
        r.removeFromQueue(c);
        c.seatAt(t);
        markActed();
        return ActionResult.success("Waiter seats " + c.label() + " at table " + t.number() + ".");
    }

    public ActionResult takeOrder(Restaurant r, int customerNo) {
        if (hasActed()) return alreadyActed();
        Customer c = r.findCustomer(customerNo);
        if (c == null) return ActionResult.fail("There is no customer C" + customerNo + ".");
        if (c.state() != CustomerState.SEATED)
            return ActionResult.fail(c.label() + " is not a seated customer without an order (state: "
                    + c.state() + ").");
        Order o = c.placeOrder();
        markActed();
        return ActionResult.success("Waiter takes " + c.label() + "'s order: " + o.item().label()
                + " (" + Money.format(o.billCents()) + ", " + o.item().prepUnits() + " prep unit(s)).");
    }

    public ActionResult serve(Restaurant r, int customerNo, int turn) {
        if (hasActed()) return alreadyActed();
        Customer c = r.findCustomer(customerNo);
        if (c == null) return ActionResult.fail("There is no customer C" + customerNo + ".");
        Order o = c.order();
        if (o == null || c.state() != CustomerState.ORDERED)
            return ActionResult.fail(c.label() + " has no order waiting to be served.");
        if (o.status() != OrderStatus.READY)
            return ActionResult.fail(c.label() + "'s " + o.item().label() + " is not READY yet (status: "
                    + o.status() + ").");
        c.serve(turn);
        r.recordServed();
        markActed();
        return ActionResult.success("Waiter serves " + o.item().label() + " to " + c.label() + ".");
    }
}
