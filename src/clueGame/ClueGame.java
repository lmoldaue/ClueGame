/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGame;
import java.lang.reflect.Field;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;

import clueBoard.BadConfigFormatException;
import clueBoard.Board;
import clueBoard.BoardCell;
import clueGUI.BoardGUI;
import clueGUI.GameControlGUI;

public class ClueGame extends JFrame {
	private String playerFile;
	private String weaponFile;
	public ArrayList<Player> playerList;
	public ArrayList<Card> seen, deck, peopleList, roomList, weaponList;
	public Map<Player, ArrayList<Card>> playerCards;
	public Player human;
	Solution solution;
	Board board;
	BoardGUI boardGUI;
	GameControlGUI controlPanel;
	
	public ClueGame() throws Exception {
		this.playerFile = "cluePlayers.txt";
		this.weaponFile = "clueWeapons.txt";
		playerList = new ArrayList<Player>();
		deck = new ArrayList<Card>();
		seen = new ArrayList<Card>();
		human = new Player();
		playerCards = new HashMap<Player, ArrayList<Card>>();
		playerList = new ArrayList<Player>();
		peopleList = new ArrayList<Card>();
		roomList = new ArrayList<Card>();
		weaponList = new ArrayList<Card>();
		this.board = new Board();
		board.setGame(this);
		board.loadConfigFiles();
		boardGUI = new BoardGUI(board, this);
		controlPanel = new GameControlGUI();
		setLayout(new GridLayout(1,0));
		setExtendedState(Frame.MAXIMIZED_BOTH); 
		//setSize(new Dimension(BoardCell.getSidelength()*board.getNumRows(), 
		//		BoardCell.getSidelength()*board.getNumColumns() + controlPanel.getHeight()));
		setTitle("Doctor Who Clue");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(BorderLayout.CENTER, boardGUI);
		add(BorderLayout.SOUTH, controlPanel);
		
	}
	
	public ClueGame(String playerFile, String weaponFile) throws Exception {
		this.playerFile = playerFile;
		this.weaponFile = weaponFile;
		playerList = new ArrayList<Player>();
		deck = new ArrayList<Card>();
		seen = new ArrayList<Card>();
		human = new Player();
		playerCards = new HashMap<Player, ArrayList<Card>>();
		playerList = new ArrayList<Player>();
		peopleList = new ArrayList<Card>();
		roomList = new ArrayList<Card>();
		weaponList = new ArrayList<Card>();
		this.board = new Board();
		board.setGame(this);
		board.loadConfigFiles();
		boardGUI = new BoardGUI(board, this);
		controlPanel = new GameControlGUI();
		setLayout(new GridLayout(1,0));
		setExtendedState(Frame.MAXIMIZED_BOTH); 
		//setSize(new Dimension(BoardCell.getSidelength()*board.getNumRows(), 
		//		BoardCell.getSidelength()*board.getNumColumns() + controlPanel.getHeight()));
		setTitle("Doctor Who Clue");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(BorderLayout.CENTER, boardGUI);
		add(BorderLayout.SOUTH, controlPanel);
	}
	
	public void initializeBoard() {
		this.board = new Board();
	}
	
	public void initializeBoard(Board board) {
		this.board = board;
	}

