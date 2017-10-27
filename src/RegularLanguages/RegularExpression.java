package RegularLanguages;

import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;

public class RegularExpression extends RegularLanguage {


	public RegularExpression(String inp) {
		super(inp, InputType.RE);
	}

	public static boolean isValidRE(String inp) {
//		return true;
		return false;
	}
	
	public String getDefinition() {
		return super.input;
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
		FiniteAutomata.FABuilder builder = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = builder.newState();
		try {
			builder.setInitial(q0);
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Get RE
	 */
	public RegularExpression getRE() {
		return this;
	}
	
	
}
