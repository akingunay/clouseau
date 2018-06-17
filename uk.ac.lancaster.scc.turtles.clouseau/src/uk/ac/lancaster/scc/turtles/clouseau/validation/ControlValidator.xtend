package uk.ac.lancaster.scc.turtles.clouseau.validation

import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.EValidatorRegistrar

// TODO there must be at most one control statement for each event per agent
// TODO there must be at least one control statement for every event
// TODO there must be at least one control statement for every attribute
// TODO there must not be a control statement for an event that does not exist
// TODO there must not be a control statement for an attribute that does not exist

class ControlValidator extends AbstractDeclarativeValidator {
	
	// This method must be here for this validator to work.
	override register(EValidatorRegistrar registrar) {}
	
}