package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.HashSet;
import java.util.Set;

class MessageCompiler {

	private final Specification specification;

	MessageCompiler(Specification specification) {
		this.specification = specification;
	}

	// TODO refactor compile method
	Set<Message> compile(final Enactment enactment, final String eventName, final String defaultSender, final String defaultReceiver) {
		Set<Message> messages = new HashSet<>();
		for (String controllerRole : specification.getControllerRoles(eventName)) {
			Set<Message> messagesByRole = new HashSet<>();
			// TODO the following constructor of Message is deprecated
			messagesByRole.add(new Message(specification.getEventWithName(eventName), controllerRole, controllerRole.equals(defaultSender) ? defaultReceiver : "?"));
			for (String attribute : specification.getEventWithName(eventName).getAttributesWithKeysFirst()) {
				Set<Message> extendedMessagesByRole = new HashSet<>();
				for (Message message : messagesByRole) {
					if (enactment.getBoundParameters(controllerRole).contains(attribute)) {
						// TODO should we check that no determinant key is out
						extendedMessagesByRole.add(adornParameterIn(attribute, message));
					} 
					// NEW BEGIN
					else if (enactment.getExceptParameters().contains(attribute)) {
						if (specification.getEventWithName(eventName).isNillable(attribute)) {
							extendedMessagesByRole.add(adornParameterNil(attribute, message));
						} else {
							// FIXME model error, this attribute must be nillable
						}
						// FIXME if the attribute is controlled by the sender, we should issue a warning
					}
					// NEW END
					else if (specification.isAttributeControlledByRoleForEvent(attribute, controllerRole, eventName)) {
						extendedMessagesByRole.add(adornParameterOut(attribute, message));
						if (!specification.getEventWithName(eventName).isKey(attribute)) {
							if (!isIntersectionEmpty(specification.getDeterminant(attribute), message.getInParameters())) {
								extendedMessagesByRole.add(adornParameterIn(attribute, message));
							}
							if (specification.getEventWithName(eventName).isNillable(attribute)) {
								extendedMessagesByRole.add(adornParameterNil(attribute, message));
							}
						} else {
								extendedMessagesByRole.add(adornParameterIn(attribute, message));
						}
					} else {
						if (!specification.getEventWithName(eventName).isKey(attribute)) {
							if (!isIntersectionEmpty(specification.getDeterminant(attribute), message.getInParameters())) {
								extendedMessagesByRole.add(adornParameterIn(attribute, message));
							}
							if (specification.getEventWithName(eventName).isNillable(attribute)) {
								extendedMessagesByRole.add(adornParameterNil(attribute, message));
							}
						} else {
								extendedMessagesByRole.add(adornParameterIn(attribute, message));
						}
					}
				}
				messagesByRole = extendedMessagesByRole;
			}
			messages.addAll(messagesByRole);
		}

		return messages;
	}

	// TODO do this more efficiently
	private boolean isIntersectionEmpty(final Set<String> first, final Set<String> second) {
		for (String elementOfFirst : first) {
			for (String elementOfSecond : second) {
				if (elementOfFirst.equals(elementOfSecond)) {
					return false;
				}
			}
		}
		return true;
	}

	private Message adornParameterIn(String parameter, Message message) {
		Set<String> inParameters = new HashSet<>(message.getInParameters());
		inParameters.add(parameter);
		Set<String> unknownParameters = new HashSet<>(message.getUnknownParameters());
		unknownParameters.remove(parameter);
		return new Message(message.getName(), message.getSender(), message.getReceiver(), message.getKeyParameters(),
				inParameters, message.getOutParameters(), message.getNilParameters(), unknownParameters);
	}

	private Message adornParameterOut(String parameter, Message message) {
		Set<String> outParameters = new HashSet<>(message.getOutParameters());
		outParameters.add(parameter);
		Set<String> unknownParameters = new HashSet<>(message.getUnknownParameters());
		unknownParameters.remove(parameter);
		return new Message(message.getName(), message.getSender(), message.getReceiver(), message.getKeyParameters(),
				message.getInParameters(), outParameters, message.getNilParameters(), unknownParameters);
	}

	private Message adornParameterNil(String parameter, Message message) {
		Set<String> nilParameters = new HashSet<>(message.getNilParameters());
		nilParameters.add(parameter);
		Set<String> unknownParameters = new HashSet<>(message.getUnknownParameters());
		unknownParameters.remove(parameter);
		return new Message(message.getName(), message.getSender(), message.getReceiver(), message.getKeyParameters(),
				message.getInParameters(), message.getOutParameters(), nilParameters, unknownParameters);
	 }
}
