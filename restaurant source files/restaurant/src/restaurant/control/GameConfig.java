package restaurant.control;

import java.util.Set;

/**
 *{@link #base(long)} 
 */
public record GameConfig(int maxTurns, int tables, Set<Integer> arrivalTurns,
                         int minPaid, int minRevenueCents, int minAvgSatisfaction, long seed) {

    public static GameConfig base(long seed) {
        return new GameConfig(18, 2, Set.of(1, 4, 7, 10, 13, 16), 4, 4500, 60, seed);
    }
}
