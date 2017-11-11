package RegularLanguages;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidBuilderException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidSymbolException;
import RegularLanguages.Operators.FAOperator;


/**
 * Immutable representation of a Finite Automata
 * Instance must be created using a FiniteAutomata.FABuilder instance
 *
 */
public class FiniteAutomata extends RegularLanguage {
	
	private final UUID uuid;
	private final State initial;
	private final SortedSet<State> states;
	private final SortedSet<State> finals;
	private final SortedSet<Character> alphabet;
	private final Map<TransitionInput, SortedSet<State>> transitions;
	
	/**
	 * Private constructor
	 * New FiniteAutomata must be created by a FABuilder
	 * @param builder
	 */
	private FiniteAutomata(FABuilder builder) {
		super(InputType.FA);
		
		this.initial = builder.initial;
		this.uuid = builder.uuid;
		
		// Make sets unmodifiable
		this.states = Collections.unmodifiableSortedSet(new TreeSet<State>(builder.states.values()));
		this.finals = Collections.unmodifiableSortedSet(new TreeSet<State>(builder.finals));
		this.alphabet = Collections.unmodifiableSortedSet(new TreeSet<Character>(builder.alphabet));

		// Make internal Sets immutable
		Map<TransitionInput, SortedSet<State>> temp;
		temp = builder.transitions.entrySet().stream()
				.collect(Collectors.toMap(
						e -> e.getKey(),
						e -> Collections.unmodifiableSortedSet(new TreeSet<State>(e.getValue()))
				));
		
		// Make external map immutable 
		this.transitions = Collections.unmodifiableMap(temp);

	}
	
