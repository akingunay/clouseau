package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.List;

//TODO replace public constructors with static factory methods to ensure immutability when being extended

/**
 * Represents an immutable atomic event as an expression.
 * 
 * @author Akin Gunay
 *
 */
public class EventExpression implements Expression {

	private final String eventName;
	
	/**
	 * Creates a new EventExpression using the given event name. The given event name
	 * cannot be empty string (i.e., eventName.equals("") must return false).
	 * 
	 * @param eventName
	 */
	public EventExpression(final String eventName) {
		if (eventName == null) {
			throw new NullPointerException();
		}
		if (eventName.equals("")) {
			throw new IllegalArgumentException();
		}
		this.eventName = eventName;
	}
	
	/**
	 * Returns the name of the event.
	 * 
	 * @return event name
	 */
	public final String getEventName() {
		return eventName;
	}
	
	@Override
	public final List<Configuration> getSatisfyingConfigurations() {
		List<String> necessaryEvents = new ArrayList<>(1);
		necessaryEvents.add(eventName);
		List<Configuration> satisfyingConfigurations = new ArrayList<>(1);
		satisfyingConfigurations.add(new Configuration(necessaryEvents, new ArrayList<>(1)));
		return satisfyingConfigurations;
	}
	
	@Override
	public final List<String> getEventNames() {
		List<String> includedEventNames = new ArrayList<>(1);
		includedEventNames.add(eventName);
		return includedEventNames;
	}

	@Override
	public String toString() {
		return eventName;
	}
}
