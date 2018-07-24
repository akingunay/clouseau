package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ClouseauToBSPLCompiler {

	private final Specification specification;
	private final MessageCompiler messageCompiler;
	
	ClouseauToBSPLCompiler(final Specification specification) {
		this.specification = specification;
		this.messageCompiler = new MessageCompiler(specification);
	}

	Protocol compile() {
		IntermediaryProtocol intermediaryProtocol = compileIntermediaryProtocol();
		Protocol finalProtocol = linkIntermediaryProtocol(intermediaryProtocol);
		return finalProtocol;
	}
	
	IntermediaryProtocol compileIntermediaryProtocol() {
		IntermediaryProtocol protocol = new IntermediaryProtocol(specification.getName(), specification.getRoles());
		for (Commitment commitment : specification.getCommitments()) {
			Set<Enactment> enactments = new HashSet<>();
			// add an empty enactment to indicate that for each commitment initially
			// there is only one enactment without any messages, from which we extend
			// all enactments of this commitment
			enactments.add(new Enactment());
			enactments = compileEnactments(commitment.getCreate(), enactments, commitment.getDebtor(), commitment.getCreditor());
			enactments = compileEnactments(commitment.getDetach(), enactments, commitment.getCreditor(), commitment.getDebtor());
			enactments = compileEnactments(commitment.getDischarge(), enactments, commitment.getDebtor(), commitment.getCreditor());
			protocol.addEnactments(commitment.getName(), enactments);
		}
		return protocol;
	}

	private Set<Enactment> compileEnactments(final Expression expression, final Set<Enactment> enactments, final String defaultSender, final String defaultReceiver) {
		Set<Enactment> extendedEnactments = new HashSet<>();
		for (Enactment enactment : enactments) {
			for (EventConfiguration eventConfiguration : expression.getSatisfyingEventConfigurations()) {
				extendedEnactments.addAll(extendEnactment(enactment, eventConfiguration, compileMessages(enactment, eventConfiguration, defaultSender, defaultReceiver)));
			}
		}
		return extendedEnactments;
	}
	
	private Set<Set<Message>> compileMessages(final Enactment enactment, final EventConfiguration eventConfiguration, final String defaultSender, final String defaultReceiver) {
		Set<Set<Message>> messageSets = new HashSet<>();
		messageSets.add(new HashSet<>());
		for (String eventName : eventConfiguration.getNecessaryEvents()) {
			messageSets = extendMessageSets(messageSets, messageCompiler.compile(enactment, eventName, defaultSender, defaultReceiver));
		}
		for (String eventName : eventConfiguration.getExceptionEvents()) {
			messageSets = extendMessageSets(messageSets, messageCompiler.compile(enactment, eventName, defaultSender, defaultReceiver));
		}
		return messageSets;
	}
	
	private Set<Set<Message>> extendMessageSets(Set<Set<Message>> messageSets, Set<Message> messages) {
		Set<Set<Message>> extendedMessageSets = new HashSet<>();
		for (Set<Message> messageSet : messageSets) {
			for (Message message : messages) {
				Set<Message> extendedMessageSet = new HashSet<>(messageSet);
				extendedMessageSet.add(message);
				extendedMessageSets.add(extendedMessageSet);
			}
		}
		return extendedMessageSets;
	}
	
	private Set<Enactment> extendEnactment(final Enactment enactment, final EventConfiguration eventConfiguration, final Set<Set<Message>> messageSets) {
		Set<Enactment> extendedEnactments = new HashSet<>();
		for (Set<Message> messageSet : messageSets) {
			// Note that the extractExceptParameters likely to return parameter names that are known
			// for the enactment.  It is the responsibility of extend method to resolve such inconsistencies.
			extendedEnactments.add(enactment.extend(messageSet, extractExceptParameters(eventConfiguration)));
		}
		return extendedEnactments;
	}	
	
	// This method creates a set of parameter names simply composing all the parameters of all exception events.
	private Set<String> extractExceptParameters(EventConfiguration eventConfiguration) {
		Set<String> knownParameters = new HashSet<>();
		for (String eventName : eventConfiguration.getNecessaryEvents()) {
			knownParameters.addAll(specification.getEventWithName(eventName).getAttributes());
		}
		Set<String> exceptParameters = new HashSet<>();
		for (String eventName : eventConfiguration.getExceptionEvents()) {
			for (String parameter : specification.getEventWithName(eventName).getAttributes()) {	
				if (!knownParameters.contains(parameter)) {
					exceptParameters.add(parameter);
				}
			}
		}
		return exceptParameters;
	}

	Protocol linkIntermediaryProtocol(IntermediaryProtocol intermediaryProtocol) {
		Protocol protocol = new Protocol(intermediaryProtocol.getName());
		for (String role : intermediaryProtocol.getRoles()) {
			protocol.addRole(role);
		}
		for (String eventName : specification.getEventNames()) {
			List<String> commitmentNames = specification.getCommitmentNames(eventName);
			Set<Message> messages = new HashSet<>(intermediaryProtocol.getMessages(eventName, commitmentNames.get(0)));
			if (1 < commitmentNames.size()) { 
				for (int i = 1 ; i < commitmentNames.size() ; i++) {
					messages.retainAll(intermediaryProtocol.getMessages(eventName, commitmentNames.get(i)));
				}
			}
			protocol.addMessages(messages);
		}
		return removeDeadMessages(protocol);
	}

	private Protocol removeDeadMessages(Protocol protocol) {
		Protocol clearedProtocol = new Protocol(protocol.getName());
		for (String role : protocol.getRoles()) {
			clearedProtocol.addRole(role);
		}
		for (Message message : protocol.getMessages()) {
			if (!isDeadMessage(message)) {
				clearedProtocol.addMessage(message);
			}
		}
		return clearedProtocol;
	}
	
	private boolean isDeadMessage(Message message) {
		// TODO refactor the following logic to extract event name "e" from the corresponding message name "eM"
		// TODO we should find a concise way to relate event names with messages instead of using "M" suffix explicitly
		String eventName = message.getName().substring(0, message.getName().length() - 1);
		for (String parameter : specification.getEventWithName(eventName).getAttributes()) {
			// TODO for efficiency specification should have a hash based method to check if a parameter is controlled in an event
			List<String> controllingEventNames = specification.getControllingEvents(parameter);
			// if this parameter is adorned in and there is no other event where it is controlled
			// then this is a dead message, since the parameter cannot be bound in any message other than this
			// TODO add is[in|out|nil]Parameter() methods to Message
			if (message.getInParameters().contains(parameter) && controllingEventNames.size() == 1 && controllingEventNames.contains(eventName)) {
				return true;
			}
		}
		return false;
	}
}
