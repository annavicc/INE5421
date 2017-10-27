package RegularLanguages;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.AnnotationHelper;

import com.bethecoder.ascii_table.ASCIITable;


/**
 * Immutable representation of a Finite Automata
 * Instance must be created using a FiniteAutomata.FABuilder instance
 *
 */
public class FiniteAutomata extends RegularLanguage {
	
	private final State initial;
	private final SortedSet<State> states;
	private final SortedSet<State> finals;
	private final SortedSet<Character> alphabet;
	private final Map<TransitionInput, SortedSet<State>> transitions;
	
	/**
	 * Private constructor
	 * FiniteAutomata must be created by a FABuilder
	 * @param builder
	 */
	private FiniteAutomata(FABuilder builder) {
		super(InputType.AF);
		
		this.initial = builder.initial;
		
		// Make Sets immutable 
		this.states = Collections.unmodifiableSortedSet(builder.states);
		this.finals = Collections.unmodifiableSortedSet(builder.finals);
		this.alphabet = Collections.unmodifiableSortedSet(builder.alphabet);

		// Make internal Sets immutable
		Map<TransitionInput, SortedSet<State>> temp;
		temp = builder.transitions.entrySet().stream()
				.collect(Collectors.toMap(
						e -> e.getKey(),
						e -> Collections.unmodifiableSortedSet(e.getValue())
				));
		
		// Make external map immutable 
		this.transitions = Collections.unmodifiableMap(temp);

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
	 * Get AF alphabet
	 * @return alphabet set
	 */
	public SortedSet<Character> getAlphabet() {
		return this.alphabet;
	}
	
	/**
	 * Get AF definition (transitions table)
	 * @return Transitions table formatted as String
	 */
	public String getDefinition() {
		int line, row;
		String[][] data = new String[this.states.size()][this.alphabet.size() + 1];
		String[] topRow = new String[this.alphabet.size() + 1];
		
		// Build top row (alphabet symbols)
		row = 0;
		topRow[row] = "\u03B4";  // small letter delta
		for (char c : this.alphabet) {
			row++;
			topRow[row] = Character.toString(c);
		}
		
		// Build transitions
		line = 0;
		for (State q : this.states) {
			// First row (state name)
			row = 0;
			String qStr = "";
			if (this.finals.contains(q)) {
				qStr += "*";
			}
			if (this.initial.equals(q)) {
				qStr += "->";
			}
			qStr += q.toString();
			data[line][row] = qStr;
			
			// Build transitions output
			for (char c : this.alphabet) {
				row++;
				SortedSet<State> outSet = this.transition(q, c);
				Stream<String> names = outSet.stream().map(
					state -> state.toString());
				data[line][row] = names.collect(Collectors.joining(", "));
			}
			
			line++;
		}
		
		return ASCIITable.getInstance().getTable(topRow, data).toString();

	}
	
	/**
	 * TODO IMPLEMENT
	 * @return Determinized FA version
	 */
	public FiniteAutomata determinize() {
		return null;
	}
	
	/**
	 * TODO IMPLEMENT
	 * @return Minimized FA version
	 */
	public FiniteAutomata minimize() {
		return null;
	}
	
	
	/**
	 * Convert AF to RG
	 * TODO implement
	 * @return
	 */
	public RegularGrammar getRG() {
		return new RegularGrammar("");
	}
	
	/**
	 * Get FA
	 * @return this
	 */
	public FiniteAutomata getAF() {
		return this;
	}
	
	/**
	 * Convert AF to RE
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
		private final int index;
		private final int copy;
		
		private static final Comparator<State> comparator = Comparator
				.comparingInt((State s) -> s.index)
				.thenComparing(s -> s.uuid)
				.thenComparing(s -> s.copy);
		
		/**
		 * Default constructor
		 * @param index State index in current FA
		 */
		private State(int index) {
			this.uuid = UUID.randomUUID();
			this.index = index;
			this.copy = 0;
		}

		/**
		 * Copy constructor
		 * Create different copy based on another state 
		 * @param st Origin state
		 * @param index new index in new FA
		 */
		private State(State st, int index) {
			this.uuid = st.uuid;
			this.index = index;
			this.copy = st.copy + 1;
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
			return this.uuid.equals(s.uuid) && this.copy == s.copy;
		}
		
		/**
		 * Calculate hash
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.uuid, copy);
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
		
		private boolean built;
		private State initial;
		private TreeSet<State> states;
		private TreeSet<State> finals;
		private TreeSet<Character> alphabet;
		private HashMap<TransitionInput, TreeSet<State>> transitions;
		
		/**
		 * Default constructor
		 */
		public FABuilder() {
			this.built = false;
			this.initial = null;
			this.states = new TreeSet<State>();
			this.finals = new TreeSet<State>();
			this.alphabet = new TreeSet<Character>();
			this.transitions = new HashMap<TransitionInput, TreeSet<State>>();
		}
		
		/**
		 * Create new state in the FA being generated
		 * @return New state
		 */
		public State newState() {
			State s = new State(states.size());
			states.add(s);
			return s;
		}
		
		/**
		 * Set state as initial
		 * @param s state
		 * @return this
		 * @throws InvalidStateException
		 */
		public FABuilder setInitial(State s) throws InvalidStateException {
			if (!states.contains(s)) {
				throw new InvalidStateException("FABuilder.setInitial");
			}
			
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
			if (!states.contains(s)) {
				throw new InvalidStateException("FABuilder.setFinal");
			}
			
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
			if (!this.states.contains(in) || !this.states.contains(out)) {
				throw new InvalidStateException("FABuilder.addTransition");
			}
			if (!Character.toString(c).matches("[a-z&]")) {
				throw new InvalidSymbolException("FABuilder.addTransition");
			}
			
			TransitionInput tInput = new TransitionInput(in, c);
			TreeSet<State> tOutput = this.transitions.get(tInput);
			
			if (tOutput == null) {
				tOutput = new TreeSet<State>();
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
		
		public static class InvalidBuilderException extends Exception {
			public InvalidBuilderException() {
		        super("FABuilder.build: Builder already used!");
		    }
		}
		
	}
	
	
}

