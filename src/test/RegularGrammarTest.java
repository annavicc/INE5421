package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	private static int lengthInvalid = 18;

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
		validRG[4] = "S -> aA | bA | a | b | &\n"
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
		// S -> aA		A -> aS		S -> &
		invalidRG[17] = "S -> aA \n" + 
				"A -> aS\n" + 
				"S -> &";
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
		}
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
	
	/**
	 * Test whether the automata representation
	 * of each regular grammar is correct
	 */
	@Test
	public void testFAEquivalence() {
		// Create Regular Grammar Objects
		RegularLanguage rg[] = new RegularLanguage[lengthValid]; 
		int i = 0;
		for (String grammar : validRG) {
			rg[i++] = RegularGrammar.isValidRG(grammar);
		}
		// L(G) = { a^n | n > 0 }
		FiniteAutomata fa1 =  FAOperator.minimize(rg[0].getFA());
		// L(G) = { a^n b^m c^k | n, m, k > 0 }
		FiniteAutomata fa2 =  FAOperator.minimize(rg[1].getFA());
		// L(G) = { abcd e^n | n > 0 } U {&}
		FiniteAutomata fa3 =  FAOperator.minimize(rg[2].getFA());
		// // L(G) = { a^n | n > 0 }
		FiniteAutomata fa4 =  FAOperator.minimize(rg[3].getFA());
		// // L(G) = (a,b)*
		FiniteAutomata fa5 =  FAOperator.minimize(rg[4].getFA());

		String expected1 = "+------+----+\n" + 
				"|   δ  |  a |\n" + 
				"+------+----+\n" + 
				"|  *q0 | q0 |\n" + 
				"| ->q1 | q0 |\n" + 
				"+------+----+\n";
		String expected2 = "+------+----+----+----+\n" + 
				"|   δ  |  a |  b |  c |\n" + 
				"+------+----+----+----+\n" + 
				"|  *q0 |    |    | q0 |\n" + 
				"|   q1 |    |    | q0 |\n" + 
				"|   q2 |    | q2 | q1 |\n" + 
				"|   q3 | q3 | q2 |    |\n" + 
				"| ->q4 | q3 |    |    |\n" + 
				"+------+----+----+----+\n";
		String expected3 = "+-------+----+----+----+----+----+\n" + 
				"|   δ   |  a |  b |  c |  d |  e |\n" + 
				"+-------+----+----+----+----+----+\n" + 
				"| *->q0 | q5 |    |    |    |    |\n" + 
				"|    q1 |    |    |    |    | q2 |\n" + 
				"|   *q2 |    |    |    |    | q2 |\n" + 
				"|    q3 |    |    |    | q1 |    |\n" + 
				"|    q4 |    |    | q3 |    |    |\n" + 
				"|    q5 |    | q4 |    |    |    |\n" + 
				"+-------+----+----+----+----+----+\n";
		String expected4 = "+-------+----+----+----+----+----+\n" + 
				"|   δ   |  a |  b |  c |  d |  e |\n" + 
				"+-------+----+----+----+----+----+\n" + 
				"|   *q0 | q0 |    |    |    |    |\n" + 
				"|    q1 | q0 |    |    |    |    |\n" + 
				"|    q2 |    | q6 |    |    |    |\n" + 
				"|    q3 |    |    | q7 |    |    |\n" + 
				"|    q4 |    |    |    | q8 |    |\n" + 
				"|    q5 |    |    |    |    | q9 |\n" + 
				"|   *q6 |    | q6 |    |    |    |\n" + 
				"|   *q7 |    |    | q7 |    |    |\n" + 
				"|   *q8 |    |    |    | q8 |    |\n" + 
				"|   *q9 |    |    |    |    | q9 |\n" + 
				"| ->q10 | q1 | q2 | q3 | q4 | q5 |\n" + 
				"+-------+----+----+----+----+----+\n";
		String expected5 = "+-------+----+----+\n" + 
				"|   δ   |  a |  b |\n" + 
				"+-------+----+----+\n" + 
				"| *->q0 | q0 | q0 |\n" + 
				"+-------+----+----+\n";
		
		assertEquals(expected1, fa1.getDefinition());
		assertEquals(expected2, fa2.getDefinition());
		assertEquals(expected3, fa3.getDefinition());
		assertEquals(expected4, fa4.getDefinition());
		assertEquals(expected5, fa5.getDefinition());
		
		assertTrue(FAOperator.isEquivalent(fa1, fa1.getRG().getFA()));
		assertTrue(FAOperator.isEquivalent(fa2, fa2.getRG().getFA()));
		assertTrue(FAOperator.isEquivalent(fa3, fa3.getRG().getFA()));
		assertTrue(FAOperator.isEquivalent(fa4, fa4.getRG().getFA()));
		assertTrue(FAOperator.isEquivalent(fa5, fa5.getRG().getFA()));
		
	}
	
	
	
	
}
