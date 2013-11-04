/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGameTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueBoard.Board;
import clueBoard.BoardCell;
import clueBoard.RoomCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ClueGame;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;
import clueGame.Suggestion;

public class GameActionTests {
	
	private static Board board;
	private static ClueGame thisGame;
	private static Solution sln; 
	private static ComputerPlayer cp;
	private static Suggestion suggested;
	private static final int NUM_CARDS = 24;
	private static final int NUM_PLAYER_CARDS = 6;
	private static final int NUM_ROOM_CARDS = 9;
	private static final int NUM_WEAPON_CARDS = 9;
	private static Card theDoctor, dalekCaan, riverSong, theSilence,
	atomDivider, glittergun, dematGun, swordOfTruth, 
	zoo, library, kitchen, observatory;
	
	@Before
	public void setUp() throws Exception {
		String layoutFile = "ClueLayout.csv";
		String legendFile = "ClueLegend.txt";
		String playerListFile = "cluePlayers.txt";
		String weaponListFile = "clueWeapons.txt";
		board = new Board(layoutFile, legendFile);
		board.loadConfigFiles();
		board.calcAdjacencies();
		thisGame = new ClueGame();
		thisGame.initializeBoard(board);
		thisGame.loadConfigFiles();
		suggested = new Suggestion();
		
		//cards that will be used as 'correct suggestion' cards
		theDoctor = new Card("The Doctor", CardType.PERSON);
		zoo = new Card("Zoo", CardType.ROOM);
		atomDivider = new Card("Atom Divider", CardType.WEAPON);
		
		//extra cards to act as incorrect suggestion cards
		dalekCaan = new Card("Dalek Caan", CardType.PERSON);
		riverSong = new Card("River Song", CardType.PERSON);
		theSilence = new Card("The Silence", CardType.PERSON);
		library = new Card("Library", CardType.ROOM);
		kitchen = new Card("Kitchen", CardType.ROOM);
		observatory = new Card("Observatory", CardType.ROOM);
		glittergun = new Card("Glittergun", CardType.WEAPON);
		dematGun = new Card("Dematerialisation Gun", CardType.WEAPON);
		swordOfTruth = new Card("The Sword of Truth", CardType.WEAPON);
		
		sln = new Solution(theDoctor, library, dematGun);

		cp = new ComputerPlayer();
	}

	@Test
	public void testCheckLeAccuse() {
		assertTrue(sln.getPerson().equals(theDoctor));
		assertTrue(sln.getRoom().equals(library));
		assertTrue(sln.getWeapon().equals(dematGun));
	}	
	
	@Test
	public void testRandTargetSelect() {
		// A location with 3 targets and no rooms: 9, 1
		board.calcTargets(9, 1, 2);
		int visit_8_0 = 0;
		int visit_8_2 = 0;
		int visit_9_3 = 0;
		
		// Run the test 100 times
		for(int i = 0; i< 100; i++) {
			BoardCell select = cp.pickLocation(board.getTargets());
			
			if(select == board.getCellAt(board.calcIndex(8, 0)))
				++visit_8_0;
			else if(select == board.getCellAt(board.calcIndex(8, 2)))
				++visit_8_2;
			else if(select == board.getCellAt(board.calcIndex(9, 3)))
				++visit_9_3;
			else 
				fail("Invalid target selected");
		}
		
		// Since we ran the test 100 times, we expect that there are 100 visits
		assertEquals(100, visit_8_0 + visit_8_2 + visit_9_3);
		
		// Check if each target was selected more than ten times
		assertTrue(visit_8_0 > 10);
		assertTrue(visit_8_2 > 10);		
		assertTrue(visit_9_3 > 10);
	}
	
