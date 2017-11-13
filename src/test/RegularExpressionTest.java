package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import RegularLanguages.RegularExpression;
import RegularLanguages.RegularLanguage;

class RegularExpressionTest {
	private static String[] validRE;
	private static String[] invalidRE;
	private static String[] toFormatRE;
	private static String[] concatenated;
	private static int lengthValid = 16;
	private static int lengthInvalid = 15;
	private static int lengthToFormatRE = 7;
	private static int lengthConcatenated = 6;	
	
	/**
	 * Original regular expressions to be converted
	 * to an equivalent simplified representation
	 */
	@BeforeEach
	void setUpToFormatRE() {
		toFormatRE = new String[lengthToFormatRE];
		toFormatRE[0] = "a*****";
		toFormatRE[1] = "a?????";
		toFormatRE[2] = "a+++++";
		toFormatRE[3] = "a?*?*?***???*";
		toFormatRE[4] = "a?+?+?+++???+";
		toFormatRE[5] = "a*+*+*++++***+*";
		toFormatRE[6] = "a?+*?+?***???+++***?+?*+*";
	}
	
	/**
	 * Possibilities of regular expressions that are valid
	 */
	@BeforeEach
	void setUpValidRE(){
		validRE= new String[lengthValid];
		// ab
		validRE[0] = "ab";
		// (ab)
		validRE[1] = "(ab)";
		//(ab)*
		validRE[2] = "(ab)*";
		// a???
		validRE[3] = "a???";
		// a***
		validRE[4] = "a***";
		// a+++
		validRE[5] = "a+++";
		// (((a)))
		validRE[6] = "(((a)))";
		// (a | ab | c*)*
		validRE[7] = "(a | ab | c*)*";
		// (a | ab | c*)**??
		validRE[8] = "(a | ab | c*)*??";
		// ( a | (ab | cd)+ )+
		validRE[9] = "(a | (ab | cd)+)+";
		// 0 (01 | (02) * | (03)++) | (1?01?)
		validRE[10] = "0 (01 | (02) * | (03)++) | (a?01?)";
		// a | &
		validRE[11] = "a | &";
		// &*
		validRE[12] = "&*";
		// (())* (empty language)
		validRE[13] = "(())*";
		// (a*b)*
		validRE[14] = "(a*b)*";
		// (a | & | a*b?c+)*
		validRE[15] = "(a | & | a*b?c+)*";
	}
	
	/**
	 * Possibilities of regular expressions that are valid
	 */
	@BeforeEach
	void setUpInvalidRE(){
		invalidRE= new String[lengthInvalid];
		// (ab
		invalidRE[0] = "(ab";
		// ab)
		invalidRE[1] = "ab)";
		// *
		invalidRE[2] = "*";
		// ?
		invalidRE[3] = "?";
		// +
		invalidRE[4] = "+";
		// a | b | +c
		invalidRE[5] = "a | b | +c";
		// a | b | *c
		invalidRE[6] = "a | b | *c";
		// a | b | ?c
		invalidRE[7] = "a | b | ?c";
		// a | b | | c
		invalidRE[8] = "a | b | | c";
		// uneven parenthesis
		invalidRE[9] = "(a(b(c(d)*)+)*";
		// a | b |
		invalidRE[10] = "a | b | ";
		// invalid symbols
		invalidRE[11] = "a | b | $ | -";
		// (*)
		invalidRE[12] = "(*)";
		// (|a)
		invalidRE[13] = "(|a)";
		// (.b)
		invalidRE[14] = "(.b)";
	}
	
	@BeforeEach
	void setUpConcatenated() {
		concatenated = new String[lengthConcatenated];
		concatenated[0] = "abcd";
		concatenated[1] = "(ab)*(cd)*";
		concatenated[2] = "ab*c**de*";
		concatenated[3] = "ab?cd+e";
		concatenated[4] = "((ab)+(cd)+)*";
		concatenated[5] = "ab | cd | ef*g";
	}
	
