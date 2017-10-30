package RegularLanguages;

/**
 * Representation of a Regular Grammar
 *
 */
public abstract class RegularLanguage {

	// RL can be represented by a Regular Expression, a Regular Grammar or a Finite Automata
	public enum InputType {RE, RG, AF, UNDEFINED};

	// Operations allowed in a RL
	public enum Operation {UNION, CONCATENATION, INTERSECTION, DIFFERENCE};
	
	protected String input; // The input entered by the user
	private String id; // an unique ID for the RL
	private InputType type = InputType.UNDEFINED; // the type that the RL is represented
	
	
	/**
	 * Public constructor
	 * @param type the type of the RL
	 */
	public RegularLanguage(InputType type) {
		this.type = type;
	}
	
	/**
	 * Public constructor
	 * @param input the input entered by the user
	 * @param type the type of the RL
	 */
	public RegularLanguage(String input, InputType type) {
		this.input = input;
		this.type = type;
	}
	
	/**
	 * Verify the type of the RL and
	 * check if the input is valid
	 * @param inp the input entered by the user
	 * @return a RegularLanguage object if valid, null if not valid
	 */
	public static RegularLanguage validate(String inp) {
		if (inp.contains("->")) {
			return (RegularLanguage)RegularGrammar.isValidRG(inp);
		} else {
			return (RegularLanguage)RegularExpression.isValidRE(inp);
		}
	}
	
	/**
	 * Return the unique ID that identifies
	 * the Regular Language
	 * @return the RL ID
	 */
	public String getId() {
		return this.id;
	}
	
	
	/**
	 * Return the input entered by the user
	 * @return String input
	 */
	public String getInput() {
		return this.input;
	}
	
	/**
	 * Set the RL ID to a given value
	 * @param id the new String id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get the type representation
	 * that represents this RL
	 * @return RE if Regular Expression, RG if Regular Grammar or undefined
	 */
	public InputType getType() {
		return this.type;
	}
	
	
	/**
	 * Verify if a RL is finite
	 * @return true if finite, false if infinite
	 */
	public boolean isFinite() {
		return true;
	}
	
	/** Verify if a RL is empty
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return true;
	}
	
	/**
	 * Verify is a Regular Language is equivalent to another
	 * @param l1 the first RL
	 * @param l2 the second RL
	 * @return true if they are equivalent, false if not
	 */
	public boolean isEqualTo(RegularLanguage l1, RegularLanguage l2) {
		return true;
	}
	
	/**
	 * Verify if a Regular Language is contained in another
	 * @param l1 the first RL
	 * @param l2 the second RL
	 * @return true if l1 is contained in l2, false if not
	 */
	public boolean isContainedIn(RegularLanguage l1, RegularLanguage l2) {
		return true;
	}
	
	/**
	 * Get the string representation of the RL
	 * according to it's type
	 * @return the string representation of the RL
	 */
	public String toString() {
		return this.id;
	}
	
	/**
	 * Return the String representation of the RL
	 * in the format according to its type
	 * @return the RL String representation
	 */
	public abstract String getDefinition();
	
	
	public abstract RegularGrammar getRG();
	public abstract RegularExpression getRE();
	public abstract FiniteAutomata getAF();
	
	
}
