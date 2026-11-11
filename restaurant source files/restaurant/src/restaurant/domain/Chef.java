package restaurant.domain;

public class Chef extends Staff {
    public Chef() { super("Chef"); }

    public ActionResult cook(Restaurant r, int customerNo) {
        if (hasActed()) return alreadyActed();
        Customer c = r.findCustomer(customerNo);
        if (c == null) return ActionResult.fail("There is no customer C" + customerNo + ".");
        Order o = c.order();
        if (o == null) return ActionResult.fail(c.label() + " has not placed an order yet.");
        if (!o.isCookable())
            return ActionResult.fail(c.label() + "'s order cannot be cooked (status: " + o.status() + ").");
        o.advance();
        markActed();
        String extra = o.status() == OrderStatus.READY
                ? " The order is READY (it can be served next turn at the earliest)."
                : " " + o.remainingUnits() + " unit(s) left.";
        return ActionResult.success("Chef prepares " + o.item().label() + " for " + c.label() + "."
                + extra);
    }
}
