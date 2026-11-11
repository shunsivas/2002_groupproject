package restaurant.domain;

public record ActionResult(boolean ok, String message) {
    public static ActionResult success(String m) { return new ActionResult(true, m); }
    public static ActionResult fail(String m) { return new ActionResult(false, m); }
}
