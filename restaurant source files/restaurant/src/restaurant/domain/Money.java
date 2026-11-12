package restaurant.domain;

/** Formats integer cents as dollars with two decimals. */
public final class Money {
    private Money() {}

    /** pct percent of an amount in cents, rounded half up to the nearest cent. */
    public static int percentOf(int cents, int pct) {
        return (cents * pct + 50) / 100;
    }

    public static String format(long cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
