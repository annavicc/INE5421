package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.bethecoder.ascii_table.ASCIITable;

import RegularLanguages.Operators.FAOperator;
import RegularLanguages.FiniteAutomata;
import RegularLanguages.FiniteAutomata.FABuilder;
import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidBuilderException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidSymbolException;
import RegularLanguages.FiniteAutomata.State;

class FiniteAutomataTest {
	FiniteAutomata.FABuilder[] builders;
	FiniteAutomata.State[][] q;
	final static int length = 5;

	void setUpBuilders() {
		builders = new FiniteAutomata.FABuilder[length];
		q = new FiniteAutomata.State[length][length];
		
		for (int b = 0; b < length; b++) {
			builders[b] = new FiniteAutomata.FABuilder();
			for (int s = 0; s < length; s++) {
				q[b][s] = builders[b].newState();
			}
		}
	}

	@Test
	void testStateValidation() throws InvalidStateException, InvalidSymbolException {
		setUpBuilders();
		for (int bA = 0; bA < length; bA++) {  // builder A index
			for (int bB = 0; bB < length; bB++) {  // builder B index
				for (int s1 = 0; s1 < length; s1++) {  // state 1 index
					final FiniteAutomata.State stA1 = q[bA][s1];
					final FiniteAutomata.State stB1 = q[bB][s1];
					if (bA == bB) {
						// Use state from correct builder
						// Do not expect exception
						builders[bA].setInitial(stB1);
						builders[bA].setFinal(stB1);
						
						for (int s2 = 0; s2 < length; s2++) {  // state 2 index
							FiniteAutomata.State stB2 = q[bB][s2];
							builders[bA].addTransition(stB1, 'a', stB2);
						}
						
					}
					else { 
						// Use state from another builder
						// Expect Exception
						final int b = bA; 
						assertThrows(InvalidStateException.class,
								() -> builders[b].setInitial(stB1));
						assertThrows(InvalidStateException.class,
								() -> builders[b].setFinal(stB1));

						assertThrows(InvalidStateException.class,
								() -> builders[b].addTransition(stA1, 'a', stB1));
						assertThrows(InvalidStateException.class,
								() -> builders[b].addTransition(stB1, 'a', stA1));
						
						builders[b].addTransition(stA1, 'a', stA1);
					
						builders[b].importState(stB1);
						builders[b].setInitial(stB1);
						
					}
				}
			}
		}
	}
	
	@Test
	void testBuildValidation() throws IncompleteAutomataException, InvalidStateException, InvalidBuilderException {
		setUpBuilders();
		// Try to build automata without states
		FiniteAutomata.FABuilder b0 = new FiniteAutomata.FABuilder();
		assertThrows(IncompleteAutomataException.class,
				() -> b0.build());
		
		for (int i = 0; i < length; i++) {
			final int pos = i;
			
			// Try to build automata without a initial state
			assertThrows(IncompleteAutomataException.class,
					() -> builders[pos].build());
			
			builders[pos].setInitial(q[pos][pos]);
			// Build complete automata
			FiniteAutomata fa = builders[pos].build();
			assertNotNull(fa);
			assertThrows(InvalidBuilderException.class,
					() -> builders[pos].build());
		}
		
	}
	
