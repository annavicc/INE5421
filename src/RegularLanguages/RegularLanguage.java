package RegularLanguages;

public abstract class RegularLanguage {

	public enum InputType {RE, RG, UNDEFINED};
	public enum Operation {UNION, CONCATENATION, INTERSECTION, DIFFERENCE};
	
	private String input;
	private String id;
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
		return null;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getDefinition() {
		return this.input;
	}
	
	public String getInput() {
		return this.input;
	}
	
	public void setId(String id) {
		this.id = id;
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
	
	public String toString() {
		return this.id;
	}
	
//	public abstract GetDefinition();
	
	public abstract RegularGrammar getRG();
	public abstract RegularExpression getRE();
	public abstract FiniteAutomata getAF();
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
