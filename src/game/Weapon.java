package game;
/**
 * Represents a weapon in the Murder Madness game.
 *
 * Model class
 */

import java.awt.Color;
import java.awt.Rectangle;

public class Weapon implements GameObject{

    private String name = "";
    private String printable = "";
    
    private Square currentPos = null;
    private Square oldPos = null;
    
    private Estate estate = null;
    
    private Color color;
    private Rectangle area;

    private final int WEAPON_SIZE = 20;

    // Constructor
    public Weapon(String name, String text, Color c) {
        this.name = name;
        this.printable = text;
        this.color = c;
        this.currentPos = null;
    }
	
	public void setWeaponSquare(Square square) {
		this.currentPos = square;
	}

    // Returns the name of the weapon
    public String getName() {
        return this.name;
    }

    // Returns the board text representation of the weapon
    public String getPrintable() {
        return this.printable;
    }

    // Returns a square from previous square position
    public Square getOldSquare(){
        return this.oldPos;
    }

    // Return estate the weapon is in 
    public Estate getEstate(){
        return this.estate;
    }

    // Set a given estate to the estate the weapon is currently in
    public void setEstate(Estate estate){
        this.estate = estate;
    }

    // Set a given square to a previous square position *** Not used
    public void setOldSquare(Square square) {
        this.oldPos = square;
    }

    public Rectangle getSquareArea() {
        Rectangle r = new Rectangle(currentPos.getRow(), currentPos.getColumn(), WEAPON_SIZE, WEAPON_SIZE);
        return r;
    }

    public void setArea(Rectangle r) {
        this.area = r;
    }

    public Color getColor() { return this.color; }

    @Override
    public Square getSquare() {
        return this.currentPos;
    }
}
