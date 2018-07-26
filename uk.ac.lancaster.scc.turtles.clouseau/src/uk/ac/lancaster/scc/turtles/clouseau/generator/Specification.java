package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

// TODO can we have a generic functional indexer and extractor
// TODO methods that return set may return empty set instead of null, when the result is empty

class Specification {

	private final String name;
	private final Set<String> roles;
	private final List<Commitment> commitments;
    private final List<Event> events;
    private final List<Control> controls;
    private final Map<String, Event> eventNameToEvent;
    private final Map<String, Set<Control>> eventNameToControls;
    private final Map<String, Set<String>> eventNameToControllerRoles;
    private final Map<String, Set<String>> attributeToDeterminant;		// the keys that determine this attribute
    private final Map<String, List<String>> eventNameToCommitmentNames; // the commitments that include the event
    private final Map<String, List<String>> parameterToEventNames; 		// the events in which the parameter is controlled
    
    
    Specification(final String name, final List<Commitment> commitments, final List<Event> events, final List<Control> controls) {
        this.name = name;
        this.commitments = new ArrayList<>(commitments);
        this.events = new ArrayList<>(events);
        this.controls = new ArrayList<>(controls);
        this.roles = extractRoles();
        this.eventNameToEvent = indexEventNameToEvent();
        this.eventNameToControls = indexEventNameToControls();
        this.eventNameToControllerRoles = indexEventNameToControllerRoles();
        this.attributeToDeterminant = extractAttributeToDeterminant();
        this.eventNameToCommitmentNames = indexEventNameToCommitmentNames();
        this.parameterToEventNames = indexParameterToEventNames();
    }

    private Set<String> extractRoles() {
    	Set<String> extractedRoles = new HashSet<>();
    	commitments.forEach(c -> extractedRoles.addAll(c.getRoles()));
    	return extractedRoles;
    }
    
    private Map<String, Event> indexEventNameToEvent() {
    	Map<String, Event> index = new HashMap<>();
    	for (Event event : events) {
    		index.put(event.getName(), event);
    	}
    	return index;
    }

    private Map<String, Set<Control>> indexEventNameToControls() {
    	Map<String, Set<Control>> index = new HashMap<>();
    	for (Control control : controls) {
    		if (!index.containsKey(control.getEventName())) {
    			index.put(control.getEventName(), new HashSet<>());
    		}
    		index.get(control.getEventName()).add(control);
    	}
    	return index;
    }
    
    private Map<String, Set<String>> indexEventNameToControllerRoles() {
    	Map<String, Set<String>> index =  new HashMap<>();
    	for (Control control : controls) {
    		if (!index.containsKey(control.getEventName())) {
    			index.put(control.getEventName(), new HashSet<>());
    		}
    		index.get(control.getEventName()).add(control.getRole());
    	}
    	return index;
    }
    
    private Map<String, Set<String>> extractAttributeToDeterminant() {
    	Map<String, Set<String>> extractedAttributeToDeterminant = new HashMap<>();
    	for (Event event : events) {
    		for (String key : event.getKeys()) {
    			if (!extractedAttributeToDeterminant.containsKey(key)) {
    				Set<String> selfDeterminant = new HashSet<>();
    				selfDeterminant.add(key);
    				extractedAttributeToDeterminant.put(key, selfDeterminant);
    			}
    		}
    		for (String attribute : event.getNonkeyAttributes()) {
    			if (extractedAttributeToDeterminant.containsKey(attribute)) {
    				extractedAttributeToDeterminant.get(attribute).retainAll(event.getKeys());
    			} else {
    				extractedAttributeToDeterminant.put(attribute, new HashSet<>(event.getKeys()));
    			}
    		}
    	}
    	return extractedAttributeToDeterminant;
    }
    
    private Map<String, List<String>> indexEventNameToCommitmentNames() {
    	Map<String, List<String>> index = new HashMap<>();
    	for (Commitment commitment : commitments) {
    		for (String eventName : commitment.getIncludedEventNames()) {
    			if (!index.containsKey(eventName)) {
    				index.put(eventName, new ArrayList<>());
    			}
    			index.get(eventName).add(commitment.getName());
    		}
    	}
    	return index;
    }
    
    private Map<String, List<String>> indexParameterToEventNames() {
    	Map<String, List<String>> index = new HashMap<>();
    	for (Control control : controls) {
    		for (String parameter : control.getParameters()) {
    			if (!index.containsKey(parameter)) {
    				index.put(parameter, new ArrayList<>());
    			}
    			index.get(parameter).add(control.getEventName());
    		}
    	}
    	return index;
    }
    
    String getName() {
    	return name;
    }
    
    Event getEventWithName(String eventName) {
    	return eventNameToEvent.get(eventName);
    }
    
    List<Commitment> getCommitments() {
    	return Collections.unmodifiableList(commitments);
    }
    
    /**
     * Returns the events in which the given parameter is controlled by a role.
     * 
     * @param parameter
     * @return
     */
    List<String> getControllingEvents(String parameter) {
    	if (parameterToEventNames.containsKey(parameter)) {
    		return Collections.unmodifiableList(parameterToEventNames.get(parameter));
    	} else {
    		throw new NoSuchElementException("There is no parameter '" + parameter + "' controlled by any event.");
    	}
    }
    
    Set<String> getRoles() {
    	return Collections.unmodifiableSet(roles);
    }
    
    Set<String> getControllerRoles(String eventName) {
    	if (eventNameToControllerRoles.containsKey(eventName)) {
    		return Collections.unmodifiableSet(eventNameToControllerRoles.getOrDefault(eventName, new HashSet<>()));
    	} else {
    		throw new NoSuchElementException("There is no event '" + eventName + "' controlled by any role.");
    	}
    }
    
    Set<String> getDeterminant(String attribute) {
    	if (attributeToDeterminant.containsKey(attribute)) {
    		return Collections.unmodifiableSet(attributeToDeterminant.get(attribute));
    	} else {
    		throw new NoSuchElementException();
    	}
    }
    
    boolean isAttributeControlledByRoleForEvent(String attribute, String role, String eventName) {
    	// TODO if the specification is validated properly, there must always be corresponding
    	// control statement for each event.  Hence, the following getOrDeafult can be replaced
    	// with a get
    	for (Control control : eventNameToControls.getOrDefault(eventName, new HashSet<>())) {
    		if (control.getRole().equals(role)) {
    			// TODO if the specification is validated properly, there can only be a single
    			// control statement for a role per event.  Hence, we can immediately return
    			// false if the following check fails.
    			if (control.getParameters().contains(attribute)) {
    				return true;
    			} else {
    				return false;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * Get the list of commitment names that involve the given event in their create, detach, or discharge expressions.
     * 
     * @param eventName
     * @return
     */
    List<String> getCommitmentNames(String eventName) {
    	return Collections.unmodifiableList(eventNameToCommitmentNames.get(eventName));
    }
    
    List<String> getEventNames() {
    	List<String> eventNames = new ArrayList<>();
    	for (Event event : events) {
    		eventNames.add(event.getName());
    	}
    	return eventNames;
    }
    
    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder(name).append("\n\n");
    	commitments.forEach((Commitment c) -> str.append(c).append("\n"));
    	str.append("\n");
    	events.forEach((Event e) -> str.append(e).append("\n"));
    	str.append("\n");
    	controls.forEach((Control c) -> str.append(c).append("\n"));
    	return str.toString();
    }
}


