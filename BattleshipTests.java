package Projects.GameProject;

public class BattleshipTests {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static Coordinate c(int row, int column) {
        return new Coordinate(row, column);
    }

    public static void main(String[] args) {
        testCoordinate();
        testShip();
        testGrid();
        System.out.println(passed + " passed, " + failed + " failed");
    }

    private static void testCoordinate() {
        check("Coordinate stores row", c(2, 9).getRow() == 2);
        check("Coordinate stores column", c(2, 9).getColumn() == 9);
        check("Coordinate equals same values", c(1, 1).equals(c(1, 1)));
        check("Coordinate not equal different values", !c(1, 1).equals(c(1, 2)));
        check("Coordinate not equal null", !c(1, 1).equals(null));
        check("Coordinate toString A1", c(0, 0).toString().equals("A1"));
        check("Coordinate toString C10", c(2, 9).toString().equals("C10"));
    }

    private static void testShip() {
        Ship cruiser = new Ship(ShipType.CRUISER);
        check("Ship type", cruiser.getType() == ShipType.CRUISER);
        check("Ship size from type", cruiser.getSize() == 3);
        check("Ship starts with 0 hits", cruiser.getNumHits() == 0);
        check("Ship starts afloat", !cruiser.isSunk());

        cruiser.hit();
        cruiser.hit();
        check("Ship counts 2 hits", cruiser.getNumHits() == 2);
        check("Ship with 2/3 hits is not sunk", !cruiser.isSunk());

        cruiser.hit();
        check("Ship with 3/3 hits is sunk", cruiser.isSunk());

        cruiser.hit();
        check("Hits never exceed size", cruiser.getNumHits() == 3);

        Ship sub = new Ship(ShipType.SUBMARINE);
        sub.addCoordinate(c(0, 0));
        sub.addCoordinate(c(0, 1));
        sub.addCoordinate(c(0, 2)); // extra square should be ignored
        check("Ship stores at most size coordinates", sub.getCoordinates().size() == 2);
        check("Ship stores the coordinates given", sub.getCoordinates().contains(c(0, 1)));
    }

    private static void testGrid() {
        Grid grid = new Grid(5);
        check("Grid size", grid.getSize() == 5);
        check("Corner is available", grid.isAvailable(c(0, 0)));
        check("Row 5 is outside a 5x5 grid", !grid.isAvailable(c(5, 0)));
        check("Negative column is outside", !grid.isAvailable(c(0, -1)));
        check("Empty grid has not lost", !grid.allShipsSunk());

        // Placement
        Ship battleship = new Ship(ShipType.BATTLESHIP);
        check("Place battleship horizontally at A1", grid.placeShip(battleship, c(0, 0), true));
        check("Battleship occupies A4", grid.getShipAt(c(0, 3)) == battleship);
        check("Square after the battleship is empty", grid.getShipAt(c(0, 4)) == null);
        check("Ship remembers its 4 squares", battleship.getCoordinates().size() == 4);

        Ship overlapping = new Ship(ShipType.CRUISER);
        check("Overlapping placement rejected", !grid.placeShip(overlapping, c(0, 2), false));
        check("Rejected ship left no trace", grid.getShipAt(c(1, 2)) == null);

        Ship offBoard = new Ship(ShipType.SUBMARINE);
        check("Off-board placement rejected", !grid.placeShip(offBoard, c(4, 4), true));

        Ship cruiser = new Ship(ShipType.CRUISER);
        check("Place cruiser vertically at B1", grid.placeShip(cruiser, c(1, 0), false));
        check("Cruiser occupies D1", grid.getShipAt(c(3, 0)) == cruiser);

        // Attacks
        check("Hit on a ship returns true", grid.recordAttack(c(0, 0)));
        check("Ship counted the hit", battleship.getNumHits() == 1);
        check("Attacked square no longer available", !grid.isAvailable(c(0, 0)));
        check("Attack on water returns false (miss)", !grid.recordAttack(c(4, 4)));
        check("Hit symbol is X", grid.getValue(c(0, 0), true).equals("X"));
        check("Miss symbol is O", grid.getValue(c(4, 4), true).equals("O"));
        check("Hidden ship shows ~", grid.getValue(c(0, 1), false).equals("~"));
        check("Visible ship shows S", grid.getValue(c(0, 1), true).equals("S"));

        boolean threw = false;
        try {
            grid.recordAttack(c(0, 0));
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("Repeat attack throws", threw);

        String board = grid.toString(false);
        check("Enemy view hides ships", !board.contains("S"));
        check("Board view shows hit and miss", board.contains("X") && board.contains("O"));

        // Sinking
        grid.recordAttack(c(0, 1));
        grid.recordAttack(c(0, 2));
        grid.recordAttack(c(0, 3));
        check("Battleship sunk after 4 hits", battleship.isSunk());
        check("Not all ships sunk yet", !grid.allShipsSunk());

        grid.recordAttack(c(1, 0));
        grid.recordAttack(c(2, 0));
        grid.recordAttack(c(3, 0));
        check("Cruiser sunk", cruiser.isSunk());
        check("All ships sunk ends the game", grid.allShipsSunk());
    }
}
