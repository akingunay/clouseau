package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class IntermediaryProtocol {

    private final String name;
    private final Set<String> roles;
    private final Map<String, Set<Enactment>> commitmentNameToEnactments;
    
    IntermediaryProtocol(final String name, final Set<String> roles) {
        this.name = name;
        this.roles = new HashSet<>(roles);
        this.commitmentNameToEnactments = new HashMap<>();
    }
    
    void addEnactments(String commitmentName, Set<Enactment> enactments) {
    	if (!commitmentNameToEnactments.containsKey(commitmentName)) {
    		commitmentNameToEnactments.put(commitmentName, new HashSet<>());
    	}
    	commitmentNameToEnactments.get(commitmentName).addAll(enactments);
    }

    String getName() {
    	return name;
    }
    
    List<String> getRoles() {
    	return new ArrayList<>(roles);
    }
    
    List<String> getCommitmentNames() {
    	return Collections.unmodifiableList(new ArrayList<>(commitmentNameToEnactments.keySet()));
    }
    
    /**
     * Retrieve the messages in the protocol for the given commitment. The messages are grouped by their name.
     * 
     * @param commitmentName
     * @return
     */
    List<Message> getMessagesGroupedByName(String commitmentName) {
    	Map<String, Set<Message>> messageNameToMessages = new HashMap<>();
    	for (Enactment enactment : commitmentNameToEnactments.get(commitmentName)) {
    		for (Message message : enactment.getMessages()) {
    			if (!messageNameToMessages.containsKey(message.getName())) {
    				messageNameToMessages.put(message.getName(), new HashSet<>());
    			}
    			messageNameToMessages.get(message.getName()).add(message);
    		}
    	}
    	List<Message> messages = new ArrayList<>();
    	for (String messageName : messageNameToMessages.keySet()) {
    		for (Message message : messageNameToMessages.get(messageName)) {
    			messages.add(message);
    		}
    	}
    	return messages;
    }
    
    /**
     * 
     * 
     * @param messageName
     * @param commitmentName
     * @return
     */
    List<Message> getMessages(String messageName, String commitmentName) {
    	List<Message> messages = new ArrayList<>();
    	for (Enactment enactment : commitmentNameToEnactments.get(commitmentName)) {
    		for (Message message : enactment.getMessages()) {
    			if (message.getName().equals(messageName + "M")) {
    				messages.add(message);
    			}
    		}
    	}
    	return messages;
    }
    
    List<Message> getMessages(String messageName) {
    	List<Message> messages = new ArrayList<>();
    	for (String commitmentName : commitmentNameToEnactments.keySet()) {
    		for (Enactment enactment : commitmentNameToEnactments.get(commitmentName)) {
    			for (Message message : enactment.getMessages()) {
    				if (message.getName().equals(messageName)) {
    					messages.add(message);
    				}
    			}
    		}
    	}
    	return messages;
    }
}