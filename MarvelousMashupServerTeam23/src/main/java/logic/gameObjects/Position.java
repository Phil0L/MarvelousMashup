package logic.gameObjects;

/**
 * Represents the position of an object on the field.
 * @author Luka Stoehr
 */
public class Position {
    /**
     * x coordinate
     */
    private int x;
    /**
     * y coordinate
     */
    private int y;

    /**
     * Constructor for Position object
     * @author Luka Stoehr
     * @param x X-Coordinate
     * @param y Y-Coordinate
     */
    public Position(int x, int y){
        setX(x);
        setY(y);
    }

    /**
     * Getter for X-Coordinate
     * @author Luka Stoehr
     * @return X-Coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for Y-Coordinate
     * @author Luka Stoehr
     * @return Y-Coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for X-Coordinate
     * @author Luka Stoehr
     * @param x new value for X-Coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter for Y-Coordinate
     * @author Luka Stoehr
     * @param y new value for Y-Coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Equals method for class Position. Compares x and y coordinate
     * @param o Object to compare
     * @return True if o is equal to this Position
     * @author Luka Stoehr
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Position)) return false;
        Position pos = (Position) o;
        if(pos.getY() == this.getY() && pos.getX() == this.getX()){
            return true;
        }else{
            return false;
        }

    }

    /**
     * Returns this Position as int[], as needed for ingame messages. int[0] is x-coordinate
     * int[1] is y-coordinate.
     * @author Luka Stoehr
     * @return Position as int[]
     */
    public int[] toArray(){
        int[] pos = new int[2];
        pos[0] = this.getX();
        pos[1] = this.getY();
        return pos;
    }
}
