package com.kkbnart.wordis.game.object.character;

public enum CharacterDefinition {
	A(0, "A"), B(1, "B"), C(2, "C"), D(3, "D"), E(4, "E"), F(5, "F"), G(6, "G"),
	H(7, "H"), I(8, "I"), J(9, "J"), K(10, "K"), L(11, "L"), M(12, "M"), N(13, "N"),
	O(14, "O"), P(15, "P"), Q(16, "Q"), R(17, "R"), S(18, "S"), T(19, "T"), U(20, "U"),
	V(21, "V"), W(22, "W"), X(23, "X"), Y(24, "Y"), Z(25, "Z"), ASTER(26, "*"), NONE(27, "");
	
	private int id;
	private String character;
	private CharacterDefinition(final int id, final String character) {
		this.id = id;
		this.character = character;
	}
	public int getId() {
		return id;
	}
	public String getCharacter() {
		return character;
	}
	
	public static CharacterDefinition getDef(final String s) {
		for (CharacterDefinition cd : values()) {
			if (cd.getCharacter().equals(s.substring(0, 1))) {
				return cd;
			}
		}
		return NONE;
	}
}
