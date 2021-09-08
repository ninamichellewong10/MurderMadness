package game;
/**
 * Displays the board to the user.
 *
 * View class
 */


import javax.swing.*;
import java.awt.*;

public class BoardView extends JComponent {

    // Board components
    public static final int SQUARE_ROWS = 24;
    public static final int SQUARE_COLS = 24;
    public static final int BOARD_LEFT = 20; // leaves a gap between the left of the board and the panel
    public static final int BOARD_TOP = 20; // leaves a gap between the top of the board and the panel
    public static final int SQUARE_SIZE = 20;
    public static final int BOARD_SIZE = 24 * SQUARE_SIZE;
    public static final int GREY_AREA_SIZE = 40;
    public static final int ESTATE_SIZE = 5 * SQUARE_SIZE;
    public static final int VC_ESTATE_WIDTH = 6 * SQUARE_SIZE;
    public static final int VC_ESTATE_HEIGHT = 4 * SQUARE_SIZE;

    /**
     * Creates board squares including estates and out of bound areas on the JPanel.
     */
    public void setBoardSquares() {
        int x = BOARD_LEFT;
        int y = BOARD_TOP;

        // create board
        for (int row = 0; row < SQUARE_ROWS; row++) {
            for (int col = 0; col < SQUARE_COLS; col++) {
                Square square = new Square(x, y);
                Board.boardSquares.add(square);
                x = x + SQUARE_SIZE;
            }
            y = y + SQUARE_SIZE;
            x = BOARD_LEFT;
        }

        // ==================================== create grey areas ====================================
        Rectangle gray1 = new Rectangle((11 * SQUARE_SIZE) + BOARD_LEFT, (5 * SQUARE_SIZE) + BOARD_TOP, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray1);

        Rectangle gray2 = new Rectangle((5 * SQUARE_SIZE) + BOARD_LEFT, (11 * SQUARE_SIZE) + BOARD_TOP, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray2);

        Rectangle gray3 = new Rectangle((17 * SQUARE_SIZE) + BOARD_LEFT, (11 * SQUARE_SIZE) + BOARD_TOP, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray3);

        Rectangle gray4 = new Rectangle((11 * SQUARE_SIZE) + BOARD_LEFT, (17 * SQUARE_SIZE) + BOARD_TOP, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray4);

        // ==================================== create estates ====================================

        //mm
        Board.doorSquares.add(getSquare(360, 120));
        Board.doorSquares.add(getSquare(420, 140));

        //cc
        Board.doorSquares.add(getSquare(140, 380));
        Board.doorSquares.add(getSquare(100, 360));

        //hh
        Board.doorSquares.add(getSquare(120, 140));
        Board.doorSquares.add(getSquare(140, 80));

        //vc
        Board.doorSquares.add(getSquare(260, 280));
        Board.doorSquares.add(getSquare(200, 260));
        Board.doorSquares.add(getSquare(300, 260));
        Board.doorSquares.add(getSquare(280, 220));

        //pp
        Board.doorSquares.add(getSquare(380, 360));
        Board.doorSquares.add(getSquare(360, 420));

        int hhX = (2 * SQUARE_SIZE) + BOARD_LEFT;
        int hhY = (2 * SQUARE_SIZE) + BOARD_TOP;
        Rectangle hhRect = new Rectangle(hhX, hhY, ESTATE_SIZE, ESTATE_SIZE);
        for (Estate e : Board.estates) {
            if (e.getName().equals("Haunted House")) {
                e.addEstateArea(hhRect);
            }
        }

        int ccX = (2 * SQUARE_SIZE) + BOARD_LEFT;
        int ccY = (17 * SQUARE_SIZE) + BOARD_TOP;
        Rectangle ccRect = new Rectangle(ccX, ccY, ESTATE_SIZE, ESTATE_SIZE);
        for (Estate e : Board.estates)
            if (e.getName().equals("Calamity Castle")) {
                e.addEstateArea(ccRect);
            }

        int mmX = (17 * SQUARE_SIZE) + BOARD_LEFT;
        int mmY = (2 * SQUARE_SIZE) + BOARD_TOP;
        Rectangle mmRect = new Rectangle(mmX, mmY, ESTATE_SIZE, ESTATE_SIZE);
        for (Estate e : Board.estates) {
            if (e.getName().equals("Manic Manor")) {
                e.addEstateArea(mmRect);
            }
        }

        int ppX = (17 * SQUARE_SIZE) + BOARD_LEFT;
        int ppY = (17 * SQUARE_SIZE) + BOARD_TOP;
        Rectangle ppRect = new Rectangle(ppX, ppY, ESTATE_SIZE, ESTATE_SIZE);
        for (Estate e : Board.estates) {
            if (e.getName().equals("Peril Palace")) {
                e.addEstateArea(ppRect);
            }
        }

        int vcX = (9 * SQUARE_SIZE) + BOARD_LEFT;
        int vcY = (10 * SQUARE_SIZE) + BOARD_TOP;
        Rectangle vcRect = new Rectangle(vcX, vcY, VC_ESTATE_WIDTH, VC_ESTATE_HEIGHT);
        for (Estate e : Board.estates) {
            if (e.getName().equals("Villa Celia")) {
                e.addEstateArea(vcRect);
            }
        }
    }

