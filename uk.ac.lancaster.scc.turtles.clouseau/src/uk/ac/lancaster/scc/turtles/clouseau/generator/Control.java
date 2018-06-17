package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.Set;

/**
 * 
 * @author Akin Gunay
 *
 */
class Control {

	private final String role;
	private final String eventName;
	private final Set<String> parameters;
	
	/**
	 * 
	 * @param role
	 * @param eventName
	 * @param parameters
	 */
	Control(final String role, final String eventName, final Set<String> parameters) {
		super();
		this.role = role;
		this.eventName = eventName;
		this.parameters = parameters;
	}

	/**
	 * 
	 * @return
	 */
	String getRole() {
		return role;
	}

	/**
	 * 
	 * @return
	 */
	String getEventName() {
		return eventName;
	}

	/**
	 * 
	 * @return
	 */
	Set<String> getParameters() {
		return Collections.unmodifiableSet(parameters);
	}
	
    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder("<Control>");
    	str.append("\n\trole : ").append(role).
    		append("\n\teventName : ").append(eventName).
    		append("\n\tparameters : {");
    	for (String parameter : parameters) {
    		str.append(parameter).append(", ");
    	}
    	return str.replace(str.length() - ", ".length(), str.length(), "}").toString();
    }
	
}
