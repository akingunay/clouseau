package uk.ac.lancaster.scc.turtles.clouseau.generator

import java.util.HashMap
import java.util.HashSet
import java.util.Map
import java.util.Set
import org.eclipse.emf.ecore.resource.Resource
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Attribute
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Commitment
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Control
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Event
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Role
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Spec

// Assumptions
// 1. An event is controlled only by one role.
// 2. Mutex assumption

// Validations to implement
// - attributes that are defined key for event are attributes of event
// - (Assumption 1) 
// - every event is controlled by at least one role 

class Specification {
	
	private String name = ""
	private Map<String, Commitment> commitments
	private Map<String, Event> events
	private Map<Role, Set<Event>> eventControls
	private Map<Role, Set<Attribute>> attributeControls
	private Map<String, String> eventControlledBy
	private Map<String, String> attributeControlledBy
	
	new(Resource resource) {
		name = resource.allContents.toIterable.filter(Spec).head.name
		commitments = new HashMap
		for (commitment : resource.allContents.toIterable.filter(Commitment)) {
			commitments.put(commitment.name, commitment)
		}
		events = new HashMap
		for (event : resource.allContents.toIterable.filter(Event)) {
			events.put(event.name, event)
		}
		eventControls = new HashMap
		attributeControls = new HashMap
		eventControlledBy = new HashMap
		attributeControlledBy = new HashMap
		for (control :  resource.allContents.toIterable.filter(Control)) {
			eventControls.put(control.role, new HashSet(control.events))
			attributeControls.put(control.role, new HashSet(control.attributes))
			for (event : control.events) {
				eventControlledBy.put(event.name, control.role.name)
			}
			for (attribute : control.attributes) {
				attributeControlledBy.put(attribute.name, control.role.name)
			}
		}
	}
	
	def CharSequence compileProtocol() {
		'''
        «name» {
        
        	«compileRoles()»
        
        	«compileParameters()»
        
        	«compileMessages()»
        }
        
		'''
	}

	private def CharSequence compileRoles() {
		'''role ''' +
        '''«FOR role : eventControls.keySet SEPARATOR ', '»«role.name»«ENDFOR»'''
	}
	
	private def CharSequence compileParameters() {
		'''parameter'''
	}
	
	private def CharSequence compileMessages() {
		val messages = createMessages()
		'''«FOR message : messages SEPARATOR '\n'»«message»«ENDFOR»'''
	}

	private def Set<Message> createMessages() {
		val messages = new HashSet<Message>
		for (commitment : commitments.values) {
			messages.addAll(processCommitmentN(commitment))
		}
		messages
	}
	
	private def Set<Message> processCommitmentN(Commitment commitment) {
		val messages = new HashSet<Message>
		// handle create
		val Set<Set<String>> incomingParameterSetsForCreate = newHashSet(new HashSet<String>) 
		val messageSetsForCreate = processLifecycle(new DNF(commitment.create), commitment.creditor.name, incomingParameterSetsForCreate, false)
		messages.addAll(extractMessages(messageSetsForCreate))
		// handle detach
		val incomingParameterSetsForDetach = extractIncomingParameterSets(messageSetsForCreate)
		val messageSetsForDetach = processLifecycle(new DNF(commitment.detach), commitment.debtor.name, incomingParameterSetsForDetach, true)
		messages.addAll(extractMessages(messageSetsForDetach))
		// handle discharge
		val incomingParameterSetsForDischarge = composeIncomingParameterSets(extractIncomingParameterSets(messageSetsForDetach), incomingParameterSetsForDetach)
		val messageSetsForDischarge = processLifecycle(new DNF(commitment.discharge), commitment.creditor.name, incomingParameterSetsForDischarge, true)
		messages.addAll(extractMessages(messageSetsForDischarge))
		messages
	}
		
