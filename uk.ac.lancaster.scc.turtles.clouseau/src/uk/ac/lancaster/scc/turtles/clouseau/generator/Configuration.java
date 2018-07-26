package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// TODO replace public constructors with static factory methods to ensure immutability when being extended

/**
 * Represents a configuration of events. A configuration models the events
 * that occur during the execution of a single commitment. A configuration
 * is a composition of two distinct sets of events. The first set contains
 * the occurred events that are necessary for the creation, detachment, or
 * fulfilment of the commitment. The second set contains the occurred events
 * that cause exceptions.
 * 
 * Configuration is immutable.
 * 
 * @author Akin Gunay
 *
 */
public class Configuration {
	
	private final List<String> necessaryEvents;
	private final List<String> exceptionEvents;
	
	/**
	 * Creates an empty configuration.
	 */
	public Configuration() {
		this.necessaryEvents = new ArrayList<>(0);
		this.exceptionEvents = new ArrayList<>(0);
	}
	
	/**
	 * Creates the configuration using the given collections of occurred
	 * events. If an event is included by both of the given collections,
	 * the constructor throws an IllegalArgumentException. If any of the 
	 * given collections contains the same event multiple times, the event
	 * is added to the corresponding collection of the configuration only
	 * once.
	 * 
	 * @param 	necessaryEvents occurred events that are needed for the
	 * 			creation, detachment, or fulfilment
	 * @param 	exceptionEvents occurred events that cause to exceptions
	 * 
	 * @throws 	IllegalArgumentException
	 */
	public Configuration(final Collection<String> necessaryEvents, final Collection<String> exceptionEvents) {
		Set<String> necessaryEventHash = new HashSet<>(necessaryEvents);
		Set<String> exceptionEventHash = new HashSet<>(exceptionEvents);
		if (isOverlapping(necessaryEventHash, exceptionEventHash)) {
			throw new IllegalArgumentException("One or more events are included by both of the given collections.");
		}
		this.necessaryEvents = new ArrayList<>(necessaryEventHash);
		this.exceptionEvents = new ArrayList<>(exceptionEventHash);
	}
	
	/**
	 * Returns the necessary events as a set.
	 * 
	 * @return the set of necessary events
	 */
	public final Set<String> getNecessaryEvents() {
		return new HashSet<>(necessaryEvents);
	}

	/**
	 * Returns the exception events as a set.
	 * 
	 * @return the set of exception events
	 */
	public final Set<String> getExceptionEvents() {
		return new HashSet<>(exceptionEvents);
	}
	
	/**
	 * Creates a new configuration that is the extension of the original
	 * configuration with respect to the given collections of events.
	 * If an event is included by both of the given collections, or 
	 * given collections are inconsistent with the original configuration
	 * (e.g., there is an overlap between original necessary events and 
	 * given exception events) the method throws an IllegalArgumentException.
	 * If any of the given collections contain the same event multiple times,
	 * the event is included in the corresponding collection of the 
	 * extended configuration only once.
	 * 
	 * @param 	necessaryEvents necessary events for extension
	 * @param 	exceptionEvents occurred events for extension
	 * 
	 * @throws 	IllegalArgumentException
	 */
	public final Configuration extend(final Collection<String> necessaryEvents, Collection<String> exceptionEvents) {
		Set<String> originalNecessaryEventHash = getNecessaryEvents();
		Set<String> originalExceptionEventHash = getExceptionEvents();
		Set<String> givenNecessaryEventHash = new HashSet<>(necessaryEvents);
		Set<String> givenExceptionEventHash = new HashSet<>(exceptionEvents);
		if (isOverlapping(givenNecessaryEventHash, givenExceptionEventHash)) {
			throw new IllegalArgumentException("One or more events are included by both of the given collections.");
		}
		if (isOverlapping(originalNecessaryEventHash, givenExceptionEventHash)) {
			throw new IllegalArgumentException("One or more events are included both by the original necessary and given exception collections.");
		}
		if (isOverlapping(originalExceptionEventHash, givenNecessaryEventHash)) {
			throw new IllegalArgumentException("One or more events are included both by the original exception and given necessary collections.");
		}
		Configuration newConfiguration = new Configuration();
		newConfiguration.necessaryEvents.addAll(originalNecessaryEventHash);
		newConfiguration.necessaryEvents.addAll(givenNecessaryEventHash);
		newConfiguration.exceptionEvents.addAll(originalExceptionEventHash);
		newConfiguration.exceptionEvents.addAll(givenExceptionEventHash);
		return newConfiguration;
	}
	
	private boolean isOverlapping(final Set<String> first, final Set<String> second) {
		for (String str : first) {
			if (second.contains(str)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public final int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.necessaryEvents);
		hash = 97 * hash + Objects.hashCode(this.exceptionEvents);
		return hash;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Configuration other = (Configuration) obj;
		if (!Objects.equals(this.necessaryEvents, other.necessaryEvents)) {
			return false;
		}
		if (!Objects.equals(this.exceptionEvents, other.exceptionEvents)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("({");
		for (String necessaryEvent : necessaryEvents) {
			str.append(necessaryEvent).append(", ");
		}
		if (str.substring(str.length() - ", ".length(), str.length()).equals(", ")) {
			str.replace(str.length() - ", ".length(), str.length(), "").append("} : {");
		}
		for (String exceptionEvent : exceptionEvents) {
			str.append(exceptionEvent).append(", ");
		}
		if (str.substring(str.length() - ", ".length(), str.length()).equals(", ")) {
			str.replace(str.length() - ", ".length(), str.length(), "");
		}
		return str.append("})").toString();
	}
	
}
