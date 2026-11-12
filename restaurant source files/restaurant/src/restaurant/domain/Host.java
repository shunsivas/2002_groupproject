package restaurant.domain;

/** Seats at most one queued customer per turn, in addition to the Waiter's own task. */
public class Host extends SeatingStaff {
    public Host() { super("Host"); }
}
