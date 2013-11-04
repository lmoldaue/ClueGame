/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard II (Custom JUnit Tests)
 */

package clueBoardTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueBoard.BadConfigFormatException;
import clueBoard.Board;
import clueBoard.BoardCell;
import clueBoard.RoomCell;

public class CustomBoardInitTests {
	
	private static Board board;
	private static final int NUM_ROOMS = 11;
	private static final int NUM_ROWS = 25;
	private static final int NUM_COLUMNS = 25;
	private static String layoutFile, legendFile,
	badColumnsLayoutConfig, badRoomsLayoutConfig, badLegendConfig;
	
	@BeforeClass
	public static void setUp() throws FileNotFoundException {
		
		layoutFile = "ClueLayout.csv";
		legendFile = "ClueLegend.txt";
		badRoomsLayoutConfig = "ClueLayoutBadRoom.csv";
		badColumnsLayoutConfig = "ClueLayoutBadColumns.csv";
		badLegendConfig = "ClueLegendBadFormat.txt";
		
		board = new Board();
		board.loadConfigFiles();
	}
	
	@Test 
	public void numberOfRooms() {
		assertEquals(NUM_ROOMS, board.getRooms().size());
	}
	
	@Test
	public void roomMapping() {
		Map<Character, String> rooms = board.getRooms();
		assertEquals("Sick Bay", rooms.get('S'));
		assertEquals("Observatory", rooms.get('O'));
		assertEquals("Drawing Room", rooms.get('D'));
		assertEquals("Kitchen", rooms.get('K'));
		assertEquals("Zoo", rooms.get('Z'));
		assertEquals("Garden", rooms.get('G'));
		assertEquals("Swimming Pool", rooms.get('P'));
		assertEquals("Library", rooms.get('L'));
		assertEquals("Wardrobe", rooms.get('R'));
		assertEquals("Walkway", rooms.get('W'));
		assertEquals("Control Room", rooms.get('X'));
	}
	
	@Test
	public void numRowsAndColumns() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}
	
	//these cells are WHITE on the board layout spreadsheet
	@Test
	public void doorDirections() {
		//test doors for correct directions
		RoomCell room = board.getRoomCellAt(7, 4);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
		
		room = board.getRoomCellAt(5, 11);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getRoomCellAt(18, 20);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
		
		room = board.getRoomCellAt(20, 6);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.LEFT, room.getDoorDirection());
		
		//test room cell
		room = board.getRoomCellAt(2, 9);
		assertFalse(room.isDoorway());	

		//test walkway cell (YELLLOW on the spreadsheet)
		BoardCell cell = board.getCellAt(board.calcIndex(8, 12));
		assertFalse(cell.isDoorway());		
	}
	
	@Test
	public void numDoors() {
		
		int numberOfCells = board.getNumColumns() * board.getNumRows();
		Assert.assertEquals(NUM_ROWS * NUM_COLUMNS, numberOfCells);
		int numDoors = 0;
		
		for (int i = 0; i < numberOfCells; i++)
		{
			BoardCell cell = board.getCellAt(i);
			
			if (cell.isDoorway()) {
				numDoors++;
			}
		}
		
		Assert.assertEquals(22, numDoors);
	}
	
	@Test
	public void roomInitials() {
		assertEquals('S', board.getRoomCellAt(0, 0).getInitial());
		assertEquals('O', board.getRoomCellAt(1, 9).getInitial());
		assertEquals('D', board.getRoomCellAt(1, 15).getInitial());
		assertEquals('K', board.getRoomCellAt(1, 22).getInitial());
		assertEquals('R', board.getRoomCellAt(11, 0).getInitial());
		assertEquals('L', board.getRoomCellAt(20, 1).getInitial());
		assertEquals('P', board.getRoomCellAt(20, 11).getInitial());
		assertEquals('G', board.getRoomCellAt(20, 20).getInitial());
		assertEquals('Z', board.getRoomCellAt(11, 21).getInitial());
	}
	
	@Test
	public void testCalcIndex() {
		//test corners
		assertEquals(0, board.calcIndex(0, 0));
		assertEquals(NUM_COLUMNS-1, board.calcIndex(0, NUM_COLUMNS-1));
		assertEquals(((NUM_ROWS-1) * (NUM_COLUMNS)), board.calcIndex(NUM_ROWS-1, 0));
		assertEquals(((NUM_ROWS * NUM_COLUMNS) - 1), board.calcIndex(NUM_ROWS-1, NUM_COLUMNS-1));
		
		//test others
		assertEquals(78, board.calcIndex(3, 3));
		assertEquals(216, board.calcIndex(8, 16));
	}
	
	
	//test that exception is thrown for bad board layout configuration
	@Test (expected = BadConfigFormatException.class)
	public void testBadRooms() throws BadConfigFormatException, FileNotFoundException {
		Board board2 = new Board(badRoomsLayoutConfig, legendFile);
		
		board2.loadRoomConfig();
		board2.loadBoardConfig();
	}
	
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		Board board2 = new Board(badColumnsLayoutConfig, legendFile);
		
		board2.loadRoomConfig();
		board2.loadBoardConfig();
	}
	
	//test that exception is thrown for bad legend configuration
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board board2 = new Board(layoutFile, badLegendConfig);
		board2.loadRoomConfig();
		board2.loadBoardConfig();
	}
}
