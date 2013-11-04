package clueBoard;
/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard III (Implemented Board Class)
 */

import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;

import clueGame.ClueGame;


public class Board extends JPanel {
	
	public ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private int numRows, numColumns;
	private String layoutFile, legendFile;
	private Map<Integer, LinkedList<Integer>> adjMtx;
	public Set<BoardCell> targets;
	Boolean[] visited;
	private ClueGame game;
	
	public Board() {
		//Default constructor, initializes our group's layout and legend files as default. 
		numRows = 0;
		numColumns = 0;
		this.layoutFile = "ClueLayout.csv";
		this.legendFile = "ClueLegend.txt";
		
		targets = new HashSet<BoardCell>();
		adjMtx = new HashMap<Integer, LinkedList<Integer>>();
	}
	

	public Board(String layoutFile, String legendFile) {
		numRows = 0;
		numColumns = 0;
		this.layoutFile = new String(layoutFile);
		this.legendFile = new String(legendFile);
		
		targets = new HashSet<BoardCell>();
		adjMtx = new HashMap<Integer, LinkedList<Integer>>();
	}
	
	public void setGame(ClueGame cg) {
		this.game = cg;
	}
	
	public void loadConfigFiles() throws FileNotFoundException {
		
		try {
			loadRoomConfig();
			loadBoardConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
	}
	
	

	public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException {
		//takes input from legendFile and loads into 'rooms' map
		//loadRoomConfig takes input from provided legendFile, loads input into "rooms" HashMap
		//in the event that an entry is formatted incorrectly, this method throws a format exception accordingly

		rooms = new HashMap<Character, String>();
		FileReader legendReader = new FileReader(legendFile);
		Scanner legendIn = new Scanner(legendReader);
		int lineNumber = 0;
		
		while (legendIn.hasNextLine()) {
			++lineNumber;
			// Splits each line by making the room initial character and proper title a separate string
			String line[] = legendIn.nextLine().split(",");
			
			if (line.length != 2) {
				legendIn.close();
				throw new BadConfigFormatException("One of the legend entries is formatted incorrectly.");
			}
			
			if(line[0].length() != 1) {
				legendIn.close();
				// If room initial is longer than one character, throws exception accordingly.
				throw new BadConfigFormatException("\"" + line[0] + "\" at line " + lineNumber 
						+ " is not a proper room initial character. (Needs to be one character!)");
			}
			
			char roomInitial = line[0].charAt(0);
			
			String roomName = line[1].trim(); //trim() gets rid of surrounding white space
			rooms.put(roomInitial, roomName);
		}
		legendIn.close();
	}
	
	
	
	public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException {
		//takes input from layoutFile and organizes it into the 'cells' arraylist
		cells = new ArrayList<BoardCell>();
		FileReader layoutReader = new FileReader(layoutFile);
		Scanner layoutIn = new Scanner(layoutReader);
		
		while(layoutIn.hasNextLine()) {
			String line[] = layoutIn.nextLine().split(",");
			int numColumnsTemp = 0;
			
			for (String cell : line) {
	
				if (cell.length() == 1) {
					String roomInitial = rooms.get(cell.charAt(0));
					
					if (roomInitial == null) {
						layoutIn.close();
					    throw new BadConfigFormatException("Cell at row " + numRows + ", column " + numColumnsTemp + " is not a valid cell.");
					}
					
					if (cell.equals("W")) {
						cells.add(new WalkwayCell(numRows, numColumnsTemp++));
					} else {
						cells.add(new RoomCell(numRows, numColumnsTemp++, cell.charAt(0), RoomCell.DoorDirection.NONE));
					} 
				} else if (cell.length() == 2) {
					RoomCell.DoorDirection doorDirection = RoomCell.DoorDirection.NONE;
					boolean writesRoomName = false;
					
					switch(cell.charAt(1)) {
					
					case 'A':
						writesRoomName = true;
						break;
					
					case 'U':
						doorDirection = RoomCell.DoorDirection.UP;
						break;
						
					case 'D':
						doorDirection = RoomCell.DoorDirection.DOWN;
						break;
						
					case 'L':
						doorDirection = RoomCell.DoorDirection.LEFT;
						break;
						
					case 'R':
						doorDirection = RoomCell.DoorDirection.RIGHT;
						break;
					}
					
					cells.add(new RoomCell(numRows, numColumnsTemp, cell.charAt(0), doorDirection));
					((RoomCell)cells.get(calcIndex(numRows, numColumnsTemp++))).setWritesRoomName(writesRoomName);
				} else {
					throw new BadConfigFormatException("The room cell at row " + numRows + " and column "
							+ numColumnsTemp + " is formatted incorrectly");
				}
			}			
			
			if (numRows == 0) {
				numColumns = numColumnsTemp;
			} else {
				if (numColumnsTemp != numColumns) {
					layoutIn.close();
					throw new BadConfigFormatException("Row " + numRows + " has " + numColumnsTemp + 
							" columns, should have " + numColumns + " columns");
				}
			}
			numRows++;
		}
		layoutIn.close();
	}	
	
	
	public void calcAdjacencies() {
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				
				LinkedList<Integer> adjacencies = new LinkedList<Integer>();
				int location = calcIndex(i, j);
				BoardCell cell = cells.get(location);
				
				if (cell.isRoom() && !cell.isDoorway()) {
					adjMtx.put(location, adjacencies);
					continue;
				}
				
				if (cell.isWalkway()) {
					
					if (i-1 >= 0) {
						BoardCell cellAbove = cells.get(calcIndex(i-1, j));
						
						//checks if the above cell is a walkway, or a door with direction DOWN
						if (cellAbove.isWalkway() || (cellAbove.isDoorway() && ((RoomCell) cellAbove).getDoorDirection() == RoomCell.DoorDirection.DOWN)) {
							adjacencies.add(calcIndex(i-1, j));
						}
					}
					
					if (i+1 < numRows) {
						BoardCell cellBelow = cells.get(calcIndex(i+1, j));
						
						if (cellBelow.isWalkway() || (cellBelow.isDoorway() && ((RoomCell) cellBelow).getDoorDirection() == RoomCell.DoorDirection.UP)) {
							adjacencies.add(calcIndex(i+1, j));
						}
					}
					
					if (j-1 >= 0) {
						BoardCell cellLeft = cells.get(calcIndex(i, j-1));
						
						if (cellLeft.isWalkway() || (cellLeft.isDoorway() && ((RoomCell) cellLeft).getDoorDirection() == RoomCell.DoorDirection.RIGHT)) {
							adjacencies.add(calcIndex(i, j-1));
						}
					} 
					
					if (j+1 < numColumns) {
						BoardCell cellRight = cells.get(calcIndex(i, j+1));
						
						if (cellRight.isWalkway() || (cellRight.isDoorway() && ((RoomCell) cellRight).getDoorDirection() == RoomCell.DoorDirection.LEFT)) {
							adjacencies.add(calcIndex(i, j+1));
						}
					} 
				} else {
					//the current cell is a door, so this checks which direction it points and adds the
					//corresponding walkway exit
					if (((RoomCell) cell).getDoorDirection() == RoomCell.DoorDirection.UP) {
						adjacencies.add(calcIndex(i-1, j));
					} else if (((RoomCell) cell).getDoorDirection() == RoomCell.DoorDirection.DOWN) {
						adjacencies.add(calcIndex(i+1, j));
					} else if (((RoomCell) cell).getDoorDirection() == RoomCell.DoorDirection.LEFT) { 
							adjacencies.add(calcIndex(i, j-1));
					} else if (((RoomCell) cell).getDoorDirection() == RoomCell.DoorDirection.RIGHT) {
							adjacencies.add(calcIndex(i, j+1));
					}
				}
				adjMtx.put(location, adjacencies);
			}
		}
	}
	
	
	
	public void calcTargets(int row, int column, int numSteps) {
		
		targets.clear();
		visited = new Boolean[numRows * numColumns];
		Arrays.fill(visited, false);		
		calculateTargets(calcIndex(row, column), numSteps);
	}
	
	
	
	public void calculateTargets(int location, int numSteps) {
		
		visited[location] = true;
		LinkedList<Integer> adjacentCells = new LinkedList<Integer>();
		
		for (int cell : getAdjList(location)) {
			if (!visited[cell]) {
				adjacentCells.add(cell);
			}
		}
		
		for (int cell : adjacentCells) {
			if (getCellAt(cell).isWalkway()) {	
				
				if (numSteps == 1) {
					targets.add(getCellAt(cell));
				} else {
					calculateTargets(cell, numSteps-1);
				}
			} else if (((RoomCell) getCellAt(cell)).isDoorway()) {
				targets.add(getCellAt(cell));
			}			
		}
		visited[location] = false;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawCells(g);
		game.drawPlayers(g, this);
	}
	
	public void drawCells(Graphics g) {
		for(BoardCell cell : cells) {
			cell.draw(g, this);
		}
	}
	
	public int calcIndex(int row, int col) {
		return (numColumns*row + col);
	}
	
	
	
	public Map<Character, String> getRooms() {
		return rooms;
	}
	
	
	
	public RoomCell getRoomCellAt(int row, int column) {
		
		BoardCell cell = getCellAt(calcIndex(row, column));
		
		if (cell.isRoom()) {
			return (RoomCell) cell;
		}
		
		return null;
	}
	
	
	public BoardCell getCellAt(int location) {
		return cells.get(location);
	}
	
	
	public BoardCell getCellAt(int row, int column) {
		return getCellAt(calcIndex(row, column));
	}
	
	
	public int getNumRows() {
		return numRows;
	}
	
	
	public int getNumColumns() {
		return numColumns;
	}
	
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public String getRoomName(char roomInitial) {
		return rooms.get(roomInitial);
	}
	
	public char getRoomInitial(String roomName) {
		char roomInitial = 0;
		for(Map.Entry entry: rooms.entrySet()){
			if (roomName.equals(entry.getValue())) {
				roomInitial = (char) entry.getKey();
				break; //roomInitial found, break out of loop
			}
		}
		
		return roomInitial;
	}
	
	
	public LinkedList<Integer> getAdjList(int location) {
		return adjMtx.get(location);	
	}	
}