	/**
	 * Formatted Regular Expressions case
	 * Complex representations should match
	 * its equivalent simplified representation
	 */
	@Test
	void testFormattedRE() {
		RegularLanguage rl[] = new RegularLanguage[lengthToFormatRE]; 
		int i = 0;
		for (String re : toFormatRE) {
			rl[i++] = RegularExpression.isValidRE(re);
		}
		// Verify if all objects were created
		for (RegularLanguage re : rl) {
			if (re.equals(null)) {
				fail(); // object couldn't be created
			}
		}

		// a***** -> a*
		assertEquals("a*", rl[0].getRE().getFormattedRegex());
		// aa????? -> a?
		assertEquals("a?", rl[1].getRE().getFormattedRegex());
		// a+++++ -> a+
		assertEquals("a+", rl[2].getRE().getFormattedRegex());
		// a?*?*?***???* -> a*
		assertEquals("a*", rl[3].getRE().getFormattedRegex());
		// a?+?+?+++???+ -> a*
		assertEquals("a*", rl[4].getRE().getFormattedRegex());
		// a*+*+*++++***+* -> a*
		assertEquals("a*", rl[5].getRE().getFormattedRegex());
		// a?+*?+?***???+++***?+?*+* -> a*
		assertEquals("a*", rl[6].getRE().getFormattedRegex());
			
	}

	/**
	 * Valid Regular Expressions case
	 * All objects are expected to be created normally
	 */ 
	@Test
	void testValidRE() {
		RegularLanguage rl[] = new RegularLanguage[lengthValid]; 
		int i = 0;
		for (String re : validRE) {
			rl[i++] = RegularExpression.isValidRE(re);
		}
		// Should be different than null:
		for (RegularLanguage lr : rl) {
			assertNotNull(lr);
		}
		// Should return a FA without error
		for (RegularLanguage lr : rl) {
			lr.getFA();
		}
		
		
	}
	/**
	 * Invalid Regular Expression case
	 * All invalid objects are expected to be null 
	 */
	@Test
	void testInvalidRE() {
		RegularLanguage rl[] = new RegularLanguage[lengthInvalid];
		int i = 0;
		for (String re: invalidRE) {
			rl[i++] = RegularExpression.isValidRE(re);
		}
		// Should be null:
		for (RegularLanguage l : rl) {
			assertNull(l);
		}
	}
	
	@Test
	void testConcatenationOperator() {
		String conc1, conc2, conc3, conc4, conc5, conc6;
		RegularExpression re1, re2, re3, re4, re5, re6;
		re1 = RegularExpression.isValidRE(concatenated[0]); // abcd
		re2 = RegularExpression.isValidRE(concatenated[1]); // (ab)*(cd)*
		re3 = RegularExpression.isValidRE(concatenated[2]); // ab*c**de*
		re4 = RegularExpression.isValidRE(concatenated[3]); // ab?cd+e
		re5 = RegularExpression.isValidRE(concatenated[4]); // ((ab)+(cd)+)*
		re6 = RegularExpression.isValidRE(concatenated[5]); // ab | cd | ef*g
		conc1 = re1.getExplicitConcatenation();
		conc2 = re2.getExplicitConcatenation();
		conc3 = re3.getExplicitConcatenation();
		conc4 = re4.getExplicitConcatenation();
		conc5 = re5.getExplicitConcatenation();
		conc6 = re6.getExplicitConcatenation();
		// obtained and expected:
		assertEquals(conc1, "a.b.c.d");
		assertEquals(conc2, "(a.b)*.(c.d)*");
		assertEquals(conc3, "a.b*.c*.d.e*");
		assertEquals(conc4, "a.b?.c.d+.e");
		assertEquals(conc5, "((a.b)+.(c.d)+)*");
		assertEquals(conc6, "a.b|c.d|e.f*.g");
		
	}
	

}
