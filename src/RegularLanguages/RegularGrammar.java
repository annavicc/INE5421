package RegularLanguages;

import java.util.ArrayList;
import java.util.HashMap;


public class RegularGrammar extends RegularLanguage {
	
	private ArrayList<Character> vn;	// non terminal symbols
	private ArrayList<String> vt;	// terminal symbols
	private HashMap<Character, ArrayList<String>> productions;	// production rules
	private char initial;	// initial S
	
	

}
