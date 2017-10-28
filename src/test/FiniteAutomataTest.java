package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.bethecoder.ascii_table.ASCIITable;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.FiniteAutomata.State;
import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidBuilderException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidSymbolException;

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

						for (int s2 = 0; s2 < length; s2++) {  // State 2 index
							FiniteAutomata.State stA2 = q[bA][s2];
							FiniteAutomata.State stB2 = q[bB][s2];
							assertThrows(InvalidStateException.class,
									() -> builders[b].addTransition(stA1, 'a', stB2));
							assertThrows(InvalidStateException.class,
									() -> builders[b].addTransition(stB1, 'a', stA2));
						}
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
	void testStateComparison() {
		setUpBuilders();
		for (int bA = 0; bA < length; bA++) {  // builder A index
			for (int s1 = 0; s1 < length; s1++) {  // state 1 index
				assertNotNull(q[bA][s1]);
				assertFalse(q[bA][s1].equals("test string"));
				assertFalse(q[bA][s1].equals(null));
				for (int bB = 0; bB < length; bB++) {  // builder B index
					for (int s2 = 0; s2 < length; s2++) {  // state 2 index
						if (bA == bB && s1 == s2) {
							assertTrue(q[bA][s1].equals(q[bB][s2]));
							
						}
						else {
							assertFalse(q[bA][s1].equals(q[bB][s2]));
						}
					}
				}
			}
		}
	}
	
	@Test
	void testInvalidSymbols() throws InvalidStateException, InvalidSymbolException, IncompleteAutomataException, InvalidBuilderException {
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = builder.newState();
		for (char c = 0; c < (char) -1; c++) {
			if ((c < 'a' || c > 'z') && c != '&') {
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
	

}
