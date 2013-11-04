package clueBoard;

import java.awt.Color;
import java.awt.Graphics;

/*
 * NAMES: David Grisham and Leah Moldauer
 * ClueBoard III (WalkwayCell class)
 */

public class WalkwayCell extends BoardCell {
	private Color innerColor = new Color(114,16,35);
	private Color outlineColor = new Color(0,0,0);
	
	public WalkwayCell(int row, int column) {
		super(row, column);
	}
	
	@Override
	public boolean isWalkway() {
		return true;
	}
	
	@Override
	public void draw(Graphics g, Board b) {
		g.setColor(outlineColor);
		g.fillRect(column*getSidelength(), row*getSidelength(), getSidelength(), getSidelength());
		g.setColor(innerColor);
		g.fillRect(column*getSidelength(), row*getSidelength(), getSidelength()-1, getSidelength()-1);
	}	
}
