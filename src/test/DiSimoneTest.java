package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.RegularExpression;
import RegularLanguages.RegularGrammar;
import RegularLanguages.Operators.DiSimone;
import RegularLanguages.Operators.DiSimone.Node;
import RegularLanguages.Operators.FAOperator;

class DiSimoneTest {
	String[] regex;
	String[] postOrderRegex;
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
				e.printStackTrace();
			}
		}
	}
	
	@BeforeEach
	void setUpPostOrderRegex() {
		postOrderRegex = new String[lengthRegex];
		postOrderRegex[0] = diSimoneObject[0].getPostOrderRegex();
		postOrderRegex[1] = diSimoneObject[1].getPostOrderRegex();
		postOrderRegex[2] = diSimoneObject[2].getPostOrderRegex();
		postOrderRegex[3] = diSimoneObject[3].getPostOrderRegex();
		postOrderRegex[4] = diSimoneObject[4].getPostOrderRegex();
		postOrderRegex[5] = diSimoneObject[5].getPostOrderRegex();
		postOrderRegex[6] = diSimoneObject[6].getPostOrderRegex();
	}
	

	/**
	 * Test postfix notation (Polish Reverse Notation)
	 */
	@Test
	void testPostOrder() {
		// Expected, actual
		assertEquals("ab.c.d.", postOrderRegex[0]);
		assertEquals("a*", postOrderRegex[1]);
		assertEquals("a*", postOrderRegex[2]);
		assertEquals("ab.cd?.|", postOrderRegex[3]);
		assertEquals("ab.*", postOrderRegex[4]);
		assertEquals("ab.+cd*.e.|", postOrderRegex[5]);
		assertEquals("ab.+cd.+|ab.+cd.+.|", postOrderRegex[6]);
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
	
	
	/**
	 * Test Expression Tree correctness
	 */
	@Test
	void testTreeCreation() {
		DiSimone s = diSimoneObject[6];
		// (ab)+ | (cd)+ | (ab)+(cd)+
		Node root = s.createTree(postOrderRegex[6].toCharArray());
		String actual1 = "";
		for (Node n : s.inOrder(root, new LinkedList<Node>())) {
			if (Character.isLetterOrDigit(n.data)) {
				actual1 += n.nodeNumber + "" + n.data + " ";
			} else {
				actual1 += n.data + " ";
			}
		}
		
		DiSimone s2 = diSimoneObject[5];
		// (ab)+ | cd*e
		Node root2 = s2.createTree(postOrderRegex[5].toCharArray());
		String actual2 = "";
		for (Node n : s2.inOrder(root2, new LinkedList<Node>())) {
			if (Character.isLetterOrDigit(n.data)) {
				actual2 += n.nodeNumber + "" + n.data + " ";
			} else {
				actual2 += n.data + " ";
			}
		}
		
		// Expected, actual
		assertEquals("1a . 2b + | 3c . 4d + | 5a . 6b + . 7c . 8d + ", actual1);
		assertEquals("1a . 2b + | 3c . 4d * . 5e " , actual2);		
	}
	
	/**
	 * Given an expression tree,
	 * check whether the tree is
	 * correctly threaded
	 */
	@Test
	void testThreadedTree() {
		DiSimone simone = diSimoneObject[6];
		Node root = simone.createTree(postOrderRegex[6].toCharArray());
		simone.makeThreadedTree(root);
		String actual1 = "";
		for (Node node : simone.inOrderThreaded(root, new LinkedList<Node>())) {
			if (Character.isLetterOrDigit(node.data)) {
				actual1 += node.nodeNumber + "" + node.data + " ";
			} else {
				actual1 += node.data + " ";
			}
		}
		DiSimone simone2 = diSimoneObject[5];
		Node root2 = simone.createTree(postOrderRegex[5].toCharArray());
		simone2.makeThreadedTree(root2);
		String actual2 = "";
		for (Node node : simone.inOrderThreaded(root2, new LinkedList<Node>())) {
			if (Character.isLetterOrDigit(node.data)) {
				actual2 += node.nodeNumber + "" + node.data + " ";
			} else {
				actual2 += node.data + " ";
			}
		}
		
		// Expected, actual
		assertEquals("1a . 2b + | 3c . 4d + | 5a . 6b + . 7c . 8d + ", actual1);
		assertEquals("1a . 2b + | 3c . 4d * . 5e " , actual2);
	}
	
	/**
	 * Test whether every FA created using the
	 * DiSimone method is correct
	 */
	@Test
	void testFAEquivalence() {
		// Get minimized automata for every DiSimone Tree
		FiniteAutomata fa1 = FAOperator.minimize(diSimoneObject[0].getFA()); // abcd
		FiniteAutomata fa2 = FAOperator.minimize(diSimoneObject[1].getFA()); // a*
		FiniteAutomata fa3 = FAOperator.minimize(diSimoneObject[2].getFA()); // a**
		FiniteAutomata fa4 = FAOperator.minimize(diSimoneObject[3].getFA()); // ab | cd?
		FiniteAutomata fa5 = FAOperator.minimize(diSimoneObject[4].getFA()); //(ab)*
		FiniteAutomata fa6 = FAOperator.minimize(diSimoneObject[5].getFA()); // (ab)+ | cd*e
		FiniteAutomata fa7 = FAOperator.minimize(diSimoneObject[6].getFA()); // (ab)+ | (cd)+ | (ab)+(cd)+
		
		String expected1 = "+------+----+----+----+----+\n" + 
				"|   δ  |  a |  b |  c |  d |\n" + 
				"+------+----+----+----+----+\n" + 
				"|  *q0 |    |    |    |    |\n" + 
				"|   q1 |    |    |    | q0 |\n" + 
				"|   q2 |    |    | q1 |    |\n" + 
				"|   q3 |    | q2 |    |    |\n" + 
				"| ->q4 | q3 |    |    |    |\n" + 
				"+------+----+----+----+----+\n" ;
		String expected2 = "+-------+----+\n" + 
				"|   δ   |  a |\n" + 
				"+-------+----+\n" + 
				"| *->q0 | q0 |\n" + 
				"+-------+----+\n";
		
		String expected3 = "+------+----+----+----+----+\n" + 
				"|   δ  |  a |  b |  c |  d |\n" + 
				"+------+----+----+----+----+\n" + 
				"|  *q0 |    |    |    | q3 |\n" + 
				"| ->q1 | q2 |    | q0 |    |\n" + 
				"|   q2 |    | q3 |    |    |\n" + 
				"|  *q3 |    |    |    |    |\n" + 
				"+------+----+----+----+----+\n";

		String expected4 = "+-------+----+----+\n" + 
				"|   δ   |  a |  b |\n" + 
				"+-------+----+----+\n" + 
				"| *->q0 | q1 |    |\n" + 
				"|    q1 |    | q0 |\n" + 
				"+-------+----+----+\n";
		
		String expected5 = "+------+----+----+----+----+----+\n" + 
				"|   δ  |  a |  b |  c |  d |  e |\n" + 
				"+------+----+----+----+----+----+\n" + 
				"|  *q0 | q1 |    |    |    |    |\n" + 
				"|   q1 |    | q0 |    |    |    |\n" + 
				"|   q2 |    |    |    | q2 | q4 |\n" + 
				"| ->q3 | q1 |    | q2 |    |    |\n" + 
				"|  *q4 |    |    |    |    |    |\n" + 
				"+------+----+----+----+----+----+\n";
		
		String expected6 = "+------+----+----+----+----+\n" + 
				"|   δ  |  a |  b |  c |  d |\n" + 
				"+------+----+----+----+----+\n" + 
				"|  *q0 | q1 |    | q2 |    |\n" + 
				"|   q1 |    | q0 |    |    |\n" + 
				"|   q2 |    |    |    | q4 |\n" + 
				"| ->q3 | q1 |    | q2 |    |\n" + 
				"|  *q4 |    |    | q2 |    |\n" + 
				"+------+----+----+----+----+\n";
		assertEquals(expected1, fa1.getDefinition());
		assertEquals(expected2, fa2.getDefinition());
		assertEquals(expected2, fa3.getDefinition());
		assertEquals(expected3, fa4.getDefinition());
		assertEquals(expected4, fa5.getDefinition());
		assertEquals(expected5, fa6.getDefinition());
		assertEquals(expected6, fa7.getDefinition());
	}
	
	/**
	 * Test equivalence between FA and RE
	 * @throws InvalidStateException 
	 */
	@Test
	void testREEquivalence() throws InvalidStateException {
		
		RegularExpression re1, re2;
		RegularGrammar rg1;
		
		// L = {x | (a,b)* && (|x| % 3 != 0)} 
		re1 = RegularExpression.isValidRE("((a|b)(a|b)(a|b))*(a|b)(a|b)?");
		rg1 = RegularGrammar.isValidRG("A->a|b|aB|bB \n B->a|b|aC|bC \n C->aA|bA");
		assertTrue(FAOperator.isEquivalent(re1.getFA(), rg1.getFA()));
		
		// L = {x | (a,b)* && (|x| is even) && (x does not contain "bb")}
		re1 = RegularExpression.isValidRE("(ba)*(ab)* (aa(ba)*(ab)*)*");
		re2 = RegularExpression.isValidRE("(a(ba)*a | ba)* (ab)*");
		rg1 = RegularGrammar.isValidRG("S->&|aB|bD \n A->aB|bD \n B->a|b|aA|bC \n C->aB \n D->a|aA");
		assertTrue(FAOperator.isEquivalent(re1.getFA(), re2.getFA()));
		assertTrue(FAOperator.isEquivalent(rg1.getFA(), re1.getFA()));
		assertTrue(FAOperator.isEquivalent(rg1.getFA(), re2.getFA()));
		
		// L = {x | (1,2,3)* && (sum(elements of x) % 3 == 0)}
		re1 = RegularExpression.isValidRE("(  3  |  1 (3|13*2)* (2|13*1)  |  2 (3|23*1)* (1|23*2)  )*");
		rg1 = RegularGrammar.isValidRG("S->&|3|3A|1B|2C \n A->3|3A|1B|2C \n B->3B|1C|2|2A \n C->3C|1|1A|2B");
		assertTrue(FAOperator.isEquivalent(rg1.getFA(), re1.getFA()));
		
		
		
	}
	
	

}