	@Test
	void testStateComparison() throws InvalidStateException, IncompleteAutomataException, InvalidBuilderException, InvalidSymbolException {
		setUpBuilders();
		for (int bA = 0; bA < length; bA++) {  // builder A index
			for (int s1 = 0; s1 < length; s1++) {  // state 1 index
				assertNotNull(q[bA][s1]);
				assertNotEquals(q[bA][s1], "test string");
				assertNotEquals(q[bA][s1], null);
				for (int bB = 0; bB < length; bB++) {  // builder B index
					for (int s2 = 0; s2 < length; s2++) {  // state 2 index
						if (bA == bB && s1 == s2) {
							assertEquals(q[bA][s1], q[bB][s2]);
							
						}
						else {
							assertNotEquals(q[bA][s1], q[bB][s2]);
						}
					}
				}
			}
		}
		
		assertThrows(InvalidStateException.class, 
				() -> builders[0].setInitial(q[1][0]));
		
		FiniteAutomata fa = builders[0].importState(q[1][0])
				.setInitial(q[1][0])
				.addTransition(q[0][0], 'x', q[1][0])
				.build();
		
		State qA = fa.getInitial();
		State qB = fa.transition(q[0][0], 'x').first();
		
		assertEquals(qA, qB);
		assertNotEquals(q[1][0], qA);
		assertNotEquals(q[1][0].hashCode(), qB.hashCode());
		
		
		
		Set<FiniteAutomata.State> set1 = new TreeSet<FiniteAutomata.State>();
		set1.add(q[0][1]);
		set1.add(q[0][2]);
		set1.add(q[0][0]);
		set1.add(q[0][3]);
		Set<FiniteAutomata.State> set2 = new TreeSet<FiniteAutomata.State>();
		set2.add(q[0][3]);
		set2.add(q[0][2]);
		set2.add(q[0][0]);
		set2.add(q[0][1]);
		
		assertEquals(set1, set2);
		assertEquals(set1.hashCode(), set2.hashCode());
		
	}
	
	@Test
	void testInvalidSymbols() throws InvalidStateException, InvalidSymbolException, IncompleteAutomataException, InvalidBuilderException {
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = builder.newState();
		for (char c = 0; c < (char) -1; c++) {
			if ((c < 'a' || c > 'z') && c != '&' && !(c >= '0' && c <= '9')) {
				final char ch = c;
				assertThrows(InvalidSymbolException.class,
						() -> builder.addTransition(q0, ch, q0));
			}
			else {
				builder.addTransition(q0, c, q0);
			}
		}
		FiniteAutomata fa = builder.setInitial(q0).build();
		Set<Character> alphabet = fa.getAlphabet();
		for (char c = 'a'; c <= 'z'; c++) {
			assertTrue(alphabet.contains(c));
		}
		assertTrue(alphabet.contains('&'));  // TODO should it contain '&'?
		assertFalse(alphabet.contains('A'));
		
	}
	
	@Test
	void testTransitions() throws InvalidStateException, IncompleteAutomataException, InvalidSymbolException, InvalidBuilderException {
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = builder.newState();
		FiniteAutomata.State q1 = builder.newState();
		FiniteAutomata.State q2 = builder.newState();
		FiniteAutomata fa = builder
				.setInitial(q0)
				.setFinal(q2)
				.addTransition(q0, 'a', q0)
				.addTransition(q0, 'a', q1)
				.addTransition(q0, 'b', q2)
				.addTransition(q1, 'b', q2)
				.build();
		
		// Check transitions output
		assertTrue(fa.transition(q0, 'a').size() == 2);
		assertTrue(fa.transition(q0, 'a').contains(q0));
		assertTrue(fa.transition(q0, 'a').contains(q1));
		assertFalse(fa.transition(q0, 'a').contains(q2));
		assertFalse(fa.transition(q0, 'b').contains(q0));
		assertFalse(fa.transition(q0, 'b').contains(q1));
		assertTrue(fa.transition(q0, 'b').contains(q2));
		
		assertTrue(fa.transition(q1, 'a').size() == 0);
		assertTrue(fa.transition(q1, 'b').contains(q2));
		
		assertTrue(fa.transition(q2, 'a').size() == 0);
		assertTrue(fa.transition(q2, 'b').size() == 0);
		
		// Check alphabet
		assertTrue(fa.getAlphabet().contains('a'));
		assertTrue(fa.getAlphabet().contains('b'));
		assertFalse(fa.getAlphabet().contains('c'));
		
		// Try to modify a transition output
		assertThrows(UnsupportedOperationException.class, 
				() -> fa.transition(q0, 'a').add(q2));
		
		assertThrows(UnsupportedOperationException.class, 
				() -> fa.transition(q0, 'b').add(q1));
		
	}
	
