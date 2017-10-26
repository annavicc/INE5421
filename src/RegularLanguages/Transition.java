package RegularLanguages;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Transition {
	
	private final State origin;
	private final char symbol;
	private final Set<State> next;
	
	private Transition(TransitionBuilder builder) {
		this.origin = builder.origin;
		this.symbol = builder.symbol;
		this.next = Collections.unmodifiableSet(builder.next);
	}
	
	public State getOrigin() {
		return this.origin;
	}
	
	public char getSymbol() {
		return this.symbol;
	}
	
	public Set<State> getNext() {
		return (TreeSet<State>) this.next;
	}
	
	
	public static class TransitionBuilder {

		private final State origin;
		private final char symbol;
		private TreeSet<State> next;
		
		public TransitionBuilder(State origin, char symbol) {
			this.origin = origin;
			this.symbol = symbol;
			this.next = new TreeSet<State>();
		}
		
		public TransitionBuilder addNext(State next) {
			this.next.add(next);
			return this;
		}
		
		public Transition build() {
			return new Transition(this);
		}
		
	}
}
