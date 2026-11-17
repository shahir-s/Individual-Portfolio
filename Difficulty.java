package Projects.GameProject;

public enum Difficulty {
    EASY(5, new ShipType[] {ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE}),
    MEDIUM(7, new ShipType[] {ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE,
            ShipType.DESTROYER}),
    HARD(10, new ShipType[] {ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.SUBMARINE,
            ShipType.DESTROYER, ShipType.CARRIER});

    private final int gridSize;
    private final ShipType[] shipTypes;

    Difficulty(int gridSize, ShipType[] shipTypes) {
        this.gridSize = gridSize;
        this.shipTypes = shipTypes;
    }

    public int getGridSize() { return gridSize; }
    public ShipType[] getShipTypes() { return shipTypes.clone(); }
}
