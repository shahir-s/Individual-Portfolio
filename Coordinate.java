package Projects.GameProject;

public class Coordinate {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Coordinate)) return false;
        Coordinate o = (Coordinate) other;
        return row == o.row && column == o.column;
    }

    @Override
    public int hashCode() { return 31 * row + column; }

    // (0,0) -> "A1", (2,9) -> "C10"
    @Override
    public String toString() { return "" + (char) ('A' + row) + (column + 1); }
}
