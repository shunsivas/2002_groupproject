package restaurant.domain;

public abstract class SeatingStaff extends Staff {
    protected SeatingStaff(String role) { super(role); }

    public ActionResult seat(Restaurant r, int customerNo, Integer tableNo) {
        if (hasActed()) return alreadyActed();
        Customer c = r.findCustomer(customerNo);
        if (c == null) return ActionResult.fail("There is no customer C" + customerNo + ".");
        if (c.state() != CustomerState.WAITING)
            return ActionResult.fail(c.label() + " is not in the waiting queue (state: " + c.state() + ").");
        Table t;
        if (tableNo == null) {
            t = r.firstFreeTable();
            if (t == null) return ActionResult.fail("No free table: all tables are occupied.");
        } else {
            t = r.findTable(tableNo);
            if (t == null) return ActionResult.fail("There is no table " + tableNo + ".");
            if (!t.isFree()) return ActionResult.fail("Table " + tableNo + " is occupied by "
                    + t.occupant().label() + ".");
        }
        r.removeFromQueue(c);
        c.seatAt(t);
        markActed();
        return ActionResult.success(role() + " seats " + c.label() + " at table " + t.number() + ".");
    }
}