	private def Set<Set<Message>> processLifecycle(DNF dnf, String receiver, Set<Set<String>> incomingParameterSets, boolean allowInForUncontrolled) {
		val messageSets = new HashSet<Set<Message>>
		for (conjunction : dnf.formula) {
			for (incomingParameters : incomingParameterSets) {
				val messages = new HashSet<Message>
				for (event : conjunction) {
					val attributeNames = extractAttributeNames(event)
					val filteredIncomingParameters = filterIncomingParameters(incomingParameters, attributeNames)
					val message  = new Message(event.name + "M", eventControlledBy.getOrDefault(event.name, "?"), receiver)
					for (attribute : event.attributes) {
						if (filteredIncomingParameters.contains(attribute.name)) {
							message.addInParameter(attribute.name)
						} else if (eventControlledBy.get(event.name).equals(attributeControlledBy.get(attribute.name))) {
							message.addOutParameter(attribute.name)
						} else {
							if (allowInForUncontrolled) {
								message.addNilParameter(attribute.name)
							} else {
								message.addUnknownParameter(attribute.name)
							}
						}
						if (event.keys.contains(attribute)) {
							message.addKeyParameter(attribute.name)
						}
					}
					messages.add(message)
				}
				messageSets.add(messages)
			}
		}
		messageSets
	}
	
	
	private def Set<Message> extractMessages(Set<Set<Message>> messageSets) {
		val extractedMessages = new HashSet<Message>
		for (messages : messageSets) {
			extractedMessages.addAll(messages)
		}
		extractedMessages
	}

	private def Set<String> extractAttributeNames(Event event) {
		val attributeNames = new HashSet<String>
		for (attribute : event.attributes) {
			attributeNames.add(attribute.name)
		} 
		attributeNames
	}
	
	private def Set<String> filterIncomingParameters(Set<String> incomingParameters, Set<String> attributeNames) {
		val filteredIncomingParameters = new HashSet<String>(incomingParameters)
		filteredIncomingParameters.retainAll(attributeNames)
		filteredIncomingParameters
	}

	private def Set<Set<String>> extractIncomingParameterSets(Set<Set<Message>> messageSets) {
		val incomingParameterSets = new HashSet<Set<String>>
		for (messages : messageSets) {
			val incomingParameters = new HashSet<String>
			for (message : messages) {
				incomingParameters.addAll(message.inParameters)
				incomingParameters.addAll(message.outParameters)
			}
			incomingParameterSets.add(incomingParameters)
		}
		incomingParameterSets
	}
	
	private def Set<Set<String>> composeIncomingParameterSets(Set<Set<String>> firstIncomingParameterSet, Set<Set<String>> secondIncomingParameterSet) {
		val incomingParameterSets = new HashSet<Set<String>>
		for (firstIncomingParameters : firstIncomingParameterSet) {
			for (secondIncomingParameters : secondIncomingParameterSet) {
				val incomingParameters = new HashSet<String>
				incomingParameters.addAll(firstIncomingParameters)
				incomingParameters.addAll(secondIncomingParameters)
				incomingParameterSets.add(incomingParameters)
			}
		}
		incomingParameterSets
	}
	
