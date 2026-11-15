package restaurant.domain;

public final class Money {
    private Money() {}

    public static int percentOf(int cents, int pct) {
        return (cents * pct + 50) / 100;
    }

    public static String format(long cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
