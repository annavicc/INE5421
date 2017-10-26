package RegularLanguages;

import java.util.UUID;

public class State {

	private UUID uuid;
	
	public State() {
		this.uuid = UUID.randomUUID();
	}
	
	public boolean equals(State st) {
		return this.uuid.equals(st.uuid);
	}
	
	public int hashCode() {
		return this.uuid.hashCode();
	}
}