package game;

/**
 * Represents an estate. Holds information of the estate object.
 *
 * Model class
 */

import java.awt.Rectangle;
import java.util.ArrayList;

public class Estate implements GameObject{

    private final String name;
    private Weapon weaponPos;
    private String printable = "#";
    
    //new fields relevant
    private Rectangle estateArea; 
    private final ArrayList<Player> playersInside;
    private final ArrayList<Square> doorSquares;

    // Constructor
    public Estate(String name){
        this.name = name;
        this.doorSquares = new ArrayList<>();
        this.playersInside = new ArrayList<>();
    }

	/**
	 * 
	 * Returns estate name.
	 * 
	 * @return String
	 * 
	 */
    public String getName() {
        return name;
    }
    
    public void addEstateArea(Rectangle rect) {
    	this.estateArea = rect;
    }
    
    public Rectangle getEstateArea() {
    	return this.estateArea;
    }
    
    public void addPlayerInside(Player player) {
    	playersInside.add(player);
    }

    public void setWeapon(Weapon weapon){
        this.weaponPos = weapon;
    }

	/**
	 * 
	 * Returns the square of the weapon position.
	 * 
	 */
    public Weapon getWeapon(){
        return this.weaponPos;
    }

    @Override
    public Square getSquare() { //just here so it is a GameObject
        return null;
    }
}