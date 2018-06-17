package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Enactment {

    private final Set<Message> messages;
    private final Map<String, Set<String>> agentsToBoundParameters;
    
    Enactment() {
    	this.messages = new HashSet<>();
    	this.agentsToBoundParameters = new HashMap<>();
    }
    
    Enactment(Set<Message> messages) {
        this.messages = new HashSet<>(messages);
        this.agentsToBoundParameters = indexAgentsToBoundParameters();
    }
    
    private Map<String, Set<String>> indexAgentsToBoundParameters() {
    	Map<String, Set<String>> agentsToBoundParameters = new HashMap<>();
    	for (Message message : messages) {
    		updateBoundParameters(message.getSender(), message.getBoundParameters(), agentsToBoundParameters);
    		updateBoundParameters(message.getReceiver(), message.getBoundParameters(), agentsToBoundParameters); 
    	}
    	return agentsToBoundParameters;
    }
    
    private void updateBoundParameters(String agent, Set<String> boundParameters, Map<String, Set<String>> agentsToBoundParameters) {
    	if (agent != null) {
			if (!agentsToBoundParameters.containsKey(agent)) {
				agentsToBoundParameters.put(agent, new HashSet<>());
			}
			agentsToBoundParameters.get(agent).addAll(boundParameters);
		}
    }
    
    Set<Message> getMessages() {
        return Collections.unmodifiableSet(messages);
    }
    
    Set<String> getBoundParameters(String role) {
        return Collections.unmodifiableSet(agentsToBoundParameters.getOrDefault(role, new HashSet<String>()));
    }
    
}