	@Test
	public void testRoomPreference() {
		// A location with a room nearby: row 5, column 12
		board.calcTargets(5, 12, 2);
		int visit_3_12 = 0;
		int visit_4_13 = 0;
		int visit_6_13 = 0;
		int visit_7_12 = 0;
		int visit_o = 0;
		
		for(int i = 0; i < 500; i++) {
			BoardCell select = cp.pickLocation(board.getTargets());
			
			if(select == board.getCellAt(board.calcIndex(3, 12)))
				++visit_3_12;
			else if(select == board.getCellAt(board.calcIndex(4, 13)))
				++visit_4_13;
			else if(select == board.getCellAt(board.calcIndex(6, 13)))
				++visit_6_13;
			else if(select == board.getCellAt(board.calcIndex(7, 12)))
				++visit_7_12;
			else if(select == board.getCellAt(board.calcIndex(5, 11)))
				++visit_o;
			else
				fail("Invalid target selected");

		}
		
		// Since we ran the test 500 times, we expect that there are 500 visits
		assertEquals(500, visit_3_12 + visit_4_13 + visit_6_13 + visit_7_12 + visit_o);
		
		// We check to see if the probability for entering a room is much higher than continuing to wander drunkenly around hallways
		int largest = Collections.max(Arrays.asList(visit_3_12, visit_4_13, visit_6_13, visit_7_12, visit_o));
		assertEquals(visit_o, largest);
		
		//(the following test is actually a bit superfluous given the testRandTargetSelect test, but we'll keep it since it works)
		
		//now we will do the same test, except with the room (at row=5, column=11) as the last visited room for the player
		//in this test, we run run through the pickLocation test 1000 times, with every 100 runs checking to see if the
		//room was the most visited cell. in at least one of these checks, the room should (hopefully) not be the most visited cell,
		//since it should have the same probability of being visited as the rest

		boolean roomAlwaysVisited = true;
		
		for (int i = 0; i < 10; i ++) {
			//reset these values before each run of the inner loop
			visit_3_12 = 0;
			visit_4_13 = 0;
			visit_6_13 = 0;
			visit_7_12 = 0;
			visit_o = 0;
			
			for(int j = 0; j < 100; j++) {
				cp.setLastRoomVisited(((RoomCell) board.cells.get(board.calcIndex(5,11))).getInitial());
				BoardCell select = cp.pickLocation(board.getTargets());
				
				if(select == board.getCellAt(board.calcIndex(3, 12)))
					++visit_3_12;
				else if(select == board.getCellAt(board.calcIndex(4, 13)))
					++visit_4_13;
				else if(select == board.getCellAt(board.calcIndex(6, 13)))
					++visit_6_13;
				else if(select == board.getCellAt(board.calcIndex(7, 12)))
					++visit_7_12;
				else if(select == board.getCellAt(board.calcIndex(5, 11)))
					++visit_o;
				else
					fail("Invalid target selected");
			}
			// Since we ran the test 100 times, we expect that there are 100 visits
			assertEquals(100, visit_3_12 + visit_4_13 + visit_6_13 + visit_7_12 + visit_o);
			
			//check to see if the probability for entering the last visited room is about the same as the rest of the choices
			largest = Collections.max(Arrays.asList(visit_3_12, visit_4_13, visit_6_13, visit_7_12, visit_o));
			
			if (visit_o != largest) {
				roomAlwaysVisited = false;
			}
		}
		
		assertFalse(roomAlwaysVisited);
		
	}
	
	@Test
	public void testLeavingRoomSelections() {
		// A location which happens to be a doorway with a few targets: 18, 21
		//this doorway also has another doorway as an option
		board.calcTargets(18, 21, 2);
		int visit_17_20 = 0;
		int visit_16_21 = 0;
		int visit_17_22 = 0;
		int visit_G = 0; //
		
		// Run the test 100 times
		for(int i = 0; i< 100; i++) {
			BoardCell select = cp.pickLocation(board.getTargets());
			
			if(select == board.getCellAt(board.calcIndex(17, 20)))
				++visit_17_20;
			else if(select == board.getCellAt(board.calcIndex(16, 21)))
				++visit_16_21;
			else if(select == board.getCellAt(board.calcIndex(17, 22)))
				++visit_17_22;
			else if(select == board.getCellAt(board.calcIndex(18, 20)))
				++visit_G;
			else 
				fail("Invalid target selected");
		}
		
		// Since we ran the test 100 times, we expect that there are 100 visits
		assertEquals(100, visit_17_20 + visit_16_21 + visit_17_22 + visit_G);
		
		// Here we need to check to see whether or not visit_G is significantly larger than the others, if so.. frowny face.
		// I assumed that if we have four target locations, we should be visiting the garden about a quarter of the time, 
		// since we're trying to leave it in the first place. So, we should have less than a third of the visits be to the garden.
		assertTrue(.30 > visit_G/100);
	}

	@Test
	public void testDisproveSuggestionOnePlayerOneMatch() {
		Player player = new Player();
		//create three suggestions, each with a different card type to be held by the player
		Suggestion correctPlayer = new Suggestion(dalekCaan, observatory, dematGun);
		Suggestion correctRoom = new Suggestion(riverSong, zoo, swordOfTruth);
		Suggestion correctWeapon = new Suggestion(theSilence, kitchen, glittergun);
		Suggestion noCorrectCards = new Suggestion(riverSong, observatory, swordOfTruth);
		
		//deal cards to player
		player.myCards.add(theDoctor);
		player.myCards.add(dalekCaan);
		player.myCards.add(zoo);
		player.myCards.add(library);
		player.myCards.add(atomDivider);
		player.myCards.add(glittergun);
		
		//make sure that the card that is returned by player.disproveSuggestion is the correct card
		assertEquals(dalekCaan, player.disproveSuggestion(correctPlayer));
		assertEquals(zoo, player.disproveSuggestion(correctRoom));
		assertEquals(glittergun, player.disproveSuggestion(correctWeapon));
		assertEquals(null, player.disproveSuggestion(noCorrectCards));
	}
	
