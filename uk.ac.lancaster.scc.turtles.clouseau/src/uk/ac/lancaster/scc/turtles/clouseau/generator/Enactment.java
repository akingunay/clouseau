package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Enactment {

    private final Set<Message> messages;
    private final Map<String, Set<String>> agentToBoundParameters;
    private final Set<String> exceptParameters;
    
    Enactment() {
    	this.messages = new HashSet<>();
    	this.agentToBoundParameters = new HashMap<>();
    	this.exceptParameters = new HashSet<>();
    }
    
    // Note that if there are inconsistencies between the known parameters, that are
    // extracted from the received messages and the except parameters, we make these 
    // two sets consistent by removing the parameters that exist in both sets, from 
    // the set of known parameters. This is necessary since the enactment does not
    // know which messages capture exceptions. Therefore we should respect to 
    // the given content of the expect parameters. 
    Enactment(final Set<Message> messages, final Set<String> exceptParameters) {
        this.messages = new HashSet<>(messages);
        this.agentToBoundParameters = indexAgentsToBoundParameters();
        this.exceptParameters = new HashSet<>(exceptParameters);
        makeBoundAndExceptParametersConsistent();
    }
    
    private Map<String, Set<String>> indexAgentsToBoundParameters() {
    	Map<String, Set<String>> agentsToBoundParameters = new HashMap<>();
    	for (Message message : messages) {
    		updateBoundParameters(message.getSender(), message.getBoundParameters(), agentsToBoundParameters);
    		updateBoundParameters(message.getReceiver(), message.getBoundParameters(), agentsToBoundParameters); 
    	}
    	return agentsToBoundParameters;
    }
    
    private void makeBoundAndExceptParametersConsistent() {
    	for (String exceptParmeter : exceptParameters) {
    		for (String agent : agentToBoundParameters.keySet()) {
    			agentToBoundParameters.get(agent).remove(exceptParmeter);
    		}
    	}
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
        return Collections.unmodifiableSet(agentToBoundParameters.getOrDefault(role, new HashSet<String>()));
    }
 
    Set<String> getExceptParameters() {
        return Collections.unmodifiableSet(exceptParameters);
    }

    // Check the note at the corresponding constructor.
    Enactment extend(final Set<Message> messages, final Set<String> exceptParameters) {
    	Set<Message> extendedMessages = new HashSet<>(this.messages);
    	extendedMessages.addAll(messages);
    	Set<String> extendedExceptParameters = new HashSet<>(this.exceptParameters);
    	extendedExceptParameters.addAll(exceptParameters);
    	return new Enactment(extendedMessages, extendedExceptParameters);
    }
}