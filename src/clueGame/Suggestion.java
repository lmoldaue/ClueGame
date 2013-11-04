package clueGame;

public class Suggestion {

	Card player, room, weapon;
	
	public Suggestion() {
	}
	
	public Suggestion(Card player, Card room, Card weapon) {
		this.player = player;
		this.room = room;
		this.weapon = weapon;
	}
	
	public Card getPlayer() {
		return player;
	}
	
	public Card getRoom() {
		return room;
	}
	
	public Card getWeapon() {
		return weapon;
	}
}
