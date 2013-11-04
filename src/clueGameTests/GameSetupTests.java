/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGameTests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueBoard.*;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ClueGame;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

public class GameSetupTests {
	
	private static Board board;
	private static ClueGame thisGame;
	private static final int NUM_CARDS = 24;
	private static final int NUM_PLAYER_CARDS = 6;
	private static final int NUM_ROOM_CARDS = 9;
	private static final int NUM_WEAPON_CARDS = 9;
	
	@Before
	public void setUp() throws Exception {
		String layoutFile = "ClueLayout.csv";
		String legendFile = "ClueLegend.txt";
		//String playerListFile = "cluePlayers.txt";
		//String weaponListFile = "clueWeapons.txt";
		board = new Board(layoutFile, legendFile);
		board.loadConfigFiles();
		thisGame = new ClueGame();
		thisGame.initializeBoard(board);
		thisGame.loadConfigFiles();
	}
	
	@Test
	public void testLoadPlayers() {
		//the first player in the player list should be the human player
		//this first check will confirm that, and that the second and
		//last players are computer players
		assertTrue(thisGame.playerList.get(0).isHuman());
		assertTrue(thisGame.playerList.get(1).isComputer());
		assertTrue(thisGame.playerList.get(5).isComputer());
		
		//now that we know the type of each of the first three players,
		//we'll instantiate one of each (one human, two computer players)
		HumanPlayer human = (HumanPlayer) thisGame.playerList.get(0);
		ComputerPlayer computer1 = (ComputerPlayer) thisGame.playerList.get(1);
		ComputerPlayer computer2 = (ComputerPlayer) thisGame.playerList.get(5);
		
		//create the colors to test that the players' colors are loaded correctly
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);
		Color pink  = new Color(255, 175, 175);
		
		//test the known names/colors/starting locations of the human player
		//and two computer players
		assertEquals("The Doctor", human.getName());
		assertEquals(blue, human.getColor());
		assertEquals(board.calcIndex(0,7), human.getStartingLocation());
		
		assertEquals("River Song", computer1.getName());
		assertEquals(green, computer1.getColor());
		assertEquals(board.calcIndex(9,0), computer1.getStartingLocation());
		
		assertEquals("Dalek Caan", computer2.getName());
		assertEquals(pink, computer2.getColor());
		assertEquals(board.calcIndex(19,24), computer2.getStartingLocation());
	}
	
	@Test
	public void testLoadCards() {
		int playerCards = 0;
		int roomCards = 0;
		int weaponCards = 0;
		int noTypeCards = 0;
		
		//check that there are the correct number of cards in the deck
		assertEquals(NUM_CARDS, thisGame.deck.size());
		
		//check that there are the correct number of each type of card in the deck
		//to do this, we run through a loop that increments each of the local variables
		//playerCards, roomCards, or weaponCards, depending on the type of the current card
		for (int i = 0; i < thisGame.deck.size(); i++) {
			Card currentCard = thisGame.deck.get(i);
			
			if (currentCard.getType() == CardType.PERSON) {
				playerCards++;
			} else if (currentCard.getType() == CardType.ROOM) {
				roomCards++;
			} else if (currentCard.getType() == CardType.WEAPON) {
				weaponCards++;
			} else {
				noTypeCards++; //if this stays at zero, then all cards have a defined type
			}
		}
		
		//make sure the correct number of each card type is in the deck
		assertEquals(NUM_PLAYER_CARDS, playerCards);
		assertEquals(NUM_ROOM_CARDS, roomCards);
		assertEquals(NUM_WEAPON_CARDS, weaponCards);
		
		//test cards
		Card theDoctor = new Card("The Doctor", CardType.PERSON);
		Card observatory = new Card("Observatory", CardType.ROOM);
		Card atomDivider = new Card("Atom Divider", CardType.WEAPON);
	
		//check the deck for each test card
		assertTrue(thisGame.deck.get(0).equals(theDoctor));
		assertTrue(thisGame.deck.get(7).equals(atomDivider));
		assertTrue(thisGame.deck.get(21).equals(observatory));
	}

	@Test
	public void testDealCards() {
		thisGame.deal();
		
		//check that all cards have been dealt, i.e. the deck is empty
		assertTrue(thisGame.deck.isEmpty());

		//create an array list for the dealt cards, which is used to ensure that a card has not been dealt to
		//two different players
		ArrayList<Card> dealtCards = new ArrayList<Card>();
		
		//the average number of cards should be given by the division, then each player should have
		//plus or minus 1 of the average value
		int minCards = NUM_CARDS/NUM_PLAYER_CARDS - 1;
		int maxCards = NUM_CARDS/NUM_PLAYER_CARDS + 1;		
		
		for (ArrayList<Card> playerHand : thisGame.playerCards.values()) { //for every player's hand
			//check that the number of cards in the hand is within one card of the average number of cards per player
			assertTrue(playerHand.size() >= minCards && playerHand.size() <= maxCards);
			
			//check that each card was only dealt once
			for (Card card : playerHand) { //for every card in the player's hand
				if (!dealtCards.contains(card)) {
					dealtCards.add(card);
				} else { //this means the same card was dealt more than once
					fail("Duplicate cards were dealt.");
				}
			}
		}
		
		assertEquals(NUM_CARDS, dealtCards.size());
		
	}
}
