package RegularLanguages;

public class RegularLanguage {

	private String input;
	private String id;
	public enum InputType {RE, RG, UNDEFINED};
	private InputType type = InputType.UNDEFINED;
	
	
	public RegularLanguage() {}
	
	public RegularLanguage(String input, InputType type) {
		this.input = input;
		this.type = type;
	}
	
	public static RegularLanguage validate(String inp) {
		if (inp.contains("->")) {
			if (RegularGrammar.isValidRG(inp)) {
				return new RegularGrammar(inp);
			}
		} else {
			if (RegularExpression.isValidRE(inp)) {
				return new RegularExpression(inp);
			}
		}
		return new RegularLanguage();
	}
	
	public String getId() {
//		return this.id;
		return "fasd";
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
//		return this.id;
		return "32132";
	}
	
	public InputType getType() {
		return this.type;
	}
	
	public boolean isFinite() {
		return true;
	}
	
	public boolean isEmpty() {
		return true;
	}
	
	public boolean isEqualTo(RegularLanguage l1, RegularLanguage l2) {
		return true;
	}
	
	public boolean isContainedIn(RegularLanguage l1, RegularLanguage l2) {
		return true;
	}
	
	/*
	 * Converts AF to RG
	public RegularGrammar AFToRG(FiniteAutomata fa) {
		
	}
	*/
	
	/*
	 * Converts RE to FA
	public FiniteAutomata REToFA(RegularExpression fa) {
		
	}
	*/
	
	/*
	 * Converts RG to FA
	public FiniteAutomata RGToFA(RegularGrammar fa) {
	}
	*/
	
	
}
