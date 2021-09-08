package game;
/**
 * Controller class. Grabs information from the Object classes (model), and processes responses triggered by the
 * the user from the GUI (view) class.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JOptionPane;

public class MurderMadness extends GUI{
	
    private Stack<Card> cards = new Stack<Card>(); // List of cards in game
    private Map<String, Card> cardMap = new HashMap<String, Card>();

    private Board board; // Current board
    private int playerNum = 0; // Number of players in game
    
    // Current players turn
    public Player currentPlayer = null;
    private int playerCount = 0; //what players turn it is

    // List of players, weapons and estates
    private Map<String, Player> players = new HashMap<String, Player>();
    private Map<String, Weapon> weapons = new HashMap<String, Weapon>();
    private Map<String, Estate> estates = new HashMap<String, Estate>();

    public final ArrayList<Player> playerList = new ArrayList<Player>();
    private final ArrayList<Weapon> weaponList = new ArrayList<Weapon>();
    public final ArrayList<Estate> estateList = new ArrayList<Estate>();

    private ArrayList<Player> playersPlaying = new ArrayList<Player>();
    
    private Player currentPlayerCardPicked = null;
    private Weapon currentWeaponCardPicked = null;

    private boolean move_status = false;

    // Solution
    private Player murderer = null;
    private Weapon murderWeapon = null;
    private Estate murderEstate = null;
    private Player winner = null;
    private boolean running = false;

    // Initialises game components
	public MurderMadness() {
        setUpCards();
        setUpBoard();
        
        // adds players and weapons to board
        for (Player p : playerList)
            board.addPlayer(p);
        for (Weapon w : weaponList)
            board.addWeapon(w);
        for (Estate e : estateList) {
        	board.addEstate(e);
        }
        
        board.setBoardSquares();
        
        solution();
	}
	
	/**
	 *
	 * Starts the game, initialises players and sets up the logic
	 *
	 */
    public void startGame() {
	    getTextOutputArea().setText("");
    	rules();
    	//nCalls set up methods from GUI.
    	playerNum = getNumPlayers();
    	ArrayList<String> playerNames = getPlayerNames(playerNum);    	
    	playersPlaying = choosePlayers(playerList, playerNames);   
        distributeCards();

        playerCount = playerNum - 1;
        currentPlayer = playersPlaying.get(playerCount-1);
        setPlayerSquares();
        setWeaponSquares();
        redraw();
        running = true;
        displayMessage("Press OK when you're ready to start!", " Start");
        startNextTurn();
    }
    
	/**
	 * 
	 * Is called when the board is updated.
	 * 
	 */
	@Override
	protected void redraw(Graphics g) {
		if (board != null) {
			board.drawBoard(g);
			for (Player p : playersPlaying) {
				if (p.getSquare()!=null && p.canMakeSolveAttempt())
					board.drawPlayer(g, p);
			}
			for (Weapon w : weaponList) {
				if (w.getSquare()!=null)
					board.drawWeapon(g, w);
			}
		}
	}
	
	/**
	 * 
	 * MouseClick on a player or estate, displays their name for reference when clicked on.
	 * 
	 */
	@Override
	protected void onClick(MouseEvent e) {
		int clickedX = e.getX();
		int clickedY = e.getY();

		// displays players
		for (Player player : playersPlaying) {
			if (player.getSquareArea().contains(clickedX, clickedY)) {
                displayMessage("This is "+player.getName()+"!", "Message");
			}
		}

		// displays estates
		for (Estate estate : estateList) {
			if (estate.getEstateArea().contains(clickedX, clickedY)) {
                displayMessage("This is the "+estate.getName()+" estate!", "Message");
			}
		}
	}
	
	/**
	 * 
	 * Moves player by key event w,s,a or d. Checks with isValid to see if the move is valid.
	 * Recognises if player has entered a room.
	 * 
	 */
	@Override
	protected void onKey(KeyEvent e) {
	    if(running && move_status) {
            String str = new String();
            KeyEvent keyPressed = e;
            Square moveTo = null;	// square player will move to

            char north = 'w';
            char south = 's';
            char east = 'd';
            char west = 'a';

            // displays the moves left to the player
            getTextOutputArea().setText(currentPlayer.getName()+"'s moves left: "+ (currentPlayer.getRoll()-1));

            if (currentPlayer.getRoll() >= 0) {
                if (keyPressed.getKeyChar() == north) {
                    moveTo = new Square(currentPlayer.getSquare().getRow(), currentPlayer.getSquare().getColumn()-20);
                }
                else if (keyPressed.getKeyChar() == south) {
                    moveTo = new Square(currentPlayer.getSquare().getRow(), currentPlayer.getSquare().getColumn()+20);
                }
                else if(keyPressed.getKeyChar() == east) {
                    moveTo = new Square(currentPlayer.getSquare().getRow()+20, currentPlayer.getSquare().getColumn());
                }
                else if(keyPressed.getKeyChar() == west) {
                    moveTo = new Square(currentPlayer.getSquare().getRow()-20, currentPlayer.getSquare().getColumn());
                }
                else {
                    getTextOutputArea().setText("That is an invalid key! Please use: w,s,d or a!");
                }

                if (moveTo != null && isValidMove(moveTo) && !currentPlayer.visitedContainsSquare(moveTo)) {
                    currentPlayer.getSquare().setObject(null);
                    currentPlayer.setPlayerSquare(moveTo);
                    currentPlayer.addToVisited(moveTo);
                    moveTo.setObject(currentPlayer);
                    redraw();
                    int newRoll = currentPlayer.getRoll() - 1;
                    currentPlayer.setRoll(newRoll);
                }

                if (hasEnteredEstate(currentPlayer)) {
                    accuse_status = true;
                    solve_status = true;
                    currentPlayer.setRoll(0);
                    displayMessage("You have entered "+currentPlayer.getEstate().getName()+", you can now chose to " +
                                    "accuse another player or attempt to solve the murder.", "Message");
                    getTextOutputArea().setText("Please select your suspected murderer and murder weapon in the Accuse or Solve Attempt menu");
                }
                else if (!hasEnteredEstate(currentPlayer) && currentPlayer.getRoll() == 0) {
                    startNextTurn();
                    getTextOutputArea().setText("");
                }

            }
        }
	    if (currentPlayer.getRoll() == 0) { move_status = false; }
	}
    
	/**
	 * 
	 * Checks if a move is valid.
	 * 
	 */
    public Boolean isValidMove(Square movingSquare) {
    	// if the square being moved to is in an estate
    	for (Estate estate : estateList) {
			if (estate.getEstateArea().contains(movingSquare.getRow(), movingSquare.getColumn())){
				if (!board.isDoor(movingSquare)) {
					return false;
				}
			}
    	}

		// if the square being moved to is out of bounds
        for (Rectangle rect : board.getOutOfBounds()) {
            if (rect.contains(movingSquare.getRow(), movingSquare.getColumn())) {
                return false;
            }
        }
        if (movingSquare.getColumn() > 480 || movingSquare.getColumn() < 20 || movingSquare.getRow() > 480 || movingSquare.getRow() < 20){
            return false;
        }

		// if the square being moved to is occupied
        if (movingSquare.getObject() != null) {
        	return false;
        }
		// if the square being moved to has already been visited by the player
        if (currentPlayer.visitedContainsSquare(movingSquare)){
            return false;
        }
        
        return true;
    }
	
	/**
	 * 
	 * Checks if a player has entered an estate.
	 * 
	 * @return boolean
	 */
	public Boolean hasEnteredEstate(Player player) {
		for (Estate estate : estateList) {
			if (estate.getEstateArea().contains(player.getSquare().getRow(), player.getSquare().getColumn())){
				player.currEstate = estate;
				estate.addPlayerInside(player);
				getTextOutputArea().setText(currentPlayer.getName()+" has now entered "+estate.getName());
				return true;
			}
		}
		return false;		
	}
	
	/**
	 * 
	 * Rolls two dice, sets current players roll to totalRoll.
	 * 
	 */
	@Override
	protected void rollDice() {
	    Random rand = new Random();
	    int roll1 = rand.nextInt(6) + 1;
	    int roll2 = rand.nextInt(6) + 1;
	    int totalRoll = roll1+roll2;
	    displayMessage("You rolled a " + totalRoll + "! Move when you're ready.", "Dice rolled");
	    getTextOutputArea().setText("Use w, s, a or d to move your character.");
	    currentPlayer.setRoll(totalRoll);
        dice_status = false;
        move_status = true;
	}
	
	/**
	 * 
	 * Gets the next players turn.
	 * 
	 */
	private void setNextPlayer() {
        if (playerCount == playerNum - 1) {
            playerCount = 0;
        } 
        else {
        	playerCount++;
        }
        currentPlayer = playersPlaying.get(playerCount);
	}
	
	/**
	 * 
	 * Moves player out of an estate at the beginning of a turn if they are in an estate.
	 * 
	 */
    public void movePlayerOutOfEstate(Player player) {
		for (Square square : board.boardSquares) {
			if (!board.isPartOfAnEstate(square) && square.getObject() == null && !board.isOutOfBounds(square)) {
                player.getSquare().setObject(null);
                player.setPlayerSquare(square);
                player.addToVisited(square);
				square.setObject(player);
				break;
			}
		}
        redraw();
    }

	/**
	 * 
	 * Combine and distribute remaining cards to players after a solution has been established.
	 * 
	 */
    private void distributeCards() {
        Collections.shuffle(cards);
        for (int i = 0, len = cards.size(); i < len; i++) {
            Player player = playersPlaying.get(i % playerNum);
            player.addCard(cards.get(i));
        }
    }
    
	/**
	 * 
	 * Displays the current players hand of cards.
	 * 
	 */
	@Override
	protected void showHand() {
	    if(currentPlayer != null){
	    	String str = new String();
	    	str = "Your hand consists of: \n";
	    	for (Card card : currentPlayer.getHand()) {
	    		str = str + card.getName()+"\n";
	    	} 	    	
	    	getTextOutputArea().setText(str);
	    }
	}

	/**
	 * 
	 * Forwards the next player card to the next move.
	 * 
	 */
	@Override
	protected void selectPlayerCardHelper(String chosenPlayer) {
		this.currentPlayerCardPicked = players.get(chosenPlayer);
		if (this.currentWeaponCardPicked != null) {
			if (isAccusation())
				makeAccusation(this.currentPlayerCardPicked,this.currentWeaponCardPicked);
			if (isSolveAttempt())
				makeSolveAttempt(this.currentPlayerCardPicked,this.currentWeaponCardPicked);
		}
		
	}
	
	/**
	 * 
	 * Forwards the selected weapon card to the next move.
	 * 
	 */
	@Override
	protected void selectWeaponCardHelper(String chosenWeapon) {
		this.currentWeaponCardPicked = weapons.get(chosenWeapon);
		if (this.currentPlayerCardPicked != null) {
			if (isAccusation())
				makeAccusation(this.currentPlayerCardPicked,this.currentWeaponCardPicked);
			if (isSolveAttempt())
				makeSolveAttempt(this.currentPlayerCardPicked,this.currentWeaponCardPicked);
		}
	}
    
	/**
	 * 
     * A player can choose to make one solveAttempt throughout the game. If the guess is correct, they win the game. If it
     * is incorrect, they can no longer make a solve attempt and cannot win the game.
     * 
     */	
	private void makeSolveAttempt(Player selectedPlayer, Weapon selectedWeapon) {
	    accuse_status = false;
    	if (isSolveAttempt() && currentPlayer != null && currentPlayer.getEstate() != null && currentPlayer.canMakeSolveAttempt()) {

    		// displays the selected cards
            displayMessage("You selected " + this.currentPlayerCardPicked.getName() + " as the murderer, "
                    + this.currentWeaponCardPicked.getName() + " as the weapon used, and "
                    + currentPlayer.getEstate().getName() + " as the murder estate. ", "Your Accusation");

    		Estate selectedEstate = currentPlayer.getEstate();

			// if the player accused isn't already in the estate teleport them
			if (currentPlayer != selectedPlayer) {
				teleportPlayer(selectedPlayer, selectedEstate);
			}
			// if the weapon isn't already in the estate teleport it
			if (!currentPlayer.getEstate().getEstateArea().contains(selectedWeapon.getSquare().getRow(), selectedWeapon.getSquare().getColumn())) {
				teleportWeapon(selectedEstate, selectedWeapon.getEstate());
			}

			// checks if the solve attempt is correct
            if (selectedPlayer.getName().equals(murderer.getName()) && 
            	selectedWeapon.getName().equals(murderWeapon.getName()) &&
            	selectedEstate.getName().equals(murderEstate.getName())){
            	winner = currentPlayer;
            	GameOver();
            }

            // if solve attempt is incorrect
            else {
            	currentPlayer.setCanMakeSolveAttempt();
            	displayMessage("Sorry " + currentPlayer.getName() + ", unfortunately that was incorrect! You cannot " +
                        "make anymore solve attempts or accusations", "Incorrect solve attempt");
            }            		
        }

    	// if user already made an incorrect guess
        else {
           displayMessage("You cannot make a solve attempt! You are exempt from making guesses", "No more solve attempts left");
        }

        solve_status = false;
    	this.currentWeaponCardPicked = null;
    	this.currentPlayerCardPicked = null;
    	resetSolveAttempt();
    	startNextTurn();
	}

    /**
     * 
     * Once a player is in an estate - they can make an accusation of the murder solution.
     * Asks the player for the suspected murderer, weapon and estate and creates a refutation card
     * from the accusation.
     * 
     */
    public void makeAccusation(Player selectedPlayer, Weapon selectedWeapon){
        solve_status = false;
    	if (isAccusation() && currentPlayer != null && currentPlayer.getEstate() != null && currentPlayer.canMakeSolveAttempt()) {

			// displays the selected cards
            displayMessage("You selected " + this.currentPlayerCardPicked.getName() + " as the murderer, "
                    + this.currentWeaponCardPicked.getName() + " as the weapon used, and "
                    + currentPlayer.getEstate().getName() + " as the murder estate. ", "Your Accusation");
        	
        	Estate selectedEstate = currentPlayer.getEstate();

            // if the player accused isn't already in the estate teleport them
        	if (currentPlayer != selectedPlayer) {
                teleportPlayer(selectedPlayer, selectedEstate);
            }
        	// if the weapon isn't already in the estate teleport it
        	if (!currentPlayer.getEstate().getEstateArea().contains(selectedWeapon.getSquare().getRow(), selectedWeapon.getSquare().getColumn())) {
                teleportWeapon(selectedEstate, selectedWeapon.getEstate());
            }
        	redraw();

        	// refute
            ArrayList<Player> playersRefuting = (ArrayList<Player>) playersPlaying.clone();
            playersRefuting.remove(this.currentPlayer);
        	
        	for (int i = 0; i < playersRefuting.size(); i++) {
                Player playerRefuting = playersRefuting.get(i);
                ArrayList<Card> refutationCards = new ArrayList<Card>();
                displayMessage(playerRefuting.getName() + ", one of your cards could now be refuted.\nCome to the screen!",
                        "Refutation Time!");

                // Adds a card to refutation cards if a card from players hand matches a refutation
                for (Card card : playerRefuting.getHand()){
                    if (card.getName().equals(selectedPlayer.getName()))
                        refutationCards.add(card);
                    if (card.getName().equals(selectedWeapon.getName()))
                        refutationCards.add(card);
                    if (card.getName().equals(selectedEstate.getName()))
                        refutationCards.add(card);
                }
                
                Card refutationCard = null;
                // If there are no refutation cards, the player cannot refute
                if (refutationCards.size() == 0){
                	displayMessage(playerRefuting.getName() + ", you have no refutation cards.", "Refutation Time!");
                    continue;
                }

                // If there is only one refutation card, then it is selected
                else if (refutationCards.size() == 1){
                    refutationCard = refutationCards.get(0);
                    askRefutation(currentPlayer, refutationCard);
                }

                // If there are more than 1 refutation cards, player selects one
                else {
                    refutationCard = selectRefutationCard(playerRefuting, refutationCards);
                    askRefutation(currentPlayer, refutationCard);
                }
        	} 
        }
    	accuse_status = false;
    	this.currentWeaponCardPicked = null;
    	this.currentPlayerCardPicked = null;
    	resetAccusation();
        startNextTurn();
    }
    
	/**
	 * 
	 * Teleport a player to a given estate.
	 * 
	 */
    public void teleportPlayer(Player player, Estate estate) {
    	Square moveTo = board.movePlayerToEstate(estate);
    	player.getSquare().setObject(null);
    	player.setPlayerSquare(moveTo);  
    	player.setEstate(estate);
		moveTo.setObject(player);
    }

	/**
	 * 
	 * Teleport a weapon to a given estate.
	 * 
	 */
    public void teleportWeapon(Estate current, Estate selected){          
    	Weapon originalWeapon = current.getWeapon();
    	Weapon swappedWeapon = selected.getWeapon();
    	
    	originalWeapon.setOldSquare(originalWeapon.getSquare());
    	originalWeapon.getOldSquare().setObject(null);
    	originalWeapon.setWeaponSquare(null);
    	
    	swappedWeapon.setOldSquare(swappedWeapon.getSquare());
    	swappedWeapon.getOldSquare().setObject(null);
    	swappedWeapon.setWeaponSquare(null);
    	
    	swappedWeapon.setWeaponSquare(originalWeapon.getOldSquare());
    	swappedWeapon.setEstate(current);
    	current.setWeapon(swappedWeapon);
    	swappedWeapon.getSquare().setObject(originalWeapon);
    	
    	originalWeapon.setWeaponSquare(swappedWeapon.getOldSquare());
    	originalWeapon.setEstate(selected);
    	selected.setWeapon(originalWeapon);
    	originalWeapon.getSquare().setObject(swappedWeapon);
    }
    
	/**
	 * 
	 * Checks if there are players left who can guess.
	 * 
	 * @return boolean
	 * 
	 */
    public Boolean noPlayersLeftToGuess() {
    	for (Player player : playersPlaying) {
    		if (player.canMakeSolveAttempt() == true) {
    			return false;
    		}
    	}
    	return true;
    }

	/**
	 * 
	 * Starts the next turn, checks if this is valid before moving forward.
	 * 
	 */
    public void startNextTurn() {
        currentPlayer.getVisited().clear();
        setNextPlayer();

        // moves players out of estate (if there are any)
        for (Player player : playersPlaying) {
            if (player.getEstate() != null) {
                player.setEstate(null);
                movePlayerOutOfEstate(player);
            }
        }

        // if all players have made solve attempts
        if (noPlayersLeftToGuess()) {
            JOptionPane.showMessageDialog(null, "There are no players left who can make guesses. Game Over.");
            running = false;
            currentPlayer = null;
            GameOver();
        }

        // if the current player has made a solve attempt
        else if (!currentPlayer.canMakeSolveAttempt()) {
            displayMessage("Sorry " + currentPlayer.getName() + ", you have already made a solve attempt You can " +
                    "no longer take your turn. However you may still continue to refute!",
                    currentPlayer.getName()+" turn");
            startNextTurn();
        }

        // current player is able to play
        else {
            dice_status = true;
            displayMessage("It's " + currentPlayer.getName() + "'s turn. Please roll the dice.", currentPlayer.getName()+" turn");
            getTextOutputArea().setText("Please click the Roll Dice button");
        }
    }
    
	/**
	 * 
	 * Ends the game if there is a winner or if all players cannot guess anymore.
	 * 
	 */
    public void GameOver(){
        // Prints the solution
    	String str = new String("GAME OVER!"
    			+ "\nThe murderer was: " + murderer.getName()
    			+ "\nThe murder weapon was: " + murderWeapon.getName()
        		+ "\nThe murderer estate was: " + murderEstate.getName() + "\n");
    	
    	displayMessage(str, "Game Over!");
        // Prints the winner if there was one
        if (winner == null) {
        	JOptionPane.showMessageDialog(null,"Unfortunately, there was no winner.");
        }
        else {
        	JOptionPane.showMessageDialog(null,"The winner is: " + winner.getName()+"!!!");
        }      
    }
    
    
    public static void main(String[] args){
    	new MurderMadness();
    }
    
    //******************************* SET UP GAME ***********************************//

	/**
	 * 
	 * Creates new board for the game.
	 * 
	 */
    private void setUpBoard() {
        board = new Board();
    }
    
	/**
	 * 
	 * Creates cards for a new game.
	 * 
	 */
    private void setUpCards() {
        // Creates player cards
        addPlayer(new Player("Lucilla", Color.RED));
        addPlayer(new Player("Bert", Color.YELLOW));
        addPlayer(new Player("Maline", Color.BLUE));
        addPlayer(new Player("Percy", Color.GREEN));

        // Creates weapon cards
        addWeapon(new Weapon("Broom", "b", Color.BLACK));
        addWeapon(new Weapon("Scissors", "s", Color.BLACK));
        addWeapon(new Weapon("Knife", "k", Color.BLACK));
        addWeapon(new Weapon("Shovel", "S", Color.BLACK)); 
        addWeapon(new Weapon("iPad", "i", Color.BLACK));

        // Creates estate cards
        addEstate(new Estate("Haunted House"));
        addEstate(new Estate("Manic Manor"));
        addEstate(new Estate("Villa Celia"));
        addEstate(new Estate("Calamity Castle"));
        addEstate(new Estate("Peril Palace"));
    }

	/**
	 * 
	 * Adds a player to the game.
	 * 
	 */
    private void addPlayer(Player player) {
        cards.push(player);
        cardMap.put(player.getName(), player);
        players.put(player.getName(), player);
        playerList.add(player);
    }

	/**
	 * 
	 * Adds a weapon to the game.
	 * 
	 */
    private void addWeapon(Weapon weapon) {
        cards.push(weapon);
        cardMap.put(weapon.getName(), weapon);
        weapons.put(weapon.getName(), weapon);
        weaponList.add(weapon);
    }

	/**
	 * 
	 * Adds an estate to the game.
	 * 
	 */
    private void addEstate(Estate estate) {
        cards.push(estate);
        cardMap.put(estate.getName(), estate);
        estates.put(estate.getName(), estate);
        estateList.add(estate);
    }

	/**
	 * 
	 * Sets original weapon squares on the board, assigns them to an estate.
	 * 
	 */
    private void setWeaponSquares() {
        weapons.get("Broom").setWeaponSquare(board.getSquare(100, 100));
        weapons.get("Broom").setEstate(estates.get("Haunted House"));
        estates.get("Haunted House").setWeapon(weapons.get("Broom"));
        board.getSquare(100, 100).setObject(weapons.get("Broom"));

        weapons.get("Scissors").setWeaponSquare(board.getSquare(400, 100));
        weapons.get("Scissors").setEstate(estates.get("Manic Manor"));
        estates.get("Manic Manor").setWeapon(weapons.get("Scissors"));
        board.getSquare(400, 100).setObject(weapons.get("Scissors"));

        weapons.get("Knife").setWeaponSquare(board.getSquare(100,400));
        weapons.get("Knife").setEstate(estates.get("Calamity Castle"));
        estates.get("Calamity Castle").setWeapon(weapons.get("Knife"));
        board.getSquare(100,400).setObject(weapons.get("Knife"));

        weapons.get("Shovel").setWeaponSquare(board.getSquare(400,400));
        weapons.get("Shovel").setEstate(estates.get("Peril Palace"));
        estates.get("Peril Palace").setWeapon(weapons.get("Shovel"));
        board.getSquare(400,400).setObject(weapons.get("Shovel"));

        weapons.get("iPad").setWeaponSquare(board.getSquare(260,240));
        weapons.get("iPad").setEstate(estates.get("Villa Celia"));
        estates.get("Villa Celia").setWeapon(weapons.get("iPad"));
        board.getSquare(260,240).setObject(weapons.get("iPad"));
    }

	/**
	 * 
	 * Sets original player squares on the board.
	 * 
	 */
    private void setPlayerSquares() {
        players.get("Lucilla").setPlayerSquare(board.getSquare(440, 280));
        players.get("Bert").setPlayerSquare(board.getSquare(280, 40));
        players.get("Maline").setPlayerSquare(board.getSquare(40, 280));
        players.get("Percy").setPlayerSquare(board.getSquare(280, 440));
    }

	/**
	 * 
	 * Selects the solution cards.
	 * 
	 */
    private void solution() {
        Random random = new Random();
        murderer = playerList.get(random.nextInt(playerList.size()));
        murderWeapon = weaponList.get(random.nextInt(weaponList.size()));
        murderEstate = estateList.get(random.nextInt(estateList.size()));
        cards.remove(murderer);
        cards.remove(murderWeapon);
        cards.remove(murderEstate);
    }
}

