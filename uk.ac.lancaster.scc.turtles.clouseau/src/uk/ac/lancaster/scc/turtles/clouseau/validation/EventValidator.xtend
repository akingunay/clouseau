package uk.ac.lancaster.scc.turtles.clouseau.validation

import org.eclipse.xtext.validation.Check
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOEvent
import java.util.HashSet
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.ClouseauPackage
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.EValidatorRegistrar

class EventValidator extends AbstractDeclarativeValidator {
	
	// This method must be here for this validator to work.
	override register(EValidatorRegistrar registrar) {}
	
	@Check
	def checkDuplicateEventAttributes(CLOEvent event) {
		var index = 0
		val attributes = new HashSet
		for (attribute : event.attributes) {
			if (attributes.contains(attribute)) {
				error("Attribute '" + attribute + "' is already defined for event '" + event.name + "'.", ClouseauPackage.eINSTANCE.CLOEvent_Attributes, index)
				return
			}
			attributes.add(attribute)
			index++
		}
	}
	
	@Check
	def checkDuplicateEventKeys(CLOEvent event) {
		var index = 0
		val keys = new HashSet
		for (key : event.keys) {
			if (keys.contains(key)) {
				error("Attribute '" + key + "' is already defined key for event '" + event.name + "'.", ClouseauPackage.eINSTANCE.CLOEvent_Keys, index)
				return
			}
			keys.add(key)
			index++
		}
	}
	
	@Check
	def checkEventKeys(CLOEvent event) {
		var index = 0
		for (key : event.keys) {
			if (!event.attributes.contains(key)) {
				error("Declared key '" + key + "' of event '" + event.name + "' is not an attribute of the event.", ClouseauPackage.eINSTANCE.CLOEvent_Keys, index)
				return
			}
			index++
		}
	}
}