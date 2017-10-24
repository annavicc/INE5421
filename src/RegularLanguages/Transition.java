package RegularLanguages;

import java.util.ArrayList;

public class Transition {
	private String state;
	private String value;
	private ArrayList<String> next;
	
	public Transition(String state, String value, String[] next) {
		this.state = state;
		this.value = value;
		this.next = new ArrayList<String>();
		for (String n : next) {
			this.next.add(n);
		}
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public ArrayList<String> getNext() {
		return this.next;
	}
}
