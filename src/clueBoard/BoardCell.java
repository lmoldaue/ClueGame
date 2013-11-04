package clueBoard;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard III (BoardCell class)
 */

public abstract class BoardCell {
	protected int row, column;
	protected static final int sideLength = 26;
	
	public BoardCell() {
		super();
	}
	
	public BoardCell(int row, int column) {
		super();
		this.row = row; 	
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}


	public int getColumn() {
		return column;
	}

	public boolean isWalkway() {
		return false;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public boolean isDoorway() {
		return false;
	}
	
	public void draw(Graphics g, Board b) {
	}

	public static int getSidelength() {
		return sideLength;
	}
}
