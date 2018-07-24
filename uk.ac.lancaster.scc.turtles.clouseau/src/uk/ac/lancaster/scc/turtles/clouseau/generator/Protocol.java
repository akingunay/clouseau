package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO make this immutable and provide a builder
class Protocol {

	private final String name;
    private final Set<String> roles;
    private final Set<Message> messages;
    
    Protocol(String name) {
    	this.name = name;
    	this.roles = new HashSet<>();
    	this.messages = new HashSet<>();
    }
    
    void addRole(String role) {
    	roles.add(role);
    }
    
    void addMessage(Message message) {
    	messages.add(message);
    }
    
    void addMessages(Collection<Message> messages) {
    	this.messages.addAll(messages);
    }
    
    void removeMessage(Message message) {
    	this.messages.remove(message);
    }
    
    void removeMessages(Collection<Message> messages) {
    	for (Message message : messages) {
    		this.messages.remove(message);
    	}
    }
    
    String getName() {
    	return name;
    }
    
    List<String> getRoles() {
    	return new ArrayList<>(roles);
    }
    
    List<Message> getMessages() {
    	return new ArrayList<>(messages);
    }
    
    List<Message> getMessagesGroupedByName() {
    	Map<String, List<Message>> messageNameToMessages = new HashMap<>();
    	for (Message message : messages) {
    		if (!messageNameToMessages.containsKey(message.getName())) {
    			messageNameToMessages.put(message.getName(), new ArrayList<>());
    		}
    		messageNameToMessages.get(message.getName()).add(message);
    	}
    	List<Message> groupedMessages = new ArrayList<>();
    	for (List<Message> messageGroup : messageNameToMessages.values()) {
    		groupedMessages.addAll(messageGroup);
    	}
    	return groupedMessages;
    }
}
