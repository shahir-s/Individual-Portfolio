package Projects.GameProject;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final ShipType type;
    private final List<Coordinate> coordinates;
    private int numHits;

    public Ship(ShipType type) {
        this.type = type;
        this.coordinates = new ArrayList<>();
        this.numHits = 0;
    }

    // A ship can never occupy more squares than its size
    public void addCoordinate(Coordinate coordinate) {
        if (coordinates.size() < getSize()) {
            coordinates.add(coordinate);
        }
    }

    public void hit() {
        if (numHits < getSize()) {
            numHits++;
        }
    }

    public boolean isSunk() { return numHits >= getSize(); }
    public ShipType getType() { return type; }
    public int getSize() { return type.getSize(); }
    public int getNumHits() { return numHits; }

    // Returns a copy so outside code cannot modify the ship's squares
    public List<Coordinate> getCoordinates() { return new ArrayList<>(coordinates); }

    @Override
    public String toString() {
        return type.getDisplayName() + " (" + numHits + "/" + getSize() + " hits)";
    }
}