	@Test
	void testAutomataRepresentation() throws InvalidStateException, InvalidSymbolException, IncompleteAutomataException, InvalidBuilderException {
		String smallDelta = "\u03B4";
		String[] topRow = {smallDelta, "&", "a", "b", "c"};
		String[][] data = {
				{"->q0",  "q0",  "q1, q2",          "",    "q5"},
				{"q1",    "",    "",                "q4",  "q4"},
				{"*q2",   "",    "",                "q4",  "q4"},
				{"q3",    "",    "q1, q2, q3, q4",  "",    ""  },
				{"q4",    "",    "q3",              "q3",  ""  },
				{"*q5",   "",    "",                "",    ""  }
		};
		
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State[] q = new FiniteAutomata.State[6];
		for (int i = 0; i < 6; i++) {
			q[i] = builder.newState();
		}
		builder.setInitial(q[0])
			.setFinal(q[2])
			.setFinal(q[5])
			.addTransition(q[0], '&', q[0])
			.addTransition(q[0], 'a', q[1])
			.addTransition(q[0], 'a', q[2])
			.addTransition(q[0], 'c', q[5])
			.addTransition(q[1], 'b', q[4])
			.addTransition(q[1], 'c', q[4])
			.addTransition(q[2], 'b', q[4])
			.addTransition(q[2], 'c', q[4])
			.addTransition(q[3], 'a', q[4])
			.addTransition(q[3], 'a', q[3])
			.addTransition(q[3], 'a', q[2])
			.addTransition(q[3], 'a', q[1])
			.addTransition(q[4], 'b', q[3])
			.addTransition(q[4], 'a', q[3]);
		
		
		String def = builder.build().getDefinition();
		String table = ASCIITable.getInstance().getTable(topRow, data).toString();
		assertEquals(table, def);
		
	}
	
	@Test
	void testDeterminize() throws IncompleteAutomataException, InvalidBuilderException, InvalidStateException, InvalidSymbolException {
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		// Ex. Cap. III pg 4
		State[] q = new State[4];
		for (int i = 0; i < 4; i++) {
			q[i] = builder.newState();
		}
		
		FiniteAutomata ndfa = builder.setInitial(q[0])
				.setFinal(q[3])
				.addTransition(q[0], 'a', q[0])
				.addTransition(q[0], 'a', q[1])
				.addTransition(q[0], 'b', q[0])
				.addTransition(q[1], 'b', q[2])
				.addTransition(q[2], 'b', q[3])
				.build();
		
		assertFalse(FAOperator.isDeterministic(ndfa));
		
		FiniteAutomata dfa = FAOperator.determinize(ndfa);
		assertTrue(FAOperator.isDeterministic(dfa));
		
		System.out.println("--------------------------------------------------");
		System.out.println("testDeterminize:");
		System.out.println(ndfa.getDefinition());
		System.out.println(dfa.getDefinition());
		
		// TODO Assert same language 
		
		
		// Example from https://www.tutorialspoint.com/automata_theory/ndfa_to_dfa_conversion.htm
		builder = new FiniteAutomata.FABuilder();
		q = new State[5];
		for (int i = 0; i < 5; i++) {
			q[i] = builder.newState();
		}
		
		ndfa = builder.setInitial(q[0])
				.setFinal(q[4])
				.addTransition(q[0], '0', q[0])
				.addTransition(q[0], '0', q[1])
				.addTransition(q[0], '0', q[2])
				.addTransition(q[0], '0', q[3])
				.addTransition(q[0], '0', q[4])
				.addTransition(q[0], '1', q[3])
				.addTransition(q[0], '1', q[4])
				.addTransition(q[1], '0', q[2])
				.addTransition(q[1], '1', q[4])
				.addTransition(q[2], '1', q[1])
				.addTransition(q[3], '0', q[4])
				.build();
		
		assertFalse(FAOperator.isDeterministic(ndfa));
		
		dfa = FAOperator.determinize(ndfa);
		assertTrue(FAOperator.isDeterministic(dfa));
		
		System.out.println(ndfa.getDefinition());
		System.out.println(dfa.getDefinition());
		
		// TODO Assert same language
	}
	
