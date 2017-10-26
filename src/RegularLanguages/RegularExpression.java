package RegularLanguages;

import RegularLanguages.FiniteAutomata.FiniteAutomataBuilder;

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
		FiniteAutomataBuilder builder = new FiniteAutomataBuilder();
		FiniteAutomata.State q0 = builder.newState();
		builder.setInitial(q0);
		return builder.build();
	}
	
	/*
	 * Get RE
	 */
	public RegularExpression getRE() {
		return this;
	}
	
	
}
