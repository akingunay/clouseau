package uk.ac.lancaster.scc.turtles.clouseau.generator

import org.eclipse.emf.ecore.resource.Resource
import java.util.List
import java.util.ArrayList
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOControl
import java.util.HashSet

package class ControlExtractor {
	
	Resource resource
	
	package new(Resource resource) {
		this.resource = resource
	}
	
	package def List<Control> extract() {
		val controls = new ArrayList
		for (control : resource.allContents.toIterable.filter(CLOControl)) {
			val role = control.role
			for (eventControl : control.eventControls) {
				controls.add(new Control(role, eventControl.event.name, new HashSet(eventControl.attributes)))
			}
		}
		controls
	}
}