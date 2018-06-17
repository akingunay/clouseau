package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Conjunction {

	private final Set<String> eventNames;
	
	Conjunction(Set<String> eventNames) {
		this.eventNames = new HashSet<>(eventNames);
	}
	
	Set<String> getEventNames() {
		return Collections.unmodifiableSet(eventNames);
	}
}
