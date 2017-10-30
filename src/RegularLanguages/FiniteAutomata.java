package RegularLanguages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bethecoder.ascii_table.ASCIITable;

import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidBuilderException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidSymbolException;


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
	 * FiniteAutomata must be created by a FABuilder
	 * @param builder
	 */
	private FiniteAutomata(FABuilder builder) {
		super(InputType.AF);
		
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
	 * Get AF initial state
	 * @return initial state
	 */
	public State getInitial() {
		return this.initial;
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
	 * Check if FA is deterministic 
	 * @return boolean whether it is deterministic
	 */
	public boolean isDeterministic() {
		// Check if any transition output has size greater than 1
		return this.transitions.values().stream().noneMatch(ts -> ts.size() > 1);
	}
	
	/**
	 * Determinizes NDFA to DFA
	 * @return Determinized FA version
	 */
	public FiniteAutomata determinize() {
		Map<SortedSet<State>, State> statesMap = new HashMap<SortedSet<State>, State>();
		List<SortedSet<State>> incompleteStates = new ArrayList<SortedSet<State>>();

		try {
			FABuilder builder = new FABuilder();
			SortedSet<State> initial = new TreeSet<State>();
			initial.add(this.initial);
			
			statesMap.put(initial, builder.newState());
			builder.setInitial(statesMap.get(initial));
			incompleteStates.add(Collections.unmodifiableSortedSet(initial));
		
			while (!incompleteStates.isEmpty()) {
				SortedSet<State> currentSet = incompleteStates.get(0);
				State currentState = statesMap.get(currentSet);
				
				// if currentSet contains some final state 
				if (currentSet.stream().anyMatch(st -> this.finals.contains(st))) {
					builder.setFinal(currentState);
				}
				
				for (char c : this.alphabet) {
					// Get states reachable from currentSet through c
					SortedSet<State> outputSet = new TreeSet<State>();
					for (State st : currentSet) {
						outputSet.addAll(this.transition(st, c));
					}
					
					if (!outputSet.isEmpty()) {
						// Get or create new state equivalent to the output set
						State outputState = statesMap.get(outputSet);
						if (outputState == null) {
							outputState = builder.newState();
							statesMap.put(outputSet, outputState);
							incompleteStates.add(outputSet);
						}
											
						builder.addTransition(currentState, c, outputState);
					}
					
				}
				incompleteStates.remove(0);
			}
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Remove states from AF
	 * Obs. initial state is never removed 
	 * @param states Set of states to be removed
	 * @return copy of this AF without states received
	 * @throws InvalidStateException 
	 */
	public FiniteAutomata removeStates(Set<State> rmStates) throws InvalidStateException {
		if (rmStates.stream().anyMatch(st -> st.owner != this.uuid)) {
			throw new InvalidStateException("removeStates");
		}
		
		FABuilder builder = new FABuilder();
		
		try {
			// Copy initial
			builder.importState(this.initial)
					.setInitial(this.initial);
			
			// Copy states not removed
			this.states.stream()
					.filter(st -> !rmStates.contains(st))
					.forEach(st -> builder.importState(st));

			// Copy final states not removed
			this.finals.stream()
					.filter(st -> !rmStates.contains(st))
					.forEach(st -> {
						try {
							builder.setFinal(st);
						} catch (InvalidStateException e) {
							e.printStackTrace();
						}
					});
			
			// Copy transitions from not removed input states
			this.transitions.entrySet().stream()
					.filter(e -> !rmStates.contains(e.getKey().getState()))
					.forEach(e -> {
						final TransitionInput in = e.getKey();
						
						// Copy transitions to not removed output states
						e.getValue().stream()
								.filter(out -> !rmStates.contains(out))
								.forEach(out -> {
									try {
										builder.addTransition(in.getState(), in.getSymbol(), out);
									} catch (InvalidStateException | InvalidSymbolException e1) {
										e1.printStackTrace();
									}
								});
					});;
			
			return builder.build();
			
		} catch (InvalidStateException | IncompleteAutomataException | InvalidBuilderException e1) {
			e1.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Get unreachable states
	 * @return Set of unreachable states
	 */
	public Set<State> getUnreachableStates() {
		Set<State> reachable = new HashSet<State>();
		reachable.add(this.initial);

		int sizeLastIteration = 0;
				
		while (reachable.size() != sizeLastIteration) {
			sizeLastIteration = reachable.size();
			
			// Find states with transitions from reachable
			this.transitions.entrySet().stream()
					.filter(e -> reachable.contains(e.getKey().getState()))
					.forEach(e -> e.getValue().stream()
							.filter(st -> !reachable.contains(st))
							.forEach(st -> reachable.add(st)));
			
		}
			
		// Return not reachable
		return this.states.stream()
				.filter(st -> !reachable.contains(st))
				.collect(Collectors.toSet());
		
	}

	/**
	 * Get dead states
	 * @return Set of dead states
	 */
	public Set<State> getDeadStates() {
		Set<State> alive = new HashSet<State>();
		int sizeLastIteration = 0;
		
		alive.addAll(this.finals);
		
		while (alive.size() != sizeLastIteration) {
			sizeLastIteration = alive.size();
			
			// Find states with transitions to alive states
			this.states.stream()
					.filter(st -> !alive.contains(st))
					.forEach(in -> {
						boolean isAlive = this.alphabet.stream()
							.anyMatch(c -> this.transition(in, c).stream()
									.anyMatch(out -> alive.contains(out)));
						
						if (isAlive) {
							alive.add(in);
						}
					});
		}

		// Return not alive
		return this.states.stream()
				.filter(st -> !alive.contains(st))
				.collect(Collectors.toSet());
	}
	
	/**
	 * Minimizes FA
	 * @return Minimized FA version
	 */
	public FiniteAutomata minimize() {
		// Determinize
		if (!this.isDeterministic()) {
			return this.determinize().minimize();
		}
		// Remove unreachable states
		Set<State> unreachable = this.getUnreachableStates();
		if(!unreachable.isEmpty()) {
			try {
				return this.removeStates(unreachable).minimize();
			} catch (InvalidStateException e) {
				e.printStackTrace();
			}
		}
		// Remove dead states
		Set<State> dead = this.getDeadStates();
		if (!dead.isEmpty()) {
			if (!(dead.size() == 1 && dead.contains(this.initial))) {
				try {
					return this.removeStates(dead).minimize();
				} catch (InvalidStateException e) {
					e.printStackTrace();
				}
			}
		}
				
		ArrayList<SortedSet<State>> eqClasses = new ArrayList<SortedSet<State>>();
		Map<State, Integer> stateMap = new HashMap<State, Integer>();
		
		State phi = new State(this.uuid, -2);
		Map<State, Integer> newStateMap = new HashMap<State, Integer>();
		ArrayList<SortedSet<State>> newEqClasses = new ArrayList<SortedSet<State>>();
		final ArrayList<SortedSet<State>> initEqClasses = newEqClasses;
		
		// Initialize first two classes (0: non-finals, 1: finals)
		newEqClasses.add(0, new TreeSet<State>());
		if (!this.finals.isEmpty()) {
			newEqClasses.add(1, new TreeSet<State>());
		}
		this.states.stream().forEach(st -> {
			int eqClass = 0;
			if (this.finals.contains(st) ) {
				eqClass = 1;
			}
			newStateMap.put(st, eqClass);
			initEqClasses.get(eqClass).add(st);
		});
		newStateMap.put(phi, 0);
		newEqClasses.get(0).add(phi);
		
		// Function to get output class map from state
		final Function<State, Map<Character, Integer>> getClassMap = st -> alphabet.stream()
				.collect(Collectors.toMap(
						c -> c,
						c -> {
							SortedSet<State> out = this.transition(st, c);
							if (out.isEmpty()) {
								return stateMap.get(phi);
							}
							return stateMap.get(out.first());
						}));
		
		
		// Subdivide classes
		while (!eqClasses.equals(newEqClasses)) {
			eqClasses = new ArrayList<SortedSet<State>>();
			for (SortedSet<State> eqClass : newEqClasses) {
				eqClasses.add(new TreeSet<State>(eqClass));
			}
			newStateMap.entrySet().stream().forEach(e -> stateMap.put(e.getKey(), e.getValue()));
			
			// For each class
			for (SortedSet<State> eqClass : eqClasses) {
				State first = eqClass.first();
				
				Map<Character, Integer> firstOutputClass = getClassMap.apply(first);
				
				// For each state in current class
				for (State st : eqClass) {
					
					if (!st.equals(first)) {
						Map<Character, Integer> stOutputClass = getClassMap.apply(st);
						
						// If current state output classes differs from first
						if (!stOutputClass.equals(firstOutputClass)) {
							
							// Check if it belongs to another class
							int newClass = -1;
							for (SortedSet<State> eqClass2 : newEqClasses) {
								State st2 = eqClass2.first();
								Map<Character, Integer> st2OutputClass = getClassMap.apply(st2);
								if (stOutputClass.equals(st2OutputClass)) {
									newClass = newStateMap.get(st2);
								}
							}
					
							// Create new class if needed
							if (newClass == -1) {
								newClass = newEqClasses.size();
								newEqClasses.add(new TreeSet<State>());
							}
							
							// Change state to new class
							int oldClass = stateMap.get(st);
							newEqClasses.get(oldClass).remove(st);
							newEqClasses.get(newClass).add(st);
							newStateMap.put(st, newClass);
												}
					}
				}
			}
		}

		
		eqClasses = newEqClasses;
		final ArrayList<SortedSet<State>> endEqClasses = eqClasses;
		
		// Build minimized FA version from eqClasses 
		try {
			FABuilder builder = new FABuilder();
			// Import one state per eqClass, except phi
			for (SortedSet<State> eqClass : endEqClasses) {
				State first = eqClass.first();
				// If class doesn't contains only phi
				if (!(first.equals(phi) && eqClasses.size() > 1)) {
					builder.importState(first);
					if (eqClass.contains(this.initial)) {
						builder.setInitial(first);
					}
					if (this.finals.contains(first)) {
						builder.setFinal(first);
					}
				}
			}

			// Map transitions to new FA 
			this.transitions.entrySet().stream().forEach(e -> {
				int inClass = stateMap.get(e.getKey().getState());
				State inSt = endEqClasses.get(inClass).first();
				int outClass = stateMap.get(e.getValue().first());
				State outSt = endEqClasses.get(outClass).first();
				
				try {
					builder.addTransition(inSt, e.getKey().getSymbol(), outSt);
				} catch (InvalidStateException | InvalidSymbolException e1) {
					e1.printStackTrace();
				}
			});
			
			return builder.build();
		
		} catch (InvalidStateException | IncompleteAutomataException | InvalidBuilderException e) {
			e.printStackTrace();
			return null;
		}
		
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
		private final UUID owner;
		private final int index;
		
		// State instances comparator
		private static final Comparator<State> comparator = Comparator
				.comparingInt((State s) -> s.index)
				.thenComparing(s -> s.owner)
				.thenComparing(s -> s.uuid);
		
		/**
		 * Default constructor
		 * @param owner UUID of the owner FA
		 * @param index State index in current FA
		 */
		private State(UUID owner, int index) {
			this.uuid = UUID.randomUUID();
			this.index = index;
			this.owner = owner;
		}

		/**
		 * Copy constructor
		 * Create different copy based on another state 
		 * @param st Origin state
		 * @param owner UUID of the owner FA
		 * @param index new index in new FA
		 */
		private State(State st, UUID owner, int index) {
			this.uuid = st.uuid;
			this.index = index;
			this.owner = owner;
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
			return this.uuid.equals(s.uuid) && this.owner == s.owner;
		}
		
		/**
		 * Calculate hash
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.uuid, this.owner);
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
			State s0 = new State(this.uuid, -1);
			State s1 = new State(s0, this.uuid, states.size());
			states.put(s0, s1);
			return s1;
		}
		
		/**
		 * Import state from another FA
		 * @param st State to be imported
		 * @return this
		 */
		public FABuilder importState(State st) {
			State s0 = new State(st, this.uuid, -1);
			if (this.states.get(s0) == null) {
				State s1 = new State(s0, this.uuid, states.size());
				states.put(s0, s1);
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
			if (!Character.toString(c).matches("[0-9a-z&]")) {
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
			if (!st.owner.equals(this.uuid)) {
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

