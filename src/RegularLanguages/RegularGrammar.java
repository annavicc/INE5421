package RegularLanguages;

import java.util.ArrayList;
import java.util.HashMap;

import RegularLanguages.RegularLanguage.InputType;


public class RegularGrammar extends RegularLanguage {
	
	private ArrayList<Character> vn;	// non terminal symbols
	private ArrayList<Character> vt;	// terminal symbols
	private HashMap<Character, ArrayList<String>> productions;	// production rules
	private char s;	// initial S

	public RegularGrammar(String inp) {
		super(inp, InputType.RG);
	}

	public static boolean isValidRG(String inp) {
		return true;
	}
	
}
