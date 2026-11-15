package restaurant.domain;

public class HappyHour {
    public static final int DURATION_TURNS = 2;
    public static final int DISCOUNT_PERCENT_PAID = 80;
    public static final int RECOVERY = 15;

    private boolean used;
    private int remainingTurns;

    public boolean isUsed() { return used; }
    public boolean isActive() { return remainingTurns > 0; }
    public int remainingTurns() { return remainingTurns; }

    public ActionResult activate() {
        if (used) return ActionResult.fail("Happy Hour can only be used once per game.");
        used = true;
        remainingTurns = DURATION_TURNS;
        return ActionResult.success("Happy Hour starts! It lasts " + DURATION_TURNS
                + " turns: 20% off new orders and waiting customers recover " + RECOVERY + " satisfaction each turn.");
    }

    public int adjust(int billCents) {
        return isActive() ? Money.percentOf(billCents, DISCOUNT_PERCENT_PAID) : billCents;
    }

    public void endTurn() { if (remainingTurns > 0) remainingTurns--; }

    public String status() {
        if (isActive()) return "ACTIVE (" + remainingTurns + " turn(s) left)";
        return used ? "used" : "available (type 'happyhour' before acting this turn)";
    }
}
