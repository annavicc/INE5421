package RegularLanguages.Operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bethecoder.ascii_table.ASCIITable;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.FiniteAutomata.FABuilder;
import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidBuilderException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidSymbolException;
import RegularLanguages.FiniteAutomata.State;
import RegularLanguages.FiniteAutomata.TransitionInput;
import RegularLanguages.RegularGrammar;

/**
 * Implements operations over Finite Automatas
 */
public final class FAOperator {

	/**
	 * Determinizes NDFA to DFA
	 * @return Determinized FA version
	 */
	public static FiniteAutomata determinize(FiniteAutomata fa) {
		Map<SortedSet<State>, State> statesMap = new HashMap<SortedSet<State>, State>();
		List<SortedSet<State>> incompleteStates = new ArrayList<SortedSet<State>>();

		try {
			FABuilder builder = new FABuilder();
			
			// Create set equivalent to old initial state
			SortedSet<State> initial = new TreeSet<State>();
			initial.add(fa.getInitial());
			statesMap.put(initial, builder.newState());
			builder.setInitial(statesMap.get(initial));
			incompleteStates.add(Collections.unmodifiableSortedSet(initial));
		
			while (!incompleteStates.isEmpty()) {
				SortedSet<State> currentSet = incompleteStates.get(0);
				State currentState = statesMap.get(currentSet);
				
				// if currentSet contains some final state 
				if (currentSet.stream().anyMatch(st -> fa.getFinals().contains(st))) {
					builder.setFinal(currentState);
				}
				
				for (char c : fa.getAlphabet()) {
					// Get states reachable from currentSet through c
					SortedSet<State> outputSet = new TreeSet<State>();
					for (State st : currentSet) {
						outputSet.addAll(fa.transition(st, c));
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
	public static FiniteAutomata removeStates(FiniteAutomata fa, Set<State> rmStates) throws InvalidStateException {
		if (rmStates.stream().anyMatch(st -> st.getOwner() != fa.getUUID())) {
			throw new InvalidStateException("removeStates");
		}
		
		FABuilder builder = new FABuilder();
		
		try {
			// Copy initial
			builder.importState(fa.getInitial())
					.setInitial(fa.getInitial());
			
			// Copy states not removed
			fa.getStates().stream()
					.filter(st -> !rmStates.contains(st))
					.forEach(st -> builder.importState(st));

			// Copy final states not removed
			fa.getFinals().stream()
					.filter(st -> !rmStates.contains(st))
					.forEach(st -> {
						try {
							builder.setFinal(st);
						} catch (InvalidStateException e) {
							e.printStackTrace();
						}
					});
			
			// Copy transitions from not removed input states
			fa.getTransitions().entrySet().stream()
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
	public static Set<State> getUnreachableStates(FiniteAutomata fa) {
		Set<State> reachable = new HashSet<State>();
		reachable.add(fa.getInitial());

		int sizeLastIteration = 0;
				
		while (reachable.size() != sizeLastIteration) {
			sizeLastIteration = reachable.size();
			
			// Find states with transitions from reachable
			fa.getTransitions().entrySet().stream()
					.filter(e -> reachable.contains(e.getKey().getState()))
					.forEach(e -> e.getValue().stream()
							.filter(st -> !reachable.contains(st))
							.forEach(st -> reachable.add(st)));
			
		}
			
		// Return not reachable
		return fa.getStates().stream()
				.filter(st -> !reachable.contains(st))
				.collect(Collectors.toSet());
		
	}

	/**
	 * Get dead states
	 * @return Set of dead states
	 */
	public static Set<State> getDeadStates(FiniteAutomata fa) {
		Set<State> alive = new HashSet<State>();
		int sizeLastIteration = 0;
		
		alive.addAll(fa.getFinals());
		
		while (alive.size() != sizeLastIteration) {
			sizeLastIteration = alive.size();
			
			// Find states with transitions to alive states
			fa.getStates().stream()
					.filter(st -> !alive.contains(st))
					.forEach(in -> {
						boolean isAlive = fa.getAlphabet().stream()
							.anyMatch(c -> fa.transition(in, c).stream()
									.anyMatch(out -> alive.contains(out)));
						
						if (isAlive) {
							alive.add(in);
						}
					});
		}

		// Return not alive
		return fa.getStates().stream()
				.filter(st -> !alive.contains(st))
				.collect(Collectors.toSet());
	}
	
	/**
	 * Minimizes FA
	 * @return Minimized FA version
	 */
	public static FiniteAutomata minimize(FiniteAutomata fa) {
		// Determinize
		if (!isDeterministic(fa)) {
			return minimize(determinize(fa));
		}
		// Remove unreachable states
		Set<State> unreachable = getUnreachableStates(fa);
		if(!unreachable.isEmpty()) {
			try {
				return minimize(removeStates(fa, unreachable));
			} catch (InvalidStateException e) {
				e.printStackTrace();
			}
		}
		// Remove dead states
		Set<State> dead = getDeadStates(fa);
		if (!dead.isEmpty()) {
			if (!(dead.size() == 1 && dead.contains(fa.getInitial()))) {
				try {
					return minimize(removeStates(fa, dead));
				} catch (InvalidStateException e) {
					e.printStackTrace();
				}
			}
		}
				
		ArrayList<SortedSet<State>> eqClasses = new ArrayList<SortedSet<State>>();
		Map<State, Integer> stateMap = new HashMap<State, Integer>();
		
		State phi = new State(fa.getUUID(), -2);
		Map<State, Integer> newStateMap = new HashMap<State, Integer>();
		ArrayList<SortedSet<State>> newEqClasses = new ArrayList<SortedSet<State>>();
		final ArrayList<SortedSet<State>> initEqClasses = newEqClasses;
		
		// Initialize first two classes (0: non-finals, 1: finals)
		newEqClasses.add(0, new TreeSet<State>());
		if (!fa.getFinals().isEmpty()) {
			newEqClasses.add(1, new TreeSet<State>());
		}
		fa.getStates().stream().forEach(st -> {
			int eqClass = 0;
			if (fa.getFinals().contains(st) ) {
				eqClass = 1;
			}
			newStateMap.put(st, eqClass);
			initEqClasses.get(eqClass).add(st);
		});
		newStateMap.put(phi, 0);
		newEqClasses.get(0).add(phi);
		
		// Function to get output class map from state
		final Function<State, Map<Character, Integer>> getClassMap = st -> fa.getAlphabet().stream()
				.collect(Collectors.toMap(
						c -> c,
						c -> {
							SortedSet<State> out = fa.transition(st, c);
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
								if (stateMap.get(st).equals(stateMap.get(st2))) {
									Map<Character, Integer> st2OutputClass = getClassMap.apply(st2);
									if (stOutputClass.equals(st2OutputClass)) {
										// set equivalent class found as new
										newClass = newStateMap.get(st2);
									}
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
					if (eqClass.contains(fa.getInitial())) {
						builder.setInitial(first);
					}
					if (fa.getFinals().contains(first)) {
						builder.setFinal(first);
					}
				}
			}

			// Map transitions to new FA 
			fa.getTransitions().entrySet().stream().forEach(e -> {
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
	 * Get FA equivalent to the union of another two
	 * @param fa1 FA1
	 * @param fa2 FA2
	 * @return New FA = FA1 union FA2 
	 */
	public static FiniteAutomata union(FiniteAutomata fa1, FiniteAutomata fa2) {
		FABuilder builder = new FABuilder();
		if (fa1.getUUID().equals(fa2.getUUID())) {
			fa2 = fa2.clone();
		}
		
		try {
			// Create new initial state
			State initial = builder.newState();
			builder.setInitial(initial);
			if (fa1.getFinals().contains(fa1.getInitial()) || fa2.getFinals().contains(fa2.getInitial())) {
				builder.setFinal(initial);
			}

			// Import states from FA 1
			for (State st : fa1.getStates()) {
				builder.importState(st);
				if (fa1.getFinals().contains(st)) {
					builder.setFinal(st);
				}
			}

			// Import states from FA 2
			for (State st : fa2.getStates()) {
				builder.importState(st);
				if (fa2.getFinals().contains(st)) {
					builder.setFinal(st);
				}
			}
			
			// Import transitions from FA1
			for (TransitionInput in : fa1.getTransitions().keySet()) {
				for (State out : fa1.getTransitions().get(in)) {
					builder.addTransition(in.getState(), in.getSymbol(), out);
					if (in.getState().equals(fa1.getInitial())) {
						builder.addTransition(initial, in.getSymbol(), out);
					}
				}
			}
			
			// Import transitions from FA2
			for (TransitionInput in : fa2.getTransitions().keySet()) {
				for (State out : fa2.getTransitions().get(in)) {
					builder.addTransition(in.getState(), in.getSymbol(), out);
					if (in.getState().equals(fa2.getInitial())) {
						builder.addTransition(initial, in.getSymbol(), out);
					}
				}
			}
			
			return builder.build();

		
		} catch (InvalidStateException | InvalidSymbolException | IncompleteAutomataException | InvalidBuilderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get FA equivalent to the complement of another
	 * @param fa FA
	 * @param alphabet New alphabet symbols
	 * @return New FA = complement(FA) 
	 */
	public static FiniteAutomata complement(FiniteAutomata fa, Set<Character> alphabet) {
		if (!isDeterministic(fa)) {
			return complement(determinize(fa), alphabet);
		}
		
		FABuilder builder = new FABuilder();
		
		try {
			// Create new dead state
			State dead = builder.newState();
			
			
			for (State st : fa.getStates()) {
				builder.importState(st);
				// Invert terminal and non-terminal sets
				if (!fa.getFinals().contains(st)) {
					builder.setFinal(st);
				}
			}
			builder.setFinal(dead);
			builder.setInitial(fa.getInitial());
			
			Set<Character> newAlphabet = new HashSet<Character>(fa.getAlphabet());
			if (alphabet != null) {
				newAlphabet.addAll(alphabet);
			}
			for (char c : newAlphabet) {
				builder.addTransition(dead, c, dead);
				
				for (State in : fa.getStates()) {
					SortedSet<State> outSet = fa.transition(in, c);
					State out;
					if (outSet.isEmpty()) {
						// Map indefinitions to dead state
						out = dead;
					} else {
						// Copy transitions
						out = outSet.first();
					}
					
					builder.addTransition(in, c, out);
				}
			}
			
			return builder.build();
		
		} catch (InvalidStateException | InvalidSymbolException | IncompleteAutomataException | InvalidBuilderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get FA equivalent to the intersection of another two
	 * @param fa1 FA1
	 * @param fa2 FA2
	 * @return New FA = FA1 intersection FA2 
	 */
	public static FiniteAutomata intersection(FiniteAutomata fa1, FiniteAutomata fa2) {
		return intersectionSteps(fa1, fa2).get("I");
	}
	
	/**
	 * Get one FA for each step of the intersection of another two
	 * @param fa1 FA1
	 * @param fa2 FA2
	 * @return Map of FAs = 
	 * {"C1" : complement(FA1), "C2": complement(FA2), "U": union("C1", "C2"), "I": intersection("U")} 
	 */
	public static Map<String, FiniteAutomata> intersectionSteps(FiniteAutomata fa1, FiniteAutomata fa2) {
		
		FiniteAutomata fa1Compl = complement(fa1, fa2.getAlphabet());
		FiniteAutomata fa2Compl = complement(fa2, fa1.getAlphabet());
		FiniteAutomata union = union(fa1Compl, fa2Compl);
		FiniteAutomata intersec = complement(union, null);
		
		Map<String, FiniteAutomata> ret = new HashMap<String, FiniteAutomata>();
		ret.put("C1", fa1Compl);
		ret.put("C2", fa2Compl);
		ret.put("U", union);
		ret.put("I", intersec);
		
		return ret;
	}

	/**
	 * Check if the language accepted by FA1 is a subset of the language accepted by FA2 
	 * @param fa1 FA1
	 * @param fa2 FA2
	 * @return whether L(FA1) is a subset of L(FA2) 
	 */
	public static boolean isSubset(FiniteAutomata fa1, FiniteAutomata fa2) {
		return isEmptyLanguage(intersection(fa1, complement(fa2, fa1.getAlphabet())));
	}
	
	/**
	 * Check if the language accepted by FA1 is a subset of the language accepted by FA2 
	 * @param fa1 FA1
	 * @param fa2 FA2
	 * @return whether L(FA1) is a subset of L(FA2) 
	 */
	public static boolean isEquivalent(FiniteAutomata fa1, FiniteAutomata fa2) {
		return isSubset(fa1, fa2) && isSubset(fa2, fa1);
	}

	
	/**
	 * Check if the language accepted by an FA is empty
	 * @param fa FA to be verified
	 * @return boolean whether it is empty
	 */
	public static boolean isEmptyLanguage(FiniteAutomata fa) {
		return getDeadStates(fa).contains(fa.getInitial());
	}
	
	/**
	 * Check if the language accepted by an FA is infinite
	 * @param fa FA to be verified
	 * @return boolean whether it is infinite
	 */
	public static boolean isInfiniteLanguage(FiniteAutomata fa) {
		FiniteAutomata faMin = minimize(fa);
		return isCyclic(faMin, faMin.getInitial(), new HashSet<State>());
	}
	
	/**
	 * Recursive function to check if a FA has cycles
	 * Called to check a language finiteness 
	 * @param fa FA to be verified
	 * @param currentState state being verified
	 * @param visited States visited on the current path
	 * @return boolean whether it is infinite
	 */
	private static boolean isCyclic(FiniteAutomata fa, State currentState, Set<State> visited) {
		
		if (visited.contains(currentState)) {
			return true;
		} else {
			Set<State> newVisited = new HashSet<State>();
			newVisited.addAll(visited);
			newVisited.add(currentState);
			
			// Get set of states reachable through one transition:
			Set<State> next = fa.getTransitions().entrySet().stream()
					.filter(e -> e.getKey().getState().equals(currentState)) 
					.flatMap(e -> e.getValue().stream())
					.collect(Collectors.toSet());
			
			// Recursion through next states: 
			for (State st : next) {
				if (isCyclic(fa, st, newVisited)) {
					return true;
				}
			}
			return false; 
		}
	}
	
	/**
	 * Get FA formatted transitions table
	 * @param fa FA to be formatted
	 * @return Transitions table formatted as String
	 */
	public static String getTransitionsTable(FiniteAutomata fa) {
		int line, row;
		String[][] data = new String[fa.getStates().size()][fa.getAlphabet().size() + 1];
		String[] topRow = new String[fa.getAlphabet().size() + 1];
		
		// Build top row (alphabet symbols)
		row = 0;
		topRow[row] = "\u03B4";  // small letter delta
		for (char c : fa.getAlphabet()) {
			row++;
			topRow[row] = Character.toString(c);
		}
		
		// Build transitions
		line = 0;
		for (State q : fa.getStates()) {
			// First row (state name)
			row = 0;
			String qStr = "";
			if (fa.getFinals().contains(q)) {
				qStr += "*";
			}
			if (fa.getInitial().equals(q)) {
				qStr += "->";
			}
			qStr += q.toString();
			data[line][row] = qStr;
			
			// Build transitions output
			for (char c : fa.getAlphabet()) {
				row++;
				SortedSet<State> outSet = fa.transition(q, c);
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
	public static boolean isDeterministic(FiniteAutomata fa) {
		// Check if any transition output has size greater than 1
		return fa.getTransitions().values().stream().noneMatch(ts -> ts.size() > 1);
	}	
	
	/**
	 * Convert FA to RG
	 * @param fa FA
	 * @return regular grammar equivalent to FA 
	 */
	public static RegularGrammar FAtoRG(FiniteAutomata fa) {
		if (!isDeterministic(fa)) {
			return FAtoRG(determinize(fa));
		}
		
		char nextVn = 'A';
		
		// Map from state to new non-terminal symbol
		Map<State, Character> vnMap = new HashMap<State, Character>();
		for (State st : fa.getStates()) {
			vnMap.put(st, nextVn++);
		}
			
		String rgString = "";
		
		if (fa.getAlphabet().isEmpty()) {
			// Add infertile non terminal
			rgString = Character.toString(nextVn) + "-> a" + nextVn++;
		}
		
		for (State in : fa.getStates()) {
			for (char c : fa.getAlphabet()) {
				SortedSet<State> outSet = fa.transition(in, c);
				
				// if transition(B,a) = C 
				if (!outSet.isEmpty()) {
					State out = outSet.first();
					
					// add B -> aC
					if (fa.getInitial().equals(in)) {
						// s (first) = q0
						rgString = vnMap.get(in) + "->" + c + vnMap.get(out) + '\n' + rgString;
					} else {
						rgString += vnMap.get(in) + "->" + c + vnMap.get(out) + '\n';
					}
					
					// if C belongs to fa.finals
					if (fa.getFinals().contains(out)) {
						// add B -> a
						rgString += vnMap.get(in) + "->" + c + '\n';
					}
				}
			}
		}
		

		// Add & to language if needed
		if (fa.getFinals().contains(fa.getInitial())) {
			// Group productions from same vn
			String formatted = RegularGrammar.isValidRG(rgString).getDefinition();
			
			// Create new initial vn
			String initialProd = formatted.substring(1, formatted.indexOf('\n'));
			initialProd = nextVn + initialProd + "| & \n";
			
			rgString = initialProd + formatted;
		}
		
		return RegularGrammar.isValidRG(rgString);
	}
	
	/**
	 * Convert RG to FA
	 * @param rg Regular Grammar
	 * @return Equivalent Finite Automata
	 */
	public static FiniteAutomata RGtoFA(RegularGrammar rg) {
		try {
			FABuilder builder = new FABuilder();
			
			// Map from symbols to new AF state
			Map<Character, State> statesMap = rg.getVn().stream()
					.collect(Collectors.toMap(vn -> vn, vn -> builder.newState()));
			
			// Add new non terminal symbol
			char newSymbol = '@';
			statesMap.put(newSymbol, builder.newState());
			
			// Set initial symbol as initial state
			builder.setInitial(statesMap.get(rg.getInitial()));
			
			// Set finals state
			builder.setFinal(statesMap.get(newSymbol));
			if (rg.getProductions(rg.getInitial()).contains("&")) {
				builder.setFinal(statesMap.get(rg.getInitial()));
			}
			
			// Create transitions
			for (char vn : rg.getVn()) {
				for (String rule : rg.getProductions(vn)) {
					char vt = rule.charAt(0);
					if (vt != '&') {
						State input = statesMap.get(vn);
						State output;
						if (rule.length() == 1) {
							output = statesMap.get(newSymbol);
						}
						else {
							output = statesMap.get(rule.charAt(1));
						}
						builder.addTransition(input, vt, output);
					}
				}
			}
			
			return builder.build();
			
			
		} catch (InvalidStateException | InvalidSymbolException | IncompleteAutomataException | InvalidBuilderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
		
}
