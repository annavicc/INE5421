package RegularLanguages;

import java.util.ArrayList;
import java.util.HashMap;


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
		// test automata:
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = builder.newState();
		FiniteAutomata.State q1 = builder.newState();
		FiniteAutomata.State q2 = builder.newState();
		try {
			for (int i = 0; i < 100; i++) {
				FiniteAutomata.State q = builder.newState();
				builder.addTransition(q, 'c', q);
				if (i == 1) {
					for (char c = 'a'; c < 'k'; c++) {
						builder.addTransition(q, c, q);
					}
				}
			}
			FiniteAutomata.State q = builder.newState();
			builder.addTransition(q, 'c', q0)
					.addTransition(q, 'c', q1)
					.addTransition(q, 'c', q2)
					.addTransition(q, '&', q0)
					.setFinal(q);
			
			return builder.setInitial(q0)
				.setFinal(q1)
				.addTransition(q0, 'a', q0)
				.addTransition(q0, 'b', q0)
				.addTransition(q0, 'b', q1)
				.addTransition(q1, 'a', q2)
				.build();
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
