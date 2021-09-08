package game;

/**
 * MurderMadness Tests
 */

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class BoardTests {

    @Test
    void test1(){
        /** Tests if weapons are assigned to estates **/
        Board board = new Board();
        board.setBoardSquares();
        Player p = new Player("Bert",new Color(0));
        board.addPlayer(p);
        Weapon w1 = new Weapon("Knife","k", null);
        Weapon w2 = new Weapon("iPad","i", null);
        Estate e1 = new Estate("Haunted House");
        Estate e2 = new Estate("Haunted House 2");
        e1.setWeapon(w1);
        e2.setWeapon(w2);
        if(e1.getWeapon().getName() == "Knife" && e2.getWeapon().getName() == "iPad"){
            assertTrue(true);
        }
        else assertTrue(false);
    }

    @Test
    void test2(){
        /** Tests if player moves to assigned square **/
        Board board = new Board();
        board.setBoardSquares();
        Player p = new Player("Bert",new Color(0));
        board.addPlayer(p);
        p.setPlayerSquare(board.getSquare(1,1));
        if(p.getSquare()==board.getSquare(1,1)){
            assertTrue(true);
        }
        else assertTrue(false);
    }

    @Test
    void test3(){
        /** Tests if a square is out of bounds **/
        Board board = new Board();
        board.setBoardSquares();
        Player p = new Player("Bert",new Color(0));
        board.addPlayer(p);
        Weapon w1 = new Weapon("Knife","k", null);
        Weapon w2 = new Weapon("iPad","i", null);
        Estate e1 = new Estate("Haunted House");
        Estate e2 = new Estate("Haunted House 2");
        e1.setWeapon(w1);
        e2.setWeapon(w2);
        Square sqr = new Square(120,240);
        if(board.isOutOfBounds(sqr) == true){
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }

    @Test
    void test4(){
        /** Tests if player is assigned to estate **/
        Board board = new Board();
        board.setBoardSquares();
        Player p = new Player("Bert",new Color(0));
        board.addPlayer(p);
        Estate e = new Estate("Haunted House");
        p.setEstate(e);
        p.setPlayerSquare(board.getSquare(1,1));
        if(p.getEstate() == e){
            assertTrue(true);
        }
        else assertTrue(false);
    }

    @Test
    void test5(){
        /** Tests if player has visited the square **/
        Player player = new Player("Percy", null);
        Square moveTo = new Square(40,40);
        player.addToVisited(moveTo);
        if (isValidMove(moveTo,player)){
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }

    /**
     * Returns a boolean to check if the move initiated is valid
     * @param movingSquare
     * @param currentPlayer
     * @return boolean
     */
    public Boolean isValidMove(Square movingSquare, Player currentPlayer) {
        if (currentPlayer.visitedContainsSquare(movingSquare)){
            return true;
        }
        return false;
    }
}
