package RegularLanguages;

import java.util.HashSet;
import java.util.Stack;

public class RegularExpression extends RegularLanguage {
	private HashSet<Character> vt;	// terminal symbols
	private String regex;
	private String formatted_regex;

	public RegularExpression(String inp) {
		super(inp, InputType.RE);
		vt = new HashSet<Character>();
		regex = "";
		formatted_regex = "" ;
	}

	
	public String toString() {
		return this.regex;
	}
	/*
	 * Convert RE to RG
	 */
	public RegularGrammar getRG() {
		return this.getAF().getRG();
	}
	
	/*
	 * Convert RE to AF
	 * TODO implement
	 */
	public FiniteAutomata getAF() {
		return new FiniteAutomata();
	}
	
	/*
	 * Get RE
	 */
	public RegularExpression getRE() {
		return this;
	}
	

	public static RegularExpression isValidRE(String inp) {
		RegularExpression re = new RegularExpression(inp);
		String formatted =  inp.replaceAll("\\s+", ""); // Remove white spaces
		// Verify invalid symbols
		if (!lexicalValidation(formatted, re)) {
			return null;
		}
		// Verify parenthesis correctness
		if (!syntaticAnalysis(formatted, re)) {
			return null;
		}
		return re;
	}
	
	public static boolean lexicalValidation(String inp, RegularExpression re) {
		if (!inp.matches("^[a-z0-9\\(\\)\\?\\*|&]+")) { // Verify invalid symbols
			return false;
		}
		return true;
	}
	
	public static boolean syntaticAnalysis(String inp, RegularExpression re) {
		Stack<Character> symbols = new Stack<Character>();
		char c, before, next;
		if (!inp.isEmpty()) { // can't begin with * | ?
			c = inp.charAt(0);
			if (c == '*' || c == '|' || c == '?') {
				return false;
			}
		}
		// Replace all '?'? '*'(('*')?('?')?)* for *
		String formatted = inp.replaceAll("\\?+", "?");
		formatted = formatted.replaceAll("\\*+", "*");
		formatted = formatted.replaceAll("\\?\\*+", "*");
		formatted = formatted.replaceAll("\\*+\\?", "*");
		re.formatted_regex = formatted;
		for (int i = 0; i < formatted.length(); i++) {
			c = formatted.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				re.vt.add(c);
			}
			// Parenthesis check
			if (c == '(') {
				symbols.push(c);
			} else if (c == ')') {
				if (symbols.isEmpty()) {
					return false;
				} else {
					if (symbols.peek() == '(') {
						symbols.pop();
					} else {
						return false;
					}
				}
			}
			
			// allowed: c*
			// forbidden: |* |?
			if (i == 0) {
				continue;
			}
			before = formatted.charAt(i-1);
			if (c == '*' || c == '?') {
				if (!Character.isLetter(before) && c != ')') { // if it's not c* )* )? c?
					return false;
				}
			} else if (c == '|') {
				if (i+1 < formatted.length()) {
					next = formatted.charAt(i+1);
					if (!Character.isLetter(next)
							&& (next != '(')) { // |* |? || 
						return false;
					}
				}
			}
			
		}
		return symbols.isEmpty();

	}
	
	
}
