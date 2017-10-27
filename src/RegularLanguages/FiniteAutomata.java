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
	
	private FiniteAutomata(FABuilder builder) {
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
	
	public Set<State> transition(State in, char c) {
		Set<State> out = this.transitions.get(new TransitionInput(in, c));
		if (out == null) {
			return Collections.unmodifiableSet(new HashSet<State>());
		}
		return out;
	}
	
	public Set<Character> getAlphabet() {
		return this.alphabet;
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
		
		private TransitionInput(State s, char c) {
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
	 * FA Builder class 
	 *
	 */
	public static class FABuilder {
		
		private State initial;
		private HashSet<State> states;
		private HashSet<State> finals;
		private HashSet<Character> alphabet;
		private HashMap<TransitionInput, HashSet<State>> transitions;
		
		public FABuilder() {
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
		
		public FABuilder setInitial(State s) throws InvalidStateException {
			if (!states.contains(s)) {
				throw new InvalidStateException("FABuilder.setInitial");
			}
			
			initial = s;
			return this;
		}
		
		public FABuilder setFinal(State s) throws InvalidStateException{
			if (!states.contains(s)) {
				throw new InvalidStateException("FABuilder.setFinal");
			}
			
			finals.add(s);
			return this;
		}
		
		public FABuilder addTransition(State in, char c, State out) throws InvalidStateException, InvalidSymbolException {
			if (!this.states.contains(in) || !this.states.contains(out)) {
				throw new InvalidStateException("FABuilder.addTransition");
			}
			if (!Character.toString(c).matches("[a-z&]")) {
				throw new InvalidSymbolException("FABuilder.addTransition");
			}
			
			TransitionInput tInput = new TransitionInput(in, c);
			HashSet<State> tOutput = this.transitions.get(tInput);
			
			if (tOutput == null) {
				tOutput = new HashSet<State>();
			}
			
			tOutput.add(out);
			this.alphabet.add(c);
			this.transitions.put(tInput, tOutput);
			return this;
		}
		
		public FiniteAutomata build() throws IncompleteAutomataException {
			if (this.initial == null) {
				throw new IncompleteAutomataException();
			}
			return new FiniteAutomata(this);
		}
		
		public static class InvalidStateException extends Exception {
			public InvalidStateException(String method) {
		        super(method + ": Received State does not belong to this FABuilder!");
		    }
		}
		
		public static class InvalidSymbolException extends Exception {
			public InvalidSymbolException(String method) {
		        super(method + ": Received Symbol does not belong to this FABuilder!");
		    }
		}
		
		public static class IncompleteAutomataException extends Exception {
			public IncompleteAutomataException() {
		        super("FABuilder.build: Initial State not set!");
		    }
		}
		
	}
	
	
}

