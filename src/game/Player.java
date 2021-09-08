package game;
/**
 * Represents a player. Holds information of a Player object
 *
 * Model class.
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player implements GameObject{

    private String name = "";
    private String playerName = "";
    private Square currentPos = null;
    private Square oldPos = null;

    public ArrayList<Card> hand = new ArrayList<>();
    public ArrayList<Square> visited = new ArrayList<Square>(); // list of coordinates of visited squares
    public Estate currEstate = null;
    
    private Boolean canMakeSolveAttempt = true;
    private int roll;
    
    private Color color;
	int PLAYER_SIZE = 20;

    // Constructor
    public Player(String name, Color c) {
        this.name = name;
        this.color = c;
    }
	
	public void setPlayerSquare(Square square) {
		this.currentPos = square;		
	}
	
    /**
	 * 
	 * Sets player dice roll.
	 * 
	 */
    public void setRoll(int r){
        this.roll = r;
    }

    /**
	 * 
	 * Returns the amount from the players dice roll.
	 * 
	 */
    public int getRoll(){
        return this.roll;
    }

    /**
	 * 
	 * Clears players visited path.
	 * 
	 */
    public void clearVisited(){
        visited.clear();
    }

    /**
	 * 
	 * Adds a square coordinates to players visited path.
	 * 
	 */
    public void addToVisited(Square square){
        this.visited.add(square);
    }

    /**
	 * 
	 * Returns a list of squares of the path the player has visited.
	 * 
	 */
    public ArrayList<Square> getVisited(){
        return this.visited;
    }
    
    public Boolean visitedContainsSquare(Square square) {
    	for (Square s : visited) {
    		if (square.getRow()==s.getRow() && square.getColumn()==s.getColumn()) {
    			return true;
    		}
    	}
    	return false;
    }
    
	/**
	 * 
	 * Sets the estate the player is in (if they are in one).
	 * 
	 */
    public void setEstate(Estate e){
        this.currEstate = e;
    }
    
	/**
	 * 
	 * Returns the estate the player is currently in.
	 * 
	 * @return Estate
	 * 
	 */
    public Estate getEstate(){
        return this.currEstate;
    }
    
	/**
	 * 
	 * Returns players name.
	 * 
	 * @return String
	 * 
	 */
    public String getName() {
        return this.name;
    }
    
	/**
	 * 
	 * Returns players current position as a Square Object
	 * 
	 * @return Square
	 * 
	 */
    public Square getSquare(){
        return this.currentPos;
    }
    
	/**
	 * 
	 * returns square area
	 * 
	 * @return Rectangle
	 * 
	 */
    public Rectangle getSquareArea() {
        Rectangle r = new Rectangle(this.currentPos.getRow(), this.currentPos.getColumn(), PLAYER_SIZE, PLAYER_SIZE);
        return r;
    }
    
	/**
	 * 
	 * Returns a boolean of whether the player is able to accuse or not.
	 * 
	 * @return boolean
	 * 
	 */
    public Boolean canMakeSolveAttempt(){
        return canMakeSolveAttempt;
    }

	/**
	 * 
	 * Sets players accusation status.
	 * 
	 */
    public void setCanMakeSolveAttempt(){
    	canMakeSolveAttempt = false;
    }

	/**
	 * 
	 * Adds a card to the player's hand.
	 * 
	 */
    public void addCard(Card card){
        if(card!=null)
            this.hand.add(card);
    }

	/**
	 * 
	 * Returns a list of cards of the players hand.
	 * 
	 */
    public List<Card> getHand(){
        return this.hand;
    }

	public void setPlayerName(String pn) {
		this.playerName = pn;
	}

	public Color getColor() { return this.color; }

}
