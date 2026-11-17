package Projects.GameProject;

import java.util.Random;
import java.util.Scanner;

public class Player {
    protected Grid grid;
    protected Difficulty difficulty;
    protected Random random = new Random();
    private Scanner input;   // null for the AI

    public Player(Difficulty difficulty, Scanner input) {
        this.difficulty = difficulty;
        this.input = input;
        this.grid = new Grid(difficulty.getGridSize());
    }

    // For each ship type: try random spots/orientations until one fits
    public void placeShips() {
        for (ShipType type : difficulty.getShipTypes()) {
            Ship ship = new Ship(type);
            boolean placed = false;
            while (!placed) {
                Coordinate start = new Coordinate(random.nextInt(grid.getSize()),
                        random.nextInt(grid.getSize()));
                placed = grid.placeShip(ship, start, random.nextBoolean());
            }
        }
    }

    // Human version: prompt until the player gives an unused, on-board coordinate
    public Coordinate chooseAttack(Grid opponentGrid) {
        while (true) {
            System.out.print("Enter coordinate to attack (e.g. B3): ");
            Coordinate target = parseCoordinate(input.nextLine());
            if (target != null && opponentGrid.isAvailable(target)) {
                return target;
            }
            System.out.println("Invalid or already attacked. Try again.");
        }
    }

    // Returns true if the attack hit a ship
    public boolean receiveAttack(Coordinate coordinate) {
        return grid.recordAttack(coordinate);
    }

    // Hook for the AI to remember results. The human does nothing.
    public void notifyResult(Coordinate coordinate, boolean hit, Ship sunkShip) {
    }

    public boolean hasLost() {
        return grid.allShipsSunk();
    }

    public Grid getGrid() {
        return grid;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    // Helper: "B3" -> Coordinate(1, 2); returns null if the text is malformed
    private Coordinate parseCoordinate(String text) {
        text = text.trim().toUpperCase();
        if (text.length() < 2) return null;
        int row = text.charAt(0) - 'A';
        try {
            int column = Integer.parseInt(text.substring(1)) - 1;
            return new Coordinate(row, column);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
