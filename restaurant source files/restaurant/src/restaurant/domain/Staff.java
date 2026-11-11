package restaurant.domain;

public abstract class Staff {
    private final String role;
    private boolean acted;

    protected Staff(String role) { this.role = role; }

    public String role() { return role; }
    public boolean hasActed() { return acted; }
    public void startTurn() { acted = false; }

    protected void markActed() { acted = true; }

    protected ActionResult alreadyActed() {
        return ActionResult.fail("The " + role + " has already done a task this turn.");
    }
}
