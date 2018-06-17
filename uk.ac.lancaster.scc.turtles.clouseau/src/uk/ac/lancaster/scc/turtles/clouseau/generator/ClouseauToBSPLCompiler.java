package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.HashSet;
import java.util.Set;

class ClouseauToBSPLCompiler {

	private final Specification specification;
	private final MessageCompiler messageCompiler;
	
	ClouseauToBSPLCompiler(Specification specification) {
		this.specification = specification;
		this.messageCompiler = new MessageCompiler(specification);
	}

	Protocol compile() {
		Protocol intermediaryProtocol = compileIntermediaryProtocol();
		Protocol finalProtocol = linkIntermediaryProtocol(intermediaryProtocol);
		return finalProtocol;
	}

	Protocol compileIntermediaryProtocol() {
		Protocol protocol = new Protocol(specification.getName(), specification.getRoles());
		for (Commitment commitment : specification.getCommitments()) {
			Set<Enactment> enactments = new HashSet<Enactment>();
			// add an empty enactment to indicate that for each commitment initially
			// there is only one enactment without any messages, from which we should
			// extend further enactments of this commitment
			enactments.add(new Enactment());
			enactments = compileEnactments(commitment.getCreate(), enactments, commitment.getDebtor(), commitment.getCreditor());
			enactments = compileEnactments(commitment.getDetach(), enactments, commitment.getCreditor(), commitment.getDebtor());
			enactments = compileEnactments(commitment.getDischarge(), enactments, commitment.getDebtor(), commitment.getCreditor());
			protocol.addEnactments(commitment.getName(), enactments);
		}
		return protocol;
	}

	private Set<Enactment> compileEnactments(final DNF expression, final Set<Enactment> enactments, final String defaultSender, final String defaultReceiver) {
		Set<Enactment> extendedEnactments = new HashSet<>();
		for (Enactment enactment : enactments) {
			for (Conjunction conjunction : expression.getConjunctions()) {
				extendedEnactments.addAll(extendEnactment(enactment, compileMessages(enactment, conjunction, defaultSender, defaultReceiver)));
			}
		}
		return extendedEnactments;
	}

	private Set<Set<Message>> compileMessages(final Enactment enactment, final Conjunction conjunction, final String defaultSender, final String defaultReceiver) {
		Set<Set<Message>> messageSets = new HashSet<>();
		messageSets.add(new HashSet<>());
		for (String eventName : conjunction.getEventNames()) {
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
	
	private Set<Enactment> extendEnactment(final Enactment enactment, final Set<Set<Message>> messageSets) {
		Set<Enactment> extendedEnactments = new HashSet<>();
		for (Set<Message> messageSet : messageSets) {
			Set<Message> messages = new HashSet<>();
			messages.addAll(enactment.getMessages());
			messages.addAll(messageSet);
			extendedEnactments.add(new Enactment(messages));
		}
		return extendedEnactments;
	}

	private Protocol linkIntermediaryProtocol(Protocol protocol) {
		return protocol;
	}

}
