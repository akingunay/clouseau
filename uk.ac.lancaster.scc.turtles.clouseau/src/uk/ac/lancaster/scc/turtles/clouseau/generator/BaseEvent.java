package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a base event in an expression. This is
 * an immutable class.
 * 
 * @author Akin Gunay
 *
 */
class BaseEvent implements Expression {

	private final String eventName;
	
	BaseEvent(final String eventName) {
		this.eventName = eventName;
	}
	
	String getEventName() {
		return eventName;
	}
	
	@Override
	public Set<EventConfiguration> getSatisfyingEventConfigurations() {
		Set<String> eventNameInSet = new HashSet<>();
		eventNameInSet.add(eventName);
		Set<EventConfiguration> satisfyingEventConfigurations = new HashSet<>();
		satisfyingEventConfigurations.add(new EventConfiguration(eventNameInSet, new HashSet<>()));
		return satisfyingEventConfigurations;
	}
	
	@Override
	public String toString() {
		return eventName;
	}
}
