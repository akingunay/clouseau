package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class EventConfiguration {

	// TODO optimization: consider using lists instead if no client needs membership check
	private final Set<String> necessaryEvents;
	private final Set<String> exceptionEvents;
	
	// TODO optimization: use static sets to create empty configuration
	EventConfiguration() {
		this.necessaryEvents = new HashSet<>();
		this.exceptionEvents = new HashSet<>();
	}
	
	private EventConfiguration(final EventConfiguration copyConfiguration) {
		this.necessaryEvents = new HashSet<>(copyConfiguration.necessaryEvents);
		this.exceptionEvents = new HashSet<>(copyConfiguration.exceptionEvents);
	}
	
	EventConfiguration(final Set<String> necessaryEvents, Set<String> exceptionEvents) {
		this.necessaryEvents = new HashSet<>(necessaryEvents);
		this.exceptionEvents = new HashSet<>(exceptionEvents);
	}
	
	Set<String> getNecessaryEvents() {
		return Collections.unmodifiableSet(necessaryEvents);
	}
	
	Set<String> getExceptionEvents() {
		return Collections.unmodifiableSet(exceptionEvents);
	}
	
	EventConfiguration extend(final Set<String> necessaryEvents, Set<String> exceptionEvents) {
		EventConfiguration newEventConfiguration = new EventConfiguration(this);
		newEventConfiguration.necessaryEvents.addAll(necessaryEvents);
		newEventConfiguration.exceptionEvents.addAll(exceptionEvents);
		return newEventConfiguration;
	}
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.necessaryEvents);
		hash = 97 * hash + Objects.hashCode(this.exceptionEvents);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EventConfiguration other = (EventConfiguration) obj;
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
