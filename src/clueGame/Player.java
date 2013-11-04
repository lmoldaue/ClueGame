/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGame;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import clueBoard.Board;
import clueBoard.BoardCell;

public class Player {
	private String name;
	Color color;
	private int row, col, startingLocation, currentLocation;
	public ArrayList<Card> myCards;
	private Color outlineColor = new Color(0,0,0);
	
	public Player() {
		super();
		myCards = new ArrayList<Card>();
	}
	
	public Player(String name, Color color, int startLoc) {
		this.name = name;
		this.color = color;
		this.startingLocation = startLoc;
		this.currentLocation = startLoc;
		myCards = new ArrayList<Card>();
	}
	
	public Card disproveSuggestion(Suggestion s) {
		ArrayList<Card> possibleCards = new ArrayList<Card>();
		
		for (Card card : myCards) {
			if (card.equals(s.player) || card.equals(s.room) || card.equals(s.weapon)) {
				possibleCards.add(card);
			}
		}
		
		return pickCard(possibleCards);
	}
	
	public Card pickCard(ArrayList<Card> cards) {
		if(!cards.isEmpty()) {
			Random rand = new Random();
			int randCard = rand.nextInt(cards.size());
			return cards.get(randCard);
		}
		
		return null;
	}
	
	public void draw(Graphics g, Board b) {
		int row = (currentLocation / b.getNumRows());
		int column = (currentLocation % b.getNumColumns());
		g.setColor(outlineColor);
		g.drawOval(BoardCell.getSidelength()*column,
				BoardCell.getSidelength()*row, 
				BoardCell.getSidelength(), BoardCell.getSidelength());
		g.setColor(color);
		g.fillOval(BoardCell.getSidelength()*column,
				BoardCell.getSidelength()*row,
				BoardCell.getSidelength()-1, BoardCell.getSidelength()-1);
	}	
	
	public boolean isComputer() {
		return false;
	}
	
	public boolean isHuman() {
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getStartingLocation() {
		return startingLocation;
	}

	public void setStartingLocation(int startingLocation) {
		this.startingLocation = startingLocation;
	}

	public int getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	
}
