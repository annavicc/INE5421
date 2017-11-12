package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.RegularExpression;
import RegularLanguages.Operators.DiSimone;
import RegularLanguages.Operators.DiSimone.Node;

class DiSimoneTest {
	String[] regex;
	RegularExpression[] reObject;
	DiSimone[] diSimoneObject;
	String[] expectedConcatenation[];
	private static int lengthRegex= 7;
	

	@BeforeEach
	void setUpRegex() {
		reObject = new RegularExpression[lengthRegex];
		diSimoneObject = new DiSimone[lengthRegex];
		regex = new String[lengthRegex];
		regex[0] = "abcd";
		regex[1] = "a*";
		regex[2] = "a**";
		regex[3] = "ab | cd?";
		regex[4] = "(ab)*";
		regex[5] = "(ab)+ | cd*e";
		regex[6] = "(ab)+ | (cd)+ | (ab)+(cd)+";
		new RegularExpression("");
		reObject[0] = RegularExpression
				.isValidRE(regex[0]);
		new RegularExpression("");
		reObject[1] = RegularExpression
				.isValidRE(regex[1]);
		new RegularExpression("");
		reObject[2] = RegularExpression
				.isValidRE(regex[2]);
		new RegularExpression("");
		reObject[3] = RegularExpression
				.isValidRE(regex[3]);
		new RegularExpression("");
		reObject[4] = RegularExpression
				.isValidRE(regex[4]);
		new RegularExpression("");
		reObject[5] = RegularExpression
				.isValidRE(regex[5]);
		new RegularExpression("");
		reObject[6] = RegularExpression
				.isValidRE(regex[6]);
		int i = 0;
		for (int j = 0; j < reObject.length; j++) {
			try {
				diSimoneObject[i] = new DiSimone(reObject[i].getExplicitConcatenation());
				i++;
			} catch (InvalidStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * Test postfix notation (Polish Reverse Notation)
	 */
	@Test
	void testPostOrder() {
		String s1, s2, s3, s4, s5, s6, s7;
		s1 = diSimoneObject[0].getPostOrderRegex(); // abcd
		s2 = diSimoneObject[1].getPostOrderRegex(); // a*
		s3 = diSimoneObject[2].getPostOrderRegex(); // a**
		s4 = diSimoneObject[3].getPostOrderRegex(); // ab | cd?
		s5 = diSimoneObject[4].getPostOrderRegex(); // (ab)*
		s6 = diSimoneObject[5].getPostOrderRegex(); // (ab)+ | cd*e
		s7 = diSimoneObject[6].getPostOrderRegex(); // (ab)+ | (cd)+ | (ab)+(cd)+
		
		// Expected, actual
		assertEquals("ab.c.d.", s1);
		assertEquals("a*", s2);
		assertEquals("a*", s3);
		assertEquals("ab.cd?.|", s4);
		assertEquals("ab.*", s5);
		assertEquals("ab.+cd*.e.|", s6);
		assertEquals("ab.+cd.+|ab.+cd.+.|", s7);
	}
	
	/**
	 * Test if leaf nodes (terminal symbols)
	 * are being numbered correctly
	 */
	@Test
	void testNumberLeafNodes() {
		int n1, n2, n3, n4, n5, n6, n7;
		n1 = diSimoneObject[0].getNumberOfTerminals();
		n2 = diSimoneObject[1].getNumberOfTerminals();
		n3 = diSimoneObject[2].getNumberOfTerminals();
		n4 = diSimoneObject[3].getNumberOfTerminals();
		n5 = diSimoneObject[4].getNumberOfTerminals();
		n6 = diSimoneObject[5].getNumberOfTerminals();
		n7 = diSimoneObject[6].getNumberOfTerminals();
		// Expected, actual
		assertEquals(4, n1);
		assertEquals(1, n2);
		assertEquals(1, n3);
		assertEquals(4, n4);
		assertEquals(2, n5);
		assertEquals(5, n6);
		assertEquals(8, n7);
	}
	
	/**
	 * Test if precedence of operators
	 * are being respected
	 */
	@Test
	void testPrecedence() {
		char op1, op2, op3, op4, op5;
		// lowest priority = 1
		op1 = '*';
		op2 = '+';
		op3 = '?';
		op4 = '.';
		op5 = '|';
		
		// Expected, actual
		assertEquals(3, DiSimone.precedence(op1));
		assertEquals(3, DiSimone.precedence(op2));
		assertEquals(3, DiSimone.precedence(op3));
		assertEquals(2, DiSimone.precedence(op4));
		assertEquals(1, DiSimone.precedence(op5));
	}
	
	/**
	 * Verify if the root of the tree is correct
	 */
	@Test
	void testRoot() {
		DiSimone.Node r1, r2, r3, r4, r5, r6, r7;
		r1 = diSimoneObject[0].getRoot();
		r2 = diSimoneObject[1].getRoot();
		r3 = diSimoneObject[2].getRoot();
		r4 = diSimoneObject[3].getRoot();
		r5 = diSimoneObject[4].getRoot();
		r6 = diSimoneObject[5].getRoot();
		r7 = diSimoneObject[6].getRoot();

		// Expected, actual
		assertEquals('.', r1.data);
		assertEquals('*', r2.data);
		assertEquals('*', r3.data);
		assertEquals('|', r4.data);
		assertEquals('*', r5.data);
		assertEquals('|', r6.data);
		assertEquals('|', r7.data);
	}
	
	@Test
	void testThreadedTree() {
		DiSimone simone = diSimoneObject[0];
		Node root = simone.
				createTree(simone.getPostOrderRegex().toCharArray());
		simone.makeThreadedTree(root);
		Queue<Node> q = new LinkedList<>();
		for (Node node : simone.inOrderThreaded(root, q)) {
//			System.out.print(node.data + "");
		}
	}
	
	

}
