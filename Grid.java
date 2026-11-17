package Projects.GameProject;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final Cell[][] cells;
    private final List<Ship> ships;
    private final int size;

    public Grid(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
        this.ships = new ArrayList<>();
        create();
    }

    // Fill the grid with empty cells
    public void create() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cells[r][c] = new Cell();
            }
        }
    }

    public int getSize() { return size; }

    public boolean isInBounds(Coordinate coordinate) {
        return coordinate.getRow() >= 0 && coordinate.getRow() < size
                && coordinate.getColumn() >= 0 && coordinate.getColumn() < size;
    }

    // Valid (on the board) and not already attacked
    public boolean isAvailable(Coordinate coordinate) {
        return isInBounds(coordinate)
                && !cells[coordinate.getRow()][coordinate.getColumn()].isAttacked();
    }

    // Places the ship only if every square is on the board and empty.
    // Nothing is changed when the placement fails.
    public boolean placeShip(Ship ship, Coordinate start, boolean horizontal) {
        for (int i = 0; i < ship.getSize(); i++) {
            Coordinate c = squareAt(start, horizontal, i);
            if (!isInBounds(c) || cells[c.getRow()][c.getColumn()].hasShip()) {
                return false;
            }
        }
        for (int i = 0; i < ship.getSize(); i++) {
            Coordinate c = squareAt(start, horizontal, i);
            cells[c.getRow()][c.getColumn()].setShip(ship);
            ship.addCoordinate(c);
        }
        ships.add(ship);
        return true;
    }

    // Marks the square as attacked. Returns true for a hit, false for a miss.
    public boolean recordAttack(Coordinate coordinate) {
        if (!isAvailable(coordinate)) {
            throw new IllegalArgumentException("Invalid or repeated attack: " + coordinate);
        }
        Cell cell = cells[coordinate.getRow()][coordinate.getColumn()];
        cell.markAttacked();
        if (cell.hasShip()) {
            cell.getShip().hit();
            return true;
        }
        return false;
    }

    // Returns the ship at this square, or null if there is none or it is off the board
    public Ship getShipAt(Coordinate coordinate) {
        if (!isInBounds(coordinate)) return null;
        return cells[coordinate.getRow()][coordinate.getColumn()].getShip();
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) return false;
        }
        return !ships.isEmpty();
    }

    // X = hit, O = miss, S = ship (only if showShips), ~ = unknown water
    public String getValue(Coordinate coordinate, boolean showShips) {
        Cell cell = cells[coordinate.getRow()][coordinate.getColumn()];
        if (cell.isAttacked()) {
            return cell.hasShip() ? "X" : "O";
        }
        return (cell.hasShip() && showShips) ? "S" : "~";
    }

    // Your own board (ships visible)
    @Override
    public String toString() { return toString(true); }

    public String toString(boolean showShips) {
        StringBuilder sb = new StringBuilder("   ");
        for (int c = 0; c < size; c++) {
            sb.append(String.format("%-3d", c + 1));
        }
        sb.append(System.lineSeparator());
        for (int r = 0; r < size; r++) {
            sb.append((char) ('A' + r)).append("  ");
            for (int c = 0; c < size; c++) {
                sb.append(getValue(new Coordinate(r, c), showShips)).append("  ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private Coordinate squareAt(Coordinate start, boolean horizontal, int offset) {
        int row = start.getRow() + (horizontal ? 0 : offset);
        int column = start.getColumn() + (horizontal ? offset : 0);
        return new Coordinate(row, column);
    }
}
