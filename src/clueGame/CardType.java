/*
 * NAMES: David Grisham and Leah Moldauer
 */

package clueGame;

public enum CardType {
/*	
	PERSON("The Doctor", "River Song", "The Master", "The Silence", "Weeping Angel", "Dalek Caan"),
	COLOR("Blue", "Green", "Red", "Black", "Grey", "Salmon Pink"),
	WEAPON("Sonic Screwdriver", "Laser Screwdriver", "Atom Divider", "Dematerialisation Gun", "Sword of Truth, "
			+ "Matter Condensation", "Micro-Explosive", "Reality Rifle", "Scimitar Sword"),
	ROOM("Sick Bay", "Observatory", "Drawing Room", "Kitchen", "Zoo", "Garden", "Swimming Pool", "Library", "Wardrobe");
*/
	PERSON("Player Card"), WEAPON("Weapon Card"), ROOM("Room Card");
	private String value;
	
	CardType(String someValue) {
		value = someValue;
	}
}
