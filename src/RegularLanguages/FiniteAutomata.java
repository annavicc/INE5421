package RegularLanguages;

import java.util.ArrayList;

public class FiniteAutomata extends RegularLanguage {
	private String initial;
	private ArrayList<String> states;
	private ArrayList<String> finals;
	private ArrayList<String> alphabet;
	private ArrayList<Transition> transitions;
	

	public FiniteAutomata() {}
	
	public FiniteAutomata(String initial, String[] states, String[] finals, String[] alphabet, Transition[] transitions) {
		this.initial = initial;
		this.states = new ArrayList<String>();
		this.finals = new ArrayList<String>();
		this.alphabet = new ArrayList<String>();
		this.transitions = new ArrayList<Transition>();
		
		for (String s : states) { this.states.add(s); }
		for (String f : finals) { this.finals.add(f); }
		for (String l : alphabet) { this.alphabet.add(l); }
		for (Transition t : transitions) { this.transitions.add(t); }
	}
	
	public FiniteAutomata determinize() {
		return null;
	}
	
	public FiniteAutomata minimize() {
		return null;
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