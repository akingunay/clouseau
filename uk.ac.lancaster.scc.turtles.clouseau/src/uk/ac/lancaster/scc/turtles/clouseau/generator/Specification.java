package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    
    String getName() {
    	return name;
    }
    
    Event getEventWithName(String eventName) {
    	return eventNameToEvent.get(eventName);
    }
    
    List<Commitment> getCommitments() {
    	return Collections.unmodifiableList(commitments);
    }
    
    Set<String> getRoles() {
    	return Collections.unmodifiableSet(roles);
    }
    
    Set<String> getControllerRoles(String eventName) {
    	return Collections.unmodifiableSet(eventNameToControllerRoles.getOrDefault(eventName, new HashSet<>()));
    }
    
    Set<String> getDeterminant(String attribute) {
    	return Collections.unmodifiableSet(attributeToDeterminant.get(attribute));
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


