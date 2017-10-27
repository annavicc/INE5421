package RegularLanguages;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


public class RegularGrammar extends RegularLanguage {
	
	private HashSet<Character> vn;	// non terminal symbols
	private HashSet<Character> vt;	// terminal symbols
	private HashMap<Character, HashSet<String>> productions;	// production rules S -> aA | a
	private char s;	// initial S

	public RegularGrammar(String inp) {
		super(inp, InputType.RG);
		vn = new HashSet<Character>();
		vt = new HashSet<Character>();
		productions = new HashMap<Character, HashSet<String>>();
	}
	
	public static RegularLanguage isValidRG(String inp) {
		RegularGrammar rg = new RegularGrammar(inp);
		// Verify invalid symbols
		if (!lexicalValidation(inp)) {
			return null;
		}
		
		// Get productions with no blanks for every vn
		String[] productions = getProductions(inp);

		// Verify productions correctness
		validateProductions(productions, rg);
		// Validate non terminals
		if (rg.vn.isEmpty()) {
			return null;
		}
		
		return rg;
	}
	
//	public static boolean isValidRG(String inp) {
//		if (isValid(inp) == null) {
//			return false;
//		}
//		return true;
//	}


	/*
	 * Get Regular Grammar representation
	 */
	public String toString() {
		String grammar = "";
		String aux = "";
		HashSet<String> prodList;
		
		for (Character vN : this.productions.keySet()) {
			prodList = this.productions.get(vN);
			
			for (String prod : prodList) {
				aux += prod + " | ";
			}
			aux = aux.substring(0, aux.length()-2);
			if (vN.equals(this.s)) {
				grammar = vN + " -> " + aux + "\n" + grammar;
			} else {
				grammar += vN + " -> " + aux + "\n";
			}
			aux = "";
		}
		return grammar;
	}
	
	/* TODO implement
	 */
	public FiniteAutomata getAF() {
		return new FiniteAutomata();
	}
	
	/*
	 * Convert RG to RE
	 * Out of scope
	 */
	public RegularExpression getRE() {
		return null;
//		return false;
	}
	

	@Override
	public RegularGrammar getRG() {
		return this;
	}

	/*
	 * Return productions for each vN
	 */
	private static String[] getProductions(String str) {
		String[] prod = str.split("[\\r\\n]+");	// Split by line break
		int i = 0;
		for (String s : prod) {
			prod[i++] = s.replaceAll("\\s+", ""); // Remove spaces
		}
		return prod;
	}
	
	private static boolean lexicalValidation(String str) {
		String formatted =  str.replaceAll("\\s+", ""); // Remove white spaces
		if (!formatted.matches("^[a-zA-Z0-9\\->|&]+")) { // Verify invalid symbols
			System.out.println("Invalid symbols.");
			return false;
		}
		return true;
	}
	
	/*
	 * Validate productions
	 */
	private static RegularGrammar validateProductions(String[] nt, RegularGrammar rg) {
		Scanner vnScan = null;
		String vn = "";
		String prod = "";
		HashSet<String> pr = new HashSet<String>();
		boolean isSDefined = false;
		
		// Iterate every non terminal symbol
		for (int i = 0; i < nt.length; i++) {
			prod = nt[i];
			vnScan = new Scanner(prod);
			vnScan.useDelimiter("->");
			
			if(vnScan.hasNext()) {
				pr = new HashSet<String>();
				vn = vnScan.next();
				// if |vN| > 1 or is not upper case or not a letter
				if (vn.length() > 1
						|| Character.isLowerCase(vn.charAt(0)) ||
						!Character.isLetter(vn.charAt(0))) { // if |vn| > 1
					rg.vn.clear();
					return null;
				} else {
					rg.vn.add(vn.charAt(0));
					if (!validateProduction(vn.charAt(0), prod, pr, rg)) {
						rg.vn.clear();
						return null;
					}
					if (!isSDefined) {
						rg.s = vn.charAt(0);
						isSDefined = true;
					}
				}
			}
			vnScan.close();
		}
		return rg;
	}
	
	/*
	 * Validate productions rule for a vN
	 */
	private static boolean validateProduction(Character vn, String productions, HashSet<String> prodList, RegularGrammar rg) {
		String prod = "";
		
		// Iterate every production for every vN
		prod = productions.substring(productions.indexOf("->")+2);
		int prodLength = 0;
		char first, second;
		Scanner prodScan = new Scanner(prod);
		prodScan.useDelimiter("[|]");
		
		while (prodScan.hasNext()) {
			prod = prodScan.next();
			prodLength = prod.length();
			if (prodLength < 1 || prodLength > 2) { // |prod| = 0 || |prod| = 2
				return false;
			} else { // |prod| = 1 or 2
				first = prod.charAt(0);
				if (Character.isUpperCase(first)){
					return false;
				}
				if (Character.isDigit(first)
						|| Character.isLetter(first)) { // if first symbol is terminal
					rg.vt.add(first); // &A valid?
				}
				
				if (prodLength == 2) { // |prod| = 2
					second = prod.charAt(1);
					if (Character.isUpperCase(first)) { // if first symbol is vN
						return false;
					} else if (Character.isLowerCase(second)
							|| Character.isDigit(second)
							|| (first == '&')) { // if both are lower case or digit
						return false;
					} else if (first == '&' && second == '&') {
						return false;
					}
					rg.vn.add(second);
					prodList.add(prod);
					rg.productions.put(vn, prodList);
				} else { // |prod| = 1
					if (Character.isUpperCase(first)) { // if S -> A
						return false;
					}
					prodList.add(prod);
					rg.productions.put(vn, prodList);
				}
			}
		}
		
		prodScan.close();
		return true;
	}
	
	
}