	/**
	 * Clone FA
	 * @return cloned FA
	 */
	public FiniteAutomata clone() {
		FABuilder builder = new FABuilder();
		
		try {
			for (State st : this.states) {
				builder.importState(st);
			}
			for (State st : this.finals) {
				builder.setFinal(st);
			}
			builder.setInitial(this.initial);
			
			for (TransitionInput in : this.transitions.keySet()) {
				for (State out : this.transitions.get(in)) {
					builder.addTransition(in.getState(), in.getSymbol(), out);
				}
			}

			return builder.build();
		} catch (IncompleteAutomataException | InvalidBuilderException | InvalidStateException | InvalidSymbolException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get AF transitions by input
	 * @param in Input State
	 * @param c Input symbol
	 * @return Output set of states
	 */
	public SortedSet<State> transition(State in, char c) {
		SortedSet<State> out = this.transitions.get(new TransitionInput(in, c));
		if (out == null) {
			return Collections.unmodifiableSortedSet(new TreeSet<State>());
		}
		return out;
	}
	
	/**
	 * Get AF unique id
	 * @return UUID
	 */
	public UUID getUUID() {
		return this.uuid;
	}
	
	/**
	 * Get AF alphabet
	 * @return alphabet set
	 */
	public SortedSet<Character> getAlphabet() {
		return this.alphabet;
	}
	
	/**
	 * Get AF initial state
	 * @return initial state
	 */
	public State getInitial() {
		return this.initial;
	}
	
	/**
	 * Get AF states
	 * @return set of states
	 */
	public SortedSet<State> getStates() {
		return this.states;
	}
	
	/**
	 * Get AF transitions
	 * @return map of transition input to outpud
	 */
	public Map<TransitionInput, SortedSet<State>> getTransitions() {
		return this.transitions;
	}
	
	/**
	 * Get AF final states
	 * @return set of final states
	 */
	public SortedSet<State> getFinals() {
		return this.finals;
	}
	
	/**
	 * Get AF definition (transitions table)
	 * @return Transitions table formatted as String
	 */
	public String getDefinition() {
		return FAOperator.getTransitionsTable(this);
	}
	
	/**
	 * Convert FA to RG
	 * @return equivalent grammar
	 */
	public RegularGrammar getRG() {
		return FAOperator.FAtoRG(this);
	}
	
	/**
	 * Get FA
	 * @return this
	 */
	public FiniteAutomata getFA() {
		return this;
	}
	
	/**
	 * Convert FA to RE
	 * Out of scope
	 * @return null
	 */
	public RegularExpression getRE() {
		return null;
	}

	
	
	/**
	 * AF State representation
	 * 
	 */
	public static class State implements Comparable<State> {

		private final UUID uuid;
		private final Stack<UUID> owners;
		private final int index;
		
		// State instances comparator
		private static final Comparator<State> comparator = Comparator
				.comparingInt((State s) -> s.index)
				.thenComparing(s -> s.owners.peek())
				.thenComparing(s -> s.uuid);
		
		/**
		 * Default constructor
		 * @param owner UUID of the owner FA
		 * @param index State index in current FA
		 */
		public State(UUID owner, int index) {
			this.uuid = UUID.randomUUID();
			this.index = index;
			this.owners = new Stack<UUID>();
			owners.push(owner);
		}

		/**
		 * Copy constructor
		 * Create different copy based on another state 
		 * @param st Origin state
		 * @param owner UUID of the owner FA
		 * @param index new index in new FA
		 */
		@SuppressWarnings("unchecked")
		private State(State st, UUID newOwner, int index) {
			this.uuid = st.uuid;
			this.index = index;
			this.owners = (Stack<UUID>) st.owners.clone();
			owners.push(newOwner);
		}	
		
		/**
		 * Get owner FA UUID
		 */
		public UUID getOwner() {
			return this.owners.peek();
		}
		
		/**
		 * Get index-based String representation (qi)
		 */
		@Override
		public String toString() {
			return "q" + index;
		}
		/**
		 * Check for equality
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !State.class.isAssignableFrom(obj.getClass())) {
		        return false;
		    }
		    final State s = (State) obj;
		    
		    return this.uuid.equals(s.uuid) && this.owners.equals(s.owners);
		}
		
		/**
		 * Calculate hash
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.uuid, this.owners);
		}

		/**
		 * Compare for sorting
		 */
		@Override
		public int compareTo(State st) {
			return comparator.compare(this, st);
		}
		
	}
	
	
	
	/**
	 * AF Transition input representation
	 */
	public static class TransitionInput {
		State s;
		char c;
		
		/**
		 * Constructor
		 * @param s Input state
		 * @param c Input symbol
		 */
		private TransitionInput(State s, char c) {
			this.s = s;
			this.c = c;
		}
		
		/**
		 * Get input state
		 * @return state
		 */
		public State getState() {
			return this.s;
		}
		
		/**
		 * Get input symbol
		 * @return symbol
		 */
		public char getSymbol() {
			return this.c;
		}
		
		/**
		 * Check for equality
		 */
		@Override
		public boolean equals(Object obj) {
		    if (obj == null || !TransitionInput.class.isAssignableFrom(obj.getClass())) {
		        return false;
		    }
		    final TransitionInput ti = (TransitionInput) obj;
			return this.s.equals(ti.s) && this.c == ti.c;
		}
		
		/**
		 * Calculate hash
		 */
		@Override
		public int hashCode() {
			return Objects.hash(s, c);
		}
		
	}
	
	
	
	/**
	 * FA Builder class 
	 * Contains the methods required to create a new FiniteAutomata instance
	 */
	public static class FABuilder {
		
		private final UUID uuid;
		private boolean built;
		private State initial;
		private Map<State, State> states;
		private Set<State> finals;
		private Set<Character> alphabet;
		private HashMap<TransitionInput, Set<State>> transitions;
		
		/**
		 * Default constructor
		 */
		public FABuilder() {
			this.uuid = UUID.randomUUID();
			this.built = false;
			this.initial = null;
			this.states = new HashMap<State, State>();
			this.finals = new HashSet<State>();
			this.alphabet = new HashSet<Character>();
			this.transitions = new HashMap<TransitionInput, Set<State>>();
		}
		
		/**
		 * Create new state in the FA being generated
		 * @return New state
		 */
		public State newState() {
			State st = new State(this.uuid, states.size());
			states.put(st, st);
			return st;
		}
		
		/**
		 * Import state from another FA
		 * @param st State to be imported
		 * @return this
		 */
		public FABuilder importState(State st) {
			State imported = new State(st, this.uuid, states.size());
			if (this.states.get(imported) == null) {
				states.put(imported, imported);
			}
			return this;
		}
		
		/**
		 * Set state as initial
		 * @param s state
		 * @return this
		 * @throws InvalidStateException
		 */
		public FABuilder setInitial(State s) throws InvalidStateException {
			s = this.validateState(s);
			
			initial = s;
			return this;
		}
		
		/**
		 * Set state as final
		 * @param s state
		 * @return this
		 * @throws InvalidStateException
		 */
		public FABuilder setFinal(State s) throws InvalidStateException{
			s = this.validateState(s);
			
			finals.add(s);
			return this;
		}
		
		/**
		 * Add new transition in the FA being generated
		 * @param in Input state
		 * @param c Input symbol
		 * @param out Output state
		 * @return this
		 * @throws InvalidStateException
		 * @throws InvalidSymbolException
		 */
		public FABuilder addTransition(State in, char c, State out) throws InvalidStateException, InvalidSymbolException {
			in = this.validateState(in);
			out = this.validateState(out);
			if (!Character.toString(c).matches("[0-9a-z]")) {
				throw new InvalidSymbolException("FABuilder.addTransition");
			}
			
			TransitionInput tInput = new TransitionInput(in, c);
			Set<State> tOutput = this.transitions.get(tInput);
			
			if (tOutput == null) {
				tOutput = new HashSet<State>();
			}
			
			tOutput.add(out);
			this.alphabet.add(c);
			this.transitions.put(tInput, tOutput);
			return this;
		}
		
		/**
		 * Finish FA construction
		 * @return new Finite Automata
		 * @throws IncompleteAutomataException
		 * @throws InvalidBuilderException 
		 */
		public FiniteAutomata build() throws IncompleteAutomataException, InvalidBuilderException {
			if (this.initial == null) {
				throw new IncompleteAutomataException();
			}
			if (this.built) {
				throw new InvalidBuilderException();
			}
			
			this.built = true;
			return new FiniteAutomata(this);
		}
		
		/**
		 * Check if state belongs to this FA
		 * @param st State to be validate
		 * @return State representation in this FA
		 * @throws InvalidStateException 
		 */
		private State validateState(State st) throws InvalidStateException {
			if (!st.getOwner().equals(this.uuid)) {
				State imported = this.states.get(new State(st, this.uuid, -1));
				if (imported == null) {
					String caller = Thread.currentThread().getStackTrace()[2].getMethodName();
					throw new InvalidStateException(caller); 
				}
				return imported;
			}
			return this.states.get(st);
		}
		
		public static class InvalidStateException extends Exception {
			// Auto-generated UID
			private static final long serialVersionUID = -4920068363089374569L;

			public InvalidStateException(String method) {
		        super(method + ": Received State does not belong to this FABuilder!");
		    }
		}
		
		public static class InvalidSymbolException extends Exception {
			// Auto-generated UID
			private static final long serialVersionUID = -5324376816337298591L;

			public InvalidSymbolException(String method) {
		        super(method + ": Received Symbol does not belong to this FABuilder!");
		    }
		}
		
		public static class IncompleteAutomataException extends Exception {
			// Auto-generated UID
			private static final long serialVersionUID = 4937858459366339539L;

			public IncompleteAutomataException() {
		        super("FABuilder.build: Initial State not set!");
		    }
		}
		
		public static class InvalidBuilderException extends Exception {
			// Auto-generated UID
			private static final long serialVersionUID = 7587522407031610014L;

			public InvalidBuilderException() {
		        super("FABuilder.build: Builder already used!");
		    }
		}
		
	}
		
}
