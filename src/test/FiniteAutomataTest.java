package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.Transition;

class FiniteAutomataTest {
	// L(A) = { x | x E (a,b)* and #a's are odd }
	FiniteAutomata automata1;
	
	@BeforeEach
	void setUp() throws Exception {
		this.automata1 = new FiniteAutomata(
				"q0",
				new String[] {"q0", "q1", "q2"},
				new String[] {"q1"},
				new String[] {"a", "b"},
				new Transition[] {
						new Transition("q0", "a", new String[] {"q1"}),
						new Transition("q0", "b", new String[] {"q0"}),
						new Transition("q1", "a", new String[] {"q0", "q2"}),
						new Transition("q1", "b", new String[] {"q1"}),
						new Transition("q2", "a", new String[] {"q1"}),
						}
				);
	}

	@Test
	void test() {
//		assertEquals(legat, 10);
	}

}
