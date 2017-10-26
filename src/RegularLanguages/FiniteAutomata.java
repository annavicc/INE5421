package RegularLanguages;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class FiniteAutomata extends RegularLanguage {
	
	private final State initial;
	private final Set<State> states;
	private final Set<State> finals;
	private final Set<Character> alphabet;
	private final Map<TransitionInput, Set<State>> transitions;
	
	private FiniteAutomata(FiniteAutomataBuilder builder) {
		super(InputType.AF);
		
		this.initial = builder.initial;
		
		// Make Sets immutable 
		this.states = Collections.unmodifiableSet(builder.states);
		this.finals = Collections.unmodifiableSet(builder.finals);
		this.alphabet = Collections.unmodifiableSet(builder.alphabet);

		// Make internal Sets immutable
		Map<TransitionInput, Set<State>> temp;
		temp = builder.transitions.entrySet().stream()
				.collect(Collectors.toMap(
						e -> e.getKey(),
						e -> Collections.unmodifiableSet(e.getValue())
				));
		
		// Make external map immutable 
		this.transitions = Collections.unmodifiableMap(temp);

	}
	
	public String getDefinition() {
		String af = "";
		af += "States:\n";
		for (State s : this.states) {
			if (this.finals.contains(s)) {
				af += '*';
			}
			if (this.initial.equals(s)) {
				af += "->";
			}
			
			af += s.hashCode() + '\n';
		}
		af += "\nAlphabet: ";
		for (char c : this.alphabet) {
			af += c + ", ";
		}
		af += "\n\nTransitions:\n";
		for (Entry<TransitionInput, Set<State>> e : this.transitions.entrySet()) {
			TransitionInput i = e.getKey();
			af += i.hashCode() + "(" + i.getState().hashCode() + ", " + i.getSymbol() + ") ->  {\n";
			for (State s : e.getValue()) {
				af += "\t" + s.toString() + "\n";
			}
			af += "}\n";
		}
		return af;
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
	 * AF State representation
	 */
	public static class State {

		private UUID uuid;
		
		private State() {
			this.uuid = UUID.randomUUID();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !State.class.isAssignableFrom(obj.getClass())) {
		        return false;
		    }
		    final State s = (State) obj;
			return this.uuid.equals(s.uuid);
		}
		
		@Override
		public int hashCode() {
			return this.uuid.hashCode();
		}
		
	}
	
	/**
	 * AF Transition input representation
	 */
	public static class TransitionInput {
		State s;
		char c;
		
		public TransitionInput(State s, char c) {
			this.s = s;
			this.c = c;
		}
		
		public State getState() {
			return this.s;
		}
		
		public char getSymbol() {
			return this.c;
		}
		
		@Override
		public boolean equals(Object obj) {
		    if (obj == null || !TransitionInput.class.isAssignableFrom(obj.getClass())) {
		        return false;
		    }
		    final TransitionInput ti = (TransitionInput) obj;
			return this.s.equals(ti.s) && this.c == ti.c;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(s, c);
		}
		
	}
	
	/**
	 * AF Builder class 
	 *
	 */
	public static class FiniteAutomataBuilder {
		
		private State initial;
		private HashSet<State> states;
		private HashSet<State> finals;
		private HashSet<Character> alphabet;
		private HashMap<TransitionInput, HashSet<State>> transitions;
		
		public FiniteAutomataBuilder() {
			this.initial = null;
			this.states = new HashSet<State>();
			this.finals = new HashSet<State>();
			this.alphabet = new HashSet<Character>();
			this.transitions = new HashMap<TransitionInput, HashSet<State>>();
		}
		
		public State newState() {
			State s = new State();
			states.add(s);
			return s;
		}
		
		public boolean setInitial(State s) {
			if (states.contains(s)) {
				initial = s;
				return true;
			}
			return false;
		}
		
		public boolean setFinal(State s) {
			if (states.contains(s)) {
				finals.add(s);
				return true;
			}
			return false;
		}
		
		public boolean addTransition(State in, char c, State out) {
			if (!this.states.contains(in) || !this.states.contains(out)) {
				return false;
			}
			TransitionInput input = new TransitionInput(in, c);
			HashSet<State> outList = this.transitions.get(input);
			if (outList == null) {
				outList = new HashSet<State>();
			}
			outList.add(out);
			this.alphabet.add(c);
			this.transitions.put(input, outList);
			return true;
		}
		
		public FiniteAutomata build() {
			if (this.initial == null) {
				return null;
			}
			return new FiniteAutomata(this);
		}
	}
	
	
}