	@Test
	void testUselessStates() throws InvalidStateException, InvalidSymbolException, IncompleteAutomataException, InvalidBuilderException {
		System.out.println("--------------------------------------------------");
		System.out.println("testUselessStates:");
		
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		State[] q = new State[6];
		for (int i = 0; i < 6; i++) {
			q[i] = builder.newState();
		}
		
		builder.setInitial(q[0])
				.addTransition(q[0], 'a', q[1])
				.addTransition(q[0], 'a', q[2])
				.addTransition(q[0], 'a', q[5])
				.addTransition(q[0], 'b', q[2])
				.addTransition(q[1], 'a', q[3])
				.addTransition(q[4], 'a', q[3])
				.addTransition(q[5], 'a', q[3])
				.setFinal(q[3]);
		
		FiniteAutomata fa = builder.build();
		assertFalse(FAOperator.getDeadStates(fa).isEmpty());
		assertFalse(FAOperator.getUnreachableStates(fa).isEmpty());
		
		FiniteAutomata faNoDeadStates = FAOperator.removeStates(fa, FAOperator.getDeadStates(fa));
		assertTrue(FAOperator.getDeadStates(faNoDeadStates).isEmpty());
		assertFalse(FAOperator.getUnreachableStates(faNoDeadStates).isEmpty());
		
		
		FiniteAutomata faNoUnreachableStates = FAOperator.removeStates(fa, FAOperator.getUnreachableStates(fa));
		assertTrue(FAOperator.getUnreachableStates(faNoUnreachableStates).isEmpty());
		assertFalse(FAOperator.getDeadStates(faNoUnreachableStates).isEmpty());
		
		
		System.out.println(fa.getDefinition());
		System.out.println(faNoDeadStates.getDefinition());
		System.out.println(faNoUnreachableStates.getDefinition());
		
		// TODO assert same language

	}
	