	public void loadConfigFiles() throws Exception { 
		
		try { 
			loadPlayerConfig();
			loadWeaponConfig();
			loadRoomConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void loadPlayerConfig() throws Exception {
		Card bobCard = null;
		int lineNumber = 0;
		
		FileReader rdr = new FileReader(playerFile);
		Scanner scn = new Scanner(rdr);
		
		while (scn.hasNextLine()) {
			lineNumber++;
			Player bob = null;
			
			String line[] = scn.nextLine().split(",");
			
			if (line[line.length-1].trim().equalsIgnoreCase("h")){
				bob = new HumanPlayer(line[0].trim(), convertColor(line[1].trim()), Integer.parseInt(line[2].trim()));
			} else if (line[line.length-1].trim().equalsIgnoreCase("c")){
				bob = new ComputerPlayer(line[0].trim(), convertColor(line[1].trim()), Integer.parseInt(line[2].trim()));
			} else
				throw new BadConfigFormatException("Incorrectly formatted player at line: " + lineNumber + ", Name: " + line[0]);
			
			playerList.add(bob);
			
			bobCard = new Card(line[0].trim(), CardType.PERSON);
			deck.add(bobCard);
			peopleList.add(bobCard);
		}
		rdr.close();
	}
	
	public void loadWeaponConfig() throws Exception {
		Card weap = null;
		
		FileReader rdr = new FileReader(weaponFile);
		Scanner scn = new Scanner(rdr);
		
		while (scn.hasNextLine()) {
			String thisLine = scn.nextLine();
			weap = new Card(thisLine.trim(), CardType.WEAPON);
			deck.add(weap);
			weaponList.add(weap);
		}
	}
	
	public void loadRoomConfig() throws Exception {
		Card theHalls = null; //wait for it
		for(Map.Entry entry: board.getRooms().entrySet()){
			//check if the room is the walkway or closet
			if ((char)entry.getKey() == 'W' || (char)entry.getKey() == ('X')) {
				continue;
			}
			theHalls = new Card((String)entry.getValue(), CardType.ROOM);
			deck.add(theHalls); //lol get it
			roomList.add(theHalls);
		}
	}
	
	
	public void deal() { 
		Collections.shuffle(deck);
		ArrayList<ArrayList<Card>> playerHands = new ArrayList<ArrayList<Card>>();
		
		for (int i=0; i < playerList.size(); i++) {
			playerHands.add(new ArrayList<Card>());
		}
		
		int cardNumber = 0;
		while(!deck.isEmpty()) {
			for (Player player : playerList) {
				Card currentCard = deck.get(0);
				player.myCards.add(currentCard);
				playerHands.get(playerList.indexOf(player)).add(currentCard);
				deck.remove(currentCard);				
				if (deck.isEmpty()) {
					break;
				}
			}
		}
		
		for (Player player : playerList) {
			playerCards.put(player, playerHands.get(playerList.indexOf(player)));
		}
	}

	
	public void selectSolution() { 
		Card player = null;
		Card room = null;
		Card weapon = null;
		boolean needPlayer = true;
		boolean needRoom = true;
		boolean needWeapon = true;
		
		for (Card card : deck) {
			if ((card.getType() == CardType.PERSON) && needPlayer) {
				player = card;
				deck.remove(card);
				needPlayer = false;
			} else if ((card.getType() == CardType.ROOM) && needRoom) {
				room = card;
				deck.remove(card);
				needRoom = false;
			} else if ((card.getType() == CardType.WEAPON) && needWeapon) {
				weapon = card;
				deck.remove(card);
				needWeapon = false;
			}
			
			if (!needPlayer && !needRoom && !needWeapon) {
				solution = new Solution(player, room, weapon);
			}
		}
	}
	
	public void addToSeen(Card card) {
		seen.add(card);
	}
	
	public Card handleSuggestion(Suggestion s, Player accusingPerson) {
		Card card = null;
		for (Player player : playerList) {
			if (player.equals(accusingPerson)) {
				continue;
			}
			
			card = player.disproveSuggestion(s);
			if(card != null) {
				return card;
			}
		}
		
		return card;
	}
	
	
	public void setHumanPlayer(HumanPlayer humanPlayer) {
		human = humanPlayer;
	}
	
	public void setPlayers(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
	
	public void drawPlayers(Graphics g, Board b) {
		for (Player player : playerList) {
			player.draw(g, b);
		}
	}
	
	// Be sure to trim the color, we don't want spaces around the name
	public Color convertColor(String strColor) {
		Color color; 
		try {     
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());     
			color = (Color)field.get(null); } 
		catch (Exception e) {  
			color = null; // Not defined } 
		}
		return color;
	}
	
	public static void main(String[] args) throws Exception {
		try {
			ClueGame game = new ClueGame();
			game.loadConfigFiles();
			game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			game.setVisible(true);
		} catch (BadConfigFormatException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("File not found (exception caught).");
		} //catch (Exception e) {
			//System.err.println("Other exception caught.");
		//}
	}
}
