/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard III (JUnit Tests)
 * CustomAdjTargetTests.java (Passing JUnit tests for adjacencies and targets)
 */

package clueBoardTests;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueBoard.Board;
import clueBoard.BoardCell;

public class CustomAdjTargetTests {
	
	private static Board board;
	
	@BeforeClass
	public static void setUp() throws FileNotFoundException {
		board = new Board(); //default constructor, loads layout/legend files from default locations
		board.loadConfigFiles();
		board.calcAdjacencies();
	}
	
	//these cells are PINK on the board layout spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		//test top/left-most corner
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(0, 0));
		Assert.assertEquals(0, testList.size());
		
		//test one with walkway underneath
		testList = board.getAdjList(board.calcIndex(7, 0));
		Assert.assertEquals(0, testList.size());
		
		//test one with walkway above
		testList = board.getAdjList(board.calcIndex(8, 21));
		Assert.assertEquals(0, testList.size());
		
		//test one in middle of room
		testList = board.getAdjList(board.calcIndex(20, 10));
		Assert.assertEquals(0, testList.size());
		
		//test one next to door
		testList = board.getAdjList(board.calcIndex(16, 3));
		Assert.assertEquals(0, testList.size());
		
		//test one in room corner
		testList = board.getAdjList(board.calcIndex(6, 8));
		Assert.assertEquals(0, testList.size());
	}
	
	//these cells are WHITE on the board layout spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY DOWN 
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(5, 16));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(6, 16)));
		
		// TEST DOORWAY RIGHT
		testList = board.getAdjList(board.calcIndex(11, 6));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(11, 7)));
		
		//TEST DOORWAY UP
		testList = board.getAdjList(board.calcIndex(16, 1));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(15, 1)));
		
		//TEST DOORWAY LEFT
		testList = board.getAdjList(board.calcIndex(21, 17));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.calcIndex(21, 16)));
	}
	
	//these cells are LIGHT BLUE/TURQUOISE on the board layout spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(6, 6));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 5)));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 6)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 6)));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 7)));
		Assert.assertEquals(4, testList.size());
		
		// Test beside a door direction DOWN
		testList = board.getAdjList(board.calcIndex(4, 21));
		Assert.assertTrue(testList.contains(board.calcIndex(3, 21)));
		Assert.assertTrue(testList.contains(board.calcIndex(4, 20)));
		Assert.assertTrue(testList.contains(board.calcIndex(5, 21)));
		Assert.assertEquals(3, testList.size());
		
		// Test beside a door direction LEFT
		testList = board.getAdjList(board.calcIndex(11, 17));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 18)));
		Assert.assertTrue(testList.contains(board.calcIndex(11, 16)));
		Assert.assertTrue(testList.contains(board.calcIndex(10, 17)));
		Assert.assertTrue(testList.contains(board.calcIndex(12, 17)));
		Assert.assertEquals(4, testList.size());
		
		// Test beside a door direction UP
		testList = board.getAdjList(board.calcIndex(15, 1));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 1)));
		Assert.assertTrue(testList.contains(board.calcIndex(14, 1)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 0)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 2)));
		Assert.assertEquals(4, testList.size());
		
		// Test beside a door that's not the right direction
		testList = board.getAdjList(board.calcIndex(23, 17));
		Assert.assertTrue(testList.contains(board.calcIndex(23, 18)));
		Assert.assertTrue(testList.contains(board.calcIndex(23, 16)));
		Assert.assertTrue(testList.contains(board.calcIndex(24, 17)));
		Assert.assertEquals(3, testList.size());		
	}
	
	//test a variety of walkway scenarios (YELLOW in spreadsheet)
	@Test
	public void testAdjacencyWalkways()
	{
		//location with only walkways as adjacent locations
		LinkedList<Integer> testList = board.getAdjList(board.calcIndex(16, 9));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 8)));
		Assert.assertTrue(testList.contains(board.calcIndex(16, 10)));
		Assert.assertTrue(testList.contains(board.calcIndex(15, 9)));
		Assert.assertTrue(testList.contains(board.calcIndex(17, 9)));
		Assert.assertEquals(4, testList.size());
		
		//top edge of board, walkway
		testList = board.getAdjList(board.calcIndex(0, 12));
		Assert.assertTrue(testList.contains(board.calcIndex(1, 12)));
		Assert.assertTrue(testList.contains(board.calcIndex(0, 13)));
		Assert.assertEquals(2, testList.size());

		//right edge of board, walkway
		testList = board.getAdjList(board.calcIndex(7, 24));
		Assert.assertTrue(testList.contains(board.calcIndex(6, 24)));
		Assert.assertTrue(testList.contains(board.calcIndex(7, 23)));
		Assert.assertEquals(2, testList.size());

		//bottom edge of board
		testList = board.getAdjList(board.calcIndex(24, 10));
		Assert.assertTrue(testList.contains(board.calcIndex(24, 9)));
		Assert.assertTrue(testList.contains(board.calcIndex(24, 11)));
		Assert.assertTrue(testList.contains(board.calcIndex(23, 10)));
		Assert.assertEquals(3, testList.size());
		
		//left edge of board
		testList = board.getAdjList(board.calcIndex(9, 0));
		Assert.assertTrue(testList.contains(board.calcIndex(8, 0)));
		Assert.assertTrue(testList.contains(board.calcIndex(9, 1)));
		Assert.assertEquals(2, testList.size());
	}
	
	//these cells are BROWN on the board layout spreadsheet
	@Test
	public void testWalkwayTargets() {
		//distance of one step
		board.calcTargets(9, 3, 1);
		Set<BoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(9, 2))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(9, 4))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 3))));

		//distance of two steps
		board.calcTargets(15, 15, 2);
		targets = board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 13))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(13, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 14))));
		
		//distance of three steps
		board.calcTargets(18, 4, 3);
		targets = board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 4))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 4))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 5))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 4))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(21, 4))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 6))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 5))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 5))));
		
		//distance of five steps
		board.calcTargets(18, 24, 5);
		targets = board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 21))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 24))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 23))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 23))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 22))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 21))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 20))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 24))));
	}
	
	//this cell is BROWN on the board layout spreadsheet
	@Test 
	public void testTargetsIntoRoom()
	{
		//one room exactly 2 steps away
		board.calcTargets(21, 5, 2);
		Set<BoardCell> targets = board.getTargets();
		Assert.assertEquals(5, targets.size());
		
		//door location
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 6))));
		
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 5))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 5))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 4))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 4))));
	}
	
	//test entering room without using all steps
	//this cell is BROWN on the board layout spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() {
		//one room exactly 2 steps away
		board.calcTargets(19, 15, 3);
		Set<BoardCell> targets = board.getTargets();
		Assert.assertEquals(11, targets.size());
		
		//door location
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 14))));
		
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 14))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 18))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 15))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(21, 16))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 15))));
	}
	
	@Test
	public void testRoomExit()
	{
		//take one step out of room
		board.calcTargets(5, 16, 1);
		Set<BoardCell> targets = board.getTargets();
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 16))));
		
		// take two steps out of room
		board.calcTargets(24, 3, 2);
		targets = board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(24, 5))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(23, 4))));
	}
}
