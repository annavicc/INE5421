package RegularLanguages;

import java.util.HashSet;
import java.util.Stack;

import RegularLanguages.Operators.FAOperator;


/**
 * Representation of a Regular Expression
 * Eg.: a*(b?c|d)*
 */
public class RegularExpression extends RegularLanguage {
	private HashSet<Character> vt;	// terminal symbols
	private String regex; // the regular expression entered by the user
	private String formatted_regex; // an equivalent simplified representation of the regex

	/**
	 * Public constructor
	 * @param inp the regex entered by the user
	 */
	public RegularExpression(String inp) {
		super(inp, InputType.RE);
		vt = new HashSet<Character>();
		regex = "";
		formatted_regex = "" ;
	}
	
	/**
	 * Representation of the regular expression
	 * @return the regex as a string
	 */
	public String getDefinition() {
		return this.input;
	}
	
	/**
	 * Get an equivalent simplified representation of the RE
	 * @return the simplified representation of the RE as a String
	 */
	public String getFormattedRegex() {
		return this.formatted_regex;
	}
	
	/*
	 * Convert RE to RG
	 */
	public RegularGrammar getRG() {
		return this.getFA().getRG();
	}
	
	/*
	 * Convert RE to FA
	 * TODO implement
	 */
	public FiniteAutomata getFA() {
		return FAOperator.REtoFA(this);
	}
	
	/*
	 * Get RE
	 */
	public RegularExpression getRE() {
		return this;
	}
	
	/**
	 * 
	 * Verify if a given input is a valid RE
	 * @param inp the input entered by the user
	 * @return a RegularExpression object if valid, null if invalid
	 */
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
	
	/**
	 * Validate if symbols in the RE are lexically valid
	 * @param str the input entered by the user
	 * @return true if lexically valid, or false otherwise
	 */
	public static boolean lexicalValidation(String inp, RegularExpression re) {
		if (!inp.matches("^[a-z0-9\\(\\)\\?\\*|&\\+]*")) { // Verify invalid symbols
			return false;
		}
		return true;
	}
	
	/**
	 * Verify if the combination of symbols in the
	 * regular expression is syntactically valid
	 * @param inp the RE entered by the user
	 * @param re the RE object
	 * @return true if syntactically correct
	 */
	public static boolean syntaticAnalysis(String inp, RegularExpression re) {
		Stack<Character> symbols = new Stack<Character>();
		char c, before, next;
		if (!inp.isEmpty()) { // can't begin with * | ?
			c = inp.charAt(0);
			if (c == '*' || c == '|' || c == '?' || c == '+') {
				return false;
			}
		}
		// Replace all '+'? '?'? '*'(('*')?('?')?('+')?)* for *
		String formatted = inp.replaceAll("\\?+", "?");
		formatted = formatted.replaceAll("\\*+", "*");
		formatted = formatted.replaceAll("\\++", "+");
		formatted = formatted.replaceAll("\\?\\*+", "*");
		formatted = formatted.replaceAll("\\?\\+", "*");
		formatted = formatted.replaceAll("\\+\\?", "*");
		formatted = formatted.replaceAll("\\*+\\?", "*");
		formatted = formatted.replaceAll("\\+\\*+", "*");
		formatted = formatted.replaceAll("\\*+\\+", "*");
		formatted = formatted.replaceAll("\\*+", "*");
		re.formatted_regex = formatted;
		for (int i = 0; i < formatted.length(); i++) {
			c = formatted.charAt(i);
			
			if (Character.isLetterOrDigit(c) || c == '&') {
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
			// forbidden: |* |? |+
			if (i == 0) {
				continue;
			}
			before = formatted.charAt(i-1);
			if (c == '*' || c == '?' || c == '+') {
				if (!Character.isLetterOrDigit(before) && before != ')'
						&& before != '&') { // if it's not c* )* )? c?
					return false;
				}
			} else if (c == '|') {
				if (i+1 < formatted.length()) {
					next = formatted.charAt(i+1);
					if (!Character.isLetterOrDigit(next)
							&& (next != '(')
							&& next != '&') { // |* |? || 
						return false;
					}
				}
			}
		}
		return symbols.isEmpty();
	}
	
}
