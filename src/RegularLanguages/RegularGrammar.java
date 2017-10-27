package RegularLanguages;

import java.util.ArrayList;
import java.util.HashMap;

import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;


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
	
	public String getDefinition() {
		return super.input;
	}
	
	/*
	 * Get RG
	 */
	public RegularGrammar getRG() {
		return this;
	}
	
	/*
	 * Convert RG to AF
	 * TODO implement
	 */
	public FiniteAutomata getAF() {
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = builder.newState();
		FiniteAutomata.State q1 = builder.newState();
		FiniteAutomata.State q2 = builder.newState();
		try {
			builder.setInitial(q0);
			builder.setFinal(q1);
			builder.addTransition(q0, 'a', q0);
			builder.addTransition(q0, 'b', q0);
			builder.addTransition(q0, 'b', q1);
			builder.addTransition(q1, 'a', q2);
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Convert RG to RE
	 * Out of scope
	 */
	public RegularExpression getRE() {
//		return null;
		return new RegularExpression("UNDEFINED RE");
	}
	
}
