package Projects.GameProject;

public class Cell {
    private Ship ship;        // null means empty water
    private boolean attacked;

    public Ship getShip() { return ship; }
    public void setShip(Ship ship) { this.ship = ship; }
    public boolean hasShip() { return ship != null; }
    public boolean isAttacked() { return attacked; }
    public void markAttacked() { attacked = true; }
}

