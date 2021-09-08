package game;
/**
 * Represents our data on the GUI to the user.
 *
 * View class
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public abstract class GUI extends JFrame{
	
	//Mouse event
	protected abstract void onClick(MouseEvent e);
	
	//Key event for moving players
    protected abstract void onKey(KeyEvent e);

    // rolls dice for players turn
	protected abstract void rollDice();

	// shows hand to player
	protected abstract void showHand();

	// helps select cards
    protected abstract void selectPlayerCardHelper(String string);
    protected abstract void selectWeaponCardHelper(String string);

    // initiates a new game
	protected abstract void startGame();

	// redraws the graphics on the board
	protected abstract void redraw(Graphics g);

	// game ends
	protected abstract void GameOver();
	
	/** GUI Fields **/
	
	public static final int DRAWING_WIDTH = 600;
	public static final int DRAWING_HEIGHT = 520;
	
	private static final boolean UPDATE_KEYMOVEMENT = true;

	private JFrame frame;
	private JPanel controls;
	private JComponent drawing; 
	private JTextArea textOutputArea;
	private JButton takeTurn;

	// gui features status
	public boolean dice_status = false;
	public boolean accuse_status = false;
	public boolean solve_status = false;

	private boolean accuse = false;
	private boolean solve = false;

    public GUI() {   	
        createView();
    }
	
	public JTextArea getTextOutputArea() {
		return textOutputArea;
	}

	public boolean isAccusation() { return accuse; }

	public boolean isSolveAttempt() { return solve; }
	
	public void resetAccusation() {
		accuse = !accuse;
	}
	
	public void resetSolveAttempt() {
		solve = !solve;
	}

	public void redraw() {
		frame.repaint();
	}
	
	/**
	 * 
	 * Creates the main view of the JFrame on the GUI. Implements JButtons RollDice and Show Hand.
	 * Adds the JMenu to the JFrame and adjusts design aspects of the JFrame.
	 * 
	 */
    public void createView() {  	
		takeTurn = new JButton("Roll Dice");
		takeTurn.addActionListener(ev -> {
			if (dice_status) { rollDice(); }
		});
		
		if (UPDATE_KEYMOVEMENT) {
			takeTurn.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					onKey(e);
				}
			});
		}
		
		JButton showHand = new JButton("Show Hand");
		showHand.addActionListener(ev -> showHand());

		controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

		Border edge = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		controls.setBorder(edge);

		JPanel nav = new JPanel();
		nav.setLayout(new GridLayout(1, 1));
		controls.add(nav);
		controls.add(Box.createRigidArea(new Dimension(0, 0)));

		JPanel navigation = new JPanel();
		navigation.setMaximumSize(new Dimension(40, 60));
		navigation.setLayout(new GridLayout(1, 4));
		navigation.add(showHand);
		navigation.add(takeTurn);
		
		controls.add(navigation);

		drawing = new JComponent() {
			protected void paintComponent(Graphics g) {
				redraw(g);
			}
		};
		
		drawing.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				requestFocusInWindow();
				onClick(e);
				redraw();
			}
		});
		
		drawing.setPreferredSize(new Dimension(DRAWING_WIDTH,DRAWING_HEIGHT));
		drawing.setVisible(true);
        drawing.setVisible(true);

		textOutputArea = new JTextArea(10,0);
		textOutputArea.setLineWrap(true);
		textOutputArea.setWrapStyleWord(true);
		
		JScrollPane scroll = new JScrollPane(textOutputArea);
		DefaultCaret caret = (DefaultCaret) textOutputArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setDividerSize(5); 
		split.setContinuousLayout(true); 
		split.setResizeWeight(1); 

		split.setBorder(BorderFactory.createEmptyBorder());
		split.setTopComponent(drawing);
		split.setBottomComponent(scroll);

		frame = new JFrame("MurderMadness");
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(controls, BorderLayout.NORTH);
		frame.setJMenuBar(createMenuBar());
		frame.add(split, BorderLayout.CENTER);
		
		// always do these two things last, in this order.
		frame.pack();
		frame.setVisible(true); 
		
    	getTextOutputArea().setText("WELCOME TO MURDER MADNESS!\nPlease press start under Game in the menu! :-)");
    }
    
	/**
	 * 
	 * Creates the JMenuBar - includes the start, restart, rules, accuse and solve attempt options.
	 * Adds action listeners to each JMenuItem.
	 * 
	 * @return the JMenu bar
	 * 
	 */
	private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem startGame = new JMenuItem("Start");
        startGame.addActionListener((event)-> startGame());
        gameMenu.add(startGame);

        JMenuItem restartMenuItem = new JMenuItem("Restart");
        restartMenuItem.setToolTipText("Restart the game");
        restartMenuItem.addActionListener((event)-> new MurderMadness());
        gameMenu.add(restartMenuItem);

		JMenuItem rulesMenuItem = new JMenuItem("Rules");
		rulesMenuItem.setToolTipText("Read the rules");
		rulesMenuItem.addActionListener((event)-> rules());
		gameMenu.add(rulesMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setToolTipText("Exit the game");
		exitMenuItem.addActionListener((event)-> GameOver());
		exitMenuItem.addActionListener((event) -> System.exit(0));
		gameMenu.add(exitMenuItem);
            
        menuBar.add(gameMenu);
        
        JMenu accuse = new JMenu("Accuse");
        JMenuItem pickPlayerA = new JMenuItem("Pick Player");
        pickPlayerA.addActionListener((event)-> choosePlayerCard());
		accuse.add(pickPlayerA);
        JMenuItem pickWeaponA = new JMenuItem("Pick Weapon");
        pickWeaponA.addActionListener((event)-> chooseWeaponCard());
        pickWeaponA.addActionListener((event)-> resetAccusation());
        accuse.add(pickWeaponA);
        
        menuBar.add(accuse);
        
		JMenu solveAttempt = new JMenu("Solve Attempt");
        JMenuItem pickPlayerS = new JMenuItem("Pick Player");
        pickPlayerS.addActionListener((event)-> choosePlayerCard());
        solveAttempt.add(pickPlayerS);
        JMenuItem pickWeaponS = new JMenuItem("Pick Weapon");
        pickWeaponS.addActionListener((event)-> chooseWeaponCard());
        pickWeaponS.addActionListener((event)-> resetSolveAttempt());
        solveAttempt.add(pickWeaponS);
        
        menuBar.add(solveAttempt);
        		
        return menuBar;
    }
	
	/**
	 * 
	 * Helper method for a player refuting or accusing to choose a player card.
	 * 
	 * @return the number of players playing
	 * 
	 */
    public int getNumPlayers() {
		String[] numOptions = { "3", "4"};
		String numPlayers = null;
		
		while (numPlayers==null) {
			numPlayers = (String) JOptionPane.showInputDialog(null, "Select the amount of players:", "MurderMadness",
					JOptionPane.QUESTION_MESSAGE, null, numOptions, numOptions[0]);
		}
		return Integer.parseInt(numPlayers);	
	}
    
	/**
	 * 
	 * Retrieves all the players names based on the number of players.
	 * 
	 * @return a list of the players names.
	 * 
	 */
	public ArrayList<String> getPlayerNames(int numPlayers) {
		ArrayList<String> names = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < numPlayers; i++) {
			count++;
			String name = null;
			while (name == null && name != "")
				name = JOptionPane.showInputDialog(null, "Player " + count + " please enter your name: ", "Player Name", 1);
			names.add(name);
		}
		return names;
	}
	
	/**
	 * 
	 * Where players choose their characters at the beginning of the game.
	 * Assigns the characters to the players.
	 * 
	 * @return an arraylist of the chosen characters.
	 * 
	 */
    public ArrayList<Player> choosePlayers(ArrayList <Player> characters, ArrayList<String> playerNames) {
    	ArrayList<Player> charactersPlaying = new ArrayList<>();
		String[] charOptions = { "Lucilla", "Bert", "Maline", "Percy" };
		for (String player : playerNames) {
			String playerChoice = null;
			while (playerChoice == null) {
				try {
					playerChoice = (String) JOptionPane.showInputDialog(null, ""+player+ " select your character:", "MurderMadness",
					JOptionPane.QUESTION_MESSAGE, null, charOptions, charOptions[0]);
					if (playerChoice.equals("Lucilla")) {
						displayMessage(player + " you have chosen Lucilla, your players counter is red " +
								"on the board.", "Player Selection");
					}
					else if (playerChoice.equals("Bert")) {
						displayMessage(player + " you have chosen Bert, your players counter is yellow " +
								"on the board.", "Player Selection");
					}
					else if (playerChoice.equals("Maline")) {
						displayMessage(player + " you have chosen Maline, your players counter is blue " +
								"on the board.", "Player Selection");
					}
					else if (playerChoice.equals("Percy")) {
						displayMessage(player + " you have chosen Percy, your players counter is green " +
								"on the board.", "Player Selection");
					}
				} catch (Exception e) {
					displayMessage("Please select a character.", "Warning");
				}
			}
			// remove player choice
			for (int i = 0; i < charOptions.length; i++) {
				if (charOptions[i].equals(playerChoice)) {
					charOptions[i] = "";
				}

			}
			for (Player p : characters) {
				if (playerChoice.equals(p.getName())) {
					p.setPlayerName(player); //sets the characters "player name" to the selected players name.
					charactersPlaying.add(p); //return a list of characters with player names (players who are playing).
				}
			}

		}
		return charactersPlaying;
    }
    
	/**
	 * 
	 * Displays a message in JOptionPane.
	 * 
	 */
    public void displayMessage(String body, String title) {
		JOptionPane.showMessageDialog(frame, body, title, JOptionPane.PLAIN_MESSAGE);
	}
    
	/**
	 * 
	 * Helper method for a player refuting or accusing to choose a weapon card.
	 * 
	 */
    public void chooseWeaponCard() {
		if (accuse_status || solve_status) {
			JDialog buttonDisplay = new JDialog(frame, "Weapon Selection");
			buttonDisplay.setBounds(400, 400, 400, 400);
			buttonDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			buttonDisplay.setBounds(200, 200, 200, 200);

			JButton weaponButton = new JButton("Confirm");
			ButtonGroup weaponButtonGroup = new ButtonGroup();

			JRadioButton broom = new JRadioButton();
			JRadioButton scissors = new JRadioButton();
			JRadioButton knife = new JRadioButton();
			JRadioButton shovel = new JRadioButton();
			JRadioButton ipad = new JRadioButton();

			broom.setText("Broom");
			broom.setActionCommand(broom.getText());
			scissors.setText("Scissors");
			scissors.setActionCommand(scissors.getText());
			knife.setText("Knife");
			knife.setActionCommand(knife.getText());
			shovel.setText("Shovel");
			shovel.setActionCommand(shovel.getText());
			ipad.setText("iPad");
			ipad.setActionCommand(ipad.getText());

			buttonDisplay.add(broom);
			buttonDisplay.add(scissors);
			buttonDisplay.add(knife);
			buttonDisplay.add(shovel);
			buttonDisplay.add(ipad);
			buttonDisplay.add(weaponButton);

			weaponButtonGroup.add(broom);
			weaponButtonGroup.add(scissors);
			weaponButtonGroup.add(knife);
			weaponButtonGroup.add(shovel);
			weaponButtonGroup.add(ipad);

			weaponButton.addActionListener(new ActionListener() {
				String chosenWeapon = "";

				@Override
				public void actionPerformed(ActionEvent e) {
					if (broom.isSelected()) {
						chosenWeapon = "Broom";
						buttonDisplay.dispose();
						selectWeaponCardHelper(chosenWeapon);
					} else if (scissors.isSelected()) {
						chosenWeapon = "Scissors";
						buttonDisplay.dispose();
						selectWeaponCardHelper(chosenWeapon);
					} else if (knife.isSelected()) {
						chosenWeapon = "Knife";
						buttonDisplay.dispose();
						selectWeaponCardHelper(chosenWeapon);
					} else if (shovel.isSelected()) {
						chosenWeapon = "Shovel";
						buttonDisplay.dispose();
						selectWeaponCardHelper(chosenWeapon);
					} else if (ipad.isSelected()) {
						chosenWeapon = "iPad";
						buttonDisplay.dispose();
						selectWeaponCardHelper(chosenWeapon);
					}
				}
			});

			buttonDisplay.setLayout(new GridLayout(3, 1));
			buttonDisplay.pack();
			buttonDisplay.setVisible(true);
		}
		else {
			displayMessage("You may not select this option during this time.", "Warning");
		}
	}
    
	/**
	 * 
	 * Helper method for a player refuting or accusing to choose a player card.
	 * 
	 */
    
    public void choosePlayerCard() {
		if (accuse_status || solve_status) {
			JDialog buttonDisplay = new JDialog(frame, "Player Selection");
			buttonDisplay.setBounds(400, 400, 400, 400);
			buttonDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			buttonDisplay.setBounds(200, 200, 200, 200);

			JButton playerButton = new JButton("Confirm");
			ButtonGroup playerButtonGroup = new ButtonGroup();

			JRadioButton lucilla = new JRadioButton();
			JRadioButton bert = new JRadioButton();
			JRadioButton maline = new JRadioButton();
			JRadioButton percy = new JRadioButton();

			lucilla.setText("Lucilla");
			bert.setText("Bert");
			maline.setText("Maline");
			percy.setText("Percy");

			buttonDisplay.add(lucilla);
			buttonDisplay.add(bert);
			buttonDisplay.add(maline);
			buttonDisplay.add(percy);
			buttonDisplay.add(playerButton);

			playerButtonGroup.add(lucilla);
			playerButtonGroup.add(bert);
			playerButtonGroup.add(maline);
			playerButtonGroup.add(percy);

			playerButton.addActionListener(new ActionListener() {
				String chosenPlayer = "";

				@Override
				public void actionPerformed(ActionEvent e) {
					if (lucilla.isSelected()) {
						chosenPlayer = "Lucilla";
						buttonDisplay.dispose();
						selectPlayerCardHelper(chosenPlayer);
					} else if (bert.isSelected()) {
						chosenPlayer = "Bert";
						buttonDisplay.dispose();
						selectPlayerCardHelper(chosenPlayer);
					} else if (maline.isSelected()) {
						chosenPlayer = "Maline";
						buttonDisplay.dispose();
						selectPlayerCardHelper(chosenPlayer);
					} else if (percy.isSelected()) {
						chosenPlayer = "Percy";
						buttonDisplay.dispose();
						selectPlayerCardHelper(chosenPlayer);
					}
				}
			});

			buttonDisplay.setLayout(new GridLayout(3, 1));
			buttonDisplay.pack();
			buttonDisplay.setVisible(true);
		}
		else {
			displayMessage("You may not select this option during this time.", "Warning");
		}
	}
    
	/**
	 * 
	 * If a player has more than one refutation card, this method is used to 
	 * select just one of them.
	 * 
	 * @param playerRefuting refuting
	 * @param cardSelection of their refutation cards
	 * @return selected card
	 * 
	 */
    public Card selectRefutationCard(Player playerRefuting, ArrayList<Card> cardSelection) {
    	String[] numOptions = new String[cardSelection.size()];
    	for (int i = 0; i < cardSelection.size(); i++) {
    		numOptions[i] = cardSelection.get(i).getName();
    	}
    	
		String ans = null;		
		while (ans==null) {
			ans = (String) JOptionPane.showInputDialog(null, playerRefuting.getName()+", which card do you want to refute?", "MurderMadness",
					JOptionPane.QUESTION_MESSAGE, null, numOptions, numOptions[0]);
		}
		
		Card selectedCard = null;
		for (Card card : cardSelection) {
			if (card.getName().equals(ans)) {
				selectedCard = card;
			}
		}
    	return selectedCard;
    }
    
    public void askRefutation(Player currentPlayer, Card refutationCard) {
		displayMessage(currentPlayer.getName() + ", click OK when you're ready to view your refutation" +
				" card.", "Refutation Time!");
		displayMessage(currentPlayer.getName() + " your Refutation Card is: " + refutationCard.getName(),
				"Refutation time!");
    }
    
	/**
	 * 
	 * Prints the rules of the game.
	 * 
	 */
    public void rules() {
		Object[] buttons = {"Yes", "I think I've got it!"};
		int ans = JOptionPane.showOptionDialog(frame, "Would you like to read the rules?", "Message",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
		
		if (ans == 0) {
			String str =
					"There is a murderer on the loose! Every player is a suspect, including yourself!\n"+
	        		"Guess the murderer, murder weapon used and the estate the murder was held in to win the game!\n"+
	        		"There are 3 or 4 players, 5 weapons, and 5 estates to choose from.\n"+
	        		"Suitable for 3-4 players only.\n\n"+

	        		"RULES:\n"+
	        		"- Each player must click Roll Dice at the start of every turn, the number rolled is how many moves you will have for that turn.\n"+
					"  All moves must be used, if a player enters an estate and has moves left they can no longer move.\n"+
					"- Players can  move one square at a time in any direction they choose: \n" +
					"        w to move up\n"+
					"        a to move left\n"+
					"        s to move down\n"+
					"        d to move right\n"+
	        		"- If a player enters an estate, they can make an accusation or solve attempt of the murderer and murder weapon,\n"+
	        		"  they do this by pressing the Solve Attempt or Accuse options in the Game menu. The estate guess will always be the one\n" +
					"  they are currently in.\n"+
	        		"- If a solve attempt is selected, the player must correctly guess the solution otherwise the player unable to make\n" +
					"  further guesses or solve attempts. They are only allowed to refute.\n"+
	        		"- If an accusation is made, other players are able to refute.\n"+
	        		"- YOU MUST MAKE A REFUTATION if you are able to - the game will not move forward if you do not do this.\n"+
	        		"- If the player is proved wrong, they are exempt from making further accusations and solve attempts but can still refute further guesses.\n\n"+

	        		"PLEASE NOTE:\n"+
	        		"- Please do not look at the screen unless it is your turn, or unless you are called to view a card.\n"+
	        		"- Please do not take other player's turns for them.\n"+
	        		"- When it is the end of your turn, the next player should come to the screen.\n\n"+
					"Please avoid leaking any information gathered about the murderer to other players! We want everyone to have a fair game :)\n"+
	        		//"- When entering a name of player, weapon, or estate, ensure it's spelt correctly and starts with an upper case.\n\n"+

	        		"GOOD LUCK and HAVE FUN!\n";

			displayMessage(str, "Game Rules");
		}
		else {
			return;
		}
    }
}
