/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGame;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.Random;

import clueBoard.BoardCell;
import clueBoard.RoomCell;

public class ComputerPlayer extends Player {
	
	private char lastRoomVisited;
	
	public ComputerPlayer() {
		super();
	}
	
	public ComputerPlayer(String name, Color color, int startLoc) {
		super(name, color, startLoc);
	}
	
	public void setLastRoomVisited(char room) {
		lastRoomVisited = room;
	}
	
	@Override
	public boolean isComputer() {
		return true;
	}
	
	public Card pickPlayer() {
		return null;
	}
	
	public Card pickWeapon() {
		return null;
	}
	
	public Card pickRoom() {
		return null;
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		BoardCell[] targetsArray = targets.toArray(new BoardCell[0]); //used to pick a random element from targets
		
		for (BoardCell cell : targets) {
			if (cell.isRoom() && (lastRoomVisited != ((RoomCell) cell).getInitial())) {
				return cell;
			} 
		}
		
		Random rand = new Random();
		int randCell = rand.nextInt(targets.size());
		
		return targetsArray[randCell];
	}
	
	public Suggestion createSuggestion() {
		return null; 
	}
	
	public void updateSeen(CardType seen) { }
	
}