	@Test
	void testMinimize() throws InvalidStateException, InvalidSymbolException, IncompleteAutomataException, InvalidBuilderException {
		System.out.println("--------------------------------------------------");
		System.out.println("testMinimize:");
		
		// Cap. III - pg. 8 - ex. 2
		{
			FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
			
			State qS = builder.newState();
			State qA = builder.newState();
			State qB = builder.newState();
			State qC = builder.newState();
			State qD = builder.newState();
			State qE = builder.newState();
			
			FiniteAutomata fa = builder.setInitial(qS)
					.setFinal(qE)
					
					.addTransition(qS, '0', qA)
					.addTransition(qS, '0', qD)
					.addTransition(qS, '1', qE)
					
					.addTransition(qA, '0', qA)
					.addTransition(qA, '0', qB)
					.addTransition(qA, '1', qC)
					.addTransition(qA, '1', qE)
	
					.addTransition(qB, '0', qB)
					
					.addTransition(qC, '0', qA)
					.addTransition(qC, '0', qB)
					
					.addTransition(qD, '0', qB)
					.addTransition(qD, '0', qD)
					.addTransition(qD, '1', qC)
					
					.addTransition(qE, '0', qE)
					.addTransition(qE, '1', qE)
					
					.build();
			
			System.out.println(fa.getDefinition());
			
			FiniteAutomata faMin = FAOperator.minimize(fa);
			System.out.println(faMin.getDefinition());
			
			assertFalse(FAOperator.isDeterministic(fa));
			assertTrue(FAOperator.isDeterministic(faMin));
			assertTrue(FAOperator.getDeadStates(faMin).isEmpty());
			assertTrue(FAOperator.getUnreachableStates(faMin).isEmpty());
			
			assertEquals(
					"+------+----+----+\n" +
					"|   δ  |  0 |  1 |\n" +
					"+------+----+----+\n" +
					"|  *q0 | q0 | q0 |\n" +
					"| ->q1 | q1 | q0 |\n" +
					"+------+----+----+\n",
					faMin.getDefinition());
			
			// TODO assert same language
			
		}

		// Empty language test
		{
			FABuilder builder = new FABuilder();
			FiniteAutomata fa = builder.setInitial(builder.newState()).build();
			
			FABuilder builder2 = new FABuilder();
			State[] q = new State[5];
			for (int i = 0; i < 5; i++) {
				q[i] = builder2.newState();
			}
			
			FiniteAutomata fa2 = builder2.setInitial(q[0])
					.setFinal(q[4])
					.addTransition(q[0], 'a', q[1])
					.addTransition(q[0], 'a', q[2])
					.addTransition(q[0], 'b', q[0])
					.addTransition(q[1], 'b', q[2])
					.addTransition(q[2], 'c', q[3])
					.addTransition(q[4], 'd', q[4])
					.build();
	
			
			System.out.println(fa.getDefinition());
			System.out.println(FAOperator.minimize(fa).getDefinition());
			
			System.out.println(fa2.getDefinition());
			System.out.println(FAOperator.minimize(fa2).getDefinition());
			
			assertEquals(fa.getDefinition(), FAOperator.minimize(fa).getDefinition());
			assertEquals(fa.getDefinition(), FAOperator.minimize(fa2).getDefinition());
			assertNotEquals(fa2.getDefinition(), FAOperator.minimize(fa2).getDefinition());
			
			// TODO assert same language
		}

		// Cap. III - pg. 8 - ex. 1
		{
			FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
			
			State qS = builder.newState();
			State qA = builder.newState();
			State qB = builder.newState();
			State qC = builder.newState();
			State qF = builder.newState();
			
			FiniteAutomata fa = builder.setInitial(qS)
					.setFinal(qS)
					.setFinal(qF)
					
					.addTransition(qS, 'a', qA)
					.addTransition(qS, 'b', qB)
					.addTransition(qS, 'b', qF)
					.addTransition(qS, 'c', qS)
					.addTransition(qS, 'c', qF)
					
					.addTransition(qA, 'a', qS)
					.addTransition(qA, 'a', qF)
					.addTransition(qA, 'b', qC)
					.addTransition(qA, 'c', qA)
					
					.addTransition(qB, 'a', qA)
					.addTransition(qB, 'c', qB)
					.addTransition(qB, 'c', qS)
					.addTransition(qB, 'c', qF)
					
					.addTransition(qC, 'a', qS)
					.addTransition(qC, 'a', qF)
					.addTransition(qC, 'c', qA)
					.addTransition(qC, 'c', qC)
					
					.build();
			
			System.out.println(fa.getDefinition());
			
			FiniteAutomata faMin = FAOperator.minimize(fa);
			System.out.println(faMin.getDefinition());
			
			assertFalse(FAOperator.isDeterministic(fa));
			assertTrue(FAOperator.isDeterministic(faMin));
			assertTrue(FAOperator.getDeadStates(faMin).isEmpty());
			assertTrue(FAOperator.getUnreachableStates(faMin).isEmpty());
			
			assertEquals(
					"+-------+----+----+----+\n" +
					"|   δ   |  a |  b |  c |\n" +
					"+-------+----+----+----+\n" +
					"| *->q0 | q1 | q2 | q0 |\n" +
					"|    q1 | q0 | q3 | q1 |\n" +
					"|   *q2 | q1 |    | q0 |\n" +
					"|    q3 | q0 |    | q1 |\n" +
					"+-------+----+----+----+\n",
					faMin.getDefinition());
			
			// TODO assert same language
			
		}		
	}
	
	@Test
	void testCloneFiniteAutomata() throws IncompleteAutomataException, InvalidBuilderException, InvalidStateException, InvalidSymbolException {
		FABuilder builder = new FABuilder();
		State[] q = new State[5];
		for (int i = 0; i < 5; i++) {
			q[i] = builder.newState();
		}
		
		FiniteAutomata fa = builder.setInitial(q[0])
				.setFinal(q[4])
				.addTransition(q[0], 'a', q[1])
				.addTransition(q[0], 'a', q[2])
				.addTransition(q[0], 'b', q[1])
				.addTransition(q[1], 'b', q[2])
				.addTransition(q[2], 'c', q[3])
				.addTransition(q[3], 'd', q[4])
				.build();
		
		assertEquals(fa.clone().getDefinition(), fa.getDefinition());

		for (int i = 0; i < 5; i++) {
			assertTrue(fa.getStates().contains(q[i]));
			assertFalse(fa.clone().getStates().contains(q[i]));
		}
	}

}
