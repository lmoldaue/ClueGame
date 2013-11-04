package clueGame;

public class Card {

	private String name;
	private CardType type;
	
	public Card(String name, CardType type) { 
		this.name = name; 
		this.type = type;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getName() { return name; }
	
	public String setName(String aName) { return aName; }
	
	public CardType getType() { return type; }
	
	public boolean equals(Card aCard) { 
		if(aCard instanceof Card && ((Card)aCard).getName().equals(this.name)){
		    return true;
		} else {
		    return false;
		}
	}

}
