/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player {
	
	public HumanPlayer() {
		super();
	}
	
	public HumanPlayer(String name, Color color, int startLoc) {
		super(name, color, startLoc);
	}



	@Override
	public boolean isHuman() {
		return true;
	}
}
