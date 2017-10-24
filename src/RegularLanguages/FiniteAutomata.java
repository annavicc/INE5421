package RegularLanguages;

public class FiniteAutomata extends RegularLanguage {

	
	public FiniteAutomata determinize() {
		return null;
	}
	
	public FiniteAutomata minimize() {
		return null;
	}
	
	/*
	 * Get string representation
	 */
	public String toString() {
		return this.getInput();
	}
	
	/*
	 * Convert AF to RG
	 * TODO implement
	 */
	public RegularGrammar getRG() {
		return new RegularGrammar("");
	}
	
	/*
	 * Get AF
	 */
	public FiniteAutomata getAF() {
		return this;
	}
	
	/*
	 * Convert AF to RE
	 * Out of scope
	 */
	public RegularExpression getRE() {
		return null;
	}
	
}
