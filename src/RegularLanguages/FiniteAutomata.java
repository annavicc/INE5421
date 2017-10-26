package RegularLanguages;

import java.util.ArrayList;
import java.util.TreeSet;

import RegularLanguages.Transition.TransitionBuilder;


public class FiniteAutomata extends RegularLanguage {
		
	private State initial;
	private ArrayList<State> states;
	private ArrayList<State> finals;
	private ArrayList<Character> alphabet;
	private ArrayList<Transition> transitions;
	
	// TODO remove
	public FiniteAutomata() {}
	
	private FiniteAutomata(FiniteAutomataBuilder builder) {
		
	}
	
	public FiniteAutomata determinize() {
		return null;
	}
	
	public FiniteAutomata minimize() {
		return null;
	}
	
	
	/**
	 * Convert AF to RG
	 * TODO implement
	 */
	public RegularGrammar getRG() {
		return new RegularGrammar("");
	}
	
	/**
	 * Get AF
	 */
	public FiniteAutomata getAF() {
		return this;
	}
	
	/**
	 * Convert AF to RE
	 * Out of scope
	 */
	public RegularExpression getRE() {
		return null;
	}
	
	/**
	 * Builder class 
	 *
	 */
	public class FiniteAutomataBuilder {
		
		private State initial;
		private TreeSet<State> states;
		private TreeSet<State> finals;
		private TreeSet<Character> alphabet;
		// TODO find a better representation
		private TreeSet<TransitionBuilder> transitions;
		
		public State newState() {
			State s = new State();
			states.add(s);
			return s;
		}
		
		public boolean addTransition(State in, char symbol, State out) {
			// TODO NOW WHAT?
			return false;
		}
		
		private FiniteAutomata build() {
			return new FiniteAutomata(this);
		}
	}
	
	
}

