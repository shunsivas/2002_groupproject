package restaurant.control;

import restaurant.domain.CustomerKind;

import java.util.Map;

/** {@link #base(long)} */
public record GameConfig(int maxTurns, int tables, Map<Integer, CustomerKind> arrivals,
                         int minPaid, int minRevenueCents, int minAvgSatisfaction, long seed) {

    public static GameConfig base(long seed) {
        Map<Integer, CustomerKind> arrivals = Map.of(
                1, CustomerKind.REGULAR,
                4, CustomerKind.VIP,
                7, CustomerKind.REGULAR,
                10, CustomerKind.CRITIC,
                13, CustomerKind.REGULAR,
                16, CustomerKind.REGULAR);
        return new GameConfig(18, 2, arrivals, 4, 4500, 60, seed);
    }
}
