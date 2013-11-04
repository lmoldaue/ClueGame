package clueBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard III (RoomCell class)
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class RoomCell extends BoardCell {
	private Color color;
	private boolean writesRoomName;
	
	public enum DoorDirection {
		UP, DOWN, LEFT, RIGHT, NONE
	}
	
	private DoorDirection doorDirection;
	private char roomInitial;
	
	public RoomCell() {
		super();
		writesRoomName = false;
		color = new Color(0,59,111);
	}
	
	public RoomCell(int location, char roomInitial, DoorDirection doorDirection) {
		this.roomInitial = roomInitial;
		this.doorDirection = doorDirection;
		writesRoomName = false;
		color = new Color(0,59,111);
	}

	public RoomCell(int row, int column, char roomInitial, DoorDirection doorDirection) {
		super(row, column);
		this.roomInitial = roomInitial;
		this.doorDirection = doorDirection;
		writesRoomName = false;
		color = new Color(0,59,111);
	}
	
	@Override
	public boolean isRoom() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isDoorway() {
		if (doorDirection != DoorDirection.NONE) {
			return true;
		}
		
		return false;
	}
	
	public char getInitial() {
		return roomInitial;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public void setWritesRoomName(boolean b) {
		writesRoomName = b;
	}
	
	@Override
	public void draw(Graphics g, Board b) {
		Color textColor = new Color(200,200,200);
		Color doorColor = new Color(170,50,120);
		g.setColor(color);
		g.fillRect(column*sideLength, row*sideLength, sideLength, sideLength);
		
		if (doorDirection != DoorDirection.NONE) {
			g.setColor(doorColor);
			Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
			if (doorDirection == DoorDirection.UP) {
				g2.draw(new Line2D.Float(column*sideLength, row*sideLength, 
	            		 (column+1)*sideLength, row*sideLength));
			} else if (doorDirection == DoorDirection.DOWN) {
				g2.draw(new Line2D.Float(column*sideLength, (row+1)*sideLength, 
	            		 (column+1)*sideLength, (row+1)*sideLength));
			} else if (doorDirection == DoorDirection.LEFT) {
				g2.draw(new Line2D.Float(column*sideLength, row*sideLength, 
	            		 column*sideLength, (row+1)*sideLength));
			} else if (doorDirection == DoorDirection.RIGHT) {
				g2.draw(new Line2D.Float((column+1)*sideLength, row*sideLength, 
	            		 (column+1)*sideLength, (row+1)*sideLength));
			}
			
			g2.setStroke(new BasicStroke(1));
		}
		
		if (writesRoomName) {
			g.setColor(textColor);
			g.drawString(b.getRooms().get(getInitial()).toUpperCase(), column*sideLength, row*sideLength);
		}
	}
	
}