	// uncontrolled captures parameters that are public and in adorned
//	private def Set<Set<Message>> processCreateN(DNF dnf, String receiver, boolean allowInForUncontrolled) {
//		val messageSets = new HashSet<Set<Message>>
//		for (conjunction : dnf.formula) {
//			val messages = new HashSet<Message>
//			for (event : conjunction) {
//				val message  = new Message(event.name + "M", eventControlledBy.getOrDefault(event.name, "?"), receiver)
//				for (attribute : event.attributes) {
//					if (eventControlledBy.getOrDefault(event.name, "") == attributeControlledBy.getOrDefault(attribute.name, "")) {
//						message.addOutParameter(attribute.name)
//					} else {
//						if (allowInForUncontrolled) {
//							message.addInParameter(attribute.name)
//						} else {
//							message.addUnknownParameter(attribute.name)
//						}
//					}
//					if (event.keys.contains(attribute)) {
//						message.addKeyParameter(attribute.name)
//					}
//				}
//				messages.add(message)
//			}
//			messageSets.add(messages)
//		}
//		messageSets
//	}
	
//	private def Set<Message> processCommitment(Commitment commitment) {
//		val messages = new HashSet
//		messages.addAll(processCreate(commitment))
//		messages.addAll(processDetach(commitment))
//		messages.addAll(processDischarge(commitment))
//		messages
//	}
	
//	private def Set<Message> processCreate(Commitment commitment) {
//		val messageNames = extractMessageNames(new DNF(commitment.create))
//		val messages = new HashSet<Message>
//		for (messageName : messageNames) {
//			val message = new Message(messageName + "M", eventControlledBy.getOrDefault(messageName, "?"), commitment.creditor.name)
//			for (attributeName : extractAttributeNames(events.get(messageName))) {
//				if (eventControlledBy.get(messageName).equals(attributeControlledBy.get(attributeName))) {
//					message.addOutParameter(attributeName)
//					if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//						message.addKeyParameter(attributeName)
//					}
//				} else {
//					if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//						message.addUnknownParameter(attributeName)
//						message.addKeyParameter(attributeName)
//					} else {
//						message.addNilParameter(attributeName)
//					}
//				}			
//			}
//			messages.add(message)
//		}
//		messages
//	}
//	
//	private def Set<Message> processDetach(Commitment commitment) {
//		val incomingParameterSets = determineIncomingParameterSetsForDetach(new DNF(commitment.create))
//		val messageNames = extractMessageNames(new DNF(commitment.detach))
//		val messages = new HashSet<Message>
//		for (messageName : messageNames) {
//			val attributeNames = extractAttributeNames(events.get(messageName))
//			for (incomingParameters : filterIncomingParameterSets(incomingParameterSets, attributeNames)) {
//				val message = new Message(messageName + "M", eventControlledBy.getOrDefault(messageName, "?"), commitment.debtor.name)
//				for (attributeName : attributeNames) {
//					if (incomingParameters.contains(attributeName)) {
//						message.addInParameter(attributeName)
//						if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//							message.addKeyParameter(attributeName)
//						}
//					} else if (eventControlledBy.get(messageName).equals(attributeControlledBy.get(attributeName))) {
//						message.addOutParameter(attributeName)
//						if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//							message.addKeyParameter(attributeName)
//						}
//					} else {
//						if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//							message.addUnknownParameter(attributeName)
//							message.addKeyParameter(attributeName)
//						} else {
//							message.addNilParameter(attributeName)
//						}
//					}
//				}
//				messages.add(message)
//			}
//		}
//		messages
//	}
//
//	private def Set<Message> processDischarge(Commitment commitment) {
//		val incomingParameterSets = determineIncomingParameterSetsForDischarge(new DNF(commitment.create), new DNF(commitment.detach))
//		val messageNames = extractMessageNames(new DNF(commitment.discharge))
//		val messages = new HashSet<Message>
//		for (messageName : messageNames) {
//			val attributeNames = extractAttributeNames(events.get(messageName))
//			for (incomingParameters : filterIncomingParameterSets(incomingParameterSets, attributeNames)) {
//				val message = new Message(messageName + "M", eventControlledBy.getOrDefault(messageName, "?"), commitment.creditor.name)
//				for (attributeName : attributeNames) {
//					if (incomingParameters.contains(attributeName)) {
//						message.addInParameter(attributeName)
//						if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//							message.addKeyParameter(attributeName)
//						}
//					} else if (eventControlledBy.get(messageName).equals(attributeControlledBy.get(attributeName))) {
//						message.addOutParameter(attributeName)
//						if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//							message.addKeyParameter(attributeName)
//						}
//					} else {
//						if (extractKeyAttributeNames(events.get(messageName)).contains(attributeName)) {
//							message.addUnknownParameter(attributeName)
//							message.addKeyParameter(attributeName)
//						} else {
//							message.addNilParameter(attributeName)
//						}
//					}
//				}
//				messages.add(message)
//			}
//		}
//		messages
//	}
	
//	private def Set<Set<String>> determineIncomingParameterSetsForDetach(DNF dnf) {
//		val incomingParameterSets = new HashSet<Set<String>>
//		for (conjunction : dnf.formula) {
//			val incomingParameters = new HashSet<String>
//			for (event : conjunction) {
//				incomingParameters.addAll(extractAttributeNames(event))
//			}
//			incomingParameterSets.add(incomingParameters)
//		}
//		incomingParameterSets
//	}
//	
//	private def Set<Set<String>> determineIncomingParameterSetsForDischarge(DNF createDNF, DNF detachDNF) {
//		val incomingParameterSets = new HashSet<Set<String>>
//		for (createConjunction : createDNF.formula) {
//			for (detachConjunction : detachDNF.formula) {
//				val incomingParameters = new HashSet<String>
//				for (event : createConjunction) {
//					incomingParameters.addAll(extractAttributeNames(event))
//				}
//				for (event : detachConjunction) {
//					incomingParameters.addAll(extractAttributeNames(event))
//				}
//				incomingParameterSets.add(incomingParameters)
//			}
//		}
//		incomingParameterSets
//	}
	
	
//	private def Set<String> extractKeyAttributeNames(Event event) {
//		val keyAttributeNames = new HashSet<String>
//		for (keyAttribute : event.keys) {
//			keyAttributeNames.add(keyAttribute.name)
//		} 
//		keyAttributeNames
//	}
	
//	private def Set<String> extractMessageNames(DNF dnf) {
//		val messageNames = new HashSet<String>
//		for (conjunction : dnf.formula) {
//			for (event : conjunction) {
//				messageNames.add(event.name)
//			}
//		}
//		messageNames	
//	}
	
}