    /**
     * Draws the board on the JPanel to the user using the square locations.
     * @param g
     */
    public void drawBoard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g.setColor(Color.WHITE);
        g.fillRect(BOARD_LEFT, BOARD_TOP, BOARD_SIZE, BOARD_SIZE);

        // Draws board grid
        g.setColor(Color.LIGHT_GRAY);
        int x = BOARD_LEFT;
        int y = BOARD_TOP;
        for (int row = 0; row < SQUARE_ROWS; row++) {
            for (int col = 0; col < SQUARE_COLS; col++) {
                g.drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
                x = x + SQUARE_SIZE;
            }
            y = y + SQUARE_SIZE;
            x = BOARD_LEFT;
        }

        // ==================================== Draws grey-areas ====================================
        g.setColor(Color.LIGHT_GRAY);
        int topX = (11 * SQUARE_SIZE) + BOARD_LEFT;
        int topY = (5 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(topX, topY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Rectangle gray1 = new Rectangle(topX, topY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray1);

        int leftX = (5 * SQUARE_SIZE) + BOARD_LEFT;
        int leftY = (11 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(leftX, leftY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Rectangle gray2 = new Rectangle(leftX, leftY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray2);

        int rightX = (17 * SQUARE_SIZE) + BOARD_LEFT;
        int rightY = (11 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(rightX, rightY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Rectangle gray3 = new Rectangle(rightX, rightY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray3);

        int bottomX = (11 * SQUARE_SIZE) + BOARD_LEFT;
        int bottomY = (17 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(bottomX, bottomY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Rectangle gray4 = new Rectangle(bottomX, bottomY, GREY_AREA_SIZE, GREY_AREA_SIZE);
        Board.outOfBounds.add(gray4);

        // ==================================== Draws estates ====================================
        g.setColor(Color.WHITE);
        int hhX = (2 * SQUARE_SIZE) + BOARD_LEFT;
        int hhY = (2 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(hhX, hhY, ESTATE_SIZE, ESTATE_SIZE);
        Rectangle hhRect = new Rectangle(hhX, hhY, ESTATE_SIZE, ESTATE_SIZE);

        int ccX = (2 * SQUARE_SIZE) + BOARD_LEFT;
        int ccY = (17 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(ccX, ccY, ESTATE_SIZE, ESTATE_SIZE);
        Rectangle ccRect = new Rectangle(ccX, ccY, ESTATE_SIZE, ESTATE_SIZE);

        int mmX = (17 * SQUARE_SIZE) + BOARD_LEFT;
        int mmY = (2 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(mmX, mmY, ESTATE_SIZE, ESTATE_SIZE);
        Rectangle mmRect = new Rectangle(mmX, mmY, ESTATE_SIZE, ESTATE_SIZE);

        int ppX = (17 * SQUARE_SIZE) + BOARD_LEFT;
        int ppY = (17 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(ppX, ppY, ESTATE_SIZE, ESTATE_SIZE);
        Rectangle ppRect = new Rectangle(ppX, ppY, ESTATE_SIZE, ESTATE_SIZE);

        int vcX = (9 * SQUARE_SIZE) + BOARD_LEFT;
        int vcY = (10 * SQUARE_SIZE) + BOARD_TOP;
        g.fillRect(vcX, vcY, VC_ESTATE_WIDTH, VC_ESTATE_HEIGHT);
        Rectangle vcRect = new Rectangle(vcX, vcY, VC_ESTATE_WIDTH, VC_ESTATE_HEIGHT);

        Color wall = new Color(40, 40, 180);
        g.setColor(wall);
        g.drawRect(hhX, hhY, ESTATE_SIZE, ESTATE_SIZE);
        g.drawRect(ccX, ccY, ESTATE_SIZE, ESTATE_SIZE);
        g.drawRect(mmX, mmY, ESTATE_SIZE, ESTATE_SIZE);
        g.drawRect(ppX, ppY, ESTATE_SIZE, ESTATE_SIZE);
        g.drawRect(vcX, vcY, VC_ESTATE_WIDTH, VC_ESTATE_HEIGHT);

        g.setColor(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(3));

        // hh
        g.drawLine((5 * SQUARE_SIZE) + BOARD_LEFT,(7 * SQUARE_SIZE) + BOARD_TOP,(6 * SQUARE_SIZE) + BOARD_LEFT, (7 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((7 * SQUARE_SIZE) + BOARD_LEFT,(3 * SQUARE_SIZE) + BOARD_TOP,(7 * SQUARE_SIZE) + BOARD_LEFT, (4 * SQUARE_SIZE) + BOARD_TOP);

        // cc
        g.drawLine((4 * SQUARE_SIZE) + BOARD_LEFT,(17 * SQUARE_SIZE) + BOARD_TOP,(5 * SQUARE_SIZE) + BOARD_LEFT, (17 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((7 * SQUARE_SIZE) + BOARD_LEFT,(18 * SQUARE_SIZE) + BOARD_TOP,(7 * SQUARE_SIZE) + BOARD_LEFT, (19 * SQUARE_SIZE) + BOARD_TOP);

        // mm
        g.drawLine((17 * SQUARE_SIZE) + BOARD_LEFT,(5 * SQUARE_SIZE) + BOARD_TOP,(17 * SQUARE_SIZE) + BOARD_LEFT, (6 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((20 * SQUARE_SIZE) + BOARD_LEFT,(7 * SQUARE_SIZE) + BOARD_TOP,(21 * SQUARE_SIZE) + BOARD_LEFT, (7 * SQUARE_SIZE) + BOARD_TOP);

        // pp
        g.drawLine((17 * SQUARE_SIZE) + BOARD_LEFT,(20 * SQUARE_SIZE) + BOARD_TOP,(17 * SQUARE_SIZE) + BOARD_LEFT, (21 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((18 * SQUARE_SIZE) + BOARD_LEFT,(17 * SQUARE_SIZE) + BOARD_TOP,(19 * SQUARE_SIZE) + BOARD_LEFT, (17 * SQUARE_SIZE) + BOARD_TOP);

        // vc
        g.drawLine((13 * SQUARE_SIZE) + BOARD_LEFT,(10 * SQUARE_SIZE) + BOARD_TOP,(14 * SQUARE_SIZE) + BOARD_LEFT, (10 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((9 * SQUARE_SIZE) + BOARD_LEFT,(12 * SQUARE_SIZE) + BOARD_TOP,(9 * SQUARE_SIZE) + BOARD_LEFT, (13 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((15 * SQUARE_SIZE) + BOARD_LEFT,(12 * SQUARE_SIZE) + BOARD_TOP,(15 * SQUARE_SIZE) + BOARD_LEFT, (13 * SQUARE_SIZE) + BOARD_TOP);
        g.drawLine((12 * SQUARE_SIZE) + BOARD_LEFT,(14 * SQUARE_SIZE) + BOARD_TOP,(13 * SQUARE_SIZE) + BOARD_LEFT, (14 * SQUARE_SIZE) + BOARD_TOP);
    }

	/**
	 * 
	 * Returns square from given row and col.
	 * 
	 */
    public Square getSquare(int row, int column) {
        for (Square square : Board.boardSquares) {
            if (row == square.getRow() && column == square.getColumn()) {
                return square;
            }
        }
        return null;
    }
    
	/**
	 * 
	 * draws players on the board.
	 * 
	 */
    public void drawPlayer(Graphics g, Player p) {
        int size = 20;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g.setColor(p.getColor());
        g2.fillOval(p.getSquare().getRow(), p.getSquare().getColumn(), size, size);
    }
	
	/**
	 * 
	 * draws weapons on the board.
	 * 
	 */
    public void drawWeapon(Graphics g, Weapon w) {
        int size = 20;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g.setColor(w.getColor());
        g2.drawOval(w.getSquare().getRow(), w.getSquare().getColumn(), size, size);
        g2.drawString(w.getPrintable(), w.getSquare().getRow()+5, w.getSquare().getColumn()+15);
        Rectangle r = new Rectangle(w.getSquare().getRow(), w.getSquare().getColumn(), size, size);
        w.setArea(r);
    }

}
