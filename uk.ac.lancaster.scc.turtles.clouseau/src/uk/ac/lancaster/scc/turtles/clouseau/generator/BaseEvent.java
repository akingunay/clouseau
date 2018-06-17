package uk.ac.lancaster.scc.turtles.clouseau.generator;

class BaseEvent implements Expression {

	private final String eventName;
	
	BaseEvent(final String eventName) {
		this.eventName = eventName;
	}
	
	String getEventName() {
		return eventName;
	}
}
