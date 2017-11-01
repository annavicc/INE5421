package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.RegularGrammar;
import RegularLanguages.RegularLanguage;
import RegularLanguages.Operators.FAOperator;

public class RegularGrammarTest {
	private static String[] validRG;
	private static String[] invalidRG;
	private static int lengthValid = 5;
	private static int lengthInvalid = 17;

	/**
	 * Possibilities of grammars that are valid
	 */
	@Before
	public void setUpValidGrammars() {
		validRG = new String[lengthValid];
		// L(G) = { a^n | n > 0 }
		validRG[0] = "A -> aA | a";
		// L(G) = { a^n b^m c^k | n, m, k > 0 }
		validRG[1] = "S -> aA\n"
				+ "A -> aA | bB\n"
				+ "B -> bB | cC\n"
				+ "C -> cC | c";
		// L(G) = { abcd e^n | n > 0 } U {&}
		validRG[2] = "S -> aA | & \n"
				+ "A -> bB | bE\n"
				+ "B -> cC\n"
				+ "E -> cC\n"
				+ "C -> dD\n"
				+ "D -> eD | e";
		// L(G) = { a^n | n > 0 }
		validRG[3] = "S -> aA | bB | cC | dD | eE\n"
				+ "A -> aA | a\n"
				+ "B -> bB | b\n"
				+ "C -> cC | c\n"
				+ "D -> dD | d\n"
				+ "E- >e E|e";
		// L(G) = (a,b)*
		validRG[4] = "S -> aA | bA | &\n"
				+ "A -> aA | bA | a | b";
	}
	
	/**
	 * Possibilities of grammars that are invalid
	 * in the context of Regular Grammars
	 */
	@Before
	public void setUpInvalidGrammars() {
		invalidRG = new String[lengthInvalid];
		// |A| < 0
		invalidRG[0] = " -> aA | a";
		// |A| > 1
		invalidRG[1] = "AA -> aA | a\n";
		// { }
		invalidRG[2] = "";
		// |A| = a
		invalidRG[3] = "a -> aA | a";
		// A -> { }
		invalidRG[4] = "A -> ";
		// A -> aAa
		invalidRG[5] = "A -> aAa | a";
		// S -> 0S | 00
		invalidRG[6] = "S -> 00 | 0S";
		// S -> S0 | 0 (vNvt instead of vtvN)
		invalidRG[7] = "S -> S0 | 0";
		// Invalid symbols
		invalidRG[8] = "S -> S0 | 0 | *";
		// S -> 0 | | 0S
		invalidRG[9] = "S -> 0 | | 0S";
		// S -> 0S | A | 1
		invalidRG[10] = "S -> 0S | A | 1";
		// S -> 0s | 0
		invalidRG[11] = "S -> 0s | 0";
		// S -> 0&
		invalidRG[12] = "S -> 0&";
		// S -> &A
		invalidRG[13] = "S -> &A";
		// S -> aS | a | &
		invalidRG[14] = "S -> aS | a | &";
		// S -> & | a | aS
		invalidRG[15] = "S -> & | a | aS";
		// S -> 0A | &    A -> aS   (A => S => &)
		invalidRG[16] = "S -> 0A | &\n"
				+ "A -> aS";
	}

	/**
	 * 
	 * Valid Grammar case
	 * All objects are expected to be created normally
	 */ 
	@Test
	public void testValidGrammar() {
		RegularLanguage rg[] = new RegularLanguage[lengthValid]; 
		int i = 0;
		for (String grammar : validRG) {
			rg[i++] = RegularGrammar.isValidRG(grammar);
		}
		// Should be different than null:
		for (RegularLanguage lr : rg) {
			assertNotNull(lr);
			System.out.println("RG:");
			System.out.println(lr.getDefinition());
			System.out.println("FA:");
			System.out.println(lr.getFA().getDefinition());
			System.out.println("-----");
		}
		// TODO assertion tests
		
		// L(G) = { abcd e^n | n > 0 }
		FiniteAutomata fa = rg[2].getFA();
		System.out.println(fa.getDefinition());
		System.out.println(FAOperator.determinize(fa).getDefinition());
		System.out.println(FAOperator.minimize(fa).getDefinition());
		
		System.err.println(fa.getRG().getDefinition());
		
		
	}
	
	
	/**
	 * Invalid Grammar case
	 * All invalid objects are expected to be null 
	 */
	@Test
	public void testInvalidGrammar() {
		RegularLanguage rg[] = new RegularLanguage[lengthInvalid];
		int i = 0;
		for (String grammar: invalidRG) {
			rg[i++] = RegularGrammar.isValidRG(grammar);
		}
		// Should be null:
		for (RegularLanguage l : rg) {
			assertNull(l);
		}
	}
	
	
	
	
}
