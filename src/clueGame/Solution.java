/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGame;

public class Solution {
	public Card person, weapon, room;

	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public Card getPerson() {
		return person;
	}

	public Card getRoom() {
		return room;
	}

	public Card getWeapon() {
		return weapon;
	}	
	
}
