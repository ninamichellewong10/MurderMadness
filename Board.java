import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Creates a new board for a new game, and updates the board for each round. The board consists of 24x24 squares, each
 * square is represented with a string. Board keeps track of locations of each four players, the location of the
 * five estates, and location of the five weapons. All squares on the board are accessible to players apart from grey
 * areas and estate walls. An estate represents only one square on the board, players must walk through the door to
 * access estates they cannot be accessed through walls.
 *
 * A model class that extends Boardview
 */

public class Board extends BoardView {

    private final ArrayList<Player> players;  // list of players on board ***
    private final ArrayList<Weapon> weapons;  // list of weapons on board ***
    
    public static ArrayList<Rectangle> outOfBounds;
    public static ArrayList<Square> doorSquares;
    public static ArrayList<Square> boardSquares;
    public static ArrayList<Estate> estates;

	// Constructor
    public Board() {
        players = new ArrayList<>();
        weapons = new ArrayList<>();
        estates = new ArrayList<>();
        boardSquares = new ArrayList<>();
        doorSquares = new ArrayList<>();
        outOfBounds = new ArrayList<>();
    }

    public ArrayList<Rectangle> getOutOfBounds(){
    	return outOfBounds;
    }
    
    public void addEstate(Estate e) {
    	estates.add(e);
    }
    
	/**
	 * 
	 * Adds a given player to list of players.
	 * 
	 */
    public void addPlayer(Player p){
        players.add(p);
    }

	/**
	 * 
	 * Adds a given weapon to list of weapons.
	 * 
	 */
    public void addWeapon(Weapon w){
        weapons.add(w);
    }

	/**
	 * 
	 * Returns true if a given square is a door to an estate.
	 * 
	 * @return Boolean
	 * 
	 */
    public boolean isDoor(Square s){
        for (Square square : doorSquares) {
        	if (s.getRow() == square.getRow() && s.getColumn() == square.getColumn()) {
        		return true;
        	}
        }
        return false;
    }
    
	/**
	 * 
	 * Returns true if a given square is a part of an estate.
	 * 
	 * @return Boolean
	 * 
	 */
    public boolean isPartOfAnEstate(Square square) {
    	for (Estate estate : estates) {
    		if (estate.getEstateArea().contains(square.getRow(), square.getColumn())) {
    			return true;
    		}
    	}
    	return false;
    }
    
	/**
	 * 
	 * Returns true if a given square out of bounds.
	 * 
	 * @return Boolean
	 * 
	 */
    public boolean isOutOfBounds(Square square) {
    	for (Rectangle rect : outOfBounds) {
    		if (rect.contains(square.getRow(), square.getColumn())) {
    			return true;
    		}
    	}
    	return false;
    }
    
	/**
	 * 
	 * Moves a player to an estate.
	 * 
	 * @return Square
	 * 
	 */
    public Square movePlayerToEstate(Estate estate) {
    	for (Square square : boardSquares) {
    		if (estate.getEstateArea().contains(square.getRow(), square.getColumn())) {
    			return square;
    		}
    	}
    	return null;
    }

}