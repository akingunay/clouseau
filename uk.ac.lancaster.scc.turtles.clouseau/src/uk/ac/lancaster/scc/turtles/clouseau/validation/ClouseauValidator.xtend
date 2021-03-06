/*
 * generated by Xtext 2.12.0
 */
package uk.ac.lancaster.scc.turtles.clouseau.validation

import org.eclipse.xtext.validation.ComposedChecks
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.CheckType
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOEvent
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOEventControl
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.ClouseauPackage
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOSpecification

// The following additional validators are used. 
@ComposedChecks(validators=#[EventValidator,CommitmentValidator])

/**
 * This class contains rules to validate the specification
 * considering relations of different elements in the specification.
 * Most of these validations are done only when the specification is
 * saved. For instance, for each event must be controlled by at least
 * one role. Other validations that consider individual elements
 * are done using validators as specified by the @ComposeChecks
 * annotation.
 * 
 */
class ClouseauValidator extends AbstractClouseauValidator {
	
	@Check(CheckType.NORMAL)
	def checkEventIsControlled(CLOEvent event) {
		if (event.eResource.allContents.toIterable.filter(CLOEventControl).filter[CLOEventControl control | control.event.name.equals(event.name)].nullOrEmpty) {
			error("Event '" + event.name + "' is not controlled.", ClouseauPackage.eINSTANCE.CLOEvent_Name)
		}
	}
	
	@Check(CheckType.NORMAL)
	def checkAllParametersAreControlled(CLOSpecification specification) {
		
	}
}
