package Projects.GameProject;

import java.util.ArrayList;
import java.util.List;

public class AI extends Player {
    private List<Coordinate> targetHits = new ArrayList<>(); // hits on ships not yet sunk

    public AI(Difficulty difficulty) {
        super(difficulty, null);
    }

    @Override
    public Coordinate chooseAttack(Grid opponentGrid) {
        if (difficulty == Difficulty.MEDIUM && !targetHits.isEmpty()
                && random.nextInt(100) < 75) {            // 75% adjacent, 25% random
            Coordinate adjacent = chooseAdjacent(opponentGrid);
            if (adjacent != null) return adjacent;
        } else if (difficulty == Difficulty.HARD && !targetHits.isEmpty()) {
            Coordinate adjacent = chooseAdjacent(opponentGrid);
            if (adjacent != null) return adjacent;
        }
        return chooseRandom(opponentGrid);                // EASY, or fallback
    }

    @Override
    public void notifyResult(Coordinate coordinate, boolean hit, Ship sunkShip) {
        if (difficulty == Difficulty.EASY) return;
        if (hit) {
            targetHits.add(coordinate);
            if (sunkShip != null) {
                targetHits.removeAll(sunkShip.getCoordinates()); // reset targeting for that ship
            }
        } else if (difficulty == Difficulty.MEDIUM) {
            targetHits.clear();   // Medium only follows its immediately previous hit
        }
    }

    // Repeat until a valid coordinate is found
    private Coordinate chooseRandom(Grid opponentGrid) {
        Coordinate c;
        do {
            c = new Coordinate(random.nextInt(opponentGrid.getSize()),
                    random.nextInt(opponentGrid.getSize()));
        } while (!opponentGrid.isAvailable(c));
        return c;
    }

    // Collect valid up/down/left/right squares around the hit(s); null if none are available
    private Coordinate chooseAdjacent(Grid opponentGrid) {
        List<Coordinate> sources = new ArrayList<>();
        if (difficulty == Difficulty.MEDIUM) {
            sources.add(targetHits.get(targetHits.size() - 1)); // most recent hit only
        } else {
            sources.addAll(targetHits);                          // Hard: all unsunk hits
        }
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        List<Coordinate> candidates = new ArrayList<>();
        for (Coordinate hit : sources) {
            for (int[] d : directions) {
                Coordinate next = new Coordinate(hit.getRow() + d[0], hit.getColumn() + d[1]);
                if (opponentGrid.isAvailable(next) && !candidates.contains(next)) {
                    candidates.add(next);
                }
            }
        }
        if (candidates.isEmpty()) return null;
        return candidates.get(random.nextInt(candidates.size()));
    }
}
