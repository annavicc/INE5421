package RegularLanguages;

public class RegularExpression extends RegularLanguage {


	public RegularExpression(String inp) {
		super(inp, InputType.RE);
	}

	public static boolean isValidRE(String inp) {
		return true;
	}
	
	/*
	 * Get string representation
	 */
	public String toString() {
		return this.getInput();
	}
	
	/*
	 * Convert RE to RG
	 */
	public RegularGrammar getRG() {
		return this.getAF().getRG();
	}
	
	/*
	 * Convert RE to AF
	 * TODO implement
	 */
	public FiniteAutomata getAF() {
		return new FiniteAutomata();
	}
	
	/*
	 * Get RE
	 */
	public RegularExpression getRE() {
		return this;
	}
	
	
}
