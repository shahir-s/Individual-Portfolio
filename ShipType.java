package Projects.GameProject;

public enum ShipType {
    BATTLESHIP("Battleship", 4),
    CRUISER("Cruiser", 3),
    SUBMARINE("Submarine", 2),
    DESTROYER("Destroyer", 3),
    CARRIER("Carrier", 5);

    private final String displayName;
    private final int size;

    ShipType(String displayName, int size) {
        this.displayName = displayName;
        this.size = size;
    }

    public String getDisplayName() { return displayName; }
    public int getSize() { return size; }
}