	@Test
	public void testDisproveSuggestionOnePlayerMultipleMatches() {
		ComputerPlayer computerPlayer = new ComputerPlayer();
		//create a suggestion
		Suggestion suggestion = new Suggestion(theSilence, zoo, swordOfTruth);
		
		//deal cards to player, where two of the cards can disprove the suggestion
		computerPlayer.myCards.add(theSilence); //first possible choice
		computerPlayer.myCards.add(library);
		computerPlayer.myCards.add(observatory);
		computerPlayer.myCards.add(swordOfTruth);//second possible choice
		computerPlayer.myCards.add(dalekCaan);
		
		//counters to make sure each card is chosen at least once
		int silenceChosen = 0;
		int swordChosen = 0;
		
		//the function disproveSuggestion(Suggestion s) takes theplayer's cards and the suggestion and returns one 
		//of the cards that can disprove the suggestion at random. so, this loop makes sure that this function is
		//working correctly by incrementing the number of times that either choice is selected as the card to disprove
		//the suggestion
		for (int i=0; i < 100; i++) {
			Card selectedCard = computerPlayer.disproveSuggestion(suggestion);
			if (selectedCard == theSilence) {
				silenceChosen++;
			} else if (selectedCard == swordOfTruth) {
				swordChosen++;
			} else {
				fail("Invalid card selected.");
			}
		}
		
		//100 runs through the loop should mean that cards were chosen 100 times (combined)
		assertEquals(100, silenceChosen + swordChosen);
		//make sure both were chosen at least once
		assertTrue(silenceChosen > 0);
		assertTrue(swordChosen > 0);
		
	}
	
	@Test
	public void testSuggestionAllPlayersQueried() {
		//suggestion that cannot be disproved
		Suggestion unDisprovableSuggestion = new Suggestion(dalekCaan, zoo, atomDivider);
		//suggestion that only the human can disprove
		Suggestion humanDisprovableSuggestion = new Suggestion(theSilence, zoo, atomDivider);
		//suggestion that the first computer player can disprove
		Suggestion cp1DisprovableSuggestion = new Suggestion(dalekCaan, library, atomDivider);
		//suggestion that the second computer player can disprove
		Suggestion cp2DisprovableSuggestion = new Suggestion(dalekCaan, zoo, dematGun);
		//suggestion that both computer players can disprove
		Suggestion bothCPDisprovableSuggestion = new Suggestion(theDoctor, zoo, atomDivider);
		//array list of players
		ArrayList<Player> players = new ArrayList<Player>(3);
		//human player
		HumanPlayer human = new HumanPlayer();
		
		human.myCards.add(theSilence);
		human.myCards.add(observatory);
		human.myCards.add(glittergun);
		players.add(human); //human is at index 0
		players.add(new ComputerPlayer());
		players.add(new ComputerPlayer());
		players.get(1).myCards.add(theDoctor);
		players.get(1).myCards.add(library);
		players.get(1).myCards.add(swordOfTruth);
		players.get(2).myCards.add(riverSong);
		players.get(2).myCards.add(observatory);
		players.get(2).myCards.add(dematGun);
		
		thisGame.setPlayers(players);
		thisGame.setHumanPlayer(human);
		
		//check the suggestion that on one can prove
		assertEquals(null, thisGame.handleSuggestion(unDisprovableSuggestion, thisGame.playerList.get(0)));
		//check the suggestion that only the human can prove, and make the accuser a computer player
		assertEquals(theSilence, thisGame.handleSuggestion(humanDisprovableSuggestion, thisGame.playerList.get(1)));
		//check that if the human is the accuser (i.e., it's the human's turn), then the null is return since the 
		//human is the only one who can disprove it
		assertEquals(null, thisGame.handleSuggestion(humanDisprovableSuggestion, thisGame.playerList.get(0)));
		//test that if two players can disprove a test, that the first who can disprove does so
		assertEquals(theDoctor, thisGame.handleSuggestion(bothCPDisprovableSuggestion, thisGame.playerList.get(0)));
		//test that the furthest player can disprove, they do (so that we know everyone is queried)
		assertEquals(dematGun, thisGame.handleSuggestion(cp2DisprovableSuggestion, thisGame.playerList.get(0)));
	}
	
	@Test
	public void testMakeSuggestion() {
		// Sets up our list of "seen" cards
		thisGame.addToSeen(theDoctor);
		thisGame.addToSeen(zoo);
		thisGame.addToSeen(swordOfTruth);
	
		// We check that the player does not pick a room they are not currently in
		// assertTrue(cp.pickRoom().equals())
		
		// We check that the player does not pick a card in our "seen" arraylist
		assertFalse(thisGame.seen.contains(cp.pickPlayer()));
		assertFalse(thisGame.seen.contains(cp.pickWeapon()));
		assertFalse(thisGame.seen.contains(cp.pickRoom()));
	}
}
