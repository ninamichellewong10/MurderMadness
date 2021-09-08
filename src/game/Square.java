package game;
/**
 * Represents a square from the board in Murder Madness game. A square can consist of an estate, estate wall, estate
 * door, grey area, a player, a weapon, or remain empty.
 *
 * Model class
 */

public class Square {

    private String textRepresentation = "";
    private GameObject object = null;
    
    private int row = 0;
    private int column = 0;
    
    private Boolean isDoorSquare = false;

    // Constructor
    public Square(int row, int column) {
        this.row = row;
        this.column = column;
    }

	/**
	 * 
	 * Sets object.
	 * 
	 */
    public void setObject(GameObject o){ 
        this.object = o;
    }
    
    public void setDoorSquare() {
    	this.isDoorSquare = true;
    }

	/**
	 * 
	 * Sets a given string to a text representation.
	 * 
	 */
    public void setText(String s) {
        this.textRepresentation = s;
    }

    public void setRow(int x) {
    	this.row = x;
    }
    
    public void setCol(int y) {
    	this.row = y;
    }
    
	/**
	 * 
	 * Returns the int row position of the square in board.
	 * 
	 * @return int
	 * 
	 */
    public int getRow() {
        return row;
    }

	/**
	 * 
	 * Returns the int col position of the square in board.
	 * 
	 * @return int
	 * 
	 */
    public int getColumn() {
        return column;
    }

	/**
	 * 
	 * Returns Game Object
	 * 
	 * @return GameObject
	 * 
	 */
    public GameObject getObject() {
        return object;
    }
    
    public Boolean isDoor() {
    	return isDoorSquare;
    }

    @Override
    public String toString(){
        return this.textRepresentation;
    }